import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DataViewModule } from 'primeng/dataview';
import { NgFor } from '@angular/common';
import { DividerModule } from 'primeng/divider';
import { Course } from '../../../model/academic/course';
import { StudentService } from '../../../services/student.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-my-courses',
  standalone: true,
  imports: [NgFor, DataViewModule, DividerModule],
  templateUrl: './my-courses.component.html',
  styleUrl: './my-courses.component.css'
})
export class MyCoursesComponent implements OnInit {
  myCourses: Course[] = [];

  constructor(private studentService: StudentService) {}

  ngOnInit(): void {
    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      const id = parsedUser.id;
      this.loadMyCourses(id);
    }
  }

  loadMyCourses(id: number) {
    this.studentService.getAktivnicoursei(id).subscribe(x => {
      this.myCourses = x;
    });
  }

  formatDate(date: Date | undefined): string {
    if (!date) return '';
    const d = new Date(date);
    const year = d.getUTCFullYear();
    const month = (d.getUTCMonth() + 1).toString().padStart(2, '0');
    const day = d.getUTCDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}
