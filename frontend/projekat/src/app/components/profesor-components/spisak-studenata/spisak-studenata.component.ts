import { NgFor, NgIf, NgStyle } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { Student } from '../../../model/users/student';
import { StudentService } from '../../../services/studenti.service';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { DividerModule } from 'primeng/divider';
import { TeacherService } from '../../../services/nastavnik.service';
import { FormsModule } from '@angular/forms';
import { ExamAttemptService } from '../../../services/polaganje.service';
import { ThesisService } from '../../../services/zavrsni-rad.service';
import { Course } from '../../../model/academic/predmet';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-spisak-studenata',
  standalone: true,
  imports: [TableModule, NgFor, DropdownModule, InputGroupModule, FormsModule, InputGroupAddonModule,
    ButtonModule, DialogModule, DividerModule, NgStyle, NgIf],
  templateUrl: './spisak-studenata.component.html',
  styleUrl: './spisak-studenata.component.css'
})
export class SpisakStudenataComponent implements OnInit {
  visible: boolean = false;

  filteredStudents: Student[] = [];
  students: Student[] = [];

  courses: Course[] = [];
  selectedCourse: any;

  enrollments: any;
  registeredExams: any;
  studentDetails: any = {};
  thesis: any = {};

  totalEcts: number = 0;
  averageGrade: number = 0;

  search = {
    firstName: '',
    lastName: '',
    indexNumber: ''
  };

  constructor(
    private studentService: StudentService,
    private teacherService: TeacherService,
    private examAttemptService: ExamAttemptService,
    private thesisService: ThesisService
  ) {}

  ngOnInit(): void {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      const id = parsedUser.id;
      this.getCourses(id);
    }
  }

  getCourses(id: number) {
    this.teacherService.getMyCourses(id).subscribe(courses => {
      this.courses = courses;
      if (this.courses.length > 0) {
        this.selectedCourse = this.courses[0];
        this.filteredStudents = this.selectedCourse.students;
      }
    });
  }

  onCourseChange(event: any) {
    if (event.value && event.value.students) {
      this.filteredStudents = event.value.students;
    } else {
      this.filteredStudents = [];
    }
  }

  getStudentInfo(id: number) {
    this.studentService.getEnrollments(id).subscribe(x => {
      this.studentDetails.enrollments = x;
    });

    this.examAttemptService.getRegisteredByStudent(id).subscribe(x => {
      this.studentDetails.registeredExams = x;
    });

    this.thesisService.findByStudent(id).subscribe(x => {
      this.studentDetails.thesis = x;
    });

    this.studentService.getPassedExams(id).subscribe(x => {
      this.studentDetails.passedExams = x;

      if (this.studentDetails.passedExams.length > 0) {
        const gradeSum = this.studentDetails.passedExams.reduce((sum: number, exam: any) => sum + exam.finalGrade, 0);
        this.studentDetails.averageGrade = gradeSum / this.studentDetails.passedExams.length;
      } else {
        this.averageGrade = 0;
      }

      this.studentDetails.totalEcts = this.studentDetails.passedExams.reduce((sum: number, exam: any) => sum + exam.ects, 0);
    });

    this.studentService.getFailedExams(id).subscribe(x => {
      this.studentDetails.failedExams = x;
    });
  }

  showStudentDetails(student: Student) {
    this.visible = true;
    this.studentDetails = student;
    if (student.id) {
      this.getStudentInfo(student.id);
    }
  }

  searchStudents() {
    this.filteredStudents = this.students.filter(s =>
      (this.search.firstName ? (s.firstName || '').toLowerCase().includes(this.search.firstName.toLowerCase()) : true) &&
      (this.search.lastName ? (s.lastName || '').toLowerCase().includes(this.search.lastName.toLowerCase()) : true) &&
      (this.search.indexNumber ? (s.indexNumber || '').toLowerCase().includes(this.search.indexNumber.toLowerCase()) : true)
    );
  }

  clearSearch() {
    this.filteredStudents = this.students;
    this.search = {
      firstName: '',
      lastName: '',
      indexNumber: ''
    };
  }
}
