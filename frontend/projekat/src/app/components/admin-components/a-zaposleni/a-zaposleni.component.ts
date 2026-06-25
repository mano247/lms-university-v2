import { NgClass, NgIf } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { Nastavnik } from '../../../model/users/nastavnik';
import { StudentskaSluzba } from '../../../model/users/studentskaSluzba';
import { Administrator } from '../../../model/users/administrator';
import { NastavnikService } from '../../../services/nastavnik.service';
import { StudentskaSluzbaService } from '../../../services/studentska-sluzba.service';
import { AdministratorService } from '../../../services/administrator.service';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { InputGroupModule } from 'primeng/inputgroup';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-a-zaposleni',
  standalone: true,
  imports: [DropdownModule, FormsModule, NgIf, TableModule, NgClass, ButtonModule, DialogModule, ToastModule, ConfirmPopupModule, InputGroupModule],
  templateUrl: './a-zaposleni.component.html',
  styleUrl: './a-zaposleni.component.css',
  providers: [MessageService, ConfirmationService]
})
export class AZaposleniComponent implements OnInit{
  profesori: Nastavnik[] = [];
  sluzba: StudentskaSluzba[] = [];
  administratori: Administrator[] = [];

  filtriraniProfesori: Nastavnik[] = [];
  filtriranaSluzba: StudentskaSluzba[] = [];
  filtriraniAdministratori: Administrator[] = [];

  izmeniProfesoraVisible: boolean = false;

  izmeniSSluzbuVisible: boolean = false;

  izmeniAdminaVisible: boolean = false;

  pretragaProfesora:any = {};
  pretragaSluzbe:any = {};
  pretragaAdmina:any = {};

  noviProfesor:any = {};
  novaSluzba:any = {};
  noviAdmin:any = {};

  zaposleniSelect: { label: string, value: string }[] = [
    { label: 'Profesori', value: 'profesori' },
    { label: 'Studentska Sluzba', value: 'ssluzba' },
    { label: 'Adminstracija', value: 'administratori' }
  ];

  selected: string = "profesori"

  constructor(private nastavnikService: NastavnikService, private ssluzbaService: StudentskaSluzbaService, 
    private adminService: AdministratorService, private messageService: MessageService, private confirmationService: ConfirmationService){}

  ngOnInit(): void {
    this.getProfesori();
    this.getSluzba();
    this.getAdmin();
  }

  getPodaci(){
    this.getProfesori();
    this.getSluzba();
    this.getAdmin();
  }

  getProfesori(){
    this.nastavnikService.getAll().subscribe(x=>{
      this.profesori = x;
      this.filtriraniProfesori = this.profesori;
      // console.log(this.profesori);
    })
  }

  getSluzba(){
    this.ssluzbaService.getAll().subscribe(x=>{
      this.sluzba = x;
      // console.log(this.sluzba);
      this.filtriranaSluzba = this.sluzba;
    })
  }

  getAdmin(){
    this.adminService.getAll().subscribe(x=>{
      this.administratori = x;
      console.log(this.administratori);
      this.filtriraniAdministratori = this.administratori;
    })
  }


  hideDialogProfesor(){
    this.izmeniProfesoraVisible = false;
  }

  izmeniProfesora(profesor: Nastavnik){
    this.noviProfesor = {...profesor};
    console.log(this.noviProfesor);
    this.izmeniProfesoraVisible = true;
  }

