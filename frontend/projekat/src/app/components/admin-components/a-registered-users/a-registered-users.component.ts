import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RegisteredUserService } from '../../../services/registered-user.service';
import { AdministratorService } from '../../../services/administrator.service';

@Component({
  selector: 'app-a-registered-users',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './a-registered-users.component.html',
  styleUrl: './a-registered-users.component.css',
})
export class ARegisteredUsersComponent implements OnInit {
  users: any[] = [];
  filtered: any[] = [];

  pageSize = 15;
  currentPage = 0;

  searchUsername = '';
  searchEmail = '';

  showRoleModal = false;
  userForRoleChange: any = null;
  selectedRole = '';

  deleteId: number | null = null;
  toast: { msg: string; type: 'success' | 'error' } | null = null;

  readonly roles = [
    { label: 'Student',          value: 'STUDENT_PERMISSION' },
    { label: 'Teacher',          value: 'TEACHER_PERMISSION' },
    { label: 'Student Affairs',  value: 'STUDENT_AFFAIRS_PERMISSION' },
    { label: 'Administrator',    value: 'ADMINISTRATOR_PERMISSION' },
  ];

  constructor(
    private registeredUserService: RegisteredUserService,
    private adminService: AdministratorService,
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.registeredUserService.getAll().subscribe(x => {
      this.users = x;
      this.applyFilter();
    });
  }

  applyFilter() {
    const un = this.searchUsername.toLowerCase();
    const em = this.searchEmail.toLowerCase();
    this.filtered = this.users.filter(u =>
      (!un || (u.username ?? u.email ?? '').toLowerCase().includes(un)) &&
      (!em || (u.email ?? '').toLowerCase().includes(em))
    );
    this.currentPage = 0;
  }

  get totalPages() { return Math.ceil(this.filtered.length / this.pageSize); }
  get pagedItems() { return this.filtered.slice(this.currentPage * this.pageSize, (this.currentPage + 1) * this.pageSize); }
  goToPage(p: number) { if (p >= 0 && p < this.totalPages) this.currentPage = p; }

  clearSearch() {
    this.searchUsername = '';
    this.searchEmail = '';
    this.applyFilter();
  }

  openRoleModal(user: any) {
    this.userForRoleChange = user;
    this.selectedRole = user.userType ?? user.permission ?? '';
    this.showRoleModal = true;
  }

  closeRoleModal() {
    this.showRoleModal = false;
    this.userForRoleChange = null;
    this.selectedRole = '';
  }

  saveRole() {
    if (!this.selectedRole || !this.userForRoleChange) return;
    this.adminService.assignStatus(this.selectedRole, this.userForRoleChange).subscribe({
      next: () => {
        this.loadUsers();
        this.closeRoleModal();
        this.showToast('Role updated successfully.', 'success');
      },
      error: () => this.showToast('Error updating role.', 'error'),
    });
  }

  confirmDelete(id: number) { this.deleteId = id; }
  cancelDelete() { this.deleteId = null; }

  doDelete() {
    if (this.deleteId === null) return;
    const id = this.deleteId;
    this.deleteId = null;
    this.registeredUserService.delete(id).subscribe({
      next: () => { this.loadUsers(); this.showToast('User deleted.', 'success'); },
      error: () => this.showToast('Error deleting user.', 'error'),
    });
  }

  showToast(msg: string, type: 'success' | 'error') {
    this.toast = { msg, type };
    setTimeout(() => (this.toast = null), 4000);
  }

  roleBadgeClass(user: any): string {
    const p = user.userType ?? user.permission ?? '';
    if (p.includes('STUDENT_AFFAIRS')) return 'role-office';
    if (p.includes('TEACHER')) return 'role-teacher';
    if (p.includes('ADMINISTRATOR')) return 'role-admin';
    if (p.includes('STUDENT')) return 'role-student';
    return 'role-default';
  }

  roleLabel(user: any): string {
    const p = user.userType ?? user.permission ?? '';
    const found = this.roles.find(r => r.value === p);
    return found ? found.label : p || 'Unknown';
  }

  get userCount() { return this.users.length; }
}

