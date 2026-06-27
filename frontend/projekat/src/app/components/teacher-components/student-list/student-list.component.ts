import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TeacherService } from '../../../services/teacher.service';
import { StudentService } from '../../../services/student.service';
import { ExamAttemptService } from '../../../services/exam-attempt.service';
import { ThesisService } from '../../../services/thesis.service';

@Component({
  selector: 'app-student-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student-list.component.html',
  styleUrl: './student-list.component.css',
})
export class StudentListComponent implements OnInit {
  courses: any[] = [];
  selectedCourseId: number | null = null;
  allStudents: any[] = [];
  filteredStudents: any[] = [];
  isLoading = true;

  search = { firstName: '', lastName: '', indexNumber: '' };

  showDetails = false;
  selectedStudent: any = null;
  studentData: any = null;
  loadingDetails = false;

  page = 1;
  pageSize = 10;

  constructor(
    private teacherService: TeacherService,
    private studentService: StudentService,
    private examAttemptService: ExamAttemptService,
    private thesisService: ThesisService
  ) {}

  ngOnInit(): void {
    const raw = localStorage.getItem('user');
    if (!raw) { this.isLoading = false; return; }
    const id = JSON.parse(raw).id;
    if (!id) { this.isLoading = false; return; }

    this.teacherService.getMyCourses(id).subscribe({
      next: courses => {
        this.courses = courses ?? [];
        if (this.courses.length) {
          this.selectedCourseId = this.courses[0].id ?? null;
          this.loadStudentsForCourse();
        } else {
          this.isLoading = false;
        }
      },
      error: () => { this.isLoading = false; }
    });
  }

  onCourseChange(val: string): void {
    this.selectedCourseId = val ? Number(val) : null;
    this.loadStudentsForCourse();
  }

  loadStudentsForCourse(): void {
    const course = this.courses.find(c => c.id === this.selectedCourseId);
    this.allStudents = course?.students ?? [];
    this.filteredStudents = [...this.allStudents];
    this.page = 1;
    this.isLoading = false;
  }

  applySearch(): void {
    const { firstName, lastName, indexNumber } = this.search;
    this.filteredStudents = this.allStudents.filter(s =>
      (!firstName || (s.firstName ?? '').toLowerCase().includes(firstName.toLowerCase())) &&
      (!lastName || (s.lastName ?? '').toLowerCase().includes(lastName.toLowerCase())) &&
      (!indexNumber || (s.indexNumber ?? '').toLowerCase().includes(indexNumber.toLowerCase()))
    );
    this.page = 1;
  }

  clearSearch(): void {
    this.search = { firstName: '', lastName: '', indexNumber: '' };
    this.filteredStudents = [...this.allStudents];
    this.page = 1;
  }

  get pagedStudents(): any[] {
    return this.filteredStudents.slice((this.page - 1) * this.pageSize, this.page * this.pageSize);
  }

  get totalPages(): number { return Math.ceil(this.filteredStudents.length / this.pageSize); }

  pageRange(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i + 1);
  }

  openDetails(student: any): void {
    this.selectedStudent = student;
    this.studentData = null;
    this.showDetails = true;
    this.loadingDetails = true;
    if (!student.id) { this.loadingDetails = false; return; }

    let pending = 5;
    const done = () => { if (--pending === 0) this.loadingDetails = false; };
    const data: any = { enrollments: [], passedExams: [], failedExams: [], registeredExams: [], thesis: null };

    this.studentService.getEnrollments(student.id).subscribe({ next: x => { data.enrollments = x ?? []; done(); }, error: done });
    this.studentService.getPassedExams(student.id).subscribe({ next: x => { data.passedExams = x ?? []; done(); }, error: done });
    this.studentService.getFailedExams(student.id).subscribe({ next: x => { data.failedExams = x ?? []; done(); }, error: done });
    this.examAttemptService.getRegisteredByStudent(student.id).subscribe({ next: x => { data.registeredExams = x ?? []; done(); }, error: done });
    this.thesisService.findByStudent(student.id).subscribe({ next: x => { data.thesis = x; done(); }, error: done });

    this.studentData = data;
  }

  closeDetails(): void { this.showDetails = false; this.selectedStudent = null; this.studentData = null; }

  get avgGrade(): string {
    const passed = this.studentData?.passedExams ?? [];
    if (!passed.length) return '—';
    return (passed.reduce((s: number, e: any) => s + (e.finalGrade ?? 0), 0) / passed.length).toFixed(2);
  }

  get totalEcts(): number {
    return (this.studentData?.passedExams ?? []).reduce((s: number, e: any) => s + (e.ects ?? 0), 0);
  }
}
