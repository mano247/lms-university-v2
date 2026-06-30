import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth/auth.service';
import { ARegistryComponent } from '../../admin-components/a-registry/a-registry.component';
import { ARegisteredUsersComponent } from '../../admin-components/a-registered-users/a-registered-users.component';
import { AStudyProgramsComponent } from '../../admin-components/a-study-programs/a-study-programs.component';
import { AOrganizationComponent } from '../../admin-components/a-organization/a-organization.component';
import { AEmployeesComponent } from '../../admin-components/a-employees/a-employees.component';
import { SaExamPeriodsComponent } from '../../student-affairs-components/sa-exam-periods/sa-exam-periods.component';
import { SchedulesComponent } from '../../student-affairs-components/schedules/schedules.component';

@Component({
  selector: 'app-e-admin',
  standalone: true,
  imports: [
    ARegistryComponent,
    ARegisteredUsersComponent,
    AStudyProgramsComponent,
    AOrganizationComponent,
    AEmployeesComponent,
    SaExamPeriodsComponent,
    SchedulesComponent,
  ],
  templateUrl: './e-admin.component.html',
  styleUrl: './e-admin.component.css',
})
export class EAdminComponent implements OnInit {
  activeTab = 'registry';
  adminName = '';
  adminInitials = 'AD';
  profileMenuOpen = false;
  headerSearch = '';

  @HostListener('document:click')
  closeProfileMenu() { this.profileMenuOpen = false; }

  readonly tabs = [
    { id: 'registry',  label: 'Registry',          icon: 'domain' },
    { id: 'users',     label: 'Registered Users',   icon: 'manage_accounts' },
    { id: 'programs',  label: 'Study Programs',     icon: 'school' },
    { id: 'org',       label: 'Organization',       icon: 'account_tree' },
    { id: 'employees', label: 'Employees',          icon: 'badge' },
    { id: 'periods',   label: 'Exam Periods',       icon: 'event_note' },
    { id: 'schedule',  label: 'Class Schedule',     icon: 'calendar_month' },
  ];

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    const saved = localStorage.getItem('adminDashboardTab');
    if (saved) this.activeTab = saved;

    const u = this.authService.getCurrentUser();
    if (u) {
      this.adminName = u.username || u.email || 'Admin';
      this.adminInitials = (u.username || u.email || 'AD').substring(0, 2).toUpperCase();
    }
  }

  setTab(tab: string): void {
    this.activeTab = tab;
    localStorage.setItem('adminDashboardTab', tab);
  }

  toggleProfileMenu(event: MouseEvent): void {
    event.stopPropagation();
    this.profileMenuOpen = !this.profileMenuOpen;
  }

  onHeaderSearch(event: Event): void {
    this.headerSearch = (event.target as HTMLInputElement).value;
  }

  goToProfile(): void { this.router.navigate(['/my-profile']); this.profileMenuOpen = false; }
  goToAnnouncements(): void { this.router.navigate(['/announcements']); this.profileMenuOpen = false; }

  logout(): void {
    this.authService.logout();
  }
}
