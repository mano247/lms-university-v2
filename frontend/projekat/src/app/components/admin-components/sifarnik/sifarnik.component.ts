import { NgClass, NgIf } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { FakultetService } from '../../../services/fakultet.service';
import { Fakultet } from '../../../model/academic/fakultet';
import { StudijskiProgram } from '../../../model/academic/studijskiProgram';
import { Predmet } from '../../../model/academic/predmet';
import { StudijskiProgramService } from '../../../services/studijski-program.service';
import { PredmetService } from '../../../services/predmet.service';
import { TableModule } from 'primeng/table';
import { PolaganjeService } from '../../../services/polaganje.service';
import { InputGroupModule } from 'primeng/inputgroup';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Univerzitet } from '../../../model/academic/univerzitet';
import { UniverzitetService } from '../../../services/univerzitet.service';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { NastavniMaterijal } from '../../../model/academic/nastavniMaterijal';
import { NastavnikService } from '../../../services/nastavnik.service';
import { Nastavnik } from '../../../model/users/nastavnik';
import { CalendarModule } from 'primeng/calendar';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-sifarnik',
  standalone: true,
  imports: [DropdownModule, FormsModule, NgIf, TableModule, NgClass, InputGroupModule, ButtonModule, DialogModule, 
    ToastModule, ConfirmPopupModule, CalendarModule],
  templateUrl: './sifarnik.component.html',
  styleUrl: './sifarnik.component.css',
  providers: [MessageService, ConfirmationService]
})
export class SifarnikComponent implements OnInit{
  fakulteti: Fakultet[] = [];
  smerovi: StudijskiProgram[] = [];
  predmeti: Predmet[] = [];
  univerzitet: Univerzitet | undefined;
  profesori: Nastavnik[] = [];

  filtriraniFakulteti: Fakultet[] = [];
  filtriraniSmerovi: StudijskiProgram[] = [];
  filtriraniPredmeti: Predmet[] = [];

  pretraga = {
    naziv: "",
    sifra: ""
  }

  visible: boolean = false;
  dodajFakultetVisible: boolean = false;
  izmeniFakultetVisible: boolean = false;
  dodajSmerVisible: boolean = false;
  izmeniSmerVisible: boolean = false;
  dodajPredmetVisible: boolean = false;
  izmeniPredmetVisible: boolean = false;

  dialogHeader: string = '';
  trenutniElement: any = null;

  novaSifra: string = "";
  noviFakultet:any = {};
  noviSmer:any = {};
  noviPredmet:any = {};

  sifarnikSelect: { label: string, value: string }[] = [
    { label: 'Fakulteti', value: 'fakulteti' },
    { label: 'Smerovi', value: 'smerovi' },
    { label: 'Predmeti', value: 'predmeti' }
  ];

  selected: string = "fakulteti";

  constructor(private fakultetService: FakultetService, private smerService: StudijskiProgramService, 
    private predmetService: PredmetService, private messageService: MessageService, 
    private univerzitetService: UniverzitetService, private confirmationService: ConfirmationService,
    private profesorService: NastavnikService){  }

  ngOnInit(): void {
  this.getPodaci();    

  }

  getPodaci(){
    this.getFakulteti();
    this.getSmerovi();
    this.getPredmeti();
    this.getUniverzitet();
    this.getProfesori();
  }

  getUniverzitet(){
    this.univerzitetService.getById(1).subscribe(x=>{
      this.univerzitet = x;
    })
  }

  getFakulteti(){
    this.fakultetService.getAll().subscribe(x=>{
      this.fakulteti = x;
      this.filtriraniFakulteti = this.fakulteti;
      // console.log(this.fakulteti);
    })
  }

  getSmerovi(){
    this.smerService.getAll().subscribe(x=>{
      this.smerovi = x;
      this.filtriraniSmerovi = this.smerovi;
      // console.log(this.smerovi);
    })
  }

