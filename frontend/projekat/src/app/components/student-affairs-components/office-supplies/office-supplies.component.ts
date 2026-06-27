import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OfficeMaterialService } from '../../../services/office-supplies.service';

@Component({
  selector: 'app-office-supplies',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './office-supplies.component.html',
  styleUrl: './office-supplies.component.css'
})
export class OfficeSuppliesComponent implements OnInit {
  inventory: any[] = [];
  filteredInventory: any[] = [];

  isLoading = false;
  submitting = false;
  deleting = false;
  showAddModal = false;
  showRequestModal = false;
  confirmDeleteId: number | null = null;

  requestingItem: any = null;
  additionalQty = 1;

  newItem = { name: '', quantity: 1, category: '' };
  search = { name: '' };

  toast: { type: 'success' | 'error'; message: string } | null = null;

  constructor(private materialService: OfficeMaterialService) {}

  ngOnInit(): void {
    this.loadInventory();
  }

  loadInventory(): void {
    this.isLoading = true;
    this.materialService.getAll().subscribe({
      next: (data: any[]) => {
        this.inventory = data ?? [];
        this.applySearch();
        this.isLoading = false;
      },
      error: () => { this.showToast('error', 'Failed to load inventory.'); this.isLoading = false; }
    });
  }

  applySearch(): void {
    this.filteredInventory = this.inventory.filter(i =>
      !this.search.name || (i.name || '').toLowerCase().includes(this.search.name.toLowerCase())
    );
  }

  clearSearch(): void {
    this.search.name = '';
    this.filteredInventory = [...this.inventory];
  }

  isLowStock(item: any): boolean {
    return (item.quantity ?? 0) <= 5;
  }

  issueItem(item: any): void {
    if ((item.quantity ?? 0) <= 0) return;
    const updated = { ...item, quantity: item.quantity - 1 };
    this.materialService.update(item.id, updated).subscribe({
      next: () => { this.showToast('success', 'Item issued.'); this.loadInventory(); },
      error: () => this.showToast('error', 'Failed to issue item.')
    });
  }

  openRequestModal(item: any): void {
    this.requestingItem = item;
    this.additionalQty = 1;
    this.showRequestModal = true;
  }

  closeRequestModal(): void {
    this.showRequestModal = false;
    this.requestingItem = null;
  }

  submitRequest(): void {
    if (!this.requestingItem || this.additionalQty < 1) return;
    this.submitting = true;
    const updated = { ...this.requestingItem, quantity: (this.requestingItem.quantity ?? 0) + this.additionalQty };
    this.materialService.update(this.requestingItem.id, updated).subscribe({
      next: () => {
        this.submitting = false;
        this.showToast('success', `${this.additionalQty} units added to stock.`);
        this.closeRequestModal();
        this.loadInventory();
      },
      error: () => { this.submitting = false; this.showToast('error', 'Failed to update stock.'); }
    });
  }

  askDelete(id: number): void {
    this.confirmDeleteId = id;
  }

  cancelDelete(): void {
    this.confirmDeleteId = null;
  }

  deleteItem(id: number): void {
    this.deleting = true;
    this.confirmDeleteId = null;
    this.materialService.delete(id).subscribe({
      next: () => {
        this.deleting = false;
        this.inventory = this.inventory.filter(i => i.id !== id);
        this.applySearch();
        this.showToast('success', 'Item deleted.');
      },
      error: () => { this.deleting = false; this.showToast('error', 'Failed to delete item.'); }
    });
  }

  openAddModal(): void {
    this.newItem = { name: '', quantity: 1, category: '' };
    this.showAddModal = true;
  }

  closeAddModal(): void {
    this.showAddModal = false;
  }

  submitAddItem(): void {
    if (!this.newItem.name.trim()) return;
    this.submitting = true;
    this.materialService.create(this.newItem as any).subscribe({
      next: () => {
        this.submitting = false;
        this.showToast('success', 'Item added successfully.');
        this.closeAddModal();
        this.loadInventory();
      },
      error: () => { this.submitting = false; this.showToast('error', 'Failed to add item.'); }
    });
  }

  private showToast(type: 'success' | 'error', message: string): void {
    this.toast = { type, message };
    setTimeout(() => { this.toast = null; }, 4000);
  }
}
