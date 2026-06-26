import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { LoginService } from '../../services/auth/login.service';
import { NgIf } from '@angular/common';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-dashboard',
  standalone: true,
  imports: [NgIf, CardModule, ButtonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit{
  roles: string[] = [];

  constructor(private loginService: LoginService, private router: Router) {}

  ngOnInit(): void {
    this.roles = this.loginService.getUserRole();
  }

  navigateToHome(){ this.router.navigate(['']); }
  navigateToStudent(){ this.router.navigate(['eStudent']); }
  navigateToTeacher(){ this.router.navigate(['eProfesor']); }
  navigateToOffice(){ this.router.navigate(['eSofficeStaff']); }
  navigateToAdmin(){ this.router.navigate(['eAdmin']); }
}
