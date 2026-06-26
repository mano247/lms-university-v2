import { NgIf } from '@angular/common';
import { Component, OnDestroy, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { LoginService } from '../../services/auth/login.service';
import { ProgressSpinnerModule } from 'primeng/progressspinner';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-registration',
  standalone: true,
  imports: [ButtonModule, ReactiveFormsModule, NgIf, RouterModule, ToastModule, ProgressSpinnerModule],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css',
  providers: [MessageService]
})
export class RegistrationComponent implements OnInit, OnDestroy {
  isLoading: boolean = false;

  emailFormControl = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);
  passwordFormControl = new FormControl('', [
    Validators.required,
    Validators.minLength(4)
  ]);
  usernameFormControl = new FormControl('', [
    Validators.required,
    Validators.minLength(4)
  ]);

  constructor(private messageService: MessageService, private router: Router, private loginService: LoginService) {}

  ngOnDestroy(): void {
    document.removeEventListener('keydown', this.handleKeydown.bind(this));
  }

  ngOnInit(): void {
    document.addEventListener('keydown', this.handleKeydown.bind(this));
  }

  handleKeydown(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.register();
    } else if (event.key === 'Escape' || event.key === 'Delete') {
      this.resetForm();
    }
  }

  register() {
    if (
      this.emailFormControl.valid &&
      this.passwordFormControl.valid &&
      this.usernameFormControl.valid
    ) {
      this.isLoading = true;
      const formData = {
        email: this.emailFormControl.value,
        password: this.passwordFormControl.value,
        username: this.usernameFormControl.value
      };
      this.loginService.registerUser(formData).subscribe(
        () => {
          this.messageService.add({ severity: 'success', summary: 'Registration successful', detail: 'User successfully registered.' });
          this.resetForm();
          setTimeout(() => {
            this.isLoading = false;
            this.router.navigate(['/']);
          }, 1000);
        },
        (error) => {
          this.messageService.add({ severity: 'error', summary: 'Registration failed', detail: error.error.message || 'Please check the entered data.' });
          this.isLoading = false;
          this.resetForm();
        }
      );
    } else {
      this.isLoading = false;
      this.usernameFormControl.markAllAsTouched();
      this.emailFormControl.markAllAsTouched();
      this.passwordFormControl.markAllAsTouched();
      this.messageService.add({ severity: 'error', summary: 'Registration failed', detail: 'Please check the entered data.' });
    }
  }

  resetForm() {
    this.usernameFormControl.reset();
    this.emailFormControl.reset();
    this.passwordFormControl.reset();
  }
}
