import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RegisteredUserService } from '../../../services/registered-user.service';
import { StudentService } from '../../../services/student.service';
import { FacultyService } from '../../../services/faculty.service';
import { StudyProgramService } from '../../../services/study-program.service';

@Component({
  selector: 'app-student-enrollment',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student-enrollment.component.html',
  styleUrl: './student-enrollment.component.css'
})
export class StudentEnrollmentComponent implements OnInit {
  viewMode: 'new-users' | 'students' = 'new-users';

  users: any[] = [];
  filteredUsers: any[] = [];
  students: any[] = [];
  filteredStudents: any[] = [];
  faculties: any[] = [];
  studyPrograms: any[] = [];
  filteredPrograms: any[] = [];

  showEnrollModal = false;
  showYearModal = false;
  enrollingUser: any = null;
  enrollingStudent: any = null;

  enrollForm: { userId: number | null; facultyId: number | null; programId: number | null; yearId: number } = {
    userId: null,
    facultyId: null,
    programId: null,
    yearId: 1
  };

  yearForm: { studentId: number | null; programId: number | null; yearId: number } = {
    studentId: null,
    programId: null,
    yearId: 1
  };

  userSearch = { username: '', email: '' };
  studentSearch = { firstName: '', lastName: '', indexNumber: '', email: '' };

  usersPage = 1;
  studentsPage = 1;
  readonly pageSize = 20;

  toast: { type: 'success' | 'error'; message: string } | null = null;
  isLoading = false;
  submitting = false;

  get pagedUsers() { return this.filteredUsers.slice((this.usersPage - 1) * this.pageSize, this.usersPage * this.pageSize); }
  get usersPages() { return Math.max(1, Math.ceil(this.filteredUsers.length / this.pageSize)); }

  get pagedStudents() { return this.filteredStudents.slice((this.studentsPage - 1) * this.pageSize, this.studentsPage * this.pageSize); }
  get studentsPages() { return Math.max(1, Math.ceil(this.filteredStudents.length / this.pageSize)); }

  studyYears = [
    { id: 1, label: '1st Year' },
    { id: 2, label: '2nd Year' },
    { id: 3, label: '3rd Year' },
    { id: 4, label: '4th Year' }
  ];

  constructor(
    private registeredUserService: RegisteredUserService,
    private studentService: StudentService,
    private facultyService: FacultyService,
    private studyProgramService: StudyProgramService
  ) {}

  ngOnInit(): void {
    this.isLoading = true;
    this.loadAll();
  }

  loadAll(): void {
    this.registeredUserService.getAll().subscribe({
      next: (data: any[]) => {
        this.users = data;
        this.filteredUsers = [...this.users];
      },
      error: () => this.showToast('error', 'Failed to load registered users.')
    });

    this.studentService.getAll().subscribe({
      next: (data: any[]) => {
        this.students = data;
        this.filteredStudents = [...this.students];
        this.isLoading = false;
      },
      error: () => {
        this.showToast('error', 'Failed to load students.');
        this.isLoading = false;
      }
    });

    this.facultyService.getAll().subscribe({
      next: (data: any[]) => { this.faculties = data; },
      error: () => this.showToast('error', 'Failed to load faculties.')
    });

    this.studyProgramService.getAll().subscribe({
      next: (data: any[]) => { this.studyPrograms = data; },
      error: () => this.showToast('error', 'Failed to load study programs.')
    });
  }

  setView(mode: 'new-users' | 'students'): void {
    this.viewMode = mode;
  }

  // ── New Users search ──────────────────────────────────────────

  searchUsers(): void {
    this.filteredUsers = this.users.filter(u =>
      (this.userSearch.username
        ? (u.username || '').toLowerCase().includes(this.userSearch.username.toLowerCase())
        : true) &&
      (this.userSearch.email
        ? (u.email || '').toLowerCase().includes(this.userSearch.email.toLowerCase())
        : true)
    );
    this.usersPage = 1;
  }