  getPredmeti(){
    this.predmetService.getAll().subscribe(x=>{
      this.predmeti = x;
      this.filtriraniPredmeti = this.predmeti
      // console.log(this.predmeti);
    })
  }

  getProfesori(){
    this.profesorService.getAll().subscribe(x=>{
      this.profesori = x;
    })
  }

  pretragaFakulteta(){
    this.filtriraniFakulteti = this.fakulteti.filter(f =>
      (this.pretraga.naziv ? f.naziv.toLowerCase().includes(this.pretraga.naziv.toLowerCase()) : true) &&
      (this.pretraga.sifra ? f.sifraFakulteta.toLowerCase().includes(this.pretraga.sifra.toLowerCase()) : true)
    );
  }

  pretragaSmerova(){
    this.filtriraniSmerovi = this.smerovi.filter(s =>
      (this.pretraga.naziv ? s.naziv.toLowerCase().includes(this.pretraga.naziv.toLowerCase()) : true) &&
      (this.pretraga.sifra ? s.sifraSP.toLowerCase().includes(this.pretraga.sifra.toLowerCase()) : true)
    );
  }

  pretragaPredmeta(){
    this.filtriraniPredmeti = this.predmeti.filter(p =>
      (this.pretraga.naziv ? p.naziv.toLowerCase().includes(this.pretraga.naziv.toLowerCase()) : true) &&
      (this.pretraga.sifra ? p.sifraPredmeta.toLowerCase().includes(this.pretraga.sifra.toLowerCase()) : true)
    );
  }

  ponistiPretragu(){
    this.pretraga = {
      naziv: "",
      sifra: ""
    }
    this.filtriraniFakulteti = this.fakulteti;
    this.filtriraniSmerovi = this.smerovi;
    this.filtriraniPredmeti = this.predmeti
  }

  izmeniSifruFakulteta(fakultet: Fakultet) {
    this.visible = true;
    this.trenutniElement = fakultet;
    this.dialogHeader = 'Izmeni šifru fakulteta';
    this.novaSifra = fakultet.sifraFakulteta;
  }

  izmeniSifruSmer(smer: StudijskiProgram) {
    this.visible = true;
    this.trenutniElement = smer;
    this.dialogHeader = 'Izmeni šifru smerova';
    this.novaSifra = smer.sifraSP;
  }

  izmeniSifruPredmeta(predmet: Predmet) {
    this.visible = true;
    this.trenutniElement = predmet;
    this.dialogHeader = 'Izmeni šifru predmeta';
    this.novaSifra = predmet.sifraPredmeta;
  }

  sacuvajSifru() {
    if(this.trenutniElement.sifraFakulteta){
      const updatedFakultet = { ...this.trenutniElement, univerzitet: this.univerzitet ,sifraFakulteta: this.novaSifra };
      console.log(updatedFakultet);
      this.fakultetService.update(updatedFakultet.id, updatedFakultet).subscribe(x=>{
        this.getPodaci();
        this.trenutniElement = null;
        this.ponistiDialog();
      })
      
    }else if(this.trenutniElement.sifraSP){
      const updatedSmer = { ...this.trenutniElement, sifraSP: this.novaSifra };
      console.log(updatedSmer);
      this.smerService.update(updatedSmer.id, updatedSmer).subscribe(x=>{
        this.getPodaci();
        this.trenutniElement = null;
        this.ponistiDialog();
      })

    }else if(this.trenutniElement.sifraPredmeta){
      const updatedPredmet = { ...this.trenutniElement, sifraPredmeta: this.novaSifra, studijskiProgram: {id:this.trenutniElement.smer.id} };
      console.log(updatedPredmet);
      this.predmetService.update(updatedPredmet.id, updatedPredmet).subscribe(x=>{
        this.getPodaci();
        this.trenutniElement = null;
        this.ponistiDialog();
      })
    }
  }

  ponistiDialog(){
    this.visible = false;
  }

