import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputSwitchModule } from 'primeng/inputswitch';
import { TableModule } from 'primeng/table';
import { Teacher } from '../../../model/users/nastavnik';
import { TeacherService } from '../../../services/nastavnik.service';
import { NgClass, NgIf } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { Course } from '../../../model/academic/predmet';
import { StudentService } from '../../../services/studenti.service';
import { Student } from '../../../model/users/student';
import { InputGroupModule } from 'primeng/inputgroup';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-izdavanje-dokumenata',
  standalone: true,
  imports: [InputSwitchModule, FormsModule, TableModule, NgClass, ButtonModule, NgIf, InputGroupModule],
  templateUrl: './izdavanje-dokumenata.component.html',
  styleUrl: './izdavanje-dokumenata.component.css'
})
export class IzdavanjeDokumenataComponent implements OnInit {
  visible: boolean = false;

  teachers: Teacher[] = [];
  filteredTeachers: Teacher[] = [];

  students: Student[] = [];
  filteredStudents: Student[] = [];

  teacherCourses: Course[] = [];

  search = {
    firstName: '',
    lastName: '',
    email: ''
  };

  constructor(private teacherService: TeacherService, private studentService: StudentService) {}

  ngOnInit(): void {
    this.getTeachers();
    this.getStudents();
  }

  getTeachers() {
    this.teacherService.getAll().subscribe(x => {
      this.teachers = x;
      this.filteredTeachers = this.teachers;
    });
  }

  getStudents() {
    this.studentService.getAll().subscribe(x => {
      this.students = x;
      this.filteredStudents = this.students;
    });
  }

  searchTeachers() {
    this.filteredTeachers = this.teachers.filter(p =>
      (this.search.firstName ? p.firstName.toLowerCase().includes(this.search.firstName.toLowerCase()) : true) &&
      (this.search.lastName ? p.lastName.toLowerCase().includes(this.search.lastName.toLowerCase()) : true) &&
      (this.search.email ? p.email.toLowerCase().includes(this.search.email.toLowerCase()) : true)
    );
  }

  searchStudents() {
    this.filteredStudents = this.students.filter(s =>
      (this.search.firstName ? s.firstName.toLowerCase().includes(this.search.firstName.toLowerCase()) : true) &&
      (this.search.lastName ? s.lastName.toLowerCase().includes(this.search.lastName.toLowerCase()) : true) &&
      (this.search.email ? s.email.toLowerCase().includes(this.search.email.toLowerCase()) : true)
    );
  }

  clearSearch() {
    this.filteredStudents = this.students;
    this.filteredTeachers = this.teachers;
    this.search = { firstName: '', lastName: '', email: '' };
  }

  exportTeacherXml(teacher: Teacher) {}

  exportTeacherPdf(teacher: Teacher) {}

  exportStudentXml(student: Student) {}

  exportStudentPdf(student: Student) {}
}
