import { Component, OnInit } from '@angular/core';
import { SifarnikComponent } from '../../admin-components/sifarnik/sifarnik.component';
import { ARegKorisniciComponent } from '../../admin-components/a-reg-korisnici/a-reg-korisnici.component';
import { AStudijskiProgramiComponent } from '../../admin-components/a-studijski-programi/a-studijski-programi.component';
import { AOrganizacijaComponent } from '../../admin-components/a-organizacija/a-organizacija.component';
import { AZaposleniComponent } from '../../admin-components/a-zaposleni/a-zaposleni.component';
import { SaExamPeriodsComponent } from '../../student-affairs-components/sa-exam-periods/sa-exam-periods.component';
import { SchedulesComponent } from '../../student-affairs-components/schedules/schedules.component';

@Component({
  selector: 'app-e-admin',
  standalone: true,
  imports: [
    SifarnikComponent,
    ARegKorisniciComponent,
    AStudijskiProgramiComponent,
    AOrganizacijaComponent,
    AZaposleniComponent,
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

  readonly tabs = [
    { id: 'registry',  label: 'Registry',          icon: 'domain' },
    { id: 'users',     label: 'Registered Users',   icon: 'manage_accounts' },
    { id: 'programs',  label: 'Study Programs',     icon: 'school' },
    { id: 'org',       label: 'Organization',       icon: 'account_tree' },
    { id: 'employees', label: 'Employees',          icon: 'badge' },
    { id: 'periods',   label: 'Exam Periods',       icon: 'event_note' },
    { id: 'schedule',  label: 'Class Schedule',     icon: 'calendar_month' },
  ];

  ngOnInit(): void {
    const saved = localStorage.getItem('adminDashboardTab');
    if (saved) this.activeTab = saved;

    const raw = localStorage.getItem('user');
    if (raw) {
      const u = JSON.parse(raw);
      const parts: string[] = [u.firstName, u.lastName].filter(Boolean);
      this.adminName = parts.join(' ') || u.email || 'Admin';
      this.adminInitials = parts.map((n: string) => n[0]).join('').substring(0, 2).toUpperCase() || 'AD';
    }
  }

  setTab(tab: string): void {
    this.activeTab = tab;
    localStorage.setItem('adminDashboardTab', tab);
  }
}
