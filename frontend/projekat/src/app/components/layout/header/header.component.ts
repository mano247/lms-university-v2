import { Component, HostListener } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule, MatMenuModule, MatDividerModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  mobileMenuOpen = false;
  scrolled = false;

  constructor(public authService: AuthService, private router: Router) {}

  @HostListener('window:scroll')
  onScroll() {
    this.scrolled = window.scrollY > 30;
  }

  get userInitial(): string {
    const user = this.authService.getCurrentUser();
    return (user?.username ?? user?.email ?? 'U')[0].toUpperCase();
  }

  toggleMobileMenu() {
    this.mobileMenuOpen = !this.mobileMenuOpen;
  }

  closeMobileMenu() {
    this.mobileMenuOpen = false;
  }

  goToMenu() {
    this.router.navigate(['/menu']);
    this.closeMobileMenu();
  }

  goToProfile() {
    this.router.navigate(['/my-profile']);
    this.closeMobileMenu();
  }

  goToAnnouncements() {
    this.router.navigate(['/announcements']);
    this.closeMobileMenu();
  }

  logout() {
    this.authService.logout();
    this.closeMobileMenu();
  }
}
