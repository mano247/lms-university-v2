import { NgFor } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { TableModule } from 'primeng/table';
import { StudentService } from '../../../services/student.service';
import { SortEvent } from 'primeng/api';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-study-history',
  standalone: true,
  imports: [TableModule, NgFor],
  templateUrl: './study-history.component.html',
  styleUrl: './study-history.component.css'
})
export class StudyHistoryComponent implements OnInit {
  passedCourses: any[] = [];
  failedCourses: any[] = [];
  enrollments: any[] = [];

  constructor(private studentService: StudentService) {}

  ngOnInit(): void {
    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      const id = parsedUser.id;
      this.loadPassedCourses(id);
      this.loadEnrollments(id);
    }
  }

  loadPassedCourses(id: number) {
    this.studentService.polozeniIspiti(id).subscribe(x => {
      this.passedCourses = x;
    });
  }

  loadEnrollments(id: number) {
    this.studentService.getenrollments(id).subscribe(x => {
      this.enrollments = x;
    });
  }

  getAverageGrade(): number {
    if (this.passedCourses.length === 0) return 0;
    const total = this.passedCourses.reduce((sum, item) => sum + (item.ocena || 0), 0);
    return parseFloat((total / this.passedCourses.length).toFixed(2));
  }

  getTotalECTS(): number {
    if (this.passedCourses.length === 0) return 0;
    return this.passedCourses.reduce((sum, item) => sum + (item.espb || 0), 0);
  }
}
