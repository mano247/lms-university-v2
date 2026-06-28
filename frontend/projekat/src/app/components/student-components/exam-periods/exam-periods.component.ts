import { environment } from '../../../../environments/environment';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

interface ExamTerm {
  id: number;
  courseName: string;
  courseCode: string;
  examDate: string;
  startTime: string;
  room: string;
  teacherName?: string;
}

interface ExamPeriod {
  id: number;
  name: string;
  startDate: string;
  endDate: string;
  isOpen: boolean;
  terms?: ExamTerm[];
}

@Component({
  selector: 'app-exam-periods',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './exam-periods.component.html',
  styleUrl: './exam-periods.component.css',
})
export class ExamPeriodsComponent implements OnInit {
  periods: ExamPeriod[] = [];
  isLoading = true;
  hasError = false;
  expandedId: number | null = null;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<ExamPeriod[]>(`${environment.apiUrl}/api/exam-periods/open`).subscribe({
      next: data => { this.periods = data ?? []; this.isLoading = false; },
      error: () => { this.hasError = true; this.isLoading = false; },
    });
  }

  toggle(id: number): void {
    this.expandedId = this.expandedId === id ? null : id;
  }

  isExpanded(id: number): boolean {
    return this.expandedId === id;
  }

  formatDate(d: string): string {
    if (!d) return '—';
    return new Date(d).toLocaleDateString('en-GB', { year: 'numeric', month: 'short', day: 'numeric' });
  }

  getDaysLeft(endDate: string): number {
    const end = new Date(endDate).getTime();
    const now = Date.now();
    return Math.max(0, Math.ceil((end - now) / 86400000));
  }
}
