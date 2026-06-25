import { NgFor } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { TableModule } from 'primeng/table';
import { StudentiService } from '../../../services/studenti.service';
import { Predmet } from '../../../model/academic/predmet';
import { SortEvent } from 'primeng/api';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-istorija-studiranja',
  standalone: true,
  imports: [TableModule, NgFor],
  templateUrl: './istorija-studiranja.component.html',
  styleUrl: './istorija-studiranja.component.css'
})
export class IstorijaStudiranjaComponent implements OnInit{
  polozeniPredmeti: any[] = [];
  nepolozeniPredmeti: Predmet[] = [];

  ECTS: number = 0.0;
  avgOcena: number = 0.0;



  upisi: any[] = [];

  constructor(private studentService: StudentiService){}

  ngOnInit(): void {

    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      const id = parsedUser.id;
      const token =  parsedUser.accessToken;
      this.getPolozeniPredmeti(id);
      this.getUpisi(id);
    }
  }


  getPolozeniPredmeti(id: number){
    this.studentService.polozeniIspiti(id).subscribe(x=>{
      this.polozeniPredmeti = x;
    })
  }



  getUpisi(id: number){
    this.studentService.getUpisi(id).subscribe(x=>{
      this.upisi = x;
    })
  }

  getProsecnaOcena(): number {
    if (this.polozeniPredmeti.length === 0) return 0;
    const totalOcena = this.polozeniPredmeti.reduce((sum, predmet) => sum + predmet.ocena, 0);
    const prosecnaOcena = totalOcena / this.polozeniPredmeti.length;
    return parseFloat(prosecnaOcena.toFixed(2));
  }

  getSumaETCS(): number {
    if (this.polozeniPredmeti.length === 0) return 0;
    return this.polozeniPredmeti.reduce((sum, predmet) => sum + predmet.espb, 0);
  }
}
