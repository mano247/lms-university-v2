import { NgClass, NgIf } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputGroupModule } from 'primeng/inputgroup';
import { TableModule } from 'primeng/table';
import { KancelarijskiMaterijalService } from '../../../services/kancelarijski-materijal.service';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-ss-kanc',
  standalone: true,
  imports: [TableModule, NgClass, ButtonModule, NgIf, InputGroupModule, FormsModule, DialogModule, ToastModule],
  templateUrl: './ss-kanc.component.html',
  styleUrl: './ss-kanc.component.css',
  providers: [MessageService]
})
export class SsKancComponent implements OnInit{
  inventar: any[] = [];
  filtriraniInventar: any[] = [];

  visible: boolean = false;
  visibleTrebuj: boolean = false;

  noviInv: any = {};
  novaKolicina:number = 0;

  izabraniInventar: any = {};

  pretraga = {
    naziv: ""
  }

  constructor(private kancService: KancelarijskiMaterijalService, private messageService: MessageService){

  }

  ngOnInit(): void {
    this.getInventar();
  }

  getInventar(){
    this.kancService.getAll().subscribe(x=>{
      this.inventar = x;
      this.inventar.sort((a, b) => {
        if (a.kolicina <= 10 && b.kolicina > 10) {
          return -1;
        } else if (a.kolicina > 10 && b.kolicina <= 10) {
          return 1;
        } else {
          return a.kolicina - b.kolicina;
        }
      });
      this.filtriraniInventar = this.inventar;
    })
  }


  trebujInventar(inventar: any){
    this.izabraniInventar = inventar;
    this.visibleTrebuj = true;
  }

  pretraziInventar(){
    this.filtriraniInventar = this.inventar.filter(i =>
      (this.pretraga.naziv ? i.naziv.toLowerCase().includes(this.pretraga.naziv.toLowerCase()) : true)
    );
  }

  ponistiPretragu(){
    this.pretraga.naziv = "";
    this.filtriraniInventar = this.inventar;
  }

  invDialog(){
    this.visible = true;
  }

  dodajInv(){
    this.kancService.create(this.noviInv).subscribe({
      next: (x) => {
        this.messageService.add({
          severity: 'success', 
          summary: 'Inventar dodat', 
          detail: 'Kancelarijski inventar je uspešno dodat.'
        });
        this.noviInv = {}; 
        this.getInventar();    
        this.hideDialog();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error', 
          summary: 'Greška', 
          detail: 'Došlo je do greške pri dodavanju udžbenika.'
        });
        console.error('Greška:', err); 
      }
    })
  }

  hideDialog(){
    this.izabraniInventar = {};
    this.visible = false;
    this.visibleTrebuj = false;
  }

  trebovanjeInventara(){
    const novaKolicina = Number(this.izabraniInventar.kolicina) + Number(this.novaKolicina);
    const updatedInventar = { ...this.izabraniInventar, kolicina: novaKolicina};
    this.kancService.update(updatedInventar.id, updatedInventar).subscribe(x=>{
      this.messageService.add({
        severity: 'success', 
        summary: 'Kolicina uvecana', 
        detail: 'Kolicina uvecana'
      });
      this.hideDialog();
      this.getInventar();
      this.novaKolicina = 0;
    })
  }

  umanjiInv(inventar: any){
    if(inventar.kolicina > 0){
      const newInv = {...inventar, kolicina: inventar.kolicina - 1};
      this.kancService.update(newInv.id, newInv).subscribe(x=>{
        this.getInventar();
      })
      }
    }

}
