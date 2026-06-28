import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Course } from '../../../model/academic/course';
import { StudentService } from '../../../services/student.service';

@Component({
  selector: 'app-my-courses',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-courses.component.html',
  styleUrl: './my-courses.component.css',
})
export class MyCoursesComponent implements OnInit {
  @Output() tabChange = new EventEmitter<string>();

  courses: Course[] = [];
  passedExams: any[] = [];
  isLoading = true;
  studentName = '';

  private readonly cardColors = [
    { bg: '#d5e0f7', fg: '#002444' },
    { bg: '#ffddb1', fg: '#5f410c' },
    { bg: '#d2f4e8', fg: '#1a4433' },
    { bg: '#f5d5f5', fg: '#4a1a48' },
  ];

  private readonly cardIcons = [
    'school', 'functions', 'biotech', 'terminal',
    'psychology', 'science', 'history_edu', 'language',
  ];

  constructor(private studentService: StudentService) {}

  ngOnInit(): void {
    const raw = localStorage.getItem('currentUser');
    if (!raw) { this.isLoading = false; return; }
    const u = JSON.parse(raw);
    this.studentName = u.username || u.email || 'Student';
    const id: number = u.id;
    if (!id) { this.isLoading = false; return; }

    let pending = 2;
    const done = () => { if (--pending === 0) this.isLoading = false; };

    this.studentService.getActiveCourses(id).subscribe({
      next: c => { this.courses = c; done(); },
      error: () => done(),
    });
    this.studentService.getPassedExams(id).subscribe({
      next: e => { this.passedExams = e; done(); },
      error: () => done(),
    });
  }

  get gpa(): string {
    if (!this.passedExams.length) return '—';
    const avg = this.passedExams.reduce((s, e) => s + (e.finalGrade ?? 0), 0) / this.passedExams.length;
    return avg.toFixed(2);
  }

  get totalEcts(): number {
    return this.passedExams.reduce((s, e) => s + (e.ects ?? 0), 0);
  }

  cardColor(i: number) { return this.cardColors[i % this.cardColors.length]; }
  cardIcon(i: number) { return this.cardIcons[i % this.cardIcons.length]; }

  getTeacher(course: Course): string {
    const t = course.teacher;
    if (!t) return '—';
    return `${t.firstName ?? ''} ${t.lastName ?? ''}`.trim() || '—';
  }
}
