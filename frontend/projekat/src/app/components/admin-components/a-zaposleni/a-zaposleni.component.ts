import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TeacherService } from '../../../services/teacher.service';
import { StudentOfficeService } from '../../../services/student-affairs.service';
import { AdministratorService } from '../../../services/administrator.service';

@Component({
  selector: 'app-a-zaposleni',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './a-zaposleni.component.html',
  styleUrl: './a-zaposleni.component.css',
})
export class AZaposleniComponent implements OnInit {
  teachers: any[] = [];
  officeStaff: any[] = [];
  administrators: any[] = [];

  filtered: any[] = [];
  selectedCategory = 'teachers';
  searchFirst = '';
  searchLast = '';
  searchEmail = '';

  pageSize = 15;
  currentPage = 0;

  showModal = false;
  editItem: any = {};
  deleteId: number | null = null;

  toast: { msg: string; type: 'success' | 'error' } | null = null;

  readonly categories = [
    { value: 'teachers',       label: 'Teachers' },
    { value: 'officeStaff',    label: 'Student Office' },
    { value: 'administrators', label: 'Administrators' },
  ];

  constructor(
    private teacherService: TeacherService,
    private officeService: StudentOfficeService,
    private adminService: AdministratorService,
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll() {
    this.teacherService.getAll().subscribe(x => { this.teachers = x; this.applyFilter(); });
    this.officeService.getAll().subscribe(x => { this.officeStaff = x; this.applyFilter(); });
    this.adminService.getAll().subscribe(x => { this.administrators = x; this.applyFilter(); });
  }

  onCategoryChange() {
    this.searchFirst = '';
    this.searchLast = '';
    this.searchEmail = '';
    this.deleteId = null;
    this.currentPage = 0;
    this.applyFilter();
  }

  applyFilter() {
    const fn = this.searchFirst.toLowerCase();
    const ln = this.searchLast.toLowerCase();
    const em = this.searchEmail.toLowerCase();
    const src = this.selectedCategory === 'teachers' ? this.teachers
              : this.selectedCategory === 'officeStaff' ? this.officeStaff
              : this.administrators;
    this.filtered = src.filter(e =>
      (!fn || (e.firstName ?? '').toLowerCase().includes(fn)) &&
      (!ln || (e.lastName ?? '').toLowerCase().includes(ln)) &&
      (!em || (e.email ?? '').toLowerCase().includes(em))
    );
    this.currentPage = 0;
  }

  get totalPages() { return Math.ceil(this.filtered.length / this.pageSize); }
  get pagedItems() { return this.filtered.slice(this.currentPage * this.pageSize, (this.currentPage + 1) * this.pageSize); }
  goToPage(p: number) { if (p >= 0 && p < this.totalPages) this.currentPage = p; }

  clearSearch() {
    this.searchFirst = '';
    this.searchLast = '';
    this.searchEmail = '';
    this.applyFilter();
  }

  openEdit(item: any) {
    this.editItem = { ...item };
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.editItem = {};
  }

  save() {
    let op: any;
    if (this.selectedCategory === 'teachers') {
      op = this.teacherService.update(this.editItem.id, this.editItem);
    } else if (this.selectedCategory === 'officeStaff') {
      op = this.officeService.update(this.editItem.id, this.editItem);
    } else {
      op = this.adminService.update(this.editItem.id, this.editItem);
    }
    op.subscribe({
      next: () => { this.loadAll(); this.closeModal(); this.showToast('Employee updated.', 'success'); },
      error: () => this.showToast('Error saving changes.', 'error'),
    });
  }

  confirmDelete(id: number) { this.deleteId = id; }
  cancelDelete() { this.deleteId = null; }

  doDelete() {
    if (this.deleteId === null) return;
    const id = this.deleteId;
    this.deleteId = null;
    let op: any;
    if (this.selectedCategory === 'teachers') {
      op = this.teacherService.delete(id);
    } else if (this.selectedCategory === 'officeStaff') {
      op = this.officeService.delete(id);
    } else {
      op = this.adminService.delete(id);
    }
    op.subscribe({
      next: () => { this.loadAll(); this.showToast('Deleted successfully.', 'success'); },
      error: () => this.showToast('Error deleting.', 'error'),
    });
  }

  showToast(msg: string, type: 'success' | 'error') {
    this.toast = { msg, type };
    setTimeout(() => (this.toast = null), 4000);
  }

  get totalCount() {
    const src = this.selectedCategory === 'teachers' ? this.teachers
              : this.selectedCategory === 'officeStaff' ? this.officeStaff
              : this.administrators;
    return src.length;
  }

  get categoryLabel() {
    return this.categories.find(c => c.value === this.selectedCategory)?.label ?? '';
  }

  initials(e: any) {
    return ((e.firstName?.[0] ?? '') + (e.lastName?.[0] ?? '')).toUpperCase();
  }
}
