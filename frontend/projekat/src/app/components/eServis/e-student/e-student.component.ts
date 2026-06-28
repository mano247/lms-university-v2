import { Component, OnInit } from '@angular/core';
import { MyCoursesComponent } from '../../student-components/my-courses/my-courses.component';
import { CourseAnnouncementsComponent } from '../../student-components/course-announcements/course-announcements.component';
import { StudyHistoryComponent } from '../../student-components/study-history/study-history.component';
import { ExamRegistrationComponent } from '../../student-components/exam-registration/exam-registration.component';
import { ClassScheduleComponent } from '../../student-components/class-schedule/class-schedule.component';
import { ExamPeriodsComponent } from '../../student-components/exam-periods/exam-periods.component';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
  selector: 'app-e-student',
  standalone: true,
  imports: [
    MyCoursesComponent,
    CourseAnnouncementsComponent,
    StudyHistoryComponent,
    ExamRegistrationComponent,
    ClassScheduleComponent,
    ExamPeriodsComponent,
  ],
  templateUrl: './e-student.component.html',
  styleUrl: './e-student.component.css',
})
export class EStudentComponent implements OnInit {
  activeTab = 'courses';
  studentName = '';
  studentInitials = 'ST';

  readonly tabs = [
    { id: 'courses',       label: 'My Courses',        icon: 'school' },
    { id: 'announcements', label: 'Announcements',      icon: 'campaign' },
    { id: 'history',       label: 'Study History',      icon: 'history_edu' },
    { id: 'exams',         label: 'Exam Registration',  icon: 'assignment_turned_in' },
    { id: 'schedule',      label: 'Class Schedule',     icon: 'calendar_month' },
    { id: 'periods',       label: 'Exam Periods',       icon: 'event_repeat' },
  ];

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    const saved = localStorage.getItem('studentDashboardTab');
    if (saved) this.activeTab = saved;

    const u = this.authService.getCurrentUser();
    if (u) {
      this.studentName = u.username || u.email || 'Student';
      this.studentInitials = (u.username || u.email || 'ST').substring(0, 2).toUpperCase();
    }
  }

  setTab(tab: string): void {
    this.activeTab = tab;
    localStorage.setItem('studentDashboardTab', tab);
  }
}
