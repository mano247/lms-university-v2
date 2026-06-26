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
  courseSyllabus: string | undefined;
  teacherId: any;
  selectedCourse: any = null;

  constructor(
    private teacherService: TeacherService,
    private courseService: CourseService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      const id = parsedUser.id;
      this.teacherId = id;
      this.loadCourses(id);
    }
  }

  loadCourses(id: number) {
    this.teacherService.mojicoursei(id).subscribe(x => {
      this.courses = x;
    });
  }

  openSyllabusDialog(course: Course) {
    this.selectedCourse = course;
    this.courseSyllabus = course.syllabus;
    this.visible = true;
  }

  updateSyllabus() {
    if (this.selectedCourse) {
      const updated: Course = {
        ...this.selectedCourse,
        syllabus: this.courseSyllabus
      };
      if (updated.id !== undefined) {
        this.teacherService.izmenaSilabusa(updated.id, updated).subscribe({
          next: () => {
            this.visible = false;
            this.courseSyllabus = undefined;
            this.loadCourses(this.teacherId);
            this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Syllabus successfully updated.' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error updating syllabus.' });
            this.courseSyllabus = undefined;
          }
        });
      }
    }
  }

  closeDialog() {
    this.visible = false;
    this.courseSyllabus = undefined;
  }
}