  clearUserSearch(): void {
    this.userSearch = { username: '', email: '' };
    this.filteredUsers = [...this.users];
    this.usersPage = 1;
  }

  // ── Existing Students search ──────────────────────────────────

  searchStudents(): void {
    this.filteredStudents = this.students.filter(s =>
      (this.studentSearch.firstName
        ? (s.firstName || '').toLowerCase().includes(this.studentSearch.firstName.toLowerCase())
        : true) &&
      (this.studentSearch.lastName
        ? (s.lastName || '').toLowerCase().includes(this.studentSearch.lastName.toLowerCase())
        : true) &&
      (this.studentSearch.indexNumber
        ? (s.indexNumber || '').toLowerCase().includes(this.studentSearch.indexNumber.toLowerCase())
        : true) &&
      (this.studentSearch.email
        ? (s.email || '').toLowerCase().includes(this.studentSearch.email.toLowerCase())
        : true)
    );
    this.studentsPage = 1;
  }

  clearStudentSearch(): void {
    this.studentSearch = { firstName: '', lastName: '', indexNumber: '', email: '' };
    this.filteredStudents = [...this.students];
    this.studentsPage = 1;
  }

  // ── Enroll Modal (new users) ──────────────────────────────────

  openEnrollModal(user: any): void {
    this.enrollingUser = user;
    this.enrollForm = { userId: user.id, facultyId: null, programId: null, yearId: 1 };
    this.filteredPrograms = [];
    this.showEnrollModal = true;
  }

  closeEnrollModal(): void {
    this.showEnrollModal = false;
    this.enrollingUser = null;
    this.filteredPrograms = [];
  }

  onFacultyChange(facultyId: any): void {
    this.enrollForm.facultyId = facultyId;
    this.enrollForm.programId = null;
    this.filteredPrograms = this.studyPrograms.filter(p => p.faculty?.id == facultyId);
  }

  submitEnroll(): void {
    if (!this.enrollForm.facultyId || !this.enrollForm.programId) {
      this.showToast('error', 'Please fill in all fields.');
      return;
    }
    this.submitting = true;
    const payload = {
      studyYear: { id: this.enrollForm.yearId },
      studyProgram: { id: this.enrollForm.programId },
      faculty: { id: this.enrollForm.facultyId }
    };
    this.registeredUserService.assignStudent(this.enrollForm.userId!, payload).subscribe({
      next: () => {
        this.showToast('success', 'User enrolled as student successfully.');
        this.submitting = false;
        this.closeEnrollModal();
        this.loadAll();
      },
      error: () => {
        this.showToast('error', 'Failed to enroll user as student.');
        this.submitting = false;
      }
    });
  }

  // ── Year Modal (existing students) ───────────────────────────

  openYearModal(student: any): void {
    this.enrollingStudent = student;
    this.yearForm = { studentId: student.id, programId: null, yearId: 1 };
    this.showYearModal = true;
  }

  closeYearModal(): void {
    this.showYearModal = false;
    this.enrollingStudent = null;
  }

  submitYearEnroll(): void {
    if (!this.yearForm.programId) {
      this.showToast('error', 'Please select a study program.');
      return;
    }
    this.submitting = true;
    const payload = {
      enrollmentDate: new Date(),
      studyYear: { id: this.yearForm.yearId },
      student: { id: this.yearForm.studentId },
      studyProgram: { id: this.yearForm.programId }
    };
    this.studentService.enrollInYear(payload).subscribe({
      next: () => {
        this.showToast('success', 'Student enrolled in next year successfully.');
        this.submitting = false;
        this.closeYearModal();
        this.loadAll();
      },
      error: () => {
        this.showToast('error', 'Failed to enroll student in year.');
        this.submitting = false;
      }
    });
  }

  // ── Toast ─────────────────────────────────────────────────────

  showToast(type: 'success' | 'error', message: string): void {
    this.toast = { type, message };
    setTimeout(() => { this.toast = null; }, 4000);
  }
}
