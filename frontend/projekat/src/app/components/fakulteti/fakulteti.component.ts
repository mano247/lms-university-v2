import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FakultetService } from '../../services/fakultet.service';
import { Fakultet } from '../../model/academic/fakultet';
import { NgFor } from '@angular/common';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';


@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-fakulteti',
  standalone: true,
  imports: [NgFor, CardModule, ButtonModule],
  templateUrl: './fakulteti.component.html',
  styleUrl: './fakulteti.component.css'
})
export class FakultetiComponent implements OnInit{
  fakulteti: Fakultet[] = [];

  constructor(private fakultetService: FakultetService, private router: Router){}

  ngOnInit(): void {
    this.fakultetService.getAll().subscribe(x => {
      this.fakulteti = x;
    })
  }

  skraceniOpis(opis: string, maxLength: number = 110): string{
    if(opis.length > maxLength){
      return opis.substring(0, maxLength) + '...';
    }
    return opis;
  }

  detaljnije(fakultet: Fakultet){
    this.router.navigate([`fakultet/${fakultet.sifraFakulteta}`])
  }

}
