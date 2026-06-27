import { Component, OnInit } from '@angular/core';
import { StudentEnrollmentComponent } from '../../student-affairs-components/student-enrollment/student-enrollment.component';
import { DocumentIssuanceComponent } from '../../student-affairs-components/document-issuance/document-issuance.component';
import { AnnouncementsComponent } from '../../announcements/announcements.component';
import { LibraryComponent } from '../../student-affairs-components/library/library.component';
import { OfficeSuppliesComponent } from '../../student-affairs-components/office-supplies/office-supplies.component';
import { SchedulesComponent } from '../../student-affairs-components/schedules/schedules.component';
import { SaExamPeriodsComponent } from '../../student-affairs-components/sa-exam-periods/sa-exam-periods.component';

@Component({
  selector: 'app-e-sluzba',
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
  templateUrl: './e-sluzba.component.html',
  styleUrl: './e-sluzba.component.css',
})
export class ESluzbaComponent implements OnInit {
  activeTab = 'enrollment';
  officeName = '';
  officeInitials = 'SA';

  readonly tabs = [
    { id: 'enrollment',    label: 'Enrollment',       icon: 'how_to_reg' },
    { id: 'documents',     label: 'Documents',        icon: 'description' },
    { id: 'announcements', label: 'Announcements',    icon: 'campaign' },
    { id: 'library',       label: 'Library',          icon: 'menu_book' },
    { id: 'supplies',      label: 'Office Supplies',  icon: 'inventory_2' },
    { id: 'schedule',      label: 'Class Schedule',   icon: 'calendar_month' },
    { id: 'periods',       label: 'Exam Periods',     icon: 'event_note' },
  ];

  ngOnInit(): void {
    const saved = localStorage.getItem('saDashboardTab');
    if (saved) this.activeTab = saved;

    const raw = localStorage.getItem('user');
    if (raw) {
      const u = JSON.parse(raw);
      const parts: string[] = [u.firstName, u.lastName].filter(Boolean);
      this.officeName = parts.join(' ') || u.email || 'Student Affairs';
      this.officeInitials = parts.map((n: string) => n[0]).join('').substring(0, 2).toUpperCase() || 'SA';
    }
  }

  setTab(tab: string): void {
    this.activeTab = tab;
    localStorage.setItem('saDashboardTab', tab);
  }
}
