import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { StudijskiProgramService } from '../../../services/studijski-program.service';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { NastavniMaterijal } from '../../../model/academic/nastavniMaterijal';
import { InputGroupModule } from 'primeng/inputgroup';
import { FormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { StudijskiProgram } from '../../../model/academic/studijskiProgram';
import { DropdownModule } from 'primeng/dropdown';
import { Fakultet } from '../../../model/academic/fakultet';
import { FakultetService } from '../../../services/fakultet.service';
import { ConfirmPopupModule } from 'primeng/confirmpopup';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-a-studijski-programi',
  standalone: true,
  imports: [TableModule, ButtonModule, InputGroupModule, FormsModule, DialogModule, ToastModule, DropdownModule, ConfirmPopupModule],
  templateUrl: './a-studijski-programi.component.html',
  styleUrl: './a-studijski-programi.component.css',
  providers: [MessageService, ConfirmationService]
})
export class AStudijskiProgramiComponent implements OnInit{
  smerovi: any[] = [];
  filtriraniSmerovi: any[] = [];
  fakulteti: Fakultet[] = [];

  visible: boolean = false;
  dodajSmerVisible: boolean = false;

  noviSmer: any ={};

  pretraga = {
    naziv: "",
    sifra: "",
    fakultet: "",
    rukovodilac: ""
  }

  izabraniSmer : any;
  smerZaIzmenu: any = {
    naziv: '',
    rukovodilac: '',
    sifraSP: ''
  }

  constructor(private smerService: StudijskiProgramService, private messageService: MessageService, private fakultetServica: FakultetService,
    private confirmationService: ConfirmationService
  ){}

  ngOnInit(): void {
    this.getSmerovi();
    this.getFakulteti();
  }

  getSmerovi(){
    this.smerService.getAll().subscribe(x=>{
      this.smerovi = x;
      this.filtriraniSmerovi = this.smerovi;
    })
  }

  getFakulteti(){
    this.fakultetServica.getAll().subscribe(x=>{
      this.fakulteti = x;
    })
  }


  izmeniSmer(smer: StudijskiProgram){
    this.visible = true;
    this.izabraniSmer = {...smer}
    this.smerZaIzmenu = this.izabraniSmer;

  }

  pretragaSmera(){
    this.filtriraniSmerovi = this.smerovi.filter(s =>
      (this.pretraga.naziv ? s.naziv.toLowerCase().includes(this.pretraga.naziv.toLowerCase()) : true) &&
      (this.pretraga.sifra ? s.sifraSP.toLowerCase().includes(this.pretraga.sifra.toLowerCase()) : true) &&
      (this.pretraga.fakultet ? s.fakultet.toLowerCase().includes(this.pretraga.fakultet.toLowerCase()) : true) &&
      (this.pretraga.rukovodilac ? s.rukovodilac.toLowerCase().includes(this.pretraga.rukovodilac.toLowerCase()) : true) 
    );
  }

  ponistiPretragu(){
    this.pretraga = {
      naziv: "",
      sifra: "",
      fakultet: "",
      rukovodilac: ""
    }
    this.filtriraniSmerovi = this.smerovi;
  }

  izmenaSmera(){
    if (this.izabraniSmer) {
      const updatedSmer = {
        ...this.izabraniSmer,
        naziv: this.smerZaIzmenu.naziv,
        sifraSP: this.smerZaIzmenu.sifraSP,
        rukovodilac: this.smerZaIzmenu.rukovodilac
      };

      console.log(updatedSmer);

      this.smerService.update(updatedSmer.id, updatedSmer).subscribe({
        next: () => {
          this.getSmerovi();
          this.ponistiDialog();
          this.messageService.add({ severity: 'success', summary: 'Uspešno izmenjeno', detail: 'Podaci korisnika su izmenjeni' });
        },
        error: err => {
          this.ponistiDialog();
          this.messageService.add({ severity: 'error', summary: 'Greška pri izmeni', detail: 'Došlo je do greške.' });
        }
      });
    }
  }

  ponistiDialog(){
    this.visible = false;
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
    this.noviSmer = {};
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
