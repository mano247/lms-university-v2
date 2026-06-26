import { NgClass, NgIf } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputGroupModule } from 'primeng/inputgroup';
import { TableModule } from 'primeng/table';
import { OfficeSuppliesService } from '../../../services/office-supplies.service';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-office-supplies',
  standalone: true,
  imports: [TableModule, NgClass, ButtonModule, NgIf, InputGroupModule, FormsModule, DialogModule, ToastModule],
  templateUrl: './office-supplies.component.html',
  styleUrl: './office-supplies.component.css',
  providers: [MessageService]
})
export class OfficeSuppliesComponent implements OnInit{
  inventar: any[] = [];
  filtriraniInventar: any[] = [];

  visible: boolean = false;
  requestVisible: boolean = false;

  newItem: any = {};
  additionalQuantity: number = 0;
  selectedItem: any = {};

  search = {
    name: ''
  };

  filter = {
    name: ""
  }

  constructor(private kancService: OfficeSuppliesService, private messageService: MessageService){

  }

  ngOnInit(): void {
    this.getInventory();
  }

  getInventar(){
    this.kancService.getAll().subscribe(x=>{
      this.inventar = x;
      this.inventar.sort((a, b) => {
        if (a.quantity <= 10 && b.quantity > 10) {
          return -1;
        } else if (a.quantity > 10 && b.quantity <= 10) {
          return 1;
        } else {
          return a.quantity - b.quantity;
        }
      });
      this.filteredInventory = this.inventory;
    });
  }

  requestItem(item: any) {
    this.selectedItem = item;
    this.requestVisible = true;
  }

  pretraziInventar(){
    this.filtriraniInventar = this.inventar.filter(i =>
      (this.filter.name ? i.name.toLowerCase().includes(this.filter.name.toLowerCase()) : true)
    );
  }

  clearFilter(){
    this.filter.name = "";
    this.filtriraniInventar = this.inventar;
  }

  openAddDialog() {
    this.visible = true;
  }

  addItem() {
    this.officeMaterialService.create(this.newItem).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Added', detail: 'Item added successfully.' });
        this.newItem = {};
        this.getInventory();
        this.closeDialog();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while adding item.' });
      }
    });
  }

  closeDialog() {
    this.selectedItem = {};
    this.visible = false;
    this.requestVisible = false;
  }

  trebovanjeInventara(){
    const novaKolicina = Number(this.izabraniInventar.quantity) + Number(this.novaKolicina);
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
    if(inventar.quantity > 0){
      const newInv = {...inventar, kolicina: inventar.quantity - 1};
      this.kancService.update(newInv.id, newInv).subscribe(x=>{
        this.getInventar();
      })
      }
    }
  }
}