  dodajFakultetDialog(){
    this.dodajFakultetVisible = true;
  }

  dodajFakultet(){
    const newFakultet = {...this.noviFakultet, univerzitet: this.univerzitet}
    this.fakultetService.create(newFakultet).subscribe({
      next: (x) => {
        this.messageService.add({
          severity: 'success', 
          summary: 'Fakultet dodat', 
          detail: 'Fakultet je uspešno dodat.'
        });
        this.noviFakultet = {}; 
        this.getFakulteti();    
        this.hideDialogFakultet();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error', 
          summary: 'Greška', 
          detail: 'Došlo je do greške pri dodavanju fakulteta.'
        });
        console.error('Greška:', err); 
      }
    })
  }

  hideDialogFakultet(){
    this.dodajFakultetVisible = false;
    this.izmeniFakultetVisible = false;
    this.noviFakultet = {};
  }

  izmeniFakultet(fakultet: Fakultet){
    this.noviFakultet = {...fakultet};
    console.log(this.noviFakultet);
    this.izmeniFakultetVisible = true;
  }

  izmenaFakulteta(){
    const updatedFakultet = { ...this.noviFakultet, univerzitet: this.univerzitet}
    this.fakultetService.update(updatedFakultet.id, updatedFakultet).subscribe({
      next: (x) => {
        this.messageService.add({
          severity: 'success', 
          summary: 'Fakultet izmenjen', 
          detail: 'Fakultet je uspešno izmenjen.'
        });
        this.noviFakultet = {}; 
        this.getFakulteti();    
        this.hideDialogFakultet();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error', 
          summary: 'Greška', 
          detail: 'Došlo je do greške pri izmeni fakulteta.'
        });
        console.error('Greška:', err); 
      }
    })
    this.hideDialogFakultet();
  }

  ukloniFakultet(id: number, event: Event) {
    this.confirmationService.confirm({
        target: event.target as EventTarget,
        message: 'Zelite da uklonite izabrani fakultet?',
        icon: 'pi pi-info-circle',
        acceptButtonStyleClass: 'p-button-danger p-button-sm',
        accept: () => {
          this.fakultetService.delete(id).subscribe({
                 next: () => {
                   this.getFakulteti();
                   this.messageService.add({ severity: 'info', summary: 'Uspešno uklonjeno', detail: 'Fakultet uklonjen' });
                 },
                  error: err => {
                   this.messageService.add({ severity: 'error', summary: 'Greška pri uklanjanju', detail: 'Došlo je do greške.' });
                 }
               });
        },
        reject: () => {
            this.messageService.add({ severity: 'error', summary: 'Ponisteno', detail: 'Uklanjanje ponisteno', life: 3000 });
        }
    });
  } 



  dodajPredmetDialog(){
    this.dodajPredmetVisible = true;
  }

  dodajPredmet(){
    this.noviPredmet = { ...this.noviPredmet, studijskiProgram: {id:this.noviPredmet.smer.id}}
    console.log("np", this.noviPredmet);
    this.predmetService.create(this.noviPredmet).subscribe({
      next: (x) => {
        this.messageService.add({
          severity: 'success', 
          summary: 'Predmet dodat', 
          detail: 'Predmet je uspešno dodat.'
        });
        this.noviPredmet = {}; 
        this.getPredmeti();    
        this.hideDialogPredmet();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error', 
          summary: 'Greška', 
          detail: 'Došlo je do greške pri dodavanju predmeta.'
        });
        console.error('Greška:', err); 
      }
    })
  }

  hideDialogPredmet(){
    this.dodajPredmetVisible = false;
    this.izmeniPredmetVisible = false;
    this.noviPredmet = {};
  }

  izmeniPredmet(predmet: Predmet){
    this.noviPredmet = {...predmet};
    this.izmeniPredmetVisible = true;
  }

  izmenaPredmeta(){
    const predmetIzmena = {...this.noviPredmet, studijskiProgram: {id:this.noviPredmet.smer.id}}
    this.predmetService.update(this.noviPredmet.id, predmetIzmena).subscribe({
      next: (x) => {
        this.messageService.add({
          severity: 'success', 
          summary: 'Predmet izmenjen', 
          detail: 'Predmet je uspešno izmenjen.'
        });
        this.noviPredmet = {}; 
        this.getPredmeti();    
        this.hideDialogPredmet();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error', 
          summary: 'Greška', 
          detail: 'Došlo je do greške pri izmeni predmeta.'
        });
        console.error('Greška:', err); 
      }
    })
  }

  ukloniPredmet(id: number, event: Event) {
    this.confirmationService.confirm({
        target: event.target as EventTarget,
        message: 'Zelite da uklonite izabrani predmet?',
        icon: 'pi pi-info-circle',
        acceptButtonStyleClass: 'p-button-danger p-button-sm',
        accept: () => {
          this.predmetService.delete(id).subscribe({
                 next: () => {
                   this.getPredmeti();
                   this.messageService.add({ severity: 'info', summary: 'Uspešno uklonjeno', detail: 'Predmet uklonjen' });
                 },
                  error: err => {
                   this.messageService.add({ severity: 'error', summary: 'Greška pri uklanjanju', detail: 'Došlo je do greške.' });
                 }
               });
        },
        reject: () => {
            this.messageService.add({ severity: 'error', summary: 'Ponisteno', detail: 'Uklanjanje ponisteno', life: 3000 });
        }
    });
  } 


  dodajSmerDialog(){
    this.dodajSmerVisible = true;
  }

  dodajSmer(){
    this.smerService.create(this.noviSmer).subscribe({
      next: (x) => {
        this.messageService.add({
          severity: 'success', 
          summary: 'Smer dodat', 
          detail: 'Smer je uspešno dodat.'
        });
        this.noviSmer = {}; 
        this.getSmerovi();    
        this.hideDialogSmer();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error', 
          summary: 'Greška', 
          detail: 'Došlo je do greške pri dodavanju smera.'
        });
        console.error('Greška:', err); 
      }
    })
  }

  hideDialogSmer(){
    this.dodajSmerVisible = false;
    this.izmeniSmerVisible = false;
    this.noviSmer = {};
  }

  izmeniSmer(smer: NastavniMaterijal){
    this.noviSmer = {...smer};
    this.izmeniSmerVisible = true;
  }

  izmenaSmera(){
    this.smerService.update(this.noviSmer.id, this.noviSmer).subscribe({
      next: (x) => {
        this.messageService.add({
          severity: 'success', 
          summary: 'Smer izmenjen', 
          detail: 'Smer je uspešno izmenjen.'
        });
        this.noviSmer = {}; 
        this.getSmerovi();    
        this.hideDialogSmer();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error', 
          summary: 'Greška', 
          detail: 'Došlo je do greške pri izmeni smera.'
        });
        console.error('Greška:', err); 
      }
    })
  }

  ukloniSmer(id: number, event: Event) {
    this.confirmationService.confirm({
        target: event.target as EventTarget,
        message: 'Zelite da uklonite izabrani smer?',
        icon: 'pi pi-info-circle',
        acceptButtonStyleClass: 'p-button-danger p-button-sm',
        accept: () => {
          this.smerService.delete(id).subscribe({
                 next: () => {
                   this.getSmerovi();
                   this.messageService.add({ severity: 'info', summary: 'Uspešno uklonjeno', detail: 'Smer uklonjen' });
                 },
                  error: err => {
                   this.messageService.add({ severity: 'error', summary: 'Greška pri uklanjanju', detail: 'Došlo je do greške.' });
                 }
               });
        },
        reject: () => {
            this.messageService.add({ severity: 'error', summary: 'Ponisteno', detail: 'Uklanjanje ponisteno', life: 3000 });
        }
    });
  } 
}
