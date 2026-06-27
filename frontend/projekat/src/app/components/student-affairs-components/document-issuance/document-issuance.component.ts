import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { TeacherService } from '../../../services/teacher.service';
import { StudentService } from '../../../services/student.service';

@Component({
  selector: 'app-document-issuance',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './document-issuance.component.html',
  styleUrl: './document-issuance.component.css'
})
export class DocumentIssuanceComponent implements OnInit {
  viewMode: 'professors' | 'students' = 'professors';

  teachers: any[] = [];
  filteredTeachers: any[] = [];
  students: any[] = [];
  filteredStudents: any[] = [];

  teacherSearch = { firstName: '', lastName: '', email: '' };
  studentSearch = { firstName: '', lastName: '', indexNumber: '' };

  isLoading = false;
  downloadingId: string | null = null;
  toast: { type: 'success' | 'error'; message: string } | null = null;

  private readonly base = 'http://localhost:8080';

  constructor(
    private http: HttpClient,
    private teacherService: TeacherService,
    private studentService: StudentService
  ) {}

  ngOnInit(): void {
    this.isLoading = true;
    this.teacherService.getAll().subscribe({
      next: (data: any[]) => {
        this.teachers = data ?? [];
        this.filteredTeachers = [...this.teachers];
      },
      error: () => this.showToast('error', 'Failed to load professors.')
    });
    this.studentService.getAll().subscribe({
      next: (data: any[]) => {
        this.students = data ?? [];
        this.filteredStudents = [...this.students];
        this.isLoading = false;
      },
      error: () => { this.showToast('error', 'Failed to load students.'); this.isLoading = false; }
    });
  }

  setView(mode: 'professors' | 'students'): void {
    this.viewMode = mode;
  }

  searchTeachers(): void {
    this.filteredTeachers = this.teachers.filter(p =>
      (!this.teacherSearch.firstName || (p.firstName || '').toLowerCase().includes(this.teacherSearch.firstName.toLowerCase())) &&
      (!this.teacherSearch.lastName || (p.lastName || '').toLowerCase().includes(this.teacherSearch.lastName.toLowerCase())) &&
      (!this.teacherSearch.email || (p.email || '').toLowerCase().includes(this.teacherSearch.email.toLowerCase()))
    );
  }

  clearTeacherSearch(): void {
    this.teacherSearch = { firstName: '', lastName: '', email: '' };
    this.filteredTeachers = [...this.teachers];
  }

  searchStudents(): void {
    this.filteredStudents = this.students.filter(s =>
      (!this.studentSearch.firstName || (s.firstName || '').toLowerCase().includes(this.studentSearch.firstName.toLowerCase())) &&
      (!this.studentSearch.lastName || (s.lastName || '').toLowerCase().includes(this.studentSearch.lastName.toLowerCase())) &&
      (!this.studentSearch.indexNumber || (s.indexNumber || '').toLowerCase().includes(this.studentSearch.indexNumber.toLowerCase()))
    );
  }

  clearStudentSearch(): void {
    this.studentSearch = { firstName: '', lastName: '', indexNumber: '' };
    this.filteredStudents = [...this.students];
  }

  downloadBlob(url: string, filename: string, key: string): void {
    this.downloadingId = key;
    this.http.get(url, { responseType: 'blob' }).subscribe({
      next: (blob) => {
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = filename;
        link.click();
        URL.revokeObjectURL(link.href);
        this.downloadingId = null;
        this.showToast('success', `${filename} downloaded.`);
      },
      error: () => {
        this.downloadingId = null;
        this.showToast('error', 'Export failed. Please try again.');
      }
    });
  }

  exportTeacherXml(teacher: any): void {
    this.downloadBlob(`${this.base}/api/export/teachers/${teacher.id}/xml`, `teacher_${teacher.id}.xml`, `t-xml-${teacher.id}`);
  }

  exportTeacherPdf(teacher: any): void {
    this.downloadBlob(`${this.base}/api/export/teachers/${teacher.id}/pdf`, `teacher_${teacher.id}.pdf`, `t-pdf-${teacher.id}`);
  }

  exportStudentXml(student: any): void {
    this.downloadBlob(`${this.base}/api/export/students/${student.id}/xml`, `student_${student.id}.xml`, `s-xml-${student.id}`);
  }

  exportStudentPdf(student: any): void {
    this.downloadBlob(`${this.base}/api/export/students/${student.id}/pdf`, `student_${student.id}.pdf`, `s-pdf-${student.id}`);
  }

  private showToast(type: 'success' | 'error', message: string): void {
    this.toast = { type, message };
    setTimeout(() => { this.toast = null; }, 4000);
  }
}
