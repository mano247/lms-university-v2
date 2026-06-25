import { NgClass, NgFor, NgIf } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputSwitchModule } from 'primeng/inputswitch';
import { TableModule } from 'primeng/table';
import { StudentskaSluzbaService } from '../../../services/studentska-sluzba.service';
import { StudentiService } from '../../../services/studenti.service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { RegistrovaniKorisnik } from '../../../model/users/registrovaniKorisnik';
import { InputGroupModule } from 'primeng/inputgroup';
import { DialogModule } from 'primeng/dialog';
import { Fakultet } from '../../../model/academic/fakultet';
import { FakultetService } from '../../../services/fakultet.service';
import { DropdownModule } from 'primeng/dropdown';
import { RegistrovaniKorisnikService } from '../../../services/registrovani-korisnik.service';
import { Student } from '../../../model/users/student';
import { PredmetComponent } from '../../predmet/predmet.component';
import { PredmetService } from '../../../services/predmet.service';
import { Predmet } from '../../../model/academic/predmet';
import { StudijskiProgramService } from '../../../services/studijski-program.service';
import { StudijskiProgram } from '../../../model/academic/studijskiProgram';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-upis-studenata',
  standalone: true,
  imports: [InputSwitchModule, FormsModule, ButtonModule, NgIf, TableModule, NgClass, ToastModule, InputGroupModule, DialogModule, DropdownModule, NgFor],
  templateUrl: './upis-studenata.component.html',
  styleUrl: './upis-studenata.component.css',
  providers: [MessageService]
})
export class UpisStudenataComponent implements OnInit{
  visible: boolean = false;
  upisVisible: boolean = false;

  studenti: Student[] = [];
  filtriraniStudenti: Student[] = [];

  predmeti: Predmet[] = [];
  smerovi: StudijskiProgram[] = [];

  upisNaGodinuVisible: boolean = false;
  studentZaGodinu:any = {};
  studentZaUpisNaGodinu:any = {};

  fakulteti: Fakultet[] = [];

  izabraniStudent:any = {};

  korisnici: any[] = [];

  postojeciZaUpis: any = {};

  upisi:any = {};

  noviStudent: any = {
    ime: "",
    prezime: "",
    email: "",
    korisnicko_ime: "",
    lozinka: "",
    fakultet: undefined
  }

  smerZaUpis: any = {};
  prvaGodina: any = {
    id: 1,
    godina: 1
  }

  pretraga: any = {
    korisnicko_ime: "",
    email: ""
  }
  pretragaStudenta: any = {
    ime: "",
    prezime: "",
    email: "",
    brojIndeksa: ""
  }
  godinaStudija:any = [
    {
      naziv: "prva",
      id: 1,
      godina: 1
    },
    {
      naziv: "druga",
      id: 2,
      godina: 2
    },
    {
      naziv: "treca",
      id: 3,
      godina: 3
    },
    {
      naziv: "cetvrta",
      id: 4,
      godina: 4
    },

  ]

  filtriraniKorisnici: any[] = []; 

  constructor(private ssluzbaService: StudentskaSluzbaService, private studentService: StudentiService, private messageService: MessageService,
    private fakultetService: FakultetService, private rkService: RegistrovaniKorisnikService, 
    private predmetService: PredmetService, private smerService: StudijskiProgramService){}

  ngOnInit(): void {
    this.getKorisnici();
    this.getFakulteti();
    this.getStudenti();
    this.getPredmeti();
    this.getSmerovi();
  }

  getSmerovi(){
    this.smerService.getAll().subscribe(x=>{
      this.smerovi = x;
    })
  }

  getKorisnici(){
    this.ssluzbaService.getKorisnici().subscribe(x=>{
      this.korisnici = x.filter(korisnik => korisnik.tipZaIzmenu === 'RegistrovaniKorisnik');
      this.filtriraniKorisnici = this.korisnici;
    })
  }

  getFakulteti(){
    this.fakultetService.getAll().subscribe(x=>{
      this.fakulteti = x;
    })
  }

  getStudenti(){
    this.studentService.getAll().subscribe(x=>{
      this.studenti = x;
      this.filtriraniStudenti = this.studenti;

    })
  }

  getPredmeti(){
    this.predmetService.getAll().subscribe(x=>{
      this.predmeti = x;
    })
  }

