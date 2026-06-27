import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StudentService } from '../../../services/student.service';

@Component({
  selector: 'app-study-history',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './study-history.component.html',
  styleUrl: './study-history.component.css',
})
export class StudyHistoryComponent implements OnInit {
  passedExams: any[] = [];
  failedExams: any[] = [];
  enrollments: any[] = [];
  isLoading = true;

  passedPage = 1;
  failedPage = 1;
  enrollPage = 1;
  readonly pageSize = 10;

  constructor(private studentService: StudentService) {}

  ngOnInit(): void {
    const raw = localStorage.getItem('user');
    if (!raw) { this.isLoading = false; return; }
    const id: number = JSON.parse(raw).id;
    if (!id) { this.isLoading = false; return; }

    let pending = 3;
    const done = () => { if (--pending === 0) this.isLoading = false; };

    this.studentService.getPassedExams(id).subscribe({ next: x => { this.passedExams = x ?? []; done(); }, error: () => done() });
    this.studentService.getFailedExams(id).subscribe({ next: x => { this.failedExams = x ?? []; done(); }, error: () => done() });
    this.studentService.getEnrollments(id).subscribe({ next: x => { this.enrollments = x ?? []; done(); }, error: () => done() });
  }

  get avgGpa(): string {
    if (!this.passedExams.length) return '—';
    const avg = this.passedExams.reduce((s, e) => s + (e.finalGrade ?? 0), 0) / this.passedExams.length;
    return avg.toFixed(2);
  }

  get totalEcts(): number {
    return this.passedExams.reduce((s, e) => s + (e.ects ?? 0), 0);
  }

  // Pagination helpers
  get pagedPassed() { return this.passedExams.slice((this.passedPage - 1) * this.pageSize, this.passedPage * this.pageSize); }
  get passedPages() { return Math.max(1, Math.ceil(this.passedExams.length / this.pageSize)); }

  get pagedFailed() { return this.failedExams.slice((this.failedPage - 1) * this.pageSize, this.failedPage * this.pageSize); }
  get failedPages() { return Math.max(1, Math.ceil(this.failedExams.length / this.pageSize)); }

  get pagedEnroll() { return this.enrollments.slice((this.enrollPage - 1) * this.pageSize, this.enrollPage * this.pageSize); }
  get enrollPages() { return Math.max(1, Math.ceil(this.enrollments.length / this.pageSize)); }

  getTeacher(exam: any): string {
    const t = exam.teacher;
    if (!t) return '—';
    return `${t.firstName ?? ''} ${t.lastName ?? ''}`.trim() || '—';
  }

  getCourseName(exam: any): string {
    return exam.courseName ?? exam.course?.name ?? '—';
  }

  formatDate(d: any): string {
    if (!d) return '—';
    return new Date(d).toLocaleDateString('en-GB', { year: 'numeric', month: 'short', day: 'numeric' });
  }
}
