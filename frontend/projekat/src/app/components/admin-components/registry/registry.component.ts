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
  selector: 'app-registry',
  standalone: true,
  imports: [DropdownModule, FormsModule, NgIf, TableModule, NgClass, InputGroupModule, ButtonModule, DialogModule,
    ToastModule, ConfirmPopupModule, CalendarModule],
  templateUrl: './registry.component.html',
  styleUrl: './registry.component.css',
  providers: [MessageService, ConfirmationService]
})
export class RegistryComponent implements OnInit {
  faculties: Faculty[] = [];
  studyPrograms: StudyProgram[] = [];
  courses: Course[] = [];
  university: University | undefined;
  teachers: Teacher[] = [];

  filteredFaculties: Faculty[] = [];
  filteredStudyPrograms: StudyProgram[] = [];
  filteredCourses: Course[] = [];

  filter = { name: '', code: '' };

  visible: boolean = false;
  addFacultyVisible: boolean = false;
  editFacultyVisible: boolean = false;
  addStudyProgramVisible: boolean = false;
  editStudyProgramVisible: boolean = false;
  addCourseVisible: boolean = false;
  editCourseVisible: boolean = false;

  dialogHeader: string = '';
  currentElement: any = null;
  newCode: string = '';
  newFaculty: any = {};
  newStudyProgram: any = {};
  newCourse: any = {};

