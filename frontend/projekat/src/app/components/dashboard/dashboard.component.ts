import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';

interface PortalCard {
  icon: string;
  title: string;
  description: string;
  buttonLabel: string;
  route: string;
  accent: boolean;
  disabled?: boolean;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  userName = '';
  userInitials = '';
  rolePortal: PortalCard | null = null;

  readonly universityCard: PortalCard = {
    icon: 'public',
    title: 'University Portal',
    description:
      'Browse public university information, institutional news, academic calendars, faculty pages, and campus life updates available to the entire community.',
    buttonLabel: 'Access Public Information',
    route: '/',
    accent: false,
  };

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.userName = user.username || user.email || 'User';
      this.userInitials = this.userName.substring(0, 2).toUpperCase();
    }
    this.rolePortal = this.resolvePortal(this.authService.getCurrentUser()?.permissions ?? []);
  }

  private resolvePortal(permissions: string[]): PortalCard {
    if (permissions.includes('STUDENT_PERMISSION')) {
      return {
        icon: 'school',
        title: 'My Student Portal',
        description:
          'Access your academic dashboard, course grades, exam registration, study history, personalized schedule, and academic announcements.',
        buttonLabel: 'Enter Dashboard',
        route: '/eStudent',
        accent: true,
      };
    }
    if (permissions.includes('TEACHER_PERMISSION')) {
      return {
        icon: 'person_book',
        title: 'My Teacher Portal',
        description:
          'Manage your assigned courses, view student rosters, record grades, create course announcements, and handle exam scheduling.',
        buttonLabel: 'Enter Dashboard',
        route: '/eTeacher',
        accent: true,
      };
    }
    if (permissions.includes('STUDENT_AFFAIRS_PERMISSION')) {
      return {
        icon: 'admin_panel_settings',
        title: 'Staff Portal',
        description:
          'Handle student enrollment, issue academic documents, manage library resources, office supplies inventory, schedules, and exam periods.',
        buttonLabel: 'Enter Dashboard',
        route: '/eOffice',
        accent: true,
      };
    }
    if (permissions.includes('ADMINISTRATOR_PERMISSION')) {
      return {
        icon: 'manage_accounts',
        title: 'Admin Panel',
        description:
          'Full system administration — manage the institutional registry, registered users, study programs, organizational structure, and all employees.',
        buttonLabel: 'Enter Dashboard',
        route: '/eAdmin',
        accent: true,
      };
    }
    return {
      icon: 'hourglass_empty',
      title: 'Pending Enrollment',
      description:
        'Your account has been registered and is awaiting enrollment. A staff member will assign you to a faculty and study program shortly.',
      buttonLabel: 'No portal yet',
      route: '',
      accent: true,
      disabled: true,
    };
  }

  navigate(route: string): void {
    if (route) this.router.navigateByUrl(route);
  }

  logout(): void {
    this.authService.logout();
  }
}
