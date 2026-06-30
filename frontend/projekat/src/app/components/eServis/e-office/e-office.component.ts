import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth/auth.service';
import { StudentEnrollmentComponent } from '../../student-affairs-components/student-enrollment/student-enrollment.component';
import { DocumentIssuanceComponent } from '../../student-affairs-components/document-issuance/document-issuance.component';
import { AnnouncementsComponent } from '../../announcements/announcements.component';
import { LibraryComponent } from '../../student-affairs-components/library/library.component';
import { OfficeSuppliesComponent } from '../../student-affairs-components/office-supplies/office-supplies.component';
import { SchedulesComponent } from '../../student-affairs-components/schedules/schedules.component';
import { SaExamPeriodsComponent } from '../../student-affairs-components/sa-exam-periods/sa-exam-periods.component';

@Component({
  selector: 'app-e-office',
  standalone: true,
  imports: [
    StudentEnrollmentComponent,
    DocumentIssuanceComponent,
    AnnouncementsComponent,
    LibraryComponent,
    OfficeSuppliesComponent,
    SchedulesComponent,
    SaExamPeriodsComponent,
  ],
  templateUrl: './e-office.component.html',
  styleUrl: './e-office.component.css',
})
export class EOfficeComponent implements OnInit {
  activeTab = 'enrollment';
  officeName = '';
  officeInitials = 'SA';
  profileMenuOpen = false;
  headerSearch = '';

  @HostListener('document:click')
  closeProfileMenu() { this.profileMenuOpen = false; }

  readonly tabs = [
    { id: 'enrollment',    label: 'Enrollment',       icon: 'how_to_reg' },
    { id: 'documents',     label: 'Documents',        icon: 'description' },
    { id: 'announcements', label: 'Announcements',    icon: 'campaign' },
    { id: 'library',       label: 'Library',          icon: 'menu_book' },
    { id: 'supplies',      label: 'Office Supplies',  icon: 'inventory_2' },
    { id: 'schedule',      label: 'Class Schedule',   icon: 'calendar_month' },
    { id: 'periods',       label: 'Exam Periods',     icon: 'event_note' },
  ];

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    const saved = localStorage.getItem('saDashboardTab');
    if (saved) this.activeTab = saved;

    const u = this.authService.getCurrentUser();
    if (u) {
      this.officeName = u.username || u.email || 'Student Affairs';
      this.officeInitials = (u.username || u.email || 'SA').substring(0, 2).toUpperCase();
    }
  }

  setTab(tab: string): void {
    this.activeTab = tab;
    localStorage.setItem('saDashboardTab', tab);
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

