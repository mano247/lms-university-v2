import { Component, HostListener, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth/auth.service';
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
  profileMenuOpen = false;
  headerSearch = '';

  @HostListener('document:click')
  closeProfileMenu() { this.profileMenuOpen = false; }

  readonly tabs = [
    { id: 'courses',       label: 'My Courses',    icon: 'book' },
    { id: 'announcements', label: 'Announcements', icon: 'campaign' },
    { id: 'students',      label: 'Student List',  icon: 'assignment_ind' },
    { id: 'grades',        label: 'Grade Entry',   icon: 'grade' },
    { id: 'schedule',      label: 'My Schedule',   icon: 'calendar_today' },
    { id: 'periods',       label: 'Exam Periods',  icon: 'event_note' },
  ];

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    const saved = localStorage.getItem('teacherDashboardTab');
    if (saved) this.activeTab = saved;

    const u = this.authService.getCurrentUser();
    if (u) {
      this.teacherName = u.username || u.email || 'Teacher';
      this.teacherInitials = (u.username || u.email || 'TC').substring(0, 2).toUpperCase();
    }
  }

  setTab(tab: string): void {
    this.activeTab = tab;
    localStorage.setItem('teacherDashboardTab', tab);
  }

  toggleProfileMenu(event: MouseEvent): void {
    event.stopPropagation();
    this.profileMenuOpen = !this.profileMenuOpen;
  }

  onHeaderSearch(event: Event): void {
    this.headerSearch = (event.target as HTMLInputElement).value;
  }

  logout(): void {
    this.authService.logout();
  }
}
