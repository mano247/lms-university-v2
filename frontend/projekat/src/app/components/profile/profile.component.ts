import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { RegisteredUserService } from '../../services/registered-user.service';
import { StudentService } from '../../services/student.service';
import { TeacherService } from '../../services/teacher.service';
import { StudentOfficeService } from '../../services/student-affairs.service';
import { AdministratorService } from '../../services/administrator.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit {
  profile: any = null;
  userId: number | null = null;
  permissions: string[] = [];

  editMode = false;
  changingPassword = false;
  editForm: any = {};
  passwordForm = { currentPassword: '', newPassword: '', confirmPassword: '' };

  toast: { msg: string; type: 'success' | 'error' } | null = null;

  constructor(
    private authService: AuthService,
    private registeredUserService: RegisteredUserService,
    private studentService: StudentService,
    private teacherService: TeacherService,
    private officeService: StudentOfficeService,
    private adminService: AdministratorService,
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.userId = user.id;
      this.permissions = user.permissions;
      this.loadProfile(user.id);
    }
  }

  loadProfile(id: number): void {
    this.registeredUserService.getById(id).subscribe({
      next: (data) => (this.profile = data),
      error: () => this.showToast('Failed to load profile.', 'error'),
    });
  }

  openEdit(): void {
    this.editForm = { ...this.profile };
    this.editMode = true;
  }

  cancelEdit(): void {
    this.editMode = false;
    this.editForm = {};
  }

  saveProfile(): void {
    if (!this.userId) return;
    const payload = { ...this.profile, ...this.editForm };
    this.getUpdateOp(this.userId, payload).subscribe({
      next: () => {
        this.editMode = false;
        this.loadProfile(this.userId!);
        this.showToast('Profile updated successfully.', 'success');
      },
      error: () => this.showToast('Failed to update profile.', 'error'),
    });
  }

  private getUpdateOp(id: number, data: any) {
    if (this.permissions.includes('ADMINISTRATOR_PERMISSION')) return this.adminService.update(id, data);
    if (this.permissions.includes('STUDENT_AFFAIRS_PERMISSION')) return this.officeService.update(id, data);
    if (this.permissions.includes('TEACHER_PERMISSION')) return this.teacherService.update(id, data);
    if (this.permissions.includes('STUDENT_PERMISSION')) return this.studentService.update(id, data);
    return this.registeredUserService.update(id, data);
  }

  savePassword(): void {
    if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
      this.showToast('Passwords do not match.', 'error');
      return;
    }
    if (!this.passwordForm.newPassword.trim()) {
      this.showToast('Please enter a new password.', 'error');
      return;
    }
    if (!this.userId) return;
    const payload = { ...this.profile, password: this.passwordForm.newPassword };
    this.getUpdateOp(this.userId, payload).subscribe({
      next: () => {
        this.changingPassword = false;
        this.passwordForm = { currentPassword: '', newPassword: '', confirmPassword: '' };
        this.showToast('Password changed successfully.', 'success');
      },
      error: () => this.showToast('Failed to change password.', 'error'),
    });
  }

  logout(): void {
    this.authService.logout();
  }

  showToast(msg: string, type: 'success' | 'error'): void {
    this.toast = { msg, type };
    setTimeout(() => (this.toast = null), 4000);
  }

  get roleLabel(): string {
    if (this.permissions.includes('ADMINISTRATOR_PERMISSION')) return 'Administrator';
    if (this.permissions.includes('STUDENT_AFFAIRS_PERMISSION')) return 'Student Affairs';
    if (this.permissions.includes('TEACHER_PERMISSION')) return 'Teacher';
    if (this.permissions.includes('STUDENT_PERMISSION')) return 'Student';
    return 'Registered User';
  }

  get initials(): string {
    const fn = this.profile?.firstName ?? '';
    const ln = this.profile?.lastName ?? '';
    return ((fn[0] ?? '') + (ln[0] ?? '')).toUpperCase() || (this.profile?.username?.[0] ?? 'U').toUpperCase();
  }
}
