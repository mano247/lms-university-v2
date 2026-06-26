import { Component, OnDestroy, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { Router, RouterModule } from '@angular/router';
import { MessageService } from 'primeng/api';
import { LoginService } from '../../services/auth/login.service';
import { NgIf } from '@angular/common';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ProgressBarModule } from 'primeng/progressbar';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-login',
  standalone: true,
  imports: [ButtonModule, ReactiveFormsModule, RouterModule, ToastModule, NgIf, ProgressSpinnerModule, ProgressBarModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  providers: [MessageService]
})
export class LoginComponent implements OnInit, OnDestroy{
  isLoading: boolean = false;

  emailFormControl = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);
  passwordFormControl = new FormControl('', [
    Validators.required,
    Validators.minLength(4)
  ]);

  constructor(private loginService: LoginService, private messageService: MessageService, private router: Router){}
  ngOnDestroy(): void {
    document.removeEventListener('keydown', this.handleKeydown.bind(this));
  }
  ngOnInit(): void {
    document.addEventListener('keydown', this.handleKeydown.bind(this));
  }

  handleKeydown(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.prijaviSe();
    }else if (event.key === 'Escape') {
      this.ponistiUnos();
    }else if (event.key === 'Delete') {
      this.ponistiUnos();
    }
  }

  prijaviSe(): void{
    if(this.emailFormControl.valid && this.passwordFormControl.valid){
      this.isLoading = true;

      const formData = {
        email: this.emailFormControl.value,
        password: this.passwordFormControl.value,
      };
      this.loginService.loginUser(formData).subscribe(
        (response) => {
          this.messageService.add({ severity: 'success', summary: 'Uspesna prijava', detail: 'Uspesno ste se prijavili.' });
          this.ponistiUnos();
          setTimeout(() => {
            this.isLoading = false;
            this.router.navigate(['/menu']);
          }, 1000); 
        },
        (error) => {
          console.error('Login error', error);
          this.emailFormControl.markAllAsTouched();
          this.passwordFormControl.markAllAsTouched();
          this.messageService.add({ severity: 'error', summary: 'Neuspesna prijava', detail: 'Proverite unesene podatke.' });
          this.isLoading = false;
        }
      );
    } else {
      this.emailFormControl.markAllAsTouched();
      this.passwordFormControl.markAllAsTouched();
      this.messageService.add({ severity: 'error', summary: 'Neuspesna prijava', detail: 'Podaci nisu validni.' });
    }
  }

  ponistiUnos(){
    this.emailFormControl.reset();
    this.passwordFormControl.reset()
  }
}

