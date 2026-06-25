import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DividerModule } from 'primeng/divider';
import { DataViewModule } from 'primeng/dataview';
import { NgFor, NgIf } from '@angular/common';
import { Obavestenje } from '../../../model/obavestenje';
import { DropdownModule } from 'primeng/dropdown';
import { Predmet } from '../../../model/academic/predmet';
import { PredmetService } from '../../../services/predmet.service';
import { ObavestenjeService } from '../../../services/obavestenje.service';
import { ButtonModule } from 'primeng/button';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { StudentiService } from '../../../services/studenti.service';
import { LoginService } from '../../../services/auth/login.service';
import { NastavnikService } from '../../../services/nastavnik.service';
import { FormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { Nastavnik } from '../../../model/users/nastavnik';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-obavestenja-prof',
  standalone: true,
  imports: [NgFor, DataViewModule, DividerModule, DropdownModule, ButtonModule, ConfirmPopupModule, ToastModule, DialogModule, NgIf, FormsModule, CalendarModule],
  templateUrl: './obavestenja-prof.component.html',
  styleUrl: './obavestenja-prof.component.css',
  providers: [ConfirmationService, MessageService]
})
export class ObavestenjaProfComponent implements OnInit{
  visible: boolean = false;

  mojiPredmeti: Predmet[] | undefined;
  nepolozeniPredmeti: Predmet[] | undefined;

  filtriranaObavestenja: Obavestenje[] | undefined;
  predmetnaObavestenja: Obavestenje[] = [];

  izabraniPredmetID: number | undefined;
  izabraniPredmet: Predmet | undefined;

  nastavnik: Nastavnik | undefined;

  novoObavestenje: Obavestenje = {
    datum: new Date(),
    sadrzaj: '',
    naslov: '',
    slika: '',
    vremePocetka: new Date(),
    vremeKraja: new Date(),
    predmet: undefined
  }

  constructor(private confirmationService: ConfirmationService, private messageService: MessageService,
    private nastavnikService: NastavnikService, private loginService: LoginService, 
    private obavestenjeService: ObavestenjeService, private predmetService: PredmetService){}

  ngOnInit(): void {
    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      const id = parsedUser.id;
      this.getPredmeti(id);
    }
  }

  getPredmeti(id: number): void {
    this.nastavnikService.mojiPredmeti(id).subscribe(predmeti => {
      this.mojiPredmeti = predmeti;

      if (this.mojiPredmeti && this.mojiPredmeti.length > 0) {
        this.izabraniPredmetID = this.mojiPredmeti[0].id;

        this.getObavestenja();
      }
    });
  }

  onPredmetChange(event: any) {
    if (event.value) {
      this.izabraniPredmetID = event.value;
      this.filterObavestenja();
    }
  }


  getNastavnik(id: number){
    this.nastavnikService.getById(id).subscribe(x=>{
      this.nastavnik = x;
    })
  }

  getObavestenja(){
      this.obavestenjeService.getAll().subscribe(x=>{
        this.predmetnaObavestenja = x;
        this.filterObavestenja();
      })
  }

  filterObavestenja(): void {
    if (this.izabraniPredmetID !== undefined) {
      this.filtriranaObavestenja = this.predmetnaObavestenja.filter(obavestenje =>
        obavestenje.predmet?.id === this.izabraniPredmetID
      );
    } else {
      this.filtriranaObavestenja = [];
    }
  }
  
  

  ukloniObavestenje(event: Event, obavestenje: Obavestenje) {
    this.confirmationService.confirm({
        target: event.target as EventTarget,
        message: 'Zalite da uklonite ovo obavestenje?',
        icon: 'pi pi-info-circle',
        acceptButtonStyleClass: 'p-button-danger p-button-sm',
        accept: () => {
          if(obavestenje.id){
            this.obavestenjeService.delete(obavestenje.id).subscribe(x=>{
              this.messageService.add({ severity: 'info', summary: 'Uklonjeno!', detail: 'Obavestenje uspesno uklonjeno.', life: 3000 });
              this.getObavestenja();
            })
          }  
        },
        reject: () => {
            this.messageService.add({ severity: 'error', summary: 'Ponisteno!', detail: 'Uklanjanje ponisteno.', life: 3000 });
        }
    });
  }

  obavestenjeDialog(){
    this.visible = true;
  }

  dodajObavestenje(){
    this.obavestenjeService.create(this.novoObavestenje).subscribe({
      next: (response) => {
        this.messageService.add({ severity: 'success', summary: 'Uspešno', detail: 'Obaveštenje je uspešno dodato!' });
        this.getObavestenja();
        this.visible = false;
        this.resetForm();
      },
      error: (error) => {
        this.messageService.add({ severity: 'error', summary: 'Greška', detail: 'Došlo je do greške prilikom dodavanja obaveštenja.' });
        this.resetForm();
      }
    });
  }

  resetForm() {
    this.novoObavestenje = {
      datum: new Date(),
      sadrzaj: '',
      naslov: '',
      slika: '',
      vremePocetka: new Date(),
      vremeKraja: new Date(),
      predmet: undefined
    };
  }

  prilagodiDatum(date: any): string {
    let d: Date;

    if (typeof date === 'string') {
      d = new Date(date);
    } else if (date instanceof Date) {
      d = date;
    } else {
      console.error('Nevalidan datum:', date);
      return '';
    }
  
    if (isNaN(d.getTime())) {
      console.error('Nevalidan datum:', date);
      return '';
    }
  
    const hours = d.getUTCHours().toString().padStart(2, '0');
    const minutes = d.getUTCMinutes().toString().padStart(2, '0');
  
    const day = d.getUTCDate().toString().padStart(2, '0');
    const month = (d.getUTCMonth() + 1).toString().padStart(2, '0'); 
    const year = d.getUTCFullYear();
  
    return `${hours}:${minutes}, ${day}-${month}-${year}`;
  }

}
