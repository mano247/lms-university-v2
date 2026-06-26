import { NgClass, NgFor, NgIf } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputSwitchModule } from 'primeng/inputswitch';
import { TableModule } from 'primeng/table';
import { StudentOfficeService } from '../../../services/studentska-sluzba.service';
import { StudentService } from '../../../services/studenti.service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { RegisteredUser } from '../../../model/users/registrovaniKorisnik';
import { InputGroupModule } from 'primeng/inputgroup';
import { DialogModule } from 'primeng/dialog';
import { Faculty } from '../../../model/academic/fakultet';
import { FacultyService } from '../../../services/fakultet.service';
import { DropdownModule } from 'primeng/dropdown';
import { RegisteredUserService } from '../../../services/registrovani-korisnik.service';
import { Student } from '../../../model/users/student';
import { CourseService } from '../../../services/predmet.service';
import { Course } from '../../../model/academic/predmet';
import { StudyProgramService } from '../../../services/studijski-program.service';
import { StudyProgram } from '../../../model/academic/studijskiProgram';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-upis-studenata',
  standalone: true,
  imports: [InputSwitchModule, FormsModule, ButtonModule, NgIf, TableModule, NgClass, ToastModule, InputGroupModule, DialogModule, DropdownModule, NgFor],
  templateUrl: './upis-studenata.component.html',
  styleUrl: './upis-studenata.component.css',
  providers: [MessageService]
})
export class UpisStudenataComponent implements OnInit {
  visible: boolean = false;
  enrollDialog: boolean = false;

  students: Student[] = [];
  filteredStudents: Student[] = [];

  courses: Course[] = [];
  studyPrograms: StudyProgram[] = [];

  yearEnrollmentDialog: boolean = false;
  studentForYear: any = {};
  yearEnrollmentData: any = {};

  faculties: Faculty[] = [];
  selectedStudent: any = {};
  users: any[] = [];
  userForEnrollment: any = {};
  enrollments: any = {};

  newStudent: any = {
    firstName: '',
    lastName: '',
    email: '',
    username: '',
    password: '',
    faculty: undefined
  };

  programForEnrollment: any = {};
  firstYear: any = { id: 1, year: 1 };

  userSearch: any = {
    username: '',
    email: ''
  };

  studentSearch: any = {
    firstName: '',
    lastName: '',
    email: '',
    indexNumber: ''
  };

  studyYears: any = [
    { label: 'first', id: 1, year: 1 },
    { label: 'second', id: 2, year: 2 },
    { label: 'third', id: 3, year: 3 },
    { label: 'fourth', id: 4, year: 4 }
  ];

  filteredUsers: any[] = [];

  constructor(
    private studentOfficeService: StudentOfficeService,
    private studentService: StudentService,
    private messageService: MessageService,
    private facultyService: FacultyService,
    private registeredUserService: RegisteredUserService,
    private courseService: CourseService,
    private studyProgramService: StudyProgramService
  ) {}

  ngOnInit(): void {
    this.getUsers();
    this.getFaculties();
    this.getStudents();
    this.getCourses();
    this.getStudyPrograms();
  }

  getStudyPrograms() {
    this.studyProgramService.getAll().subscribe(x => {
      this.studyPrograms = x;
    });
  }

  getUsers() {
    this.studentOfficeService.getUsers().subscribe(x => {
      this.users = x.filter((user: any) => user.tipZaIzmenu === 'RegistrovaniKorisnik');
      this.filteredUsers = this.users;
    });
  }

  getFaculties() {
    this.facultyService.getAll().subscribe(x => {
      this.faculties = x;
    });
  }

  getStudents() {
    this.studentService.getAll().subscribe(x => {
      this.students = x;
      this.filteredStudents = this.students;
    });
  }

  getCourses() {
    this.courseService.getAll().subscribe(x => {
      this.courses = x;
    });
  }

  openEnrollExistingUser(user: RegisteredUser) {
    this.userForEnrollment = user;
    this.userForEnrollment = { ...this.userForEnrollment };
    this.enrollDialog = true;
  }

  enrollExistingUser() {
    this.registeredUserService.assignStudent(this.userForEnrollment.id, this.userForEnrollment).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Enrolled', detail: 'Student enrolled successfully.' });
        this.programForEnrollment = {};
        this.getUsers();
        this.closeEnrollDialog();
        this.getStudents();
        this.userForEnrollment = {};
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while enrolling student.' });
      }
    });
  }

  closeEnrollDialog() {
    this.enrollDialog = false;
  }

  resetForm() {
    this.newStudent = {
      firstName: '',
      lastName: '',
      email: '',
      username: '',
      password: '',
      faculty: undefined
    };
  }

  searchUsers() {
    this.filteredUsers = this.users.filter((u: any) =>
      (this.userSearch.username ? u.username.toLowerCase().includes(this.userSearch.username.toLowerCase()) : true) &&
      (this.userSearch.email ? u.email.toLowerCase().includes(this.userSearch.email.toLowerCase()) : true)
    );
  }

  clearSearch() {
    this.filteredUsers = this.users;
    this.userSearch = { username: '', email: '' };
    this.filteredStudents = this.students;
    this.studentSearch = { firstName: '', lastName: '', email: '', indexNumber: '' };
  }

  searchStudents() {
    this.filteredStudents = this.students.filter(s => {
      if (s && s.firstName && s.lastName && s.indexNumber && s.email) {
        return (
          (this.studentSearch.firstName ? s.firstName.toLowerCase().includes(this.studentSearch.firstName.toLowerCase()) : true) &&
          (this.studentSearch.lastName ? s.lastName.toLowerCase().includes(this.studentSearch.lastName.toLowerCase()) : true) &&
          (this.studentSearch.email ? s.email.toLowerCase().includes(this.studentSearch.email.toLowerCase()) : true) &&
          (this.studentSearch.indexNumber ? s.indexNumber.toLowerCase().includes(this.studentSearch.indexNumber.toLowerCase()) : true)
        );
      } else {
        return false;
      }
    });
  }

  openYearEnrollmentDialog(student: Student) {
    this.yearEnrollmentDialog = true;
    this.studentForYear = student;
    if (student.id) {
      this.studentService.getEnrollments(student.id).subscribe(x => {
        this.enrollments = x;
      });
    }
  }

  enrollInYear() {
    const enrollmentData = {
      enrollmentDate: new Date(),
      studyYear: this.yearEnrollmentData.studyYear,
      student: this.studentForYear,
      studyProgram: { id: this.yearEnrollmentData.studyProgram.id }
    };
    const studentRef = { id: this.studentForYear.id };
    this.studentService.enrollInYear(enrollmentData).subscribe(x => {
      this.studentService.addStudentToCourse(this.yearEnrollmentData.studyProgram.id, studentRef).subscribe(() => {});
      this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Student enrolled in year successfully.' });
      this.closeYearEnrollmentDialog();
      this.studentForYear = {};
      this.yearEnrollmentData = {};
    }, () => {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while enrolling student in year.' });
    });
  }

  closeYearEnrollmentDialog() {
    this.yearEnrollmentDialog = false;
    this.studentForYear = {};
  }
}
