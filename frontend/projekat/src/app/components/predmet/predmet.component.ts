import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { StudijskiProgramService } from '../../services/studijski-program.service';
import { StudijskiProgram } from '../../model/academic/studijskiProgram';
import { PredmetService } from '../../services/predmet.service';
import { Predmet } from '../../model/academic/predmet';
import { DividerModule } from 'primeng/divider';
import { Fakultet } from '../../model/academic/fakultet';
import { FakultetService } from '../../services/fakultet.service';
import { NastavniMaterijalService } from '../../services/nastavni-materijal.service';
import { NgFor } from '@angular/common';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-predmet',
  standalone: true,
  imports: [RouterModule, DividerModule, NgFor],
  templateUrl: './predmet.component.html',
  styleUrl: './predmet.component.css'
})
export class PredmetComponent implements OnInit{
  fakultetSifra: string | null = null;
  sifraSP: string | null = null;
  sifraPredmeta: string | null = null;
  // nastavniMaterijal: any[] = [];

  fakultet: Fakultet | null = null;
  predmet: Predmet | null = null;
  smer: StudijskiProgram | null = null;


  constructor(private route: ActivatedRoute,private nmService: NastavniMaterijalService,
     private spService: StudijskiProgramService, private fakultetService: FakultetService, private predmetService: PredmetService){}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.fakultetSifra = params.get('sifraFakulteta');
      this.sifraSP = params.get('sifraSP');
      this.sifraPredmeta = params.get('sifraPredmeta');

      this.getPredmet();
      this.getSmer();
      this.getFakultet();
    });
    // this.getNMaterijal();
  }

  getPredmet(){
    if(this.sifraPredmeta !== null){
      this.predmetService.getBySifra(this.sifraPredmeta).subscribe(x=>{
        this.predmet = x;
      })
    }
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
      });
    }
  }

  // getNMaterijal(){
  //   this.nmService.getAll().subscribe(x=>{
  //     this.nastavniMaterijal = x;
  //     // console.log(this.nastavniMaterijal);
  //   })
  // }

  formatSilabus(silabus: string | undefined): string{
    if(!silabus) return "silabus";
    
    let formatted = silabus
    .replace(/"/g, '')

    .replace(/\n\n/g, '</p><p><br>')

    .replace(/\n/g, '<br>');

  formatted = formatted.replace(/\[([^\]]+)\]/g, (match, p1) => {
      const items = p1.split(';').map((item: string) => `<p>${item.trim()}</p>`).join('');
      return `<div>${items}</div>`;
    });

    return formatted;
  }

  formatirajDatum(vreme: Date | undefined){
    if(vreme){
      const datum = new Date(vreme);

      const godina = datum.getUTCFullYear();
      const mesec = (datum.getUTCMonth() + 1).toString().padStart(2, '0'); 
      const dan = datum.getUTCDate().toString().padStart(2, '0');

      return `${godina}-${mesec}-${dan}`;
    }else{
      return "";
    }
  }
  
}
