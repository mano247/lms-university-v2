import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CourseMaterialService } from '../../../services/teaching-material.service';

@Component({
  selector: 'app-library',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './library.component.html',
  styleUrl: './library.component.css'
})
export class LibraryComponent implements OnInit {
  textbooks: any[] = [];
  filteredTextbooks: any[] = [];

  isLoading = false;
  submitting = false;
  showAddModal = false;
  showRequestModal = false;

  requestingBook: any = null;
  additionalQty = 1;

  newBook = { title: '', authors: '', publicationYear: new Date().getFullYear(), quantity: 1, isbn: '' };

  search = { title: '', author: '' };

  toast: { type: 'success' | 'error'; message: string } | null = null;

  constructor(private materialService: CourseMaterialService) {}

  ngOnInit(): void {
    this.loadTextbooks();
  }

  loadTextbooks(): void {
    this.isLoading = true;
    this.materialService.getAll().subscribe({
      next: (data: any[]) => {
        this.textbooks = data ?? [];
        this.applySearch();
        this.isLoading = false;
      },
      error: () => { this.showToast('error', 'Failed to load textbooks.'); this.isLoading = false; }
    });
  }

  applySearch(): void {
    this.filteredTextbooks = this.textbooks.filter(t =>
      (!this.search.title || (t.title || '').toLowerCase().includes(this.search.title.toLowerCase())) &&
      (!this.search.author || (t.authors || '').toLowerCase().includes(this.search.author.toLowerCase()))
    );
  }

  clearSearch(): void {
    this.search = { title: '', author: '' };
    this.filteredTextbooks = [...this.textbooks];
  }

  isLowStock(book: any): boolean {
    return (book.quantity ?? 0) <= 5;
  }

  issueTextbook(book: any): void {
    if ((book.quantity ?? 0) <= 0) return;
    const updated = { ...book, quantity: book.quantity - 1 };
    this.materialService.update(book.id, updated).subscribe({
      next: () => { this.showToast('success', 'Copy issued successfully.'); this.loadTextbooks(); },
      error: () => this.showToast('error', 'Failed to issue copy.')
    });
  }

  openRequestModal(book: any): void {
    this.requestingBook = book;
    this.additionalQty = 1;
    this.showRequestModal = true;
  }

  closeRequestModal(): void {
    this.showRequestModal = false;
    this.requestingBook = null;
  }

  submitRequest(): void {
    if (!this.requestingBook || this.additionalQty < 1) return;
    this.submitting = true;
    const updated = { ...this.requestingBook, quantity: (this.requestingBook.quantity ?? 0) + this.additionalQty };
    this.materialService.update(this.requestingBook.id, updated).subscribe({
      next: () => {
        this.submitting = false;
        this.showToast('success', `${this.additionalQty} copies added to stock.`);
        this.closeRequestModal();
        this.loadTextbooks();
      },
      error: () => { this.submitting = false; this.showToast('error', 'Failed to update stock.'); }
    });
  }

  openAddModal(): void {
    this.newBook = { title: '', authors: '', publicationYear: new Date().getFullYear(), quantity: 1, isbn: '' };
    this.showAddModal = true;
  }

  closeAddModal(): void {
    this.showAddModal = false;
  }

  submitAddBook(): void {
    if (!this.newBook.title.trim()) return;
    this.submitting = true;
    this.materialService.create(this.newBook as any).subscribe({
      next: () => {
        this.submitting = false;
        this.showToast('success', 'Textbook added successfully.');
        this.closeAddModal();
        this.loadTextbooks();
      },
      error: () => { this.submitting = false; this.showToast('error', 'Failed to add textbook.'); }
    });
  }

  private showToast(type: 'success' | 'error', message: string): void {
    this.toast = { type, message };
    setTimeout(() => { this.toast = null; }, 4000);
  }
}
