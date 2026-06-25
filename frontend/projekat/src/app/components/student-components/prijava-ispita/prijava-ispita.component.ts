import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { Predmet } from '../../../model/academic/predmet';
import { StudentiService } from '../../../services/studenti.service';
import { PolaganjeService } from '../../../services/polaganje.service';
import { Polaganje } from '../../../model/polaganje';
import { Student } from '../../../model/users/student';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-prijava-ispita',
  standalone: true,
  imports: [TableModule, ButtonModule, NgFor, ConfirmDialogModule, ToastModule, DialogModule, NgIf],
  templateUrl: './prijava-ispita.component.html',
  styleUrl: './prijava-ispita.component.css',
  providers: [ConfirmationService, MessageService]
})
export class PrijavaIspitaComponent implements OnInit{
  balans: number = 1200.0;
  visible: boolean = false;

  mojiPredmeti: any[] = [];
  izabraniPredmet: any | undefined;
  prijavljeniIspiti: any[] = [];

  studentId: number | undefined;
  student: Student | undefined;

  constructor(private confirmationService: ConfirmationService, private messageService: MessageService, 
    private studentService: StudentiService, private polaganjeService: PolaganjeService) {}

  ngOnInit(): void {
    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      this.studentId = parsedUser.id;
      if(this.studentId){
        this.getIspitiZaPrijavu(this.studentId);
        this.getStudent(this.studentId);
        this.getPrijavljeniIspiti(this.studentId);

      }
    }
    
  }

  prijavaIspita(event: Event, predmet: Predmet) {
    this.izabraniPredmet = predmet;

    this.confirmationService.confirm({
        target: event.target as EventTarget,
        message: 'Prijava ispita kosta 1000 dinara, zelite li da nastavite?',
        header: 'Prijava ispita',
        icon: 'pi pi-question-circle',
        acceptIcon:"none",
        rejectIcon:"none",
        rejectButtonStyleClass:"p-button-text",
        accept: () => {
          if (this.balans >= 1000) {
            this.balans -= 1000;
            this.messageService.add({ severity: 'info', summary: 'Prijava uspesna', detail: 'Prijavili ste uspesno ispit' });

            const polaganje: Polaganje = {
              predmet: this.izabraniPredmet,
              student: this.student,
              nastavnik: this.izabraniPredmet.nastavnik
            };



            this.polaganjeService.create(polaganje).subscribe(
              response => {
                this.messageService.add({ severity: 'success', summary: 'Uspesno', detail: 'Ispit prijavljen' });
                if(this.studentId){
                  this.getPrijavljeniIspiti(this.studentId);
                }
              },
              error => {
                this.messageService.add({ severity: 'error', summary: 'Greska', detail: 'Neuspesna prijava ispita' });
              }
            );
            
          } else {
            this.messageService.add({ severity: 'error', summary: 'Nedovoljno sredstava', detail: 'Nemate dovoljno sredstava za uplatu' });
          }
        },
        reject: () => {
          this.messageService.add({ severity: 'error', summary: 'Ponisteno', detail: 'Prijava ispita ponistena', life: 3000 });
        }
    });
  }


  getIspitiZaPrijavu(id: number) {
    this.studentService.getIspitiZaPrijavu(id).subscribe(x => {
      this.mojiPredmeti = x;
      console.log("m.p", this.mojiPredmeti);
    });
  }

  getStudent(id: number){
    this.studentService.getById(id).subscribe(x=>{
      this.student = x;
    })
  }

  getPrijavljeniIspiti(id: number){
    this.polaganjeService.getPrijavljeni(id).subscribe(x=>{
      this.prijavljeniIspiti = x;
    })
  }

  prikaziDialogZaUplatu(){
    this.visible = true;
  }

  potvrdiDialog(event: Event, iznos: number){
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Zelite li da izvrsite uplatu?',
      header: 'Potvrda',
      icon: 'pi pi-question-circle',
      acceptIcon:"none",
      rejectIcon:"none",
      rejectButtonStyleClass:"p-button-text",
      accept: () => {
          this.balans += iznos;
          this.visible = false;
          this.messageService.add({ severity: 'info', summary: 'Potvrdjeno!', detail: 'Uplata sredstava izvrsena' });
      },
      reject: () => {
          this.messageService.add({ severity: 'error', summary: 'Ponisteno!', detail: 'Uplata ponistena', life: 3000 });
      }
    });
  }

  ponistiUplatu(){
    this.visible = false;
  }

  jePredmetPrijavljen(predmet: Predmet): boolean {
    return this.prijavljeniIspiti.some(prijavljeni => prijavljeni.predmet.id === predmet.id);
  }

}