  registryOptions: { label: string, value: string }[] = [
    { label: 'Faculties', value: 'faculties' },
    { label: 'Study Programs', value: 'study-programs' },
    { label: 'Courses', value: 'courses' }
  ];
  selected: string = 'faculties';

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
    this.loadData();
  }

  loadData() {
    this.loadFaculties();
    this.loadStudyPrograms();
    this.loadCourses();
    this.loadUniversity();
    this.loadTeachers();
  }

  loadUniversity() {
    this.universityService.getById(1).subscribe(x => {
      this.university = x;
    });
  }

  loadFaculties() {
    this.facultyService.getAll().subscribe(x => {
      this.faculties = x;
      this.filteredFaculties = this.faculties;
    });
  }

  loadStudyPrograms() {
    this.studyProgramService.getAll().subscribe(x => {
      this.studyPrograms = x;
      this.filteredStudyPrograms = this.studyPrograms;
    });
  }

  loadCourses() {
    this.courseService.getAll().subscribe(x => {
      this.courses = x;
      this.filteredCourses = this.courses;
    });
  }

  loadTeachers() {
    this.teacherService.getAll().subscribe(x => {
      this.teachers = x;
    });
  }

  filterFaculties() {
    this.filteredFaculties = this.faculties.filter(f =>
      (this.filter.name ? f.name.toLowerCase().includes(this.filter.name.toLowerCase()) : true) &&
      (this.filter.code ? f.facultyCode.toLowerCase().includes(this.filter.code.toLowerCase()) : true)
    );
  }

  filterStudyPrograms() {
    this.filteredStudyPrograms = this.studyPrograms.filter(s =>
      (this.filter.name ? s.name.toLowerCase().includes(this.filter.name.toLowerCase()) : true) &&
      (this.filter.code ? s.programCode.toLowerCase().includes(this.filter.code.toLowerCase()) : true)
    );
  }

  filterCourses() {
    this.filteredCourses = this.courses.filter(c =>
      (this.filter.name ? c.name.toLowerCase().includes(this.filter.name.toLowerCase()) : true) &&
      (this.filter.code ? c.courseCode.toLowerCase().includes(this.filter.code.toLowerCase()) : true)
    );
  }

  clearFilter() {
    this.filter = { name: '', code: '' };
    this.filteredFaculties = this.faculties;
    this.filteredStudyPrograms = this.studyPrograms;
    this.filteredCourses = this.courses;
  }

  editFacultyCode(faculty: Faculty) {
    this.visible = true;
    this.currentElement = faculty;
    this.dialogHeader = 'Edit faculty code';
    this.newCode = faculty.facultyCode;
  }

  editStudyProgramCode(sp: StudyProgram) {
    this.visible = true;
    this.currentElement = sp;
    this.dialogHeader = 'Edit study program code';
    this.newCode = sp.programCode;
  }

  editCourseCode(course: Course) {
    this.visible = true;
    this.currentElement = course;
    this.dialogHeader = 'Edit course code';
    this.newCode = course.courseCode;
  }

  saveCode() {
    if (this.currentElement?.facultyCode !== undefined) {
      const updated = { ...this.currentElement, university: { id: this.university?.id }, facultyCode: this.newCode };
      this.facultyService.update(updated.id, updated).subscribe(() => {
        this.loadData();
        this.currentElement = null;
        this.closeDialog();
      });
    } else if (this.currentElement?.programCode !== undefined) {
      const updated = { ...this.currentElement, programCode: this.newCode };
      this.studyProgramService.update(updated.id, updated).subscribe(() => {
        this.loadData();
        this.currentElement = null;
        this.closeDialog();
      });
    } else if (this.currentElement?.courseCode !== undefined) {
      const updated = { ...this.currentElement, courseCode: this.newCode, studyProgram: { id: this.currentElement.studyProgram?.id } };
      this.courseService.update(updated.id, updated).subscribe(() => {
        this.loadData();
        this.currentElement = null;
        this.closeDialog();
      });
    }
  }

  closeDialog() {
    this.visible = false;
  }

  openAddFacultyDialog() {
    this.addFacultyVisible = true;
  }

  createFaculty() {
    const faculty = { ...this.newFaculty, university: { id: this.university?.id } };
    this.facultyService.create(faculty).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Faculty added', detail: 'Faculty was successfully added.' });
        this.newFaculty = {};
        this.loadFaculties();
        this.closeDialogFaculty();
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to add faculty.' });
        console.error(err);
      }
    });
  }

  closeDialogFaculty() {
    this.addFacultyVisible = false;
    this.editFacultyVisible = false;
    this.newFaculty = {};
  }

  openEditFaculty(faculty: Faculty) {
    this.newFaculty = { ...faculty };
    this.editFacultyVisible = true;
  }

  updateFaculty() {
    const updated = { ...this.newFaculty, university: { id: this.university?.id } };
    this.facultyService.update(updated.id, updated).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Faculty updated', detail: 'Faculty was successfully updated.' });
        this.newFaculty = {};
        this.loadFaculties();
        this.closeDialogFaculty();
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to update faculty.' });
        console.error(err);
      }
    });
  }

  deleteFaculty(id: number, event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure you want to delete this faculty?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.facultyService.delete(id).subscribe({
          next: () => {
            this.loadFaculties();
            this.messageService.add({ severity: 'info', summary: 'Deleted', detail: 'Faculty removed' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete faculty.' });
          }
        });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled', detail: 'Delete cancelled', life: 3000 });
      }
    });
  }

  openAddCourseDialog() {
    this.addCourseVisible = true;
  }

  createCourse() {
    const course = { ...this.newCourse, studyProgram: { id: this.newCourse.studyProgram?.id } };
    this.courseService.create(course).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Course added', detail: 'Course was successfully added.' });
        this.newCourse = {};
        this.loadCourses();
        this.closeDialogCourse();
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to add course.' });
        console.error(err);
      }
    });
  }

  closeDialogCourse() {
    this.addCourseVisible = false;
    this.editCourseVisible = false;
    this.newCourse = {};
  }

  openEditCourse(course: Course) {
    this.newCourse = { ...course };
    this.editCourseVisible = true;
  }

  updateCourse() {
    const updated = { ...this.newCourse, studyProgram: { id: this.newCourse.studyProgram?.id } };
    this.courseService.update(this.newCourse.id, updated).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Course updated', detail: 'Course was successfully updated.' });
        this.newCourse = {};
        this.loadCourses();
        this.closeDialogCourse();
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to update course.' });
        console.error(err);
      }
    });
  }

  deleteCourse(id: number, event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure you want to delete this course?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.courseService.delete(id).subscribe({
          next: () => {
            this.loadCourses();
            this.messageService.add({ severity: 'info', summary: 'Deleted', detail: 'Course removed' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete course.' });
          }
        });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled', detail: 'Delete cancelled', life: 3000 });
      }
    });
  }

  openAddStudyProgramDialog() {
    this.addStudyProgramVisible = true;
  }

  createStudyProgram() {
    this.studyProgramService.create(this.newStudyProgram).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Study program added', detail: 'Study program was successfully added.' });
        this.newStudyProgram = {};
        this.loadStudyPrograms();
        this.closeDialogStudyProgram();
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to add study program.' });
        console.error(err);
      }
    });
  }

  closeDialogStudyProgram() {
    this.addStudyProgramVisible = false;
    this.editStudyProgramVisible = false;
    this.newStudyProgram = {};
  }

  openEditStudyProgram(sp: StudyProgram) {
    this.newStudyProgram = { ...sp };
    this.editStudyProgramVisible = true;
  }

  updateStudyProgram() {
    this.studyProgramService.update(this.newStudyProgram.id, this.newStudyProgram).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Study program updated', detail: 'Study program was successfully updated.' });
        this.newStudyProgram = {};
        this.loadStudyPrograms();
        this.closeDialogStudyProgram();
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to update study program.' });
        console.error(err);
      }
    });
  }

  deleteStudyProgram(id: number, event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure you want to delete this study program?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.studyProgramService.delete(id).subscribe({
          next: () => {
            this.loadStudyPrograms();
            this.messageService.add({ severity: 'info', summary: 'Deleted', detail: 'Study program removed' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete study program.' });
          }
        });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled', detail: 'Delete cancelled', life: 3000 });
      }
    });
  }
}
