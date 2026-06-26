import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { TabViewModule } from 'primeng/tabview';
import { AssignedCoursesComponent } from "../../teacher-components/assigned-courses/assigned-courses.component";
import { CourseAnnouncementsComponent } from "../../student-components/course-announcements/course-announcements.component";
import { StudentListComponent } from "../../teacher-components/student-list/student-list.component";
import { GradeEntryComponent } from "../../teacher-components/grade-entry/grade-entry.component";
import { TeacherAnnouncementsComponent } from "../../teacher-components/teacher-announcements/teacher-announcements.component";

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-e-professor',
  standalone: true,
  imports: [TabViewModule, AssignedCoursesComponent, CourseAnnouncementsComponent, StudentListComponent, GradeEntryComponent, TeacherAnnouncementsComponent],
  templateUrl: './e-professor.component.html',
  styleUrl: './e-professor.component.css'
})
export class EProfessorComponent implements OnInit{

  selectedTabIndex: number = 0;

  ngOnInit(): void {
    const savedIndex = localStorage.getItem('selectedTabIndex');
    if (savedIndex) {
      this.selectedTabIndex = +savedIndex;
    }else{
      this.selectedTabIndex = 0;
    }
  }

  onTabChange(event: any) {
    localStorage.setItem('selectedTabIndex', event.index);
  }

}
