import { Component, inject } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { NgIf, TitleCasePipe } from '@angular/common';

import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AuthService } from '../../services/auth/auth.service';

const passwordMatchValidator: ValidatorFn = (group: AbstractControl): ValidationErrors | null => {
  const password = group.get('password')?.value;
  const confirmPassword = group.get('confirmPassword')?.value;
  return password && confirmPassword && password !== confirmPassword
    ? { passwordMismatch: true }
    : null;
};

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    RouterModule,
    NgIf,
    TitleCasePipe,
    MatProgressSpinnerModule,
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  form = this.fb.group(
    {
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      username: ['', [Validators.required, Validators.minLength(4), Validators.pattern(/^\S+$/)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', Validators.required],
    },
    { validators: passwordMatchValidator },
  );

  isLoading = false;
  showPassword = false;
  showConfirm = false;
  errorMessage = '';

  get firstNameCtrl() { return this.form.get('firstName')!; }
  get lastNameCtrl()  { return this.form.get('lastName')!; }
  get usernameCtrl()  { return this.form.get('username')!; }
  get emailCtrl()     { return this.form.get('email')!; }
  get passwordCtrl()  { return this.form.get('password')!; }
  get confirmCtrl()   { return this.form.get('confirmPassword')!; }

  get passwordStrength(): 'weak' | 'fair' | 'strong' {
    const val = this.passwordCtrl.value ?? '';
    let score = 0;
    if (val.length > 5) score++;
    if (val.length > 8) score++;
    if (/[A-Z]/.test(val) && /[0-9]/.test(val)) score++;
    if (score >= 3) return 'strong';
    if (score === 2) return 'fair';
    return 'weak';
  }

  get passwordStrengthWidth(): string {
    return { weak: '33%', fair: '66%', strong: '100%' }[this.passwordStrength];
  }

  togglePassword(): void { this.showPassword = !this.showPassword; }
  toggleConfirm(): void  { this.showConfirm = !this.showConfirm; }

  register(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    this.errorMessage = '';

    const { firstName, lastName, username, email, password } = this.form.value;

    this.authService.register({
      firstName: firstName!,
      lastName: lastName!,
      username: username!,
      email: email!,
      password: password!,
    }).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage =
          err.error?.message ?? 'Registration failed. Please check your details and try again.';
      },
    });
  }
}
