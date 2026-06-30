import { environment } from '../../../../environments/environment';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CourseService } from '../../../services/course.service';
import { TeacherService } from '../../../services/teacher.service';

@Component({
  selector: 'app-schedules',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './schedules.component.html',
  styleUrl: './schedules.component.css'
})
export class SchedulesComponent implements OnInit {
  schedule: any[] = [];
  isLoading = false;
  showAddModal = false;
  submitting = false;
  confirmDeleteId: number | null = null;
  deletingId: number | null = null;

  courses: any[] = [];
  teachers: any[] = [];

  toast: { type: 'success' | 'error'; message: string } | null = null;

  readonly days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];

  form = {
    day: 'Monday',
    startTime: '',
    endTime: '',
    classroom: '',
    courseId: null as number | null,
    teacherId: null as number | null
  };

  private readonly base = `${environment.apiUrl}`;

  constructor(
    private http: HttpClient,
    private courseService: CourseService,
    private teacherService: TeacherService
  ) {}

  ngOnInit(): void {
    this.loadSchedule();
    this.courseService.getAll().subscribe({ next: (d: any[]) => { this.courses = d ?? []; }, error: () => {} });
    this.teacherService.getAll().subscribe({ next: (d: any[]) => { this.teachers = d ?? []; }, error: () => {} });
  }

  loadSchedule(): void {
    this.isLoading = true;
    this.http.get<any>(`${this.base}/api/class-schedules?size=1000`).subscribe({
      next: (data) => { this.schedule = (data as any)?.content ?? data ?? []; this.isLoading = false; },
      error: () => { this.showToast('error', 'Failed to load schedule.'); this.isLoading = false; }
    });
  }

  slotsByDay(day: string): any[] {
    return this.schedule
      .filter(s => s.day === day || s.dayOfWeek === day)
      .sort((a, b) => (a.startTime ?? '').localeCompare(b.startTime ?? ''));
  }

  openAddModal(): void {
    this.form = { day: 'Monday', startTime: '', endTime: '', classroom: '', courseId: null, teacherId: null };
    this.showAddModal = true;
  }

  closeAddModal(): void {
    this.showAddModal = false;
  }

  hasOverlap(day: string, startTime: string, endTime: string, classroom: string, excludeId?: number): boolean {
    return this.schedule
      .filter(s => (s.day === day || s.dayOfWeek === day) && s.classroom === classroom && s.id !== excludeId)
      .some(s => {
        const sStart = s.startTime ?? '';
        const sEnd = s.endTime ?? '';
        return startTime < sEnd && endTime > sStart;
      });
  }

  submitSlot(): void {
    if (!this.form.startTime || !this.form.endTime || !this.form.classroom) {
      this.showToast('error', 'Day, start time, end time and classroom are required.');
      return;
    }
    if (this.form.startTime >= this.form.endTime) {
      this.showToast('error', 'Start time must be before end time.');
      return;
    }
    if (this.hasOverlap(this.form.day, this.form.startTime, this.form.endTime, this.form.classroom)) {
      this.showToast('error', `Classroom ${this.form.classroom} is already occupied during this time slot.`);
      return;
    }
    this.submitting = true;
    const payload = {
      dayOfWeek: this.form.day,
      day: this.form.day,
      startTime: this.form.startTime,
      endTime: this.form.endTime,
      classroom: this.form.classroom,
      course: this.form.courseId ? { id: this.form.courseId } : null,
      teacher: this.form.teacherId ? { id: this.form.teacherId } : null
    };
    this.http.post(`${this.base}/api/class-schedules`, payload).subscribe({
      next: () => {
        this.submitting = false;
        this.showToast('success', 'Class slot added successfully.');
        this.closeAddModal();
        this.loadSchedule();
      },
      error: () => { this.submitting = false; this.showToast('error', 'Failed to add class slot.'); }
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
    this.http.delete(`${this.base}/api/class-schedules/${id}`).subscribe({
      next: () => {
        this.deletingId = null;
        this.schedule = this.schedule.filter(s => s.id !== id);
        this.showToast('success', 'Class slot removed.');
      },
      error: () => { this.deletingId = null; this.showToast('error', 'Failed to remove class slot.'); }
    });
  }

  getCourseName(slot: any): string {
    if (slot.course?.name) return slot.course.name;
    if (slot.courseId) {
      const c = this.courses.find(c => c.id === slot.courseId);
      return c?.name ?? '—';
    }
    return '—';
  }

  getTeacherName(slot: any): string {
    if (slot.teacher?.firstName) return `${slot.teacher.firstName} ${slot.teacher.lastName}`;
    if (slot.teacherId) {
      const t = this.teachers.find(t => t.id === slot.teacherId);
      return t ? `${t.firstName} ${t.lastName}` : '—';
    }
    return '—';
  }

  private showToast(type: 'success' | 'error', message: string): void {
    this.toast = { type, message };
    setTimeout(() => { this.toast = null; }, 5000);
  }
}
