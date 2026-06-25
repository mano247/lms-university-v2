import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DividerModule } from 'primeng/divider';
import { DropdownModule } from 'primeng/dropdown';
import { ToastModule } from 'primeng/toast';
import { Predmet } from '../../../model/academic/predmet';
import { Student } from '../../../model/users/student';
import { StudentiService } from '../../../services/studenti.service';
import { NastavnikService } from '../../../services/nastavnik.service';
import { PolaganjeService } from '../../../services/polaganje.service';
import { Polaganje } from '../../../model/polaganje';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { FormsModule } from '@angular/forms';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-unos-ocena',
  standalone: true,
  imports: [DropdownModule, ButtonModule, DividerModule, ConfirmDialogModule, ToastModule, TableModule, DialogModule, FormsModule],
  templateUrl: './unos-ocena.component.html',
  styleUrl: './unos-ocena.component.css',
  providers: [ConfirmationService, MessageService]
})
export class UnosOcenaComponent implements OnInit{
  predmeti: Predmet[] = [];
  studenti: Student[] = [];
  ocene: number[] | undefined;

  profId: number = 0;

  izabranoPolaganje:any = {};

  rezultat:any = {
    osvojeniBodovi: 0,
    ocena: 0,
    napomena: ""
  }

  visible: boolean = false;

  izabraniPredmetID: number | undefined;

  polaganja: Polaganje[] = [];

  constructor(private confirmationService: ConfirmationService, private messageService: MessageService,
    private nastavnikService: NastavnikService, private studentService: StudentiService, private polaganjeService: PolaganjeService
  ){}

  ngOnInit(): void {
    this.ocene = [
      6, 7, 8, 9, 10
    ]

    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      const id = parsedUser.id;
      this.profId = parsedUser.id;
      this.getPredmeti(id);
      this.getStudenti();
    }
  }

  potvrdaOcene(event: Event){
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Zelite da unesete ocenu?',
      header: 'Potvrda ocene',
      icon: 'pi pi-exclamation-triangle',
      acceptIcon:"none",
      rejectIcon:"none",
      rejectButtonStyleClass:"p-button-text",
      accept: () => {
          this.messageService.add({ severity: 'info', summary: 'Potvcrdjeno!', detail: 'Ocena uspesno unesena' });
      },
      reject: () => {
          this.messageService.add({ severity: 'error', summary: 'Ponisteno', detail: 'Potvrda odbijena', life: 3000 });
      }
    });
  }

  onPredmetChange(event: any) {
    const predmetID = event.value; 
    console.log(predmetID);
    if (predmetID) {
      this.polaganjeService.getPrijavljeniPoPredmetu(predmetID).subscribe(x => {
        this.polaganja = x; 
        console.log("polaganja", this.polaganja);
      });
    }
  }

  getPredmeti(id: number){
    this.nastavnikService.mojiPredmeti(id).subscribe(x=>{
      this.predmeti = x;
    })
  }

  getStudenti(){
    this.studentService.getAll().subscribe(x=>{
      this.studenti = x;
    })
  }

  upisiOcenu(polaganje: any){
    this.visible = true;
    this.izabranoPolaganje = polaganje;
  }

  upisOcene(){
    if (this.izabranoPolaganje && this.izabranoPolaganje.id) {
      const updatedPolaganje = {
        ...this.izabranoPolaganje, 
        konacnaOcena: this.rezultat.ocena, 
        bodovi: this.rezultat.osvojeniBodovi, 
        napomena: this.rezultat.napomena 
      };
      console.log(updatedPolaganje);
      this.polaganjeService.update(updatedPolaganje.id, updatedPolaganje).subscribe({
        next: () => {
          this.getPredmeti(this.profId);
          this.messageService.add({severity:'success', summary: 'Uspeh', detail: 'Ocena uspešno upisana'});
          if(this.izabraniPredmetID){
            this.polaganjeService.getPrijavljeniPoPredmetu(this.izabraniPredmetID).subscribe(x => {
              this.polaganja = x; 
              console.log("polaganja", this.polaganja);
            });
          }
          this.ponistiDialog();
        },
        error: () => {
          this.messageService.add({severity:'error', summary: 'Greška', detail: 'Greška prilikom upisa ocene'});
          this.ponistiDialog();
        }
      });
    } else {
      this.messageService.add({severity:'error', summary: 'Greška', detail: 'Polaganje nema definisan ID'});
      this.ponistiDialog();
    }
  }

  ponistiDialog(){
    this.visible = false;
    this.izabranoPolaganje = {};
    this.rezultat = {
      osvojeniBodovi: 0,
      ocena: 0,
      napomena: ""
    }
  }
}
