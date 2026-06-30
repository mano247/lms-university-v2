import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { StudyProgramService } from '../../../services/study-program.service';
import { FacultyService } from '../../../services/faculty.service';
import { TeacherService } from '../../../services/teacher.service';

@Component({
  selector: 'app-a-study-programs',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './a-study-programs.component.html',
  styleUrl: './a-study-programs.component.css',
})
export class AStudyProgramsComponent implements OnInit {
  programs: any[] = [];
  filtered: any[] = [];
  faculties: any[] = [];
  teachers: any[] = [];

  pageSize = 15;
  currentPage = 0;

  searchName = '';
  searchFaculty = '';

  showModal = false;
  isEdit = false;
  form: any = {};
  deleteId: number | null = null;

  toast: { msg: string; type: 'success' | 'error' } | null = null;

  constructor(
    private studyProgramService: StudyProgramService,
    private facultyService: FacultyService,
    private teacherService: TeacherService,
  ) {}

  ngOnInit(): void {
    this.loadPrograms();
    this.facultyService.getAll().subscribe(x => (this.faculties = x));
    this.teacherService.getAll().subscribe(x => (this.teachers = x));
  }

  loadPrograms() {
    this.studyProgramService.getAll().subscribe(x => {
      this.programs = x;
      this.applyFilter();
    });
  }

  applyFilter() {
    const nm = this.searchName.toLowerCase();
    const fc = this.searchFaculty.toLowerCase();
    this.filtered = this.programs.filter(p =>
      (!nm || (p.name ?? '').toLowerCase().includes(nm)) &&
      (!fc || (p.faculty?.name ?? p.faculty ?? '').toLowerCase().includes(fc))
    );
    this.currentPage = 0;
  }

  get totalPages() { return Math.ceil(this.filtered.length / this.pageSize); }
  get pagedItems() { return this.filtered.slice(this.currentPage * this.pageSize, (this.currentPage + 1) * this.pageSize); }
  goToPage(p: number) { if (p >= 0 && p < this.totalPages) this.currentPage = p; }

  clearSearch() {
    this.searchName = '';
    this.searchFaculty = '';
    this.applyFilter();
  }

  openAdd() {
    this.form = {};
    this.isEdit = false;
    this.showModal = true;
  }

  openEdit(program: any) {
    this.form = { ...program };
    this.isEdit = true;
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.form = {};
  }

  save() {
    const op = this.isEdit
      ? this.studyProgramService.update(this.form.id, this.form)
      : this.studyProgramService.create(this.form);
    op.subscribe({
      next: () => { this.loadPrograms(); this.closeModal(); this.showToast('Study program saved.', 'success'); },
      error: () => this.showToast('Error saving study program.', 'error'),
    });
  }

  confirmDelete(id: number) { this.deleteId = id; }
  cancelDelete() { this.deleteId = null; }

  doDelete() {
    if (this.deleteId === null) return;
    const id = this.deleteId;
    this.deleteId = null;
    this.studyProgramService.delete(id).subscribe({
      next: () => { this.loadPrograms(); this.showToast('Deleted.', 'success'); },
      error: () => this.showToast('Error deleting.', 'error'),
    });
  }

  showToast(msg: string, type: 'success' | 'error') {
    this.toast = { msg, type };
    setTimeout(() => (this.toast = null), 4000);
  }

  teacherLabel(t: any) { return `${t.firstName ?? ''} ${t.lastName ?? ''}`.trim(); }

  get programCount() { return this.programs.length; }
}

