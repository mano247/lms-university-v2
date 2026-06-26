import { Component, Input, OnChanges, SimpleChanges, NO_ERRORS_SCHEMA } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { MenubarModule } from 'primeng/menubar';
import { Faculty } from '../../../model/academic/faculty';
import { Router } from '@angular/router';
import { FacultyService } from '../../../services/faculty.service';
import { StudyProgram } from '../../../model/academic/study-program';
import { LoginService } from '../../../services/auth/login.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-menu-bar',
  standalone: true,
  imports: [MenubarModule],
  templateUrl: './menu-bar.component.html',
  styleUrl: './menu-bar.component.css'
})
export class MenuBarComponent implements OnChanges {
  @Input() faculties: Faculty[] = [];
  @Input() studyPrograms: StudyProgram[] = [];

  items: MenuItem[] | undefined;

  constructor(private router: Router, private facultyService: FacultyService, private loginService: LoginService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['faculties'] || changes['studyPrograms']) {
      this.buildMenu();
    }
  }

  buildMenu() {
    this.items = [
      {
        label: 'University',
        icon: 'pi pi-graduation-cap',
        command: () => this.router.navigate([''])
      },
      {
        label: 'Faculties',
        icon: 'pi pi-building-columns',
        command: () => this.router.navigate(['faculties']),
        items: this.faculties.map(f => ({
          label: f.name,
          icon: 'pi pi-building',
          command: () => this.router.navigate([`faculty/${f.facultyCode}`])
        }))
      },
      {
        label: 'Enrollment',
        icon: 'pi pi-pen-to-square',
        command: () => this.router.navigate(['enrollment'])
      },
      {
        label: 'Announcements',
        icon: 'pi pi-bell',
        command: () => this.router.navigate(['all-announcements'])
      },
      {
        label: 'Rectorate',
        icon: 'pi pi-users',
        command: () => this.router.navigate(['rectorate'])
      },
      {
        label: 'Contact',
        icon: 'pi pi-envelope',
        command: () => this.router.navigate(['contact'])
      }
    ];
  }
}
