import { Component, Input, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { Univerzitet } from '../../model/academic/univerzitet';
import { UniverzitetService } from '../../services/univerzitet.service';
import { FakultetService } from '../../services/fakultet.service';
import { Fakultet } from '../../model/academic/fakultet';
import { NgFor } from '@angular/common';
import { DividerModule } from 'primeng/divider';
import { Router, RouterModule } from '@angular/router';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-univerzitet',
  standalone: true,
  imports: [NgFor, DividerModule, RouterModule],
  templateUrl: './univerzitet.component.html',
  styleUrl: './univerzitet.component.css'
})
export class UniverzitetComponent implements OnInit{
  private univerzitet_id: number = 1;
  univerzitet: Univerzitet = {
    naziv: '',
    datumOsnivanja: new Date,
    kontakt: '',
    opis: '',
    slika: '',
    adresa: '',
    rektorat: {
      naziv: '',
      kontakt: '',
      slika: '',
      adresa: '',
      univerziteti: [],
      ime_rektora: ''
    }
  };

  @Input()
  fakulteti: Fakultet[] = [];

  constructor(private univerzitetService: UniverzitetService, private fakultetService: FakultetService, private router: Router) {}

  ngOnInit(): void {
    this.getUniverzitet();
    this.getFakulteti();
  }

  getUniverzitet(){
    return this.univerzitetService.getById(this.univerzitet_id).subscribe( x => {
      this.univerzitet = x;
    });
  }

  getFakulteti(){
    return this.fakultetService.getAll().subscribe(x => {
      this.fakulteti = x;
    })
  }

  getDatum(): string{
    const datum = new Date(this.univerzitet.datumOsnivanja);
    const godina = datum.getFullYear();
    const mesec = String(datum.getMonth() + 1).padStart(2, '0'); 
    const dan = String(datum.getDate()).padStart(2, '0');
  
    return `${dan}. ${mesec}. ${godina}`
  }

  postaviLokaciju(){
    
  }

}
