import { NgFor } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { TableModule } from 'primeng/table';
import { StudentService } from '../../../services/student.service';
import { Course } from '../../../model/academic/course';
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
  passedExams: any[] = [];
  failedCourses: Course[] = [];
  enrollments: any[] = [];

  constructor(private studentService: StudentService) {}

  ngOnInit(): void {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      const id = parsedUser.id;
      this.getPassedExams(id);
      this.getEnrollments(id);
    }
  }

  getPassedExams(id: number) {
    this.studentService.getPassedExams(id).subscribe(x => {
      this.passedExams = x;
    });
  }

  getEnrollments(id: number) {
    this.studentService.getEnrollments(id).subscribe(x => {
      this.enrollments = x;
    });
  }

  getAverageGrade(): number {
    if (this.passedExams.length === 0) return 0;
    const total = this.passedExams.reduce((sum, exam) => sum + exam.finalGrade, 0);
    return parseFloat((total / this.passedExams.length).toFixed(2));
  }

  getTotalEcts(): number {
    if (this.passedExams.length === 0) return 0;
    return this.passedExams.reduce((sum, exam) => sum + exam.ects, 0);
  }
}
