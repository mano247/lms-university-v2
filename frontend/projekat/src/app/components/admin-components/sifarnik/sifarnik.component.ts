import { NgClass, NgIf } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { FacultyService } from '../../../services/faculty.service';
import { Faculty } from '../../../model/academic/faculty';
import { StudyProgram } from '../../../model/academic/study-program';
import { Course } from '../../../model/academic/course';
import { StudyProgramService } from '../../../services/study-program.service';
import { CourseService } from '../../../services/course.service';
import { TableModule } from 'primeng/table';
import { InputGroupModule } from 'primeng/inputgroup';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { University } from '../../../model/academic/university';
import { UniversityService } from '../../../services/university.service';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { TeacherService } from '../../../services/teacher.service';
import { Teacher } from '../../../model/users/teacher';
import { CalendarModule } from 'primeng/calendar';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-sifarnik',
  standalone: true,
  imports: [DropdownModule, FormsModule, NgIf, TableModule, NgClass, InputGroupModule, ButtonModule, DialogModule,
    ToastModule, ConfirmPopupModule, CalendarModule],
  templateUrl: './sifarnik.component.html',
  styleUrl: './sifarnik.component.css',
  providers: [MessageService, ConfirmationService]
})
export class SifarnikComponent implements OnInit {
  faculties: Faculty[] = [];
  studyPrograms: StudyProgram[] = [];
  courses: Course[] = [];
  university: University | undefined;
  teachers: Teacher[] = [];

  filteredFaculties: Faculty[] = [];
  filteredStudyPrograms: StudyProgram[] = [];
  filteredCourses: Course[] = [];

  search = {
    name: '',
    code: ''
  };

  visible: boolean = false;
  addFacultyDialog: boolean = false;
  editFacultyDialog: boolean = false;
  addProgramDialog: boolean = false;
  editProgramDialog: boolean = false;
  addCourseDialog: boolean = false;
  editCourseDialog: boolean = false;

  dialogHeader: string = '';
  currentElement: any = null;
  newCode: string = '';

  newFaculty: any = {};
  newProgram: any = {};
  newCourse: any = {};

  categories: { label: string; value: string }[] = [
    { label: 'Faculties', value: 'faculties' },
    { label: 'Study Programs', value: 'studyPrograms' },
    { label: 'Courses', value: 'courses' }
  ];

  selectedCategory: string = 'faculties';

