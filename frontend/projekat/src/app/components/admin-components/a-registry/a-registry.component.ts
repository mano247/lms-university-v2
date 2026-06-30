import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FacultyService } from '../../../services/faculty.service';
import { StudyProgramService } from '../../../services/study-program.service';
import { CourseService } from '../../../services/course.service';
import { UniversityService } from '../../../services/university.service';
import { TeacherService } from '../../../services/teacher.service';

@Component({
  selector: 'app-a-registry',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './a-registry.component.html',
  styleUrl: './a-registry.component.css',
})
export class ARegistryComponent implements OnInit {
  faculties: any[] = [];
  studyPrograms: any[] = [];
  courses: any[] = [];
  university: any = null;
  teachers: any[] = [];
  filtered: any[] = [];

  selectedCategory = 'faculties';
  searchName = '';
  searchCode = '';

  showModal = false;
  isEdit = false;
  form: any = {};
  deleteId: number | null = null;

  toast: { msg: string; type: 'success' | 'error' } | null = null;

  readonly categories = [
    { value: 'faculties',     label: 'Faculties' },
    { value: 'studyPrograms', label: 'Study Programs' },
    { value: 'courses',       label: 'Courses' },
  ];

  constructor(
    private facultyService: FacultyService,
    private studyProgramService: StudyProgramService,
    private courseService: CourseService,
    private universityService: UniversityService,
    private teacherService: TeacherService,
  ) {}

  ngOnInit(): void {
    this.universityService.getById(1).subscribe(x => (this.university = x));
    this.teacherService.getAll().subscribe(x => (this.teachers = x));
    this.facultyService.getAll().subscribe(x => { this.faculties = x; this.applyFilter(); });
    this.studyProgramService.getAll().subscribe(x => { this.studyPrograms = x; this.applyFilter(); });
    this.courseService.getAll().subscribe(x => { this.courses = x; this.applyFilter(); });
  }

  onCategoryChange() {
    this.searchName = '';
    this.searchCode = '';
    this.deleteId = null;
    this.applyFilter();
  }

  private loadCurrent() {
    if (this.selectedCategory === 'faculties') {
      this.facultyService.getAll().subscribe(x => { this.faculties = x; this.applyFilter(); });
    } else if (this.selectedCategory === 'studyPrograms') {
      this.studyProgramService.getAll().subscribe(x => { this.studyPrograms = x; this.applyFilter(); });
    } else {
      this.courseService.getAll().subscribe(x => { this.courses = x; this.applyFilter(); });
    }
  }

  applyFilter() {
    const nm = this.searchName.toLowerCase();
    const cd = this.searchCode.toLowerCase();
    const src = this.selectedCategory === 'faculties' ? this.faculties
              : this.selectedCategory === 'studyPrograms' ? this.studyPrograms
              : this.courses;
    const codeKey = this.selectedCategory === 'faculties' ? 'facultyCode'
                  : this.selectedCategory === 'studyPrograms' ? 'programCode'
                  : 'courseCode';
    this.filtered = src.filter(item =>
      (!nm || (item.name ?? '').toLowerCase().includes(nm)) &&
      (!cd || (item[codeKey] ?? '').toLowerCase().includes(cd))
    );
  }

  clearSearch() {
    this.searchName = '';
    this.searchCode = '';
    this.applyFilter();
  }

  openAdd() {
    this.form = {};
    this.isEdit = false;
    this.showModal = true;
  }

  openEdit(item: any) {
    this.form = { ...item };
    this.isEdit = true;
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.form = {};
  }

  save() {
    if (this.selectedCategory === 'faculties') {
      const payload = { ...this.form, university: this.university };
      const op = this.isEdit
        ? this.facultyService.update(this.form.id, payload)
        : this.facultyService.create(payload);
      op.subscribe({
        next: () => { this.loadCurrent(); this.closeModal(); this.showToast('Faculty saved successfully.', 'success'); },
        error: () => this.showToast('Error saving faculty.', 'error'),
      });
    } else if (this.selectedCategory === 'studyPrograms') {
      const op = this.isEdit
        ? this.studyProgramService.update(this.form.id, this.form)
        : this.studyProgramService.create(this.form);
      op.subscribe({
        next: () => { this.loadCurrent(); this.closeModal(); this.showToast('Study program saved.', 'success'); },
        error: () => this.showToast('Error saving study program.', 'error'),
      });
    } else {
      const payload = { ...this.form, studyProgram: { id: this.form.studyProgram?.id } };
      const op = this.isEdit
        ? this.courseService.update(this.form.id, payload)
        : this.courseService.create(payload);
      op.subscribe({
        next: () => { this.loadCurrent(); this.closeModal(); this.showToast('Course saved.', 'success'); },
        error: () => this.showToast('Error saving course.', 'error'),
      });
    }
  }

  confirmDelete(id: number) { this.deleteId = id; }
  cancelDelete() { this.deleteId = null; }

  doDelete() {
    if (this.deleteId === null) return;
    const id = this.deleteId;
    this.deleteId = null;
    const svc: any = this.selectedCategory === 'faculties' ? this.facultyService
                   : this.selectedCategory === 'studyPrograms' ? this.studyProgramService
                   : this.courseService;
    svc.delete(id).subscribe({
      next: () => { this.loadCurrent(); this.showToast('Deleted successfully.', 'success'); },
      error: () => this.showToast('Error deleting.', 'error'),
    });
  }

  showToast(msg: string, type: 'success' | 'error') {
    this.toast = { msg, type };
    setTimeout(() => (this.toast = null), 4000);
  }

  get statFaculties() { return this.faculties.length; }
  get statPrograms() { return this.studyPrograms.length; }
  get statCourses() { return this.courses.length; }
  get statTeachers() { return this.teachers.length; }

  teacherLabel(t: any) { return `${t.firstName ?? ''} ${t.lastName ?? ''}`.trim(); }
}

