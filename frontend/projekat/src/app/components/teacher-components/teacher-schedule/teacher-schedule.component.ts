import { environment } from '../../../../environments/environment';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

interface ScheduleEntry {
  id?: number;
  dayOfWeek: string;
  startTime: string;
  endTime: string;
  courseName: string;
  room: string;
  type: string;
  groupName?: string;
}

@Component({
  selector: 'app-teacher-schedule',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './teacher-schedule.component.html',
  styleUrl: './teacher-schedule.component.css',
})
export class TeacherScheduleComponent implements OnInit {
  schedule: ScheduleEntry[] = [];
  isLoading = true;
  hasError = false;
  viewMode: 'week' | 'list' = 'week';

  readonly days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const raw = localStorage.getItem('currentUser');
    if (!raw) { this.isLoading = false; return; }
    const teacherId = JSON.parse(raw).id;
    if (!teacherId) { this.isLoading = false; return; }

    this.http.get<ScheduleEntry[]>(`${environment.apiUrl}/api/schedules/teacher/${teacherId}`).subscribe({
      next: data => { this.schedule = data ?? []; this.isLoading = false; },
      error: () => { this.hasError = true; this.isLoading = false; },
    });
  }

  getEntriesForDay(day: string): ScheduleEntry[] {
    return this.schedule
      .filter(e => (e.dayOfWeek ?? '').toLowerCase() === day.toLowerCase())
      .sort((a, b) => (a.startTime ?? '').localeCompare(b.startTime ?? ''));
  }

  get sortedSchedule(): ScheduleEntry[] {
    const order = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday'];
    return [...this.schedule].sort((a, b) => {
      const di = order.indexOf((a.dayOfWeek ?? '').toLowerCase()) - order.indexOf((b.dayOfWeek ?? '').toLowerCase());
      return di !== 0 ? di : (a.startTime ?? '').localeCompare(b.startTime ?? '');
    });
  }

  setView(mode: 'week' | 'list'): void { this.viewMode = mode; }
}
