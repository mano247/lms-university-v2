import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Course } from '../../../model/academic/course';
import { Student } from '../../../model/users/student';
import { ExamAttempt } from '../../../model/exam-attempt';
import { StudentService } from '../../../services/student.service';
import { ExamAttemptService } from '../../../services/exam-attempt.service';

@Component({
  selector: 'app-exam-registration',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './exam-registration.component.html',
  styleUrl: './exam-registration.component.css',
})
export class ExamRegistrationComponent implements OnInit {
  availableCourses: Course[] = [];
  registeredExams: any[] = [];
  student: Student | undefined;
  studentId: number | undefined;
  isLoading = true;
  registeringId: number | null = null;
  toast: { type: 'success' | 'error'; message: string } | null = null;

  availPage = 1;
  regPage = 1;
  readonly pageSize = 10;

  constructor(
    private studentService: StudentService,
    private examAttemptService: ExamAttemptService,
  ) {}

  ngOnInit(): void {
    const raw = localStorage.getItem('user');
    if (!raw) { this.isLoading = false; return; }
    this.studentId = JSON.parse(raw).id;
    if (!this.studentId) { this.isLoading = false; return; }

    let pending = 3;
    const done = () => { if (--pending === 0) this.isLoading = false; };

    this.studentService.getAvailableExams(this.studentId).subscribe({ next: x => { this.availableCourses = x ?? []; done(); }, error: () => done() });
    this.studentService.getById(this.studentId).subscribe({ next: s => { this.student = s; done(); }, error: () => done() });
    this.examAttemptService.getRegisteredByStudent(this.studentId).subscribe({ next: x => { this.registeredExams = x ?? []; done(); }, error: () => done() });
  }

  get pagedAvail() { return this.availableCourses.slice((this.availPage - 1) * this.pageSize, this.availPage * this.pageSize); }
  get availPages() { return Math.max(1, Math.ceil(this.availableCourses.length / this.pageSize)); }

  get pagedReg() { return this.registeredExams.slice((this.regPage - 1) * this.pageSize, this.regPage * this.pageSize); }
  get regPages() { return Math.max(1, Math.ceil(this.registeredExams.length / this.pageSize)); }

  isRegistered(course: Course): boolean {
    return this.registeredExams.some(e => e.course?.id === course.id);
  }

  register(course: Course): void {
    if (!this.student || this.registeringId !== null) return;

    this.registeringId = course.id ?? null;
    const attempt: ExamAttempt = { course, student: this.student, teacher: course.teacher };

    this.examAttemptService.create(attempt).subscribe({
      next: () => {
        this.registeringId = null;
        this.showToast('success', `Successfully registered for ${course.name}.`);
        if (this.studentId) {
          this.examAttemptService.getRegisteredByStudent(this.studentId).subscribe({
            next: x => { this.registeredExams = x ?? []; },
            error: () => {},
          });
        }
      },
      error: () => {
        this.registeringId = null;
        this.showToast('error', 'Registration failed. Please try again later.');
      },
    });
  }

  private showToast(type: 'success' | 'error', message: string): void {
    this.toast = { type, message };
    setTimeout(() => { this.toast = null; }, 4500);
  }

  getTeacher(course: Course): string {
    const t = course.teacher;
    if (!t) return '—';
    return `${t.firstName ?? ''} ${t.lastName ?? ''}`.trim() || '—';
  }

  getRegCourse(exam: any): string {
    return exam.courseName ?? exam.course?.name ?? '—';
  }

  getRegTeacher(exam: any): string {
    const t = exam.teacher ?? exam.course?.teacher;
    if (!t) return '—';
    return `${t.firstName ?? ''} ${t.lastName ?? ''}`.trim() || '—';
  }
}
