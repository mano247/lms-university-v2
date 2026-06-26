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

  constructor(
    private router: Router,
    private facultyService: FacultyService,
    private loginService: LoginService
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['faculties'] || changes['studyPrograms']) {
      this.buildMenuItems();
    }
  }

  buildMenuItems() {
    this.items = [
      {
        label: 'University',
        icon: 'pi pi-graduation-cap',
        command: () => this.goToUniversity()
      },
      {
        label: 'Faculties',
        icon: 'pi pi-building-columns',
        command: () => this.goToFaculties(),
        items: this.faculties.map(faculty => ({
          label: faculty.name,
          icon: 'pi pi-building',
          command: () => this.goToFaculty(faculty.facultyCode)
        }))
      },
      {
        label: 'Enrollment',
        icon: 'pi pi-pen-to-square',
        command: () => this.goToEnrollment()
      },
      {
        label: 'Announcements',
        icon: 'pi pi-bell',
        command: () => this.goToAnnouncements()
      },
      {
        label: 'Rectorate',
        icon: 'pi pi-users',
        command: () => this.goToRectorate()
      },
      {
        label: 'Contact',
        icon: 'pi pi-envelope',
        command: () => this.goToContact()
      }
    ];
  }

  goToUniversity() {
    this.router.navigate(['']);
  }

  goToFaculty(facultyCode: string) {
    this.router.navigate([`faculty/${facultyCode}`]);
  }

  goToEnrollment() {
    this.router.navigate(['enrollment']);
  }

  goToAnnouncements() {
    this.router.navigate(['all-announcements']);
  }

  goToRectorate() {
    this.router.navigate(['rectorate']);
  }

  goToContact() {
    this.router.navigate(['contact']);
  }

  goToLogin() {
    this.router.navigate(['login']);
  }

  goToFaculties() {
    this.router.navigate(['faculties']);
  }
}