  upisiPostojecegStudenta(korisnik: RegistrovaniKorisnik){
    this.postojeciZaUpis = korisnik;
    this.postojeciZaUpis = {...this.postojeciZaUpis}  
    this.upisVisible = true;
  }

  upisiPostojeceg(){
      this.rkService.dodeliStudenta(this.postojeciZaUpis.id, this.postojeciZaUpis).subscribe({
        next: (response) => {
          this.messageService.add({ 
            severity: 'success', 
            summary: 'Upis uspešan', 
            detail: 'Postojeći student je uspešno upisan.' 
          });
          this.smerZaUpis = {};
          this.getKorisnici();
          this.hideDialogPostojeci();
          this.getStudenti();
          this.postojeciZaUpis = {};
        },
        error: (err) => {
          this.messageService.add({ 
            severity: 'error', 
            summary: 'Greška', 
            detail: 'Došlo je do greške pri upisu postojećeg studenta.' 
          });
          console.error('Došlo je do greške:', err);
        }
      })
  }

  hideDialogPostojeci(){
    this.upisVisible = false;
  }

  ponistiFormu(){
    this.noviStudent = {
      ime: "",
      prezime: "",
      email: "",
      korisnicko_ime: "",
      lozinka: "",
      fakultet: undefined
    }
  }

  pretraziKorisnike(){
    this.filtriraniKorisnici = this.korisnici.filter(k =>
      (this.pretraga.korisnicko_ime ? k.korisnickoIme.toLowerCase().includes(this.pretraga.korisnicko_ime.toLowerCase()) : true) &&
      (this.pretraga.email ? k.email.toLowerCase().includes(this.pretraga.email.toLowerCase()) : true)
    );
  }

  ponistiPretragu(){
    this.filtriraniKorisnici = this.korisnici;
    this.pretraga = {
      korisnicko_ime: "",
      email: ""
    }

    this.filtriraniStudenti = this.studenti;
    this.pretragaStudenta = {
      ime: "",
      prezime: "",
      email: "",
      brojIndeksa: ""
    }
  }


  pretraziStudente(){
    this.filtriraniStudenti = this.studenti.filter(s => {
      if (s && s.ime && s.prezime && s.brojIndeksa && s.email) {
        return (
          (this.pretragaStudenta.ime ? s.ime.toLowerCase().includes(this.pretragaStudenta.ime.toLowerCase()) : true) &&
          (this.pretragaStudenta.prezime ? s.prezime.toLowerCase().includes(this.pretragaStudenta.prezime.toLowerCase()) : true) &&
          (this.pretragaStudenta.email ? s.email.toLowerCase().includes(this.pretragaStudenta.email.toLowerCase()) : true) &&
          (this.pretragaStudenta.brojIndeksa ? s.brojIndeksa.toLowerCase().includes(this.pretragaStudenta.brojIndeksa.toLowerCase()) : true)
        );
      } else {
        return false;
      }
    });
  }

  upisNaGodinu(student: Student){
    this.upisNaGodinuVisible = true;
    this.studentZaGodinu = student;
    if(student.id){
      this.studentService.getUpisi(student.id).subscribe(x=>{
        this.upisi = x;
        console.log(this.upisi);
      })

    }
  }

  upisiNaGodinu(){
    const zaGodinu = {datumUpisa: new Date(), godinaStudija: this.studentZaUpisNaGodinu.godinaStudija, student: this.studentZaGodinu,
       studijskiProgram: {id:this.studentZaUpisNaGodinu.studijskiProgram.id} }
    const student = {id:this.studentZaGodinu.id}
    this.studentService.upisiNaGodinu(zaGodinu).subscribe(x=>{
      this.studentService.dodajStudentaNaPredmet(this.studentZaUpisNaGodinu.studijskiProgram.id, student).subscribe(x=>{});
      this.messageService.add({severity: 'success', summary: 'Uspešno', detail: 'Student upisan na godinu.'});
      this.hideDialogUpisNaGodinu();
      this.studentZaGodinu = {};
      this.studentZaUpisNaGodinu = {};
    }, error => {
      this.messageService.add({severity: 'error', summary: 'Greška', detail: 'Došlo je do greške prilikom upisa na godinu.'});
    })
  }

  hideDialogUpisNaGodinu(){
    this.upisNaGodinuVisible = false;
    this.studentZaGodinu = {};
  }
}
