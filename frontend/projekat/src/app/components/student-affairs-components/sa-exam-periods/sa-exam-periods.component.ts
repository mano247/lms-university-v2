import { environment } from '../../../../environments/environment';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CourseService } from '../../../services/course.service';

@Component({
  selector: 'app-sa-exam-periods',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './sa-exam-periods.component.html',
  styleUrl: './sa-exam-periods.component.css'
})
export class SaExamPeriodsComponent implements OnInit {
  periods: any[] = [];
  courses: any[] = [];
  isLoading = false;
  submitting = false;
  expandedId: number | null = null;
  confirmDeleteId: number | null = null;
  deletingId: number | null = null;

  showAddPeriodModal = false;
  showAddTermModal = false;
  addingTermToPeriodId: number | null = null;

  toast: { type: 'success' | 'error'; message: string } | null = null;

  periodForm = { name: '', startDate: '', endDate: '' };

  termForm = {
    courseId: null as number | null,
    examDate: '',
    registrationStart: '',
    registrationEnd: ''
  };

  private readonly base = `${environment.apiUrl}`;

  constructor(private http: HttpClient, private courseService: CourseService) {}

  ngOnInit(): void {
    this.loadPeriods();
    this.courseService.getAll().subscribe({ next: (d: any[]) => { this.courses = d ?? []; }, error: () => {} });
  }

  loadPeriods(): void {
    this.isLoading = true;
    this.http.get<any[]>(`${this.base}/api/exam-periods`).subscribe({
      next: (data) => { this.periods = data ?? []; this.isLoading = false; },
      error: () => { this.showToast('error', 'Failed to load exam periods.'); this.isLoading = false; }
    });
  }

  toggleExpand(id: number): void {
    this.expandedId = this.expandedId === id ? null : id;
  }

  openAddPeriodModal(): void {
    this.periodForm = { name: '', startDate: '', endDate: '' };
    this.showAddPeriodModal = true;
  }

  closeAddPeriodModal(): void {
    this.showAddPeriodModal = false;
  }

  submitPeriod(): void {
    if (!this.periodForm.name.trim() || !this.periodForm.startDate || !this.periodForm.endDate) {
      this.showToast('error', 'All fields are required.');
      return;
    }
    if (this.periodForm.startDate >= this.periodForm.endDate) {
      this.showToast('error', 'Start date must be before end date.');
      return;
    }
    this.submitting = true;
    this.http.post(`${this.base}/api/exam-periods`, this.periodForm).subscribe({
      next: () => {
        this.submitting = false;
        this.showToast('success', 'Exam period created.');
        this.closeAddPeriodModal();
        this.loadPeriods();
      },
      error: () => { this.submitting = false; this.showToast('error', 'Failed to create exam period.'); }
    });
  }

  askDeletePeriod(id: number): void {
    this.confirmDeleteId = id;
  }

  cancelDelete(): void {
    this.confirmDeleteId = null;
  }

  confirmDeletePeriod(id: number): void {
    this.deletingId = id;
    this.confirmDeleteId = null;
    this.http.delete(`${this.base}/api/exam-periods/${id}`).subscribe({
      next: () => {
        this.deletingId = null;
        this.periods = this.periods.filter(p => p.id !== id);
        this.showToast('success', 'Exam period deleted.');
      },
      error: () => { this.deletingId = null; this.showToast('error', 'Failed to delete exam period.'); }
    });
  }

  openAddTermModal(periodId: number): void {
    this.addingTermToPeriodId = periodId;
    this.termForm = { courseId: null, examDate: '', registrationStart: '', registrationEnd: '' };
    this.showAddTermModal = true;
  }

  closeAddTermModal(): void {
    this.showAddTermModal = false;
    this.addingTermToPeriodId = null;
  }

  submitTerm(): void {
    if (!this.termForm.courseId || !this.termForm.examDate) {
      this.showToast('error', 'Course and exam date are required.');
      return;
    }
    this.submitting = true;
    const payload = {
      examPeriod: { id: this.addingTermToPeriodId },
      course: { id: this.termForm.courseId },
      examDate: this.termForm.examDate,
      registrationStart: this.termForm.registrationStart || null,
      registrationEnd: this.termForm.registrationEnd || null
    };
    this.http.post(`${this.base}/api/exam-period-terms`, payload).subscribe({
      next: () => {
        this.submitting = false;
        this.showToast('success', 'Exam term added.');
        this.closeAddTermModal();
        this.loadPeriods();
      },
      error: () => { this.submitting = false; this.showToast('error', 'Failed to add exam term.'); }
    });
  }

  getCourseName(courseId: number): string {
    const c = this.courses.find(c => c.id === courseId);
    return c?.name ?? '—';
  }

  formatDate(d: any): string {
    if (!d) return '—';
    const date = new Date(d);
    if (isNaN(date.getTime())) return d;
    return date.toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' });
  }

  private showToast(type: 'success' | 'error', message: string): void {
    this.toast = { type, message };
    setTimeout(() => { this.toast = null; }, 4000);
  }
}
