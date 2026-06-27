import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TeacherService } from '../../../services/teacher.service';
import { ExamAttemptService } from '../../../services/exam-attempt.service';

@Component({
  selector: 'app-grade-entry',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './grade-entry.component.html',
  styleUrl: './grade-entry.component.css',
})
export class GradeEntryComponent implements OnInit {
  courses: any[] = [];
  selectedCourseId: number | null = null;
  examAttempts: any[] = [];
  isLoading = false;

  showModal = false;
  editingAttempt: any = null;
  submitting = false;
  toast: { type: 'success' | 'error'; message: string } | null = null;

  result = { points: 0, grade: 5, note: '' };

  page = 1;
  pageSize = 10;

  private teacherId: number | null = null;

  constructor(private teacherService: TeacherService, private examAttemptService: ExamAttemptService) {}

  ngOnInit(): void {
    const raw = localStorage.getItem('user');
    if (!raw) return;
    const id = JSON.parse(raw).id;
    this.teacherId = id;
    if (!id) return;

    this.teacherService.getMyCourses(id).subscribe({
      next: courses => {
        this.courses = courses ?? [];
        if (this.courses.length) {
          this.selectedCourseId = this.courses[0].id ?? null;
          this.loadAttempts();
        }
      },
      error: () => {}
    });
  }

  onCourseChange(val: string): void {
    this.selectedCourseId = val ? Number(val) : null;
    this.loadAttempts();
  }

  loadAttempts(): void {
    if (!this.selectedCourseId) { this.examAttempts = []; return; }
    this.isLoading = true;
    this.examAttemptService.getRegisteredByCourse(this.selectedCourseId).subscribe({
      next: data => { this.examAttempts = data ?? []; this.isLoading = false; this.page = 1; },
      error: () => { this.isLoading = false; }
    });
  }

  get pagedAttempts(): any[] {
    return this.examAttempts.slice((this.page - 1) * this.pageSize, this.page * this.pageSize);
  }

  get totalPages(): number { return Math.ceil(this.examAttempts.length / this.pageSize); }
  pageRange(): number[] { return Array.from({ length: this.totalPages }, (_, i) => i + 1); }

  openEdit(attempt: any): void {
    this.editingAttempt = attempt;
    this.result = { points: attempt.points ?? 0, grade: attempt.finalGrade ?? 5, note: attempt.note ?? '' };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.editingAttempt = null;
    this.result = { points: 0, grade: 6, note: '' };
  }

  submitGrade(): void {
    if (!this.editingAttempt?.id) return;
    if (this.result.points < 0 || this.result.points > 100) {
      this.showToast('error', 'Points must be between 0 and 100.');
      return;
    }
    if (this.result.grade < 5 || this.result.grade > 10) {
      this.showToast('error', 'Grade must be between 5 and 10.');
      return;
    }
    this.submitting = true;
    const updated = { ...this.editingAttempt, finalGrade: this.result.grade, points: this.result.points, note: this.result.note };
    this.examAttemptService.update(this.editingAttempt.id, updated).subscribe({
      next: () => {
        this.submitting = false;
        this.closeModal();
        this.showToast('success', 'Grade submitted successfully.');
        this.loadAttempts();
      },
      error: () => {
        this.submitting = false;
        this.showToast('error', 'Failed to submit grade. Please try again.');
      }
    });
  }

  private showToast(type: 'success' | 'error', message: string): void {
    this.toast = { type, message };
    setTimeout(() => { this.toast = null; }, 4000);
  }
}
