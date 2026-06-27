import { Component, OnInit, inject } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { NgIf } from '@angular/common';

import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    RouterModule,
    NgIf,
    MatProgressSpinnerModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4)]],
  });

  isLoading = false;
  showPassword = false;
  errorMessage = '';
  sessionExpiredMessage = '';

  ngOnInit(): void {
    const reason = sessionStorage.getItem('logoutReason');
    if (reason === 'session-expired') {
      this.sessionExpiredMessage = 'Your session has expired. Please sign in again.';
      sessionStorage.removeItem('logoutReason');
    }
  }

  get emailCtrl() { return this.form.get('email')!; }
  get passwordCtrl() { return this.form.get('password')!; }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  signIn(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    this.errorMessage = '';
    const { email, password } = this.form.value;

    this.authService.login(email!, password!).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/menu']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage =
          err.error?.message ?? 'Invalid email or password. Please try again.';
      },
    });
  }
}
