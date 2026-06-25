import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { RegistrovaniKorisnikService } from '../../../services/registrovani-korisnik.service';
import { TableModule } from 'primeng/table';
import { InputGroupModule } from 'primeng/inputgroup';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { RegistrovaniKorisnik } from '../../../model/users/registrovaniKorisnik';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { DialogModule } from 'primeng/dialog';
import { NgIf } from '@angular/common';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { DropdownModule } from 'primeng/dropdown';
import { AdministratorService } from '../../../services/administrator.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-a-reg-korisnici',
  standalone: true,
  imports: [TableModule, InputGroupModule, FormsModule, ButtonModule, ToastModule, DialogModule, NgIf, ConfirmPopupModule, DropdownModule],
  templateUrl: './a-reg-korisnici.component.html',
  styleUrl: './a-reg-korisnici.component.css',
  providers: [ConfirmationService, MessageService]
})
export class ARegKorisniciComponent implements OnInit{
  korisnici: any[] = [];
  filtriraniKorisnici: any[] = [];

  izabraniKorisnik: any;

  dodajKorisnikaDialog: boolean = false;

  visible: boolean = false;
  tipDialog: boolean = false;

  korisnikZaIzmenuTipa: any = {};
  tip: any;

  tipovi = [
    {
      naziv: "student",
      tip: "student_premission"
    },
    {
      naziv: "nastavnik",
      tip: "nastavnik_premission"
    },
    {
      naziv: "sluzba",
      tip: "studentskaSluzba_premission"
    },
    {
      naziv: "admin",
      tip: "administrator_premission"
    },

  ]

  noviKorisnik: any = {};

  pretraga = {
    korisnickoIme: "",
    email: ""
  }

  constructor(private rkService: RegistrovaniKorisnikService, private confirmationService: ConfirmationService, 
    private messageService: MessageService, private adminService: AdministratorService){}

  ngOnInit(): void {
    this.getKorisnici();
  }
  
  getKorisnici(){
    this.rkService.getAll().subscribe(x=>{
      this.korisnici = x;
      this.filtriraniKorisnici = this.korisnici;
    })
  }
  

  pretragaKorisnika() {
    this.filtriraniKorisnici = this.korisnici.filter(k => 
      (this.pretraga.korisnickoIme ? (k.korisnickoIme || '').toLowerCase().includes(this.pretraga.korisnickoIme.toLowerCase()) : true) &&
      (this.pretraga.email ? (k.email || '').toLowerCase().includes(this.pretraga.email.toLowerCase()) : true)
    );
  }

  ponistiPretragu(){
    this.pretraga = {
      korisnickoIme: "",
      email: ""
    }
    this.filtriraniKorisnici = this.korisnici;
  }

  ukloni(id: number, event: Event) {
    this.confirmationService.confirm({
        target: event.target as EventTarget,
        message: 'Zelite da uklonite izabranog korisnika?',
        icon: 'pi pi-info-circle',
        acceptButtonStyleClass: 'p-button-danger p-button-sm',
        accept: () => {
          this.rkService.delete(id).subscribe({
                 next: () => {
                   this.getKorisnici();
                   this.messageService.add({ severity: 'info', summary: 'Uspešno uklonjeno', detail: 'Korisnik uklonjen' });
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




  izmeniTip(korisnik: any){
    this.tipDialog = true;
    this.korisnikZaIzmenuTipa = korisnik;
    console.log(this.korisnikZaIzmenuTipa);
  }

  izmenaTipa(){
    this.adminService.dodelaStatusa(this.tip.tip, this.korisnikZaIzmenuTipa).subscribe(x=>{
      this.getKorisnici();
      this.ponistiTipDialog();
    })
  }

  ponistiTipDialog(){
    this.tipDialog = false;
    this.korisnikZaIzmenuTipa = {};
  }


  hideDialogKorisnik(){
    this.dodajKorisnikaDialog = false;
  }

  korisnikDialog(){
    this.dodajKorisnikaDialog = true;
  }

  dodajKorisnika(){
    this.rkService.create(this.noviKorisnik).subscribe({
      next: () => {
        this.getKorisnici();
        this.hideDialogKorisnik();
        this.noviKorisnik = {};
        this.messageService.add({ severity: 'success', summary: 'Uspešno dodato', detail: 'Novi korisnik je uspesno dodat' });
      },
      error: err => {
        this.hideDialogKorisnik();
        this.messageService.add({ severity: 'error', summary: 'Greška pri dodavanju', detail: 'Došlo je do greške.' });
      }
    });
  }



  
}
