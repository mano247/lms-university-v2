import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { MyCoursesComponent } from "../../student-components/my-courses/my-courses.component";
import { CourseAnnouncementsComponent } from "../../student-components/course-announcements/course-announcements.component";
import { StudyHistoryComponent } from "../../student-components/study-history/study-history.component";
import { ExamRegistrationComponent } from "../../student-components/exam-registration/exam-registration.component";
import { TabViewModule } from 'primeng/tabview';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-e-student',
  standalone: true,
  imports: [MyCoursesComponent, CourseAnnouncementsComponent, StudyHistoryComponent, ExamRegistrationComponent, TabViewModule],
  templateUrl: './e-student.component.html',
  styleUrl: './e-student.component.css'
})
export class EStudentComponent implements OnInit {
  selectedTabIndex: number = 0;

  ngOnInit(): void {
    const savedIndex = localStorage.getItem('selectedTabIndex');
    this.selectedTabIndex = savedIndex ? +savedIndex : 0;
  }

  onTabChange(event: any) {
    localStorage.setItem('selectedTabIndex', event.index);
  }
}