  izmenaProfesora(){
    this.nastavnikService.update(this.noviProfesor.id, this.noviProfesor).subscribe({
      next: (x) => {
        this.messageService.add({
          severity: 'success', 
          summary: 'Profesor izmenjen', 
          detail: 'Profesor je uspešno izmenjen.'
        });
        this.noviProfesor = {}; 
        this.getPodaci();    
        this.hideDialogProfesor();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error', 
          summary: 'Greška', 
          detail: 'Došlo je do greške pri izmeni profesora.'
        });
        console.error('Greška:', err); 
      }
    })
  }

  ukloniProfesora(id: number, event: Event){
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Zelite da uklonite izabranog profesora?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.nastavnikService.delete(id).subscribe({
               next: () => {
                 this.getPodaci();
                 this.messageService.add({ severity: 'info', summary: 'Uspešno uklonjeno', detail: 'Profesor je uklonjen' });
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



  hideDialogSluzba(){
    this.izmeniSSluzbuVisible = false;
  }

  izmeniSluzbu(sluzba: StudentskaSluzba){
    this.novaSluzba = {...sluzba};
    this.izmeniSSluzbuVisible = true;
  }

  izmenaSluzbe(){
    this.ssluzbaService.update(this.novaSluzba.id, this.novaSluzba).subscribe({
      next: (x) => {
        this.messageService.add({
          severity: 'success', 
          summary: 'Sluzba izmenjena', 
          detail: 'Sluzba je uspešno izmenjena.'
        });
        this.novaSluzba = {}; 
        this.getPodaci();    
        this.hideDialogSluzba();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error', 
          summary: 'Greška', 
          detail: 'Došlo je do greške pri izmeni sluzbe.'
        });
        console.error('Greška:', err); 
      }
    })
  }

  ukloniSluzbu(id: number, event: Event){
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Zelite da uklonite izabranog zaposlenog sluzbe?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.ssluzbaService.delete(id).subscribe({
               next: () => {
                 this.getPodaci();
                 this.messageService.add({ severity: 'info', summary: 'Uspešno uklonjeno', detail: 'Zaposleni je uklonjen' });
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

  hideDialogAdmin(){
    this.izmeniAdminaVisible = false;
  }

  izmeniAdmina(admin: Administrator){
    this.noviAdmin = {...admin};
    this.izmeniAdminaVisible = true;
  }

  izmenaAdmina(){
    this.adminService.update(this.noviAdmin.id, this.noviAdmin).subscribe({
      next: (x) => {
        this.messageService.add({
          severity: 'success', 
          summary: 'Admin izmenjen', 
          detail: 'Admin je uspešno izmenjen.'
        });
        this.noviAdmin = {}; 
        this.getPodaci();    
        this.hideDialogAdmin();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error', 
          summary: 'Greška', 
          detail: 'Došlo je do greške pri izmeni admina.'
        });
        console.error('Greška:', err); 
      }
    })
  }

  ukloniAdmina(id: number, event: Event){
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Zelite da uklonite izabranog admina?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.adminService.delete(id).subscribe({
               next: () => {
                 this.getPodaci();
                 this.messageService.add({ severity: 'info', summary: 'Uspešno uklonjeno', detail: 'Admin je uklonjen' });
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

  pretraziProfesore(){
    this.filtriraniProfesori = this.profesori.filter(p =>
      (this.pretragaProfesora.ime ? p.ime.toLowerCase().includes(this.pretragaProfesora.ime.toLowerCase()) : true) &&
      (this.pretragaProfesora.prezime ? p.prezime.toLowerCase().includes(this.pretragaProfesora.prezime.toLowerCase()) : true) &&
      (this.pretragaProfesora.email ? p.email.toLowerCase().includes(this.pretragaProfesora.email.toLowerCase()) : true) 
    );
  }

  pretraziSluzbe(){
    this.filtriranaSluzba = this.sluzba.filter(s =>
      (this.pretragaSluzbe.ime ? s.ime.toLowerCase().includes(this.pretragaSluzbe.ime.toLowerCase()) : true) &&
      (this.pretragaSluzbe.prezime ? s.prezime.toLowerCase().includes(this.pretragaSluzbe.prezime.toLowerCase()) : true) &&
      (this.pretragaSluzbe.email ? s.email.toLowerCase().includes(this.pretragaSluzbe.email.toLowerCase()) : true) 
    );
  }

  pretraziAdmina(){
    this.filtriraniAdministratori = this.administratori.filter(a =>
      (this.pretragaAdmina.ime ? a.ime.toLowerCase().includes(this.pretragaAdmina.ime.toLowerCase()) : true) &&
      (this.pretragaAdmina.prezime ? a.prezime.toLowerCase().includes(this.pretragaAdmina.prezime.toLowerCase()) : true)
    );
  }

  ponistiPretragu(){
    this.pretragaProfesora= {};
    this.pretragaSluzbe = {};
    this.pretragaAdmina = {};

    this.filtriraniProfesori = this.profesori;
    this.filtriranaSluzba = this.sluzba;
    this.filtriraniAdministratori = this.administratori;
  }

}
