import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FakultetService } from '../../services/fakultet.service';
import { StudijskiProgramService } from '../../services/studijski-program.service';
import { StudijskiProgram } from '../../model/academic/studijskiProgram';
import { Fakultet } from '../../model/academic/fakultet';
import { DividerModule } from 'primeng/divider';
import { Predmet } from '../../model/academic/predmet';
import { PredmetService } from '../../services/predmet.service';
import { NgFor } from '@angular/common';
import { map, switchMap, tap } from 'rxjs';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-studijski-program',
  standalone: true,
  imports: [DividerModule, RouterModule, NgFor],
  templateUrl: './studijski-program.component.html',
  styleUrl: './studijski-program.component.css'
})
export class StudijskiProgramComponent implements OnInit{
  fakultetSifra: string | null = null;
  sifraSP: string | null = null;
  smer: StudijskiProgram | null = null;
  predmeti: Predmet[] = [];

  fakultet: Fakultet = {
    naziv: '',
    kontakt: '',
    tekstualniOpis: '',
    dekan: '',
    slika: '',
    adresa: '',
    nazivUniverziteta: '',
    sifraFakulteta: ''
  }


  constructor(private route: ActivatedRoute, private spService: StudijskiProgramService, private predmetiService: PredmetService, private fakultetService: FakultetService) { }
  
  ngOnInit(): void {

    // this.route.paramMap.subscribe(params => {
    //   this.fakultetSifra = params.get('sifraFakulteta');
    //   this.sifraSP = params.get('sifraSP');
    //   this.getSmer();
    // });

    // this.getSmer();
    // this.getPredmeti();
    // this.getFakultet();

    this.route.paramMap.pipe(
      switchMap(params => {
        this.fakultetSifra = params.get('sifraFakulteta');
        this.sifraSP = params.get('sifraSP');

        if (this.fakultetSifra) {
          return this.fakultetService.getBySifra(this.fakultetSifra).pipe(
            tap(fakultet => this.fakultet = fakultet),
            switchMap(() => {
              if (this.sifraSP) {
                return this.spService.getBySifra(this.sifraSP).pipe(
                  tap(smer => {
                    this.smer = smer;
                    this.getPredmeti();
                  })
                );
              } else {
                return [];
              }
            })
          );
        } else {
          return [];
        }
      })
    ).subscribe();
  }

  getFakultet(){
    if(this.fakultetSifra !== null){
      this.fakultetService.getBySifra(this.fakultetSifra).subscribe(x=>{
        this.fakultet = x;
      })
    }
  }

  getSmer(){
    if(this.sifraSP !== null){
      this.spService.getBySifra(this.sifraSP).subscribe(x=>{
        this.smer = x;
        console.log(this.smer);
      });
    }
  }

  getPredmeti(){
    this.predmetiService.getAll().pipe(
      map(pr => pr.filter(p => p.smer.naziv === this.smer?.naziv))
    ).subscribe(filteredPredmeti => {
      this.predmeti = filteredPredmeti;
    });
  }

}
