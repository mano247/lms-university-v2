import { ChangeDetectorRef, Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DataViewModule } from 'primeng/dataview';
import { NgFor } from '@angular/common';
import { DividerModule } from 'primeng/divider';
import { Course } from '../../../model/academic/course';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { FormsModule } from '@angular/forms';
import { TeacherService } from '../../../services/teacher.service';
import { CourseService } from '../../../services/course.service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-assigned-courses',
  standalone: true,
  imports: [NgFor, DataViewModule, DividerModule, ButtonModule, DialogModule, InputTextareaModule, FormsModule, ToastModule],
  templateUrl: './assigned-courses.component.html',
  styleUrl: './assigned-courses.component.css',
  providers: [MessageService]
})
export class AssignedCoursesComponent implements OnInit {
  visible: boolean = false;
  courses: Course[] = [];
  syllabus: string | undefined;
  teacherId: any;
  selectedCourse: any = null;

  constructor(
    private teacherService: TeacherService,
    private courseService: CourseService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      const id = parsedUser.id;
      this.teacherId = parsedUser.id;
      this.getCourses(id);
    }
  }

  getCourses(id: number) {
    this.teacherService.getMyCourses(id).subscribe(x => {
      this.courses = x;
    });
  }

  openSyllabusDialog(course: Course) {
    this.selectedCourse = course;
    this.syllabus = course.syllabus;
    this.visible = true;
  }

  updateSyllabus() {
    if (this.selectedCourse) {
      const updatedCourse: Course = {
        ...this.selectedCourse,
        syllabus: this.syllabus
      };

      if (updatedCourse.id !== undefined) {
        this.teacherService.updateSyllabus(updatedCourse.id, updatedCourse).subscribe({
          next: () => {
            this.visible = false;
            this.syllabus = undefined;
            this.getCourses(this.teacherId);
            this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Syllabus updated successfully.' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error updating syllabus.' });
            this.syllabus = undefined;
          }
        });
      }
    }
  }

  cancelDialog() {
    this.visible = false;
    this.syllabus = undefined;
  }
}
