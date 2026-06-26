import { NgClass, NgIf } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputGroupModule } from 'primeng/inputgroup';
import { TableModule } from 'primeng/table';
import { OfficeMaterialService } from '../../../services/office-supplies.service';
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
export class OfficeSuppliesComponent implements OnInit {
  inventory: any[] = [];
  filteredInventory: any[] = [];

  visible: boolean = false;
  requestVisible: boolean = false;

  newItem: any = {};
  additionalQuantity: number = 0;
  selectedItem: any = {};

  search = {
    name: ''
  };

  constructor(private officeMaterialService: OfficeMaterialService, private messageService: MessageService) {}

  ngOnInit(): void {
    this.getInventory();
  }

  getInventory() {
    this.officeMaterialService.getAll().subscribe(x => {
      this.inventory = x;
      this.inventory.sort((a, b) => {
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

  searchInventory() {
    this.filteredInventory = this.inventory.filter(i =>
      (this.search.name ? i.name.toLowerCase().includes(this.search.name.toLowerCase()) : true)
    );
  }

  clearSearch() {
    this.search.name = '';
    this.filteredInventory = this.inventory;
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

  processRequest() {
    const newQuantity = Number(this.selectedItem.quantity) + Number(this.additionalQuantity);
    const updated = { ...this.selectedItem, quantity: newQuantity };
    this.officeMaterialService.update(updated.id, updated).subscribe(() => {
      this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Quantity increased.' });
      this.closeDialog();
      this.getInventory();
      this.additionalQuantity = 0;
    });
  }

  issueItem(item: any) {
    if (item.quantity > 0) {
      const updated = { ...item, quantity: item.quantity - 1 };
      this.officeMaterialService.update(updated.id, updated).subscribe(() => {
        this.getInventory();
      });
    }
  }
}
