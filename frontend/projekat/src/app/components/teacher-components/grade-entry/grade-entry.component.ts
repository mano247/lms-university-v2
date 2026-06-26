import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DividerModule } from 'primeng/divider';
import { DropdownModule } from 'primeng/dropdown';
import { ToastModule } from 'primeng/toast';
import { Course } from '../../../model/academic/course';
import { Student } from '../../../model/users/student';
import { StudentService } from '../../../services/student.service';
import { TeacherService } from '../../../services/teacher.service';
import { ExamAttemptService } from '../../../services/exam-attempt.service';
import { ExamAttempt } from '../../../model/exam-attempt';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { FormsModule } from '@angular/forms';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-grade-entry',
  standalone: true,
  imports: [DropdownModule, ButtonModule, DividerModule, ConfirmDialogModule, ToastModule, TableModule, DialogModule, FormsModule],
  templateUrl: './grade-entry.component.html',
  styleUrl: './grade-entry.component.css',
  providers: [ConfirmationService, MessageService]
})
export class GradeEntryComponent implements OnInit {
  courses: Course[] = [];
  students: Student[] = [];
  grades: number[] = [6, 7, 8, 9, 10];

  teacherId: number = 0;

  selectedExamAttempt: any = {};

  result: any = {
    points: 0,
    grade: 0,
    note: ''
  };

  visible: boolean = false;
  selectedCourseId: number | undefined;
  examAttempts: ExamAttempt[] = [];

  constructor(
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private teacherService: TeacherService,
    private studentService: StudentService,
    private examAttemptService: ExamAttemptService
  ) {}

  ngOnInit(): void {
    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      const id = parsedUser.id;
      this.teacherId = id;
      this.loadCourses(id);
      this.loadStudents();
    }
  }

  confirmGrade(event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Do you want to submit this grade?',
      header: 'Grade Confirmation',
      icon: 'pi pi-exclamation-triangle',
      acceptIcon: 'none',
      rejectIcon: 'none',
      rejectButtonStyleClass: 'p-button-text',
      accept: () => {
        this.messageService.add({ severity: 'info', summary: 'Confirmed!', detail: 'Grade successfully submitted.' });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled', detail: 'Confirmation rejected.', life: 3000 });
      }
    });
  }

  onCourseChange(event: any) {
    const courseId = event.value;
    if (courseId) {
      this.examAttemptService.getPrijavljeniPocourseu(courseId).subscribe(x => {
        this.examAttempts = x;
      });
    }
  }

  loadCourses(id: number) {
    this.teacherService.mojicoursei(id).subscribe(x => {
      this.courses = x;
    });
  }

  loadStudents() {
    this.studentService.getAll().subscribe(x => {
      this.students = x;
    });
  }

  openGradeDialog(attempt: any) {
    this.visible = true;
    this.selectedExamAttempt = attempt;
  }

  submitGrade() {
    if (this.selectedExamAttempt && this.selectedExamAttempt.id) {
      const updated = {
        ...this.selectedExamAttempt,
        finalGrade: this.result.grade,
        points: this.result.points,
        note: this.result.note
      };
      this.examAttemptService.update(updated.id, updated).subscribe({
        next: () => {
          this.loadCourses(this.teacherId);
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Grade successfully entered.' });
          if (this.selectedCourseId) {
            this.examAttemptService.getPrijavljeniPocourseu(this.selectedCourseId).subscribe(x => {
              this.examAttempts = x;
            });
          }
          this.closeDialog();
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error entering grade.' });
          this.closeDialog();
        }
      });
    } else {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Exam attempt has no defined ID.' });
      this.closeDialog();
    }
  }

  closeDialog() {
    this.visible = false;
    this.selectedExamAttempt = {};
    this.result = { points: 0, grade: 0, note: '' };
  }
}
