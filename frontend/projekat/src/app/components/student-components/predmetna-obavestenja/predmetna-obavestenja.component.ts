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
import { FormsModule } from '@angular/forms';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-predmetna-obavestenja',
  standalone: true,
  imports: [NgFor, DataViewModule, DividerModule, DropdownModule, ButtonModule, ConfirmPopupModule, ToastModule, DialogModule, NgIf, FormsModule],
  templateUrl: './predmetna-obavestenja.component.html',
  styleUrl: './predmetna-obavestenja.component.css',
  providers: [ConfirmationService, MessageService]
})
export class PredmetnaObavestenjaComponent implements OnInit{
  visible: boolean = false;

  mojiPredmeti: Predmet[] | undefined;
  aktivniPrdmeti: Predmet[] | undefined;
  predmetnaObavestenja: Obavestenje[] = [];
  filtriranaObavestenja: Obavestenje[] = []; 
  izabraniPredmetID: number | undefined;


  constructor(private confirmationService: ConfirmationService, private messageService: MessageService,
    private studentService: StudentiService, private predmetnaObavestenjaService: ObavestenjeService){}

    ngOnInit(): void {
      const user = localStorage.getItem('user');
      if (user) {
        const parsedUser = JSON.parse(user);
        const id = parsedUser.id;
        this.getAktivniPredmeti(id);
      }
  
      this.getAllObavestenja(); 
    }

    getAllObavestenja(){
      this.predmetnaObavestenjaService.getAll().subscribe(x => {
        this.predmetnaObavestenja = x; 
      });
    }

  getAktivniPredmeti(id: number) {
    this.studentService.getAktivniPredmeti(id).subscribe(x => {
      const uniquePredmeti = x.filter((predmet, index, self) =>
        index === self.findIndex((t) => (
          t.id === predmet.id
        ))
      );
      
      this.aktivniPrdmeti = uniquePredmeti;

      if (this.aktivniPrdmeti && this.aktivniPrdmeti.length > 0) {
        this.izabraniPredmetID = this.aktivniPrdmeti[0].id;

        setTimeout(() => {
          this.filterObavestenja();
        }, 0);
      }

    });
  }

  onPredmetChange(event: any) {
    if (event.value) {
      this.izabraniPredmetID = event.value;
      this.filterObavestenja();
    }
  }

  filterObavestenja(){
    if (this.izabraniPredmetID !== undefined) {
      this.filtriranaObavestenja = this.predmetnaObavestenja.filter(x => 
        x.predmet?.id === this.izabraniPredmetID
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
            this.messageService.add({ severity: 'info', summary: 'Uklonjeno!', detail: 'Obavestenje uspesno uklonjeno.', life: 3000 });
            // dodati delete zahtev
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
    this.visible = false;
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
