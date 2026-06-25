import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { NastavniMaterijal } from '../../../model/academic/nastavniMaterijal';
import { StudentskaSluzbaService } from '../../../services/studentska-sluzba.service';
import { TableModule } from 'primeng/table';
import { NgClass, NgIf } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { NastavniMaterijalService } from '../../../services/nastavni-materijal.service';
import { InputGroupModule } from 'primeng/inputgroup';
import { FormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { PredmetService } from '../../../services/predmet.service';
import { Predmet } from '../../../model/academic/predmet';
import { DropdownModule } from 'primeng/dropdown';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-ss-biblioteka',
  standalone: true,
  imports: [TableModule, NgClass, ButtonModule, InputGroupModule, FormsModule, NgIf, DialogModule, DropdownModule, ToastModule],
  templateUrl: './ss-biblioteka.component.html',
  styleUrl: './ss-biblioteka.component.css',
  providers: [MessageService]
})
export class SsBibliotekaComponent implements OnInit{
  uzdbenici: NastavniMaterijal[] = [];
  filtriraniUdzbenici: NastavniMaterijal[] = [];

  visible: boolean = false;
  dodajVisible: boolean = false;

  udzbenikZaIzmenu: any;

  novaKolicina = 0

  predmeti: Predmet[] = [];

  noviUdzbenik:any = {};

  pretraga: any = {
    naslov: "",
    autor: ""
  }

  constructor(private ssluzbaService: StudentskaSluzbaService, private udzbenikService: NastavniMaterijalService, private predmetService: PredmetService,
    private messageService: MessageService
  ){}

  ngOnInit(): void {
    this.getUdzbenici();
    this.getPredmeti();
  }

  getUdzbenici() {
    this.ssluzbaService.getUdzbenici().subscribe(x => {
      this.uzdbenici = x;
      this.uzdbenici.sort((a, b) => {
        if (a.kolicina <= 10 && b.kolicina > 10) {
          return -1;
        } else if (a.kolicina > 10 && b.kolicina <= 10) {
          return 1;
        } else {
          return a.kolicina - b.kolicina;
        }
      });
      this.filtriraniUdzbenici = this.uzdbenici;
    });
  }

  getPredmeti(){
    this.predmetService.getAll().subscribe(x=>{
      this.predmeti = x;
    })
  }

  izdajUdzbenik(udzbenik: NastavniMaterijal){
    if (udzbenik.kolicina <= 0) {
      return;
    }else{
      const updatedUdzbenik = { ...udzbenik, kolicina: udzbenik.kolicina - 1 };
      if(updatedUdzbenik.id){
        this.udzbenikService.update(updatedUdzbenik.id, updatedUdzbenik).subscribe(x=>{
          this.getUdzbenici();
        })
      }
    }
  }

  trebujUdzbenik(udzbenik: NastavniMaterijal){
    this.visible = true;
    this.udzbenikZaIzmenu = udzbenik;
  }

  trebovanjeUdzbenika(){
    const novaKolicina = Number(this.udzbenikZaIzmenu.kolicina) + Number(this.novaKolicina);
    const updatedUdzbenik = { ...this.udzbenikZaIzmenu, kolicina: novaKolicina};
    this.udzbenikService.update(updatedUdzbenik.id, updatedUdzbenik).subscribe(x=>{
      this.messageService.add({
        severity: 'success', 
        summary: 'Kolicina uvecana', 
        detail: 'Kolicina uvecana'
      });
      this.visible = false;
      this.getUdzbenici();
      this.novaKolicina = 0;
    })
  }

  hideDialog(){
    this.visible = false;
  }

  udzbenikForma(){
    this.dodajVisible = true;
  }

  dodajUdzbenik(){
    this.udzbenikService.create(this.noviUdzbenik).subscribe({
      next: (x) => {
        this.messageService.add({
          severity: 'success', 
          summary: 'Udžbenik dodat', 
          detail: 'Udžbenik je uspešno dodat.'
        });
        this.noviUdzbenik = {}; 
        this.getUdzbenici();    
        this.hideDialogUdzbenik();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error', 
          summary: 'Greška', 
          detail: 'Došlo je do greške pri dodavanju udžbenika.'
        });
        console.error('Greška:', err); 
      }
    });
  }

  hideDialogUdzbenik(){
    this.dodajVisible = false;
  }

  pretraziUdzbenike(){
    this.filtriraniUdzbenici = this.uzdbenici.filter(u =>
      (this.pretraga.naslov ? u.naslov.toLowerCase().includes(this.pretraga.naslov.toLowerCase()) : true) &&
      (this.pretraga.autor ? u.autori.toLowerCase().includes(this.pretraga.autor.toLowerCase()) : true)
    );
  }

  ponistiPretragu(){
    this.pretraga = { 
      naslov: "",
      autor: "" 
    };
    this.filtriraniUdzbenici = this.uzdbenici;
  }

}
