import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputSwitchModule } from 'primeng/inputswitch';
import { TableModule } from 'primeng/table';
import { Nastavnik } from '../../../model/users/nastavnik';
import { NastavniMaterijalService } from '../../../services/nastavni-materijal.service';
import { NastavnikService } from '../../../services/nastavnik.service';
import { NgClass, NgIf } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { Predmet } from '../../../model/academic/predmet';
import { StudentiService } from '../../../services/studenti.service';
import { Student } from '../../../model/users/student';
import { InputGroupModule } from 'primeng/inputgroup';
// import { jsPDF } from 'jspdf';



@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-izdavanje-dokumenata',
  standalone: true,
  imports: [InputSwitchModule, FormsModule, TableModule, NgClass, ButtonModule, NgIf, InputGroupModule],
  templateUrl: './izdavanje-dokumenata.component.html',
  styleUrl: './izdavanje-dokumenata.component.css'
})
export class IzdavanjeDokumenataComponent implements OnInit {
  visible: boolean = false;

  profesori: Nastavnik[] = [];
  filtriraniProfesori: Nastavnik[] = [];

  studenti: Student[] = [];
  filtriraniStudenti: Student[] = [];

  profesorPredmeti: Predmet[] = [];

  pretraga = {
    ime: '',
    prezime: '',
    email: ''
  }

  constructor(private nastavnikService: NastavnikService, private studentService: StudentiService){}

  ngOnInit(): void {
    this.getNastavnici();
    this.getStudenti();
  }

  getNastavnici(){
    this.nastavnikService.getAll().subscribe(x=>{
      this.profesori = x;
      this.filtriraniProfesori = this.profesori;
      console.log(this.profesori);
    })
  }

  getStudenti(){
    this.studentService.getAll().subscribe(x=>{
      this.studenti = x;
      this.filtriraniStudenti = this.studenti;
    })
  }

  pretraziProfesore(){
    this.filtriraniProfesori = this.profesori.filter(p =>
      (this.pretraga.ime ? p.ime.toLowerCase().includes(this.pretraga.ime.toLowerCase()) : true) &&
      (this.pretraga.prezime ? p.prezime.toLowerCase().includes(this.pretraga.prezime.toLowerCase()) : true) &&
      (this.pretraga.email ? p.email.toLowerCase().includes(this.pretraga.email.toLowerCase()) : true)
    );
  }

  pretraziStudente(){
    this.filtriraniStudenti = this.studenti.filter(s =>
      (this.pretraga.ime ? s.ime.toLowerCase().includes(this.pretraga.ime.toLowerCase()) : true) &&
      (this.pretraga.prezime ? s.prezime.toLowerCase().includes(this.pretraga.prezime.toLowerCase()) : true) &&
      (this.pretraga.email ? s.email.toLowerCase().includes(this.pretraga.email.toLowerCase()) : true)
    );
  }

  ponistiPretragu(){
    this.filtriraniStudenti = this.studenti;
    this.filtriraniProfesori = this.profesori;
    this.pretraga = {
      ime: '',
      prezime: '',
      email: ''
    }

  }

  xmlProfesor(profesor: Nastavnik){
   
  }

  pdfProfesor(profesor: Nastavnik){

  }

  xmlStudent(student: Student){

  }

  pdfStudent(student: Student){

  }
}