  constructor(
    private facultyService: FacultyService,
    private studyProgramService: StudyProgramService,
    private courseService: CourseService,
    private messageService: MessageService,
    private universityService: UniversityService,
    private confirmationService: ConfirmationService,
    private teacherService: TeacherService
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll() {
    this.getFaculties();
    this.getStudyPrograms();
    this.getCourses();
    this.getUniversity();
    this.getTeachers();
  }

  getUniversity() {
    this.universityService.getById(1).subscribe(x => {
      this.university = x;
    });
  }

  getFaculties() {
    this.facultyService.getAll().subscribe(x => {
      this.faculties = x;
      this.filteredFaculties = this.faculties;
    });
  }

  getStudyPrograms() {
    this.studyProgramService.getAll().subscribe(x => {
      this.studyPrograms = x;
      this.filteredStudyPrograms = this.studyPrograms;
    });
  }

  getCourses() {
    this.courseService.getAll().subscribe(x => {
      this.courses = x;
      this.filteredCourses = this.courses;
    });
  }

  getTeachers() {
    this.teacherService.getAll().subscribe(x => {
      this.teachers = x;
    });
  }

  searchFaculties() {
    this.filteredFaculties = this.faculties.filter(f =>
      (this.search.name ? f.name.toLowerCase().includes(this.search.name.toLowerCase()) : true) &&
      (this.search.code ? f.facultyCode.toLowerCase().includes(this.search.code.toLowerCase()) : true)
    );
  }

  searchPrograms() {
    this.filteredStudyPrograms = this.studyPrograms.filter(s =>
      (this.search.name ? s.name.toLowerCase().includes(this.search.name.toLowerCase()) : true) &&
      (this.search.code ? s.programCode.toLowerCase().includes(this.search.code.toLowerCase()) : true)
    );
  }

  searchCourses() {
    this.filteredCourses = this.courses.filter(c =>
      (this.search.name ? c.name.toLowerCase().includes(this.search.name.toLowerCase()) : true) &&
      (this.search.code ? c.courseCode.toLowerCase().includes(this.search.code.toLowerCase()) : true)
    );
  }

  clearSearch() {
    this.search = { name: '', code: '' };
    this.filteredFaculties = this.faculties;
    this.filteredStudyPrograms = this.studyPrograms;
    this.filteredCourses = this.courses;
  }

  openCodeDialogFaculty(faculty: Faculty) {
    this.visible = true;
    this.currentElement = faculty;
    this.dialogHeader = 'Edit Faculty Code';
    this.newCode = faculty.facultyCode;
  }

  openCodeDialogProgram(program: StudyProgram) {
    this.visible = true;
    this.currentElement = program;
    this.dialogHeader = 'Edit Study Program Code';
    this.newCode = program.programCode;
  }

  openCodeDialogCourse(course: Course) {
    this.visible = true;
    this.currentElement = course;
    this.dialogHeader = 'Edit Course Code';
    this.newCode = course.courseCode;
  }

  saveCode() {
    if (this.currentElement.facultyCode !== undefined) {
      const updatedFaculty = { ...this.currentElement, university: this.university, facultyCode: this.newCode };
      this.facultyService.update(updatedFaculty.id, updatedFaculty).subscribe(() => {
        this.loadAll();
        this.currentElement = null;
        this.cancelDialog();
      });
    } else if (this.currentElement.programCode !== undefined) {
      const updatedProgram = { ...this.currentElement, programCode: this.newCode };
      this.studyProgramService.update(updatedProgram.id, updatedProgram).subscribe(() => {
        this.loadAll();
        this.currentElement = null;
        this.cancelDialog();
      });
    } else if (this.currentElement.courseCode !== undefined) {
      const updatedCourse = { ...this.currentElement, courseCode: this.newCode, studyProgram: { id: this.currentElement.studyProgram.id } };
      this.courseService.update(updatedCourse.id, updatedCourse).subscribe(() => {
        this.loadAll();
        this.currentElement = null;
        this.cancelDialog();
      });
    }
  }

  cancelDialog() {
    this.visible = false;
  }

  openAddFacultyDialog() {
    this.addFacultyDialog = true;
  }

  addFaculty() {
    const newFacultyData = { ...this.newFaculty, university: this.university };
    this.facultyService.create(newFacultyData).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Added', detail: 'Faculty added successfully.' });
        this.newFaculty = {};
        this.getFaculties();
        this.closeFacultyDialog();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while adding faculty.' });
      }
    });
  }

  closeFacultyDialog() {
    this.addFacultyDialog = false;
    this.editFacultyDialog = false;
    this.newFaculty = {};
  }

  openEditFacultyDialog(faculty: Faculty) {
    this.newFaculty = { ...faculty };
    this.editFacultyDialog = true;
  }

  updateFaculty() {
    const updatedFaculty = { ...this.newFaculty, university: this.university };
    this.facultyService.update(updatedFaculty.id, updatedFaculty).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Faculty updated successfully.' });
        this.newFaculty = {};
        this.getFaculties();
        this.closeFacultyDialog();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while updating faculty.' });
      }
    });
    this.closeFacultyDialog();
  }

  removeFaculty(id: number, event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure you want to remove this faculty?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.facultyService.delete(id).subscribe({
          next: () => {
            this.getFaculties();
            this.messageService.add({ severity: 'info', summary: 'Removed', detail: 'Faculty removed successfully.' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while removing.' });
          }
        });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled', detail: 'Removal cancelled.', life: 3000 });
      }
    });
  }

  openAddCourseDialog() {
    this.addCourseDialog = true;
  }

  addCourse() {
    this.newCourse = { ...this.newCourse, studyProgram: { id: this.newCourse.studyProgram.id } };
    this.courseService.create(this.newCourse).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Added', detail: 'Course added successfully.' });
        this.newCourse = {};
        this.getCourses();
        this.closeCourseDialog();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while adding course.' });
      }
    });
  }

  closeCourseDialog() {
    this.addCourseDialog = false;
    this.editCourseDialog = false;
    this.newCourse = {};
  }

  openEditCourseDialog(course: Course) {
    this.newCourse = { ...course };
    this.editCourseDialog = true;
  }

  updateCourse() {
    const courseData = { ...this.newCourse, studyProgram: { id: this.newCourse.studyProgram.id } };
    this.courseService.update(this.newCourse.id, courseData).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Course updated successfully.' });
        this.newCourse = {};
        this.getCourses();
        this.closeCourseDialog();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while updating course.' });
      }
    });
  }

  removeCourse(id: number, event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure you want to remove this course?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.courseService.delete(id).subscribe({
          next: () => {
            this.getCourses();
            this.messageService.add({ severity: 'info', summary: 'Removed', detail: 'Course removed successfully.' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while removing.' });
          }
        });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled', detail: 'Removal cancelled.', life: 3000 });
      }
    });
  }

  openAddProgramDialog() {
    this.addProgramDialog = true;
  }

  addProgram() {
    this.studyProgramService.create(this.newProgram).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Added', detail: 'Study program added successfully.' });
        this.newProgram = {};
        this.getStudyPrograms();
        this.closeProgramDialog();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while adding study program.' });
      }
    });
  }

  closeProgramDialog() {
    this.addProgramDialog = false;
    this.editProgramDialog = false;
    this.newProgram = {};
  }

  openEditProgramDialog(program: StudyProgram) {
    this.newProgram = { ...program };
    this.editProgramDialog = true;
  }

  updateProgram() {
    this.studyProgramService.update(this.newProgram.id, this.newProgram).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Study program updated successfully.' });
        this.newProgram = {};
        this.getStudyPrograms();
        this.closeProgramDialog();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while updating study program.' });
      }
    });
  }

  removeProgram(id: number, event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure you want to remove this study program?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.studyProgramService.delete(id).subscribe({
          next: () => {
            this.getStudyPrograms();
            this.messageService.add({ severity: 'info', summary: 'Removed', detail: 'Study program removed successfully.' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while removing.' });
          }
        });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled', detail: 'Removal cancelled.', life: 3000 });
      }
    });
  }
}
