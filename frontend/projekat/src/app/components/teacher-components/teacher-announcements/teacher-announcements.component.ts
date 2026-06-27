import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TeacherService } from '../../../services/teacher.service';
import { NotificationService } from '../../../services/announcement.service';

interface AnnouncementForm {
  title: string;
  content: string;
  startDate: string;
  endDate: string;
  image: string;
  courseId: number | null;
}

@Component({
  selector: 'app-teacher-announcements',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './teacher-announcements.component.html',
  styleUrl: './teacher-announcements.component.css',
})
export class TeacherAnnouncementsComponent implements OnInit {
  courses: any[] = [];
  announcements: any[] = [];
  filtered: any[] = [];
  selectedCourseId: number | null = null;
  isLoading = true;

  showAddModal = false;
  submitting = false;
  deletingId: number | null = null;
  confirmDeleteId: number | null = null;
  toast: { type: 'success' | 'error'; message: string } | null = null;

  form: AnnouncementForm = { title: '', content: '', startDate: '', endDate: '', image: '', courseId: null };

  constructor(private teacherService: TeacherService, private notificationService: NotificationService) {}

  ngOnInit(): void {
    const raw = localStorage.getItem('user');
    if (!raw) { this.isLoading = false; return; }
    const id = JSON.parse(raw).id;
    if (!id) { this.isLoading = false; return; }

    this.teacherService.getMyCourses(id).subscribe({
      next: courses => {
        this.courses = courses ?? [];
        if (this.courses.length) this.selectedCourseId = this.courses[0].id ?? null;
        this.loadAnnouncements();
      },
      error: () => { this.isLoading = false; }
    });
  }

  loadAnnouncements(): void {
    this.notificationService.getAll().subscribe({
      next: all => {
        this.announcements = all ?? [];
        this.applyFilter();
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  onCourseChange(id: string): void {
    this.selectedCourseId = id ? Number(id) : null;
    this.applyFilter();
  }

  applyFilter(): void {
    if (this.selectedCourseId == null) { this.filtered = []; return; }
    this.filtered = this.announcements.filter(a => a.course?.id === this.selectedCourseId);
    this.filtered.sort((a, b) => new Date(b.startDate ?? b.date ?? 0).getTime() - new Date(a.startDate ?? a.date ?? 0).getTime());
  }

  openAddModal(): void {
    const today = new Date().toISOString().slice(0, 16);
    this.form = { title: '', content: '', startDate: today, endDate: today, image: '', courseId: this.selectedCourseId };
    this.showAddModal = true;
  }

  closeModal(): void { this.showAddModal = false; }

  submitAnnouncement(): void {
    if (!this.form.title || !this.form.content || !this.form.courseId) return;
    this.submitting = true;
    const course = this.courses.find(c => c.id === Number(this.form.courseId));
    const payload = {
      title: this.form.title,
      content: this.form.content,
      startDate: this.form.startDate ? new Date(this.form.startDate) : new Date(),
      endDate: this.form.endDate ? new Date(this.form.endDate) : new Date(),
      image: this.form.image || '',
      date: new Date(),
      course: course ?? null,
    };
    this.notificationService.create(payload as any).subscribe({
      next: () => {
        this.submitting = false;
        this.closeModal();
        this.showToast('success', 'Announcement added successfully.');
        this.loadAnnouncements();
      },
      error: () => {
        this.submitting = false;
        this.showToast('error', 'Failed to add announcement. Please try again.');
      }
    });
  }

  confirmDelete(id: number): void { this.confirmDeleteId = id; }
  cancelDelete(): void { this.confirmDeleteId = null; }

  deleteAnnouncement(id: number): void {
    this.deletingId = id;
    this.confirmDeleteId = null;
    this.notificationService.delete(id).subscribe({
      next: () => {
        this.deletingId = null;
        this.showToast('success', 'Announcement removed.');
        this.loadAnnouncements();
      },
      error: () => {
        this.deletingId = null;
        this.showToast('error', 'Failed to remove announcement.');
      }
    });
  }

  formatDate(d: any): string {
    if (!d) return '—';
    const date = d instanceof Date ? d : new Date(d);
    if (isNaN(date.getTime())) return '—';
    return date.toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' });
  }

  private showToast(type: 'success' | 'error', message: string): void {
    this.toast = { type, message };
    setTimeout(() => { this.toast = null; }, 4000);
  }
}
