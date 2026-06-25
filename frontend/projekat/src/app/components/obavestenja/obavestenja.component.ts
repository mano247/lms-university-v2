import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { Obavestenje } from '../../model/obavestenje';
import { DataViewModule } from 'primeng/dataview';
import { NgFor } from '@angular/common';
import { ObavestenjeService } from '../../services/obavestenje.service';
import { DividerModule } from 'primeng/divider';
import { GlobalnaObavestenjaService } from '../../services/globalna-obavestenja.service';
import { GObavestenje } from '../../model/gObavestenje';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { FormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';


@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-obavestenja',
  standalone: true,
  imports: [NgFor, DataViewModule, DividerModule, ButtonModule, DialogModule, FormsModule, CalendarModule, ToastModule],
  templateUrl: './obavestenja.component.html',
  styleUrl: './obavestenja.component.css',
  providers: [MessageService]
})

export class ObavestenjaComponent implements OnInit{
  obavestenja: GObavestenje[] = [];
  displayDialog: boolean = false;

  novoObavestenje: GObavestenje = {
    datum: new Date(),
    sadrzaj: '',
    naslov: '',
    slika: '',
    vremePocetka: new Date(),
    vremeKraja: new Date()
  }

  constructor(private globalObavestenjeService: GlobalnaObavestenjaService, private messageService: MessageService){}

  ngOnInit(): void {
    this.getObavestenja();
  }

  getObavestenja(){
    this.globalObavestenjeService.getAll().subscribe(x=>{
      this.obavestenja = x;
    })
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

  showDialog(){
    this.displayDialog = true;
  }

  dodajObavestenje() {
    this.globalObavestenjeService.create(this.novoObavestenje).subscribe({
      next: (response) => {
        this.getObavestenja();
        this.resetModel();
        this.hideDialog();
        this.messageService.add({
          severity: 'success',
          summary: 'Uspešno dodato',
          detail: 'Obaveštenje je uspešno dodato.'
        });
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Greška',
          detail: 'Došlo je do greške pri dodavanju obaveštenja.'
        });
      }
    });
  }

  hideDialog(){
    this.displayDialog = false;
  }

  resetModel(){
    this.novoObavestenje = {
      datum: new Date(),
      sadrzaj: '',
      naslov: '',
      slika: '',
      vremePocetka: new Date(),
      vremeKraja: new Date()
    }
  }
}
