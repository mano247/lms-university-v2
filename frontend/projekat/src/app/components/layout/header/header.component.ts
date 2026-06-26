import { Component, Input, NO_ERRORS_SCHEMA } from '@angular/core';
import { University } from '../../../model/academic/univerzitet';
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
  @Input()
  university: University = {
    name: '',
    foundingDate: new Date(),
    contact: '',
    description: '',
    image: '',
    address: '',
    rectorate: {
      name: '',
      contact: '',
      image: '',
      address: '',
      universities: [],
      rectorName: ''
    }
  };

  items: MenuItem[];

  constructor(private router: Router, private loginService: LoginService) {
    this.items = [
      {
        label: 'My Menu',
        icon: 'pi pi-bars',
        command: () => this.goToMenu()
      },
      {
        label: 'My Profile',
        icon: 'pi pi-user',
        command: () => this.goToProfile()
      },
      {
        label: 'Announcements',
        icon: 'pi pi-bell',
        command: () => this.goToAnnouncements()
      },
      {
        label: 'Logout',
        icon: 'pi pi-sign-out',
        command: () => this.logout()
      }
    ];
  }

  goToMenu() {
    this.router.navigate(['menu']);
  }

  goToProfile() {
    this.router.navigate(['moj-profil']);
  }

  goToAnnouncements() {
    this.router.navigate(['sva_obavestenja']);
  }

  logout() {
    this.loginService.logout();
  }

  isLoggedIn(): boolean {
    return this.loginService.loggedIn();
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
