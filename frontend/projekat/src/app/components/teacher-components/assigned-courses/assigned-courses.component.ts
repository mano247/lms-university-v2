import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TeacherService } from '../../../services/teacher.service';

@Component({
  selector: 'app-assigned-courses',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './assigned-courses.component.html',
  styleUrl: './assigned-courses.component.css',
})
export class AssignedCoursesComponent implements OnInit {
  courses: any[] = [];
  isLoading = true;
  hasError = false;
  expandedId: number | null = null;

  showSyllabusModal = false;
  editingCourse: any = null;
  syllabusText = '';
  saving = false;
  toast: { type: 'success' | 'error'; message: string } | null = null;

  private teacherId: number | null = null;

  readonly cardColors = [
    { bg: '#e8f0fe', fg: '#1a56db' },
    { bg: '#fce8e6', fg: '#c5221f' },
    { bg: '#e6f4ea', fg: '#1e7e34' },
    { bg: '#fef3e2', fg: '#b45309' },
  ];

  constructor(private teacherService: TeacherService) {}

  ngOnInit(): void {
    const raw = localStorage.getItem('user');
    if (!raw) { this.isLoading = false; return; }
    this.teacherId = JSON.parse(raw).id;
    if (!this.teacherId) { this.isLoading = false; return; }
    this.load();
  }

  load(): void {
    if (!this.teacherId) return;
    this.teacherService.getMyCourses(this.teacherId).subscribe({
      next: data => { this.courses = data ?? []; this.isLoading = false; },
      error: () => { this.hasError = true; this.isLoading = false; }
    });
  }

  toggle(id: number): void {
    this.expandedId = this.expandedId === id ? null : id;
  }

  colorFor(i: number) { return this.cardColors[i % this.cardColors.length]; }

  syllabusLines(syllabus: string | undefined): string[] {
    if (!syllabus) return [];
    const trimmed = syllabus.trim();
    if (!trimmed) return [];
    try {
      const parsed = JSON.parse(trimmed);
      if (Array.isArray(parsed)) return parsed.map(String);
    } catch {}
    return trimmed.split(/\n+/).map(s => s.replace(/^\s*[\d]+[.)]\s*/, '').trim()).filter(Boolean);
  }

  openEditSyllabus(course: any, e: Event): void {
    e.stopPropagation();
    this.editingCourse = course;
    this.syllabusText = course.syllabus ?? '';
    this.showSyllabusModal = true;
  }

  closeModal(): void {
    this.showSyllabusModal = false;
    this.editingCourse = null;
    this.syllabusText = '';
  }

  saveSyllabus(): void {
    if (!this.editingCourse?.id) return;
    this.saving = true;
    const updated = { ...this.editingCourse, syllabus: this.syllabusText };
    this.teacherService.updateSyllabus(this.editingCourse.id, updated).subscribe({
      next: () => {
        this.saving = false;
        this.closeModal();
        this.showToast('success', 'Syllabus updated successfully.');
        this.load();
      },
      error: () => {
        this.saving = false;
        this.showToast('error', 'Failed to update syllabus. Please try again.');
      }
    });
  }

  private showToast(type: 'success' | 'error', message: string): void {
    this.toast = { type, message };
    setTimeout(() => { this.toast = null; }, 4000);
  }
}
