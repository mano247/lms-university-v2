import { NO_ERRORS_SCHEMA } from '@angular/core';
import { Component, OnInit} from '@angular/core';
import { MenuItem } from 'primeng/api';
import { MenuModule } from 'primeng/menu';
import { Fakultet } from '../../model/academic/fakultet';
import { FakultetService } from '../../services/fakultet.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DividerModule } from 'primeng/divider';
import { NgFor, NgIf } from '@angular/common';
import { StudijskiProgram } from '../../model/academic/studijskiProgram';
import { StudijskiProgramService } from '../../services/studijski-program.service';


@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-fakultet',
  standalone: true,
  imports: [MenuModule, DividerModule, NgIf, NgFor],
  templateUrl: './fakultet.component.html',
  styleUrl: './fakultet.component.css'
})
export class FakultetComponent implements OnInit{
  fakultet: Fakultet | undefined;
  sifraFakuklteta: string | null = null;
  studijskiProgrami: StudijskiProgram[] = [];

  constructor(private fakultetService: FakultetService, private router: Router, private route: ActivatedRoute, private spService: StudijskiProgramService){}


  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.sifraFakuklteta = params.get('sifraFakulteta');
      this.loadFakultet();
    });


  }

  loadFakultet(){
    if(this.sifraFakuklteta){
      this.fakultetService.getBySifra(this.sifraFakuklteta).subscribe(x=>{
        this.fakultet = x;
        this.getSmerovi();
      })
    }
  }

  getSmerovi(){
    return this.spService.getAll().subscribe(x => {
      this.studijskiProgrami = x.filter(program => program.fakultet.naziv === this.fakultet?.naziv)
    })
  }


}
