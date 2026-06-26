import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputSwitchModule } from 'primeng/inputswitch';
import { TableModule } from 'primeng/table';
import { Teacher } from '../../../model/users/teacher';
import { TeacherService } from '../../../services/teacher.service';
import { NgClass, NgIf } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { Course } from '../../../model/academic/course';
import { StudentService } from '../../../services/student.service';
import { Student } from '../../../model/users/student';
import { InputGroupModule } from 'primeng/inputgroup';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-document-issuance',
  standalone: true,
  imports: [InputSwitchModule, FormsModule, TableModule, NgClass, ButtonModule, NgIf, InputGroupModule],
  templateUrl: './document-issuance.component.html',
  styleUrl: './document-issuance.component.css'
})
export class DocumentIssuanceComponent implements OnInit {
  visible: boolean = false;

  teachers: Teacher[] = [];
  filteredTeachers: Teacher[] = [];

  students: Student[] = [];
  filteredStudents: Student[] = [];

  filter = {
    firstName: '',
    lastName: '',
    email: ''
  };

  constructor(private teacherService: TeacherService, private studentService: StudentService) {}

  ngOnInit(): void {
    this.loadTeachers();
    this.loadStudents();
  }

  loadTeachers() {
    this.teacherService.getAll().subscribe(x => {
      this.teachers = x;
      this.filteredTeachers = this.teachers;
    });
  }

  loadStudents() {
    this.studentService.getAll().subscribe(x => {
      this.students = x;
      this.filteredStudents = this.students;
    });
  }

  filterTeachers() {
    this.filteredTeachers = this.teachers.filter(p =>
      (this.filter.firstName ? p.firstName.toLowerCase().includes(this.filter.firstName.toLowerCase()) : true) &&
      (this.filter.lastName ? p.lastName.toLowerCase().includes(this.filter.lastName.toLowerCase()) : true) &&
      (this.filter.email ? p.email.toLowerCase().includes(this.filter.email.toLowerCase()) : true)
    );
  }

  filterStudents() {
    this.filteredStudents = this.students.filter(s =>
      (this.filter.firstName ? s.firstName.toLowerCase().includes(this.filter.firstName.toLowerCase()) : true) &&
      (this.filter.lastName ? s.lastName.toLowerCase().includes(this.filter.lastName.toLowerCase()) : true) &&
      (this.filter.email ? s.email.toLowerCase().includes(this.filter.email.toLowerCase()) : true)
    );
  }

  clearFilter() {
    this.filteredStudents = this.students;
    this.filteredTeachers = this.teachers;
    this.filter = { firstName: '', lastName: '', email: '' };
  }

  xmlTeacher(teacher: Teacher) {}
  pdfTeacher(teacher: Teacher) {}
  xmlStudent(student: Student) {}
  pdfStudent(student: Student) {}
}
