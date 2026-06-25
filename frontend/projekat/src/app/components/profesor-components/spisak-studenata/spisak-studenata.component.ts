import { NgFor, NgIf, NgStyle } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { Student } from '../../../model/users/student';
import { StudentiService } from '../../../services/studenti.service';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { DividerModule } from 'primeng/divider';
import { NastavnikService } from '../../../services/nastavnik.service';
import { FormsModule } from '@angular/forms';
import { PolaganjeService } from '../../../services/polaganje.service';
import { ZavrsniRadService } from '../../../services/zavrsni-rad.service';
import { Predmet } from '../../../model/academic/predmet';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-spisak-studenata',
  standalone: true,
  imports: [TableModule, NgFor, DropdownModule, InputGroupModule, FormsModule, InputGroupAddonModule, 
    ButtonModule, DialogModule, DividerModule, NgStyle, NgIf],
  templateUrl: './spisak-studenata.component.html',
  styleUrl: './spisak-studenata.component.css'
})
export class SpisakStudenataComponent implements OnInit{
  visible: boolean = false;

  filtriraniStudenti: Student[] = [];
  studenti: Student[] = [];

  predmeti: Predmet[] = [];
  izabraniPredmet: any;

  // godinaUpisa: any;
  // smer: any;
  upisi: any;
  // polozeni: any;
  // neuspesnaPolaganja: any;
  prijavljeniIspiti: any;

  studentPodaci: any = {}
  podaciStudenta: any;

  zavrsniRad: any = {}

  ETCS:number = 0;
  avgOcena:number = 0;

  pretraga = {
    ime: '',
    prezime: '',
    brojIndeksa: '',
    // godinaUpisa: '',
    // prosecnaOcena: ''
  };

  constructor(private studentiService: StudentiService, private nastavnikService: NastavnikService, 
    private polaganjeService: PolaganjeService, private zavrsniRadService: ZavrsniRadService){}

  ngOnInit(): void {
    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      const id = parsedUser.id;
      this.getPredmeti(id);
    }
  }

  getPredmeti(id: number) {
    this.nastavnikService.mojiPredmeti(id).subscribe(predmeti => {
      this.predmeti = predmeti;
      if (this.predmeti.length > 0) {

        this.izabraniPredmet = this.predmeti[0];
        this.filtriraniStudenti = this.izabraniPredmet.studenti;
      }
    });
  }

  onPredmetChange(event: any) {
    console.log("Izabrani predmet:", event.value); 
  
    if (event.value && event.value.studenti) {
      this.filtriraniStudenti = event.value.studenti;
    } else {
      this.filtriraniStudenti = [];
    }
  }

  getStudentInfo(id: number) {
    this.studentiService.getUpisi(id).subscribe(x=>{
      this.studentPodaci.upisi = x;
    })

    this.polaganjeService.getPrijavljeni(id).subscribe(x=>{
      this.studentPodaci.prijavljeniIspiti = x;
    });

    this.zavrsniRadService.findByStudent(id).subscribe(x=>{
      this.studentPodaci.zavrsniRad = x;
    })

    this.studentiService.polozeniIspiti(id).subscribe(x=>{
      this.studentPodaci.polozeniIspiti = x;

      if (this.studentPodaci.polozeniIspiti.length > 0) {
        const sumaOcena = this.studentPodaci.polozeniIspiti.reduce((sum: number, ispit: any) => sum + ispit.ocena, 0);
        this.studentPodaci.avgOcena = sumaOcena / this.studentPodaci.polozeniIspiti.length;
      } else {
        this.avgOcena = 0;  
      }

      this.studentPodaci.ETCS = this.studentPodaci.polozeniIspiti.reduce((sum: number, ispit: any) => sum + ispit.espb, 0);

    })

    this.studentiService.nepolozeniIspiti(id).subscribe(x=>{
      this.studentPodaci.nepolozeniIspiti = x;
    })


  }


  prikazStudenta(student: Student){
    this.visible = true;
    this.studentPodaci = student;
    if(student.id){
      this.getStudentInfo(student.id);
    }
    console.log(this.studentPodaci);
  }

  pretraziStudente(){
    this.filtriraniStudenti = this.studenti.filter(s => 
      (this.pretraga.ime ? (s.ime || '').toLowerCase().includes(this.pretraga.ime.toLowerCase()) : true) &&
      (this.pretraga.prezime ? (s.prezime || '').toLowerCase().includes(this.pretraga.prezime.toLowerCase()) : true) &&
      (this.pretraga.brojIndeksa ? (s.brojIndeksa || '').toLowerCase().includes(this.pretraga.brojIndeksa.toLowerCase()) : true)
    );
  }

  ponistiPretragu(){
    this.filtriraniStudenti = this.studenti;
    this.pretraga = {
      ime: '',
      prezime: '',
      brojIndeksa: '',
    }
  }


}
