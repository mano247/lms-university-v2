import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { NastavniMaterijal } from '../../model/academic/nastavniMaterijal';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { NastavniMaterijalService } from '../../services/nastavni-materijal.service';
import { DividerModule } from 'primeng/divider';
import { NgFor } from '@angular/common';
import { FakultetService } from '../../services/fakultet.service';
import { PredmetService } from '../../services/predmet.service';
import { StudijskiProgramService } from '../../services/studijski-program.service';
import { Predmet } from '../../model/academic/predmet';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-nastavni-materijal',
  standalone: true,
  imports: [RouterModule, DividerModule, NgFor],
  templateUrl: './nastavni-materijal.component.html',
  styleUrl: './nastavni-materijal.component.css'
})
export class NastavniMaterijalComponent implements OnInit{
  fakultetSifra: string | null = null;
  sifraSP: string | null = null;
  sifraPredmeta: string | null = null;
  materijalNaziv: string | null = null;

  fakultetNaziv: string | null = null;
  programNaziv: string | null = null;
  predmet: Predmet = {
    silabus: '',
    naziv: '',
    espb: 0,
    vremePocetka: new Date,
    vremeKraja: new Date,
    opis: '',
    nastavniMaterijal: [],
    polaganje: [],
    nastavnik: undefined,
    studenti: [],
    obavestenja: [],
    smer: '',
    sifraPredmeta: '',
  }

  materijal: NastavniMaterijal | null = null;


  constructor(private route: ActivatedRoute, private fakultetService: FakultetService, 
    private predmetService: PredmetService, private smerService: StudijskiProgramService){}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.fakultetSifra = params.get('sifraFakulteta');
      this.sifraSP = params.get('sifraSP');
      this.sifraPredmeta = params.get('sifraPredmeta');
      this.materijalNaziv = params.get('nMaterijalNaziv');
    });
    this.getFakultet();
    this.getSmer();
    this.getPredmet();
  }


  getFakultet(){  
    if(this.fakultetSifra !== null){
      this.fakultetService.getBySifra(this.fakultetSifra).subscribe(x=>{
        this.fakultetNaziv = x.naziv;
      })
    }
  }

  getPredmet(){
    if(this.sifraPredmeta !== null){
      this.predmetService.getBySifra(this.sifraPredmeta).subscribe(x=>{
        this.predmet = x;
        if (this.materijalNaziv) {
          this.materijal = this.predmet.nastavniMaterijal.find(m => m.naslov === this.materijalNaziv) || null;
        } else {
          this.materijal = null; 
        }
      })
    }
  }

  getSmer(){
    if(this.sifraSP !== null){
      this.smerService.getBySifra(this.sifraSP).subscribe(x=>{
        this.programNaziv = x.naziv;
      })
    }
  }

}
