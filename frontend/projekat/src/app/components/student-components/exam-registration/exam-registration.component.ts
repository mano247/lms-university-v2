import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { Course } from '../../../model/academic/course';
import { StudentService } from '../../../services/student.service';
import { ExamAttemptService } from '../../../services/exam-attempt.service';
import { ExamAttempt } from '../../../model/exam-attempt';
import { Student } from '../../../model/users/student';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-exam-registration',
  standalone: true,
  imports: [TableModule, ButtonModule, NgFor, ConfirmDialogModule, ToastModule, DialogModule, NgIf],
  templateUrl: './exam-registration.component.html',
  styleUrl: './exam-registration.component.css',
  providers: [ConfirmationService, MessageService]
})
export class ExamRegistrationComponent implements OnInit {
  balance: number = 1200.0;
  visible: boolean = false;

  availableCourses: any[] = [];
  selectedCourse: any | undefined;
  registeredExams: any[] = [];

  studentId: number | undefined;
  student: Student | undefined;

  constructor(
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private studentService: StudentService,
    private examAttemptService: ExamAttemptService
  ) {}

  ngOnInit(): void {
    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      this.studentId = parsedUser.id;
      if (this.studentId) {
        this.loadAvailableCourses(this.studentId);
        this.loadStudent(this.studentId);
        this.loadRegisteredExams(this.studentId);
      }
    }
  }

  registerForExam(event: Event, course: Course) {
    this.selectedCourse = course;
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Exam registration costs 1000 dinars. Do you want to continue?',
      header: 'Exam Registration',
      icon: 'pi pi-question-circle',
      acceptIcon: 'none',
      rejectIcon: 'none',
      rejectButtonStyleClass: 'p-button-text',
      accept: () => {
        if (this.balance >= 1000) {
          this.balance -= 1000;
          this.messageService.add({ severity: 'info', summary: 'Registration successful', detail: 'You have successfully registered for the exam.' });
          const attempt: ExamAttempt = {
            course: this.selectedCourse,
            student: this.student,
            teacher: this.selectedCourse?.teacher
          };
          this.examAttemptService.create(attempt).subscribe(
            () => {
              this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Exam registered.' });
              if (this.studentId) {
                this.loadRegisteredExams(this.studentId);
              }
            },
            () => {
              this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Exam registration failed.' });
            }
          );
        } else {
          this.messageService.add({ severity: 'error', summary: 'Insufficient funds', detail: 'You do not have enough funds.' });
        }
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled', detail: 'Exam registration cancelled.', life: 3000 });
      }
    });
  }

  loadAvailableCourses(id: number) {
    this.studentService.getIspitiZaPrijavu(id).subscribe(x => {
      this.availableCourses = x;
    });
  }

  loadStudent(id: number) {
    this.studentService.getById(id).subscribe(x => {
      this.student = x;
    });
  }

  loadRegisteredExams(id: number) {
    this.examAttemptService.getPrijavljeni(id).subscribe(x => {
      this.registeredExams = x;
    });
  }

  showPaymentDialog() {
    this.visible = true;
  }

  confirmPayment(event: Event, amount: number) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Do you want to complete the payment?',
      header: 'Confirmation',
      icon: 'pi pi-question-circle',
      acceptIcon: 'none',
      rejectIcon: 'none',
      rejectButtonStyleClass: 'p-button-text',
      accept: () => {
        this.balance += amount;
        this.visible = false;
        this.messageService.add({ severity: 'info', summary: 'Confirmed!', detail: 'Payment completed.' });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled!', detail: 'Payment cancelled.', life: 3000 });
      }
    });
  }

  cancelPayment() {
    this.visible = false;
  }

  isCourseRegistered(course: Course): boolean {
    return this.registeredExams.some(exam => exam.course?.id === course.id);
  }
}
