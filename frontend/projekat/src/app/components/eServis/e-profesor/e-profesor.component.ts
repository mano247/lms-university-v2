import { Component, OnInit } from '@angular/core';
import { AssignedCoursesComponent } from '../../teacher-components/assigned-courses/assigned-courses.component';
import { TeacherAnnouncementsComponent } from '../../teacher-components/teacher-announcements/teacher-announcements.component';
import { StudentListComponent } from '../../teacher-components/student-list/student-list.component';
import { GradeEntryComponent } from '../../teacher-components/grade-entry/grade-entry.component';
import { TeacherScheduleComponent } from '../../teacher-components/teacher-schedule/teacher-schedule.component';
import { TeacherExamPeriodsComponent } from '../../teacher-components/teacher-exam-periods/teacher-exam-periods.component';

@Component({
  selector: 'app-e-profesor',
  standalone: true,
  imports: [
    AssignedCoursesComponent,
    TeacherAnnouncementsComponent,
    StudentListComponent,
    GradeEntryComponent,
    TeacherScheduleComponent,
    TeacherExamPeriodsComponent,
  ],
  templateUrl: './e-profesor.component.html',
  styleUrl: './e-profesor.component.css',
})
export class EProfesorComponent implements OnInit {
  activeTab = 'courses';
  teacherName = '';
  teacherInitials = 'TC';

  readonly tabs = [
    { id: 'courses',       label: 'My Courses',    icon: 'book' },
    { id: 'announcements', label: 'Announcements', icon: 'campaign' },
    { id: 'students',      label: 'Student List',  icon: 'assignment_ind' },
    { id: 'grades',        label: 'Grade Entry',   icon: 'grade' },
    { id: 'schedule',      label: 'My Schedule',   icon: 'calendar_today' },
    { id: 'periods',       label: 'Exam Periods',  icon: 'event_note' },
  ];

  ngOnInit(): void {
    const saved = localStorage.getItem('teacherDashboardTab');
    if (saved) this.activeTab = saved;

    const raw = localStorage.getItem('user');
    if (raw) {
      const u = JSON.parse(raw);
      const parts: string[] = [u.firstName, u.lastName].filter(Boolean);
      this.teacherName = parts.join(' ') || u.email || 'Teacher';
      this.teacherInitials = parts.map((n: string) => n[0]).join('').substring(0, 2).toUpperCase() || 'TC';
    }
  }

  setTab(tab: string): void {
    this.activeTab = tab;
    localStorage.setItem('teacherDashboardTab', tab);
  }
}
