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
export class OfficeSuppliesComponent implements OnInit {
  inventory: any[] = [];
  filteredInventory: any[] = [];

  visible: boolean = false;
  requestVisible: boolean = false;

  newItem: any = { name: '', quantity: 0 };
  additionalQuantity: number = 0;
  selectedItem: any = {};

  filter = { name: '' };

  constructor(
    private officeSuppliesService: OfficeSuppliesService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.getInventory();
  }

  getInventory() {
    this.officeSuppliesService.getAll().subscribe((x: any[]) => {
      this.inventory = x.sort((a, b) => {
        if (a.quantity <= 10 && b.quantity > 10) return -1;
        if (a.quantity > 10 && b.quantity <= 10) return 1;
        return a.quantity - b.quantity;
      });
      this.filteredInventory = [...this.inventory];
    });
  }

  filterInventory() {
    this.filteredInventory = this.inventory.filter(i =>
      this.filter.name ? i.name.toLowerCase().includes(this.filter.name.toLowerCase()) : true
    );
  }

  clearFilter() {
    this.filter.name = '';
    this.filteredInventory = [...this.inventory];
  }

  openAddDialog() {
    this.newItem = { name: '', quantity: 0 };
    this.visible = true;
  }

  addItem() {
    this.officeSuppliesService.create(this.newItem).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Added', detail: 'Item added successfully.' });
        this.newItem = { name: '', quantity: 0 };
        this.getInventory();
        this.closeDialog();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while adding item.' });
      }
    });
  }

  issueItem(item: any) {
    if (item.quantity > 0) {
      const updated = { ...item, quantity: item.quantity - 1 };
      this.officeSuppliesService.update(updated.id, updated).subscribe(() => {
        this.getInventory();
      });
    }
  }

  requestItem(item: any) {
    this.selectedItem = item;
    this.requestVisible = true;
  }

  submitRequisition() {
    const updatedQty = Number(this.selectedItem.quantity) + Number(this.additionalQuantity);
    const updated = { ...this.selectedItem, quantity: updatedQty };
    this.officeSuppliesService.update(updated.id, updated).subscribe(() => {
      this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Quantity increased.' });
      this.closeDialog();
      this.getInventory();
      this.additionalQuantity = 0;
    });
  }

  closeDialog() {
    this.selectedItem = {};
    this.visible = false;
    this.requestVisible = false;
  }
}
