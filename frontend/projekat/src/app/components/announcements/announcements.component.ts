import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GlobalNotificationsService } from '../../services/global-announcements.service';

@Component({
  selector: 'app-announcements',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './announcements.component.html',
  styleUrl: './announcements.component.css'
})
export class AnnouncementsComponent implements OnInit {
  announcements: any[] = [];
  isLoading = false;
  showAddModal = false;
  submitting = false;
  confirmDeleteId: number | null = null;
  deletingId: number | null = null;
  toast: { type: 'success' | 'error'; message: string } | null = null;

  form = {
    title: '',
    content: '',
    startDate: '',
    endDate: ''
  };

  constructor(private notificationsService: GlobalNotificationsService) {}

  ngOnInit(): void {
    this.loadAnnouncements();
  }

  loadAnnouncements(): void {
    this.isLoading = true;
    this.notificationsService.getAll().subscribe({
      next: (data) => {
        this.announcements = data;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.showToast('error', 'Failed to load announcements.');
      }
    });
  }

  openAddModal(): void {
    this.form = { title: '', content: '', startDate: '', endDate: '' };
    this.showAddModal = true;
  }

  closeAddModal(): void {
    this.showAddModal = false;
  }

  submitAnnouncement(): void {
    if (!this.form.title.trim() || !this.form.content.trim()) return;
    this.submitting = true;
    const payload: any = {
      title: this.form.title,
      content: this.form.content,
      startDate: this.form.startDate || null,
      endDate: this.form.endDate || null
    };
    this.notificationsService.create(payload).subscribe({
      next: () => {
        this.submitting = false;
        this.showAddModal = false;
        this.showToast('success', 'Announcement posted successfully.');
        this.loadAnnouncements();
      },
      error: () => {
        this.submitting = false;
        this.showToast('error', 'Failed to post announcement.');
      }
    });
  }

  askDelete(id: number): void {
    this.confirmDeleteId = id;
  }

  cancelDelete(): void {
    this.confirmDeleteId = null;
  }

  confirmDelete(id: number): void {
    this.deletingId = id;
    this.confirmDeleteId = null;
    this.notificationsService.delete(id).subscribe({
      next: () => {
        this.deletingId = null;
        this.announcements = this.announcements.filter(a => a.id !== id);
        this.showToast('success', 'Announcement deleted.');
      },
      error: () => {
        this.deletingId = null;
        this.showToast('error', 'Failed to delete announcement.');
      }
    });
  }

  formatDate(d: any): string {
    if (!d) return '—';
    const date = new Date(d);
    if (isNaN(date.getTime())) return '—';
    return date.toLocaleDateString('en-GB', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  formatDateRange(start: any, end: any): string {
    return `${this.formatDate(start)} – ${this.formatDate(end)}`;
  }

  showToast(type: 'success' | 'error', message: string): void {
    this.toast = { type, message };
    setTimeout(() => (this.toast = null), 4000);
  }
}
