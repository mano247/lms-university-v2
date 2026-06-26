import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { TabViewModule } from 'primeng/tabview';
import { AssignedCoursesComponent } from "../../teacher-components/assigned-courses/assigned-courses.component";
import { CourseAnnouncementsComponent } from "../../student-components/course-announcements/course-announcements.component";
import { StudentListComponent } from "../../teacher-components/student-list/student-list.component";
import { GradeEntryComponent } from "../../teacher-components/grade-entry/grade-entry.component";
import { TeacherAnnouncementsComponent } from "../../teacher-components/teacher-announcements/teacher-announcements.component";

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-e-profesor',
  standalone: true,
  imports: [TabViewModule, AssignedCoursesComponent, CourseAnnouncementsComponent, StudentListComponent, GradeEntryComponent, TeacherAnnouncementsComponent],
  templateUrl: './e-profesor.component.html',
  styleUrl: './e-profesor.component.css'
})
export class EProfesorComponent implements OnInit {
  selectedTabIndex: number = 0;

  ngOnInit(): void {
    const savedIndex = localStorage.getItem('selectedTabIndex');
    this.selectedTabIndex = savedIndex ? +savedIndex : 0;
  }

  onTabChange(event: any) {
    localStorage.setItem('selectedTabIndex', event.index);
  }
}
