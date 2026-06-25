import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DividerModule } from 'primeng/divider';
import { DropdownModule } from 'primeng/dropdown';
import { ToastModule } from 'primeng/toast';
import { Course } from '../../../model/academic/predmet';
import { Student } from '../../../model/users/student';
import { StudentService } from '../../../services/studenti.service';
import { TeacherService } from '../../../services/nastavnik.service';
import { ExamAttemptService } from '../../../services/polaganje.service';
import { ExamAttempt } from '../../../model/polaganje';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { FormsModule } from '@angular/forms';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-unos-ocena',
  standalone: true,
  imports: [DropdownModule, ButtonModule, DividerModule, ConfirmDialogModule, ToastModule, TableModule, DialogModule, FormsModule],
  templateUrl: './unos-ocena.component.html',
  styleUrl: './unos-ocena.component.css',
  providers: [ConfirmationService, MessageService]
})
export class UnosOcenaComponent implements OnInit {
  courses: Course[] = [];
  students: Student[] = [];
  grades: number[] | undefined;

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
    this.grades = [6, 7, 8, 9, 10];

    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      const id = parsedUser.id;
      this.teacherId = parsedUser.id;
      this.getCourses(id);
      this.getStudents();
    }
  }

  confirmGrade(event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Do you want to submit this grade?',
      header: 'Confirm grade',
      icon: 'pi pi-exclamation-triangle',
      acceptIcon: 'none',
      rejectIcon: 'none',
      rejectButtonStyleClass: 'p-button-text',
      accept: () => {
        this.messageService.add({ severity: 'info', summary: 'Confirmed!', detail: 'Grade submitted successfully.' });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled', detail: 'Grade submission cancelled.', life: 3000 });
      }
    });
  }

  onCourseChange(event: any) {
    const courseId = event.value;
    if (courseId) {
      this.examAttemptService.getRegisteredByCourse(courseId).subscribe(x => {
        this.examAttempts = x;
      });
    }
  }

  getCourses(id: number) {
    this.teacherService.getMyCourses(id).subscribe(x => {
      this.courses = x;
    });
  }

  getStudents() {
    this.studentService.getAll().subscribe(x => {
      this.students = x;
    });
  }

  enterGrade(examAttempt: any) {
    this.visible = true;
    this.selectedExamAttempt = examAttempt;
  }

  submitGrade() {
    if (this.selectedExamAttempt && this.selectedExamAttempt.id) {
      const updatedAttempt = {
        ...this.selectedExamAttempt,
        finalGrade: this.result.grade,
        points: this.result.points,
        note: this.result.note
      };

      this.examAttemptService.update(updatedAttempt.id, updatedAttempt).subscribe({
        next: () => {
          this.getCourses(this.teacherId);
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Grade submitted successfully.' });
          if (this.selectedCourseId) {
            this.examAttemptService.getRegisteredByCourse(this.selectedCourseId).subscribe(x => {
              this.examAttempts = x;
            });
          }
          this.cancelDialog();
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error submitting grade.' });
          this.cancelDialog();
        }
      });
    } else {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Exam attempt has no defined ID.' });
      this.cancelDialog();
    }
  }

  cancelDialog() {
    this.visible = false;
    this.selectedExamAttempt = {};
    this.result = {
      points: 0,
      grade: 0,
      note: ''
    };
  }
}
