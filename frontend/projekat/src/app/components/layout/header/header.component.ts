import { Component, Input, NO_ERRORS_SCHEMA } from '@angular/core';
import { University } from '../../../model/academic/university';
import { Router, RouterModule } from '@angular/router';
import { AvatarModule } from 'primeng/avatar';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { MenuItem } from 'primeng/api';
import { LoginService } from '../../../services/auth/login.service';
import { NgIf } from '@angular/common';
import { ButtonModule } from 'primeng/button';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule, AvatarModule, TieredMenuModule, NgIf, ButtonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  @Input() univerzitet: University = {
    name: '',
    foundingDate: new Date(),
    contact: '',
    description: '',
    image: '',
    address: ''
  };

  items: MenuItem[];

  constructor(private router: Router, private loginService: LoginService) {
    this.items = [
      {
        label: 'My Menu',
        icon: 'pi pi-bars',
        command: () => this.router.navigate(['menu'])
      },
      {
        label: 'My Profile',
        icon: 'pi pi-user',
        command: () => this.router.navigate(['my-profile'])
      },
      {
        label: 'Announcements',
        icon: 'pi pi-bell',
        command: () => this.router.navigate(['all-announcements'])
      },
      {
        label: 'Logout',
        icon: 'pi pi-sign-out',
        command: () => this.loginService.logout()
      }
    ];
  }

  isLoggedIn(): boolean {
    return this.loginService.loggedIn();
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
