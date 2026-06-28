import { environment } from '../../../../environments/environment';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { TeacherService } from '../../../services/teacher.service';

interface ExamPeriod {
  id: number;
  name: string;
  startDate: string;
  endDate: string;
  isOpen?: boolean;
  terms?: ExamTerm[];
  courseId?: number;
  courseName?: string;
}

interface ExamTerm {
  id?: number;
  date: string;
  startTime: string;
  room: string;
  maxStudents: number;
  courseName?: string;
}

interface NewTerm {
  date: string;
  startTime: string;
  room: string;
  maxStudents: number;
}

@Component({
  selector: 'app-teacher-exam-periods',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './teacher-exam-periods.component.html',
  styleUrl: './teacher-exam-periods.component.css',
})
export class TeacherExamPeriodsComponent implements OnInit {
  periods: ExamPeriod[] = [];
  courses: any[] = [];
  isLoading = true;
  hasError = false;
  expandedId: number | null = null;

  showAddTermModal = false;
  addingToPeriod: ExamPeriod | null = null;
  submitting = false;
  toast: { type: 'success' | 'error'; message: string } | null = null;

  newTerm: NewTerm = { date: '', startTime: '', room: '', maxStudents: 30 };

  private teacherId: number | null = null;

  constructor(private http: HttpClient, private teacherService: TeacherService) {}

  ngOnInit(): void {
    const raw = localStorage.getItem('currentUser');
    if (!raw) { this.isLoading = false; return; }
    this.teacherId = JSON.parse(raw).id;
    if (!this.teacherId) { this.isLoading = false; return; }

    this.teacherService.getMyCourses(this.teacherId).subscribe({
      next: c => { this.courses = c ?? []; },
      error: () => {}
    });

    this.http.get<ExamPeriod[]>(`${environment.apiUrl}/api/exam-periods?teacherId=${this.teacherId}`).subscribe({
      next: data => { this.periods = data ?? []; this.isLoading = false; },
      error: () => { this.hasError = true; this.isLoading = false; }
    });
  }

  toggle(id: number): void {
    this.expandedId = this.expandedId === id ? null : id;
  }

  getDaysLeft(endDate: string): number {
    const end = new Date(endDate).getTime();
    const now = Date.now();
    return Math.max(0, Math.ceil((end - now) / 86400000));
  }

  isOpen(period: ExamPeriod): boolean {
    const now = Date.now();
    return new Date(period.startDate).getTime() <= now && new Date(period.endDate).getTime() >= now;
  }

  openAddTerm(period: ExamPeriod, e: Event): void {
    e.stopPropagation();
    this.addingToPeriod = period;
    this.newTerm = { date: '', startTime: '', room: '', maxStudents: 30 };
    this.showAddTermModal = true;
  }

  closeModal(): void {
    this.showAddTermModal = false;
    this.addingToPeriod = null;
  }

  submitTerm(): void {
    if (!this.addingToPeriod) return;
    this.submitting = true;
    const payload = { ...this.newTerm, examPeriodId: this.addingToPeriod.id };
    this.http.post(`${environment.apiUrl}/api/exam-period-terms`, payload).subscribe({
      next: () => {
        this.submitting = false;
        this.closeModal();
        this.showToast('success', 'Exam term added successfully.');
        this.reload();
      },
      error: () => {
        this.submitting = false;
        this.showToast('error', 'Failed to add exam term. Please try again.');
      }
    });
  }

  private reload(): void {
    if (!this.teacherId) return;
    this.http.get<ExamPeriod[]>(`${environment.apiUrl}/api/exam-periods?teacherId=${this.teacherId}`).subscribe({
      next: data => { this.periods = data ?? []; },
      error: () => {}
    });
  }

  private showToast(type: 'success' | 'error', message: string): void {
    this.toast = { type, message };
    setTimeout(() => { this.toast = null; }, 4000);
  }
}
