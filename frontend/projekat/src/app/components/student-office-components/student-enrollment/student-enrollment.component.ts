import { NgClass, NgFor, NgIf } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputSwitchModule } from 'primeng/inputswitch';
import { TableModule } from 'primeng/table';
import { StudentOfficeService } from '../../../services/student-office.service';
import { StudentService } from '../../../services/student.service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { RegisteredUser } from '../../../model/users/registered-user';
import { InputGroupModule } from 'primeng/inputgroup';
import { DialogModule } from 'primeng/dialog';
import { Faculty } from '../../../model/academic/faculty';
import { FacultyService } from '../../../services/faculty.service';
import { DropdownModule } from 'primeng/dropdown';
import { RegisteredUserService } from '../../../services/registered-user.service';
import { Student } from '../../../model/users/student';
import { CourseService } from '../../../services/course.service';
import { Course } from '../../../model/academic/course';
import { StudyProgramService } from '../../../services/study-program.service';
import { StudyProgram } from '../../../model/academic/study-program';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-student-enrollment',
  standalone: true,
  imports: [InputSwitchModule, FormsModule, ButtonModule, NgIf, TableModule, NgClass, ToastModule, InputGroupModule, DialogModule, DropdownModule, NgFor],
  templateUrl: './student-enrollment.component.html',
  styleUrl: './student-enrollment.component.css',
  providers: [MessageService]
})
export class StudentEnrollmentComponent implements OnInit {
  visible: boolean = false;
  enrollmentVisible: boolean = false;

  students: Student[] = [];
  filteredStudents: Student[] = [];

  courses: Course[] = [];
  programs: StudyProgram[] = [];

  yearEnrollmentVisible: boolean = false;
  studentForYear: any = {};
  enrollmentData: any = {};

  faculties: Faculty[] = [];
  selectedStudent: any = {};
  users: any[] = [];
  filteredUsers: any[] = [];
  existingUserForEnrollment: any = {};
  enrollments: any[] = [];

  newStudent: any = {
    firstName: '',
    lastName: '',
    email: '',
    username: '',
    password: '',
    faculty: undefined
  };

  programForEnrollment: any = {};

  studyYears: any[] = [
    { name: 'First', id: 1, godina: 1 },
    { name: 'Second', id: 2, godina: 2 },
    { name: 'Third', id: 3, godina: 3 },
    { name: 'Fourth', id: 4, godina: 4 }
  ];

  userFilter: any = {
    username: '',
    email: ''
  };

  filterStudent: any = {
    firstName: '',
    lastName: '',
    email: '',
    indexNumber: ''
  };

  constructor(
    private officeStaffService: StudentOfficeService,
    private studentService: StudentService,
    private messageService: MessageService,
    private facultyService: FacultyService,
    private registeredUserService: RegisteredUserService,
    private courseService: CourseService,
    private studyProgramService: StudyProgramService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
    this.loadFaculties();
    this.loadStudents();
    this.loadCourses();
    this.loadPrograms();
  }

  loadPrograms() {
    this.studyProgramService.getAll().subscribe(x => {
      this.programs = x;
    });
  }

  loadUsers() {
    this.officeStaffService.getusers().subscribe(x => {
      this.users = x.filter((u: any) => u.userType === 'RegisteredUser');
      this.filteredUsers = this.users;
    });
  }

  loadFaculties() {
    this.facultyService.getAll().subscribe(x => {
      this.faculties = x;
    });
  }

  loadStudents() {
    this.studentService.getAll().subscribe(x => {
      this.students = x;
      this.filteredStudents = this.students;
    });
  }

  loadCourses() {
    this.courseService.getAll().subscribe(x => {
      this.courses = x;
    });
  }

  enrollExistingStudent(user: RegisteredUser) {
    this.existingUserForEnrollment = { ...user };
    this.enrollmentVisible = true;
  }

  enrollExisting() {
    this.registeredUserService.dodeliStudenta(this.existingUserForEnrollment.id, this.existingUserForEnrollment).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Enrollment successful', detail: 'Student successfully enrolled.' });
        this.programForEnrollment = {};
        this.loadUsers();
        this.closeEnrollmentDialog();
        this.loadStudents();
        this.existingUserForEnrollment = {};
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred during enrollment.' });
      }
    });
  }

  closeEnrollmentDialog() {
    this.enrollmentVisible = false;
  }

  resetForm() {
    this.newStudent = { firstName: '', lastName: '', email: '', username: '', password: '', faculty: undefined };
  }

  searchUsers() {
    this.filteredUsers = this.users.filter(k =>
      (this.userFilter.username ? k.username.toLowerCase().includes(this.userFilter.username.toLowerCase()) : true) &&
      (this.userFilter.email ? k.email.toLowerCase().includes(this.userFilter.email.toLowerCase()) : true)
    );
  }

  clearFilter() {
    this.filteredUsers = this.users;
    this.userFilter = { username: '', email: '' };
    this.filteredStudents = this.students;
    this.filterStudent = { firstName: '', lastName: '', email: '', indexNumber: '' };
  }

  filterStudents() {
    this.filteredStudents = this.students.filter(s => {
      if (s && s.firstName && s.lastName && s.indexNumber && s.email) {
        return (
          (this.filterStudent.firstName ? s.firstName.toLowerCase().includes(this.filterStudent.firstName.toLowerCase()) : true) &&
          (this.filterStudent.lastName ? s.lastName.toLowerCase().includes(this.filterStudent.lastName.toLowerCase()) : true) &&
          (this.filterStudent.email ? s.email.toLowerCase().includes(this.filterStudent.email.toLowerCase()) : true) &&
          (this.filterStudent.indexNumber ? s.indexNumber.toLowerCase().includes(this.filterStudent.indexNumber.toLowerCase()) : true)
        );
      }
      return false;
    });
  }

  enrollToYear(student: Student) {
    this.yearEnrollmentVisible = true;
    this.studentForYear = student;
    if (student.id) {
      this.studentService.getenrollments(student.id).subscribe(x => {
        this.enrollments = x;
      });
    }
  }

  saveYearEnrollment() {
    const payload = {
      datumUpisa: new Date(),
      StudyYear: this.enrollmentData.StudyYear,
      student: this.studentForYear,
      StudyProgram: { id: this.enrollmentData.studijskiProgram?.id }
    };
    const student = { id: this.studentForYear.id };
    this.studentService.saveYearEnrollment(payload).subscribe(() => {
      this.studentService.dodajStudentaNacourse(this.enrollmentData.studijskiProgram?.id, student).subscribe(() => {});
      this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Student enrolled in year.' });
      this.closeYearEnrollmentDialog();
      this.studentForYear = {};
      this.enrollmentData = {};
    }, () => {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred during year enrollment.' });
    });
  }

  closeYearEnrollmentDialog() {
    this.yearEnrollmentVisible = false;
    this.studentForYear = {};
  }
}
