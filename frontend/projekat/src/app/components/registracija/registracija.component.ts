import { NgIf } from '@angular/common';
import { Component, OnDestroy, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { LoginService } from '../../services/auth/login.service';
import { ProgressSpinnerModule } from 'primeng/progressspinner';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-registracija',
  standalone: true,
  imports: [ButtonModule, ReactiveFormsModule, NgIf, RouterModule, ToastModule, ProgressSpinnerModule],
  templateUrl: './registracija.component.html',
  styleUrl: './registracija.component.css',
  providers: [MessageService]
})
export class RegistracijaComponent implements OnInit, OnDestroy{
  isLoading: boolean = false;

  emailFormControl = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);
  lozinkaFormControl = new FormControl('', [
    Validators.required,
    Validators.minLength(4)
  ]);
  korisnickoFormControl = new FormControl('', [
    Validators.required,
    Validators.minLength(4)
  ])
  

  constructor(private messageService: MessageService, private router: Router, private loginService: LoginService){}
  ngOnDestroy(): void {
    document.removeEventListener('keydown', this.handleKeydown.bind(this));
  }
  ngOnInit(): void {
    document.addEventListener('keydown', this.handleKeydown.bind(this));
  }

  handleKeydown(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.registracija();
    }else if (event.key === 'Escape') {
      this.ponistiFormu();
    }else if (event.key === 'Delete') {
      this.ponistiFormu();
    }
  }

  registracija(){
    if (
      this.emailFormControl.valid &&
      this.lozinkaFormControl.valid &&
      this.korisnickoFormControl.valid
    ){
      this.isLoading = true;

      const formData = {
        email: this.emailFormControl.value,
        lozinka: this.lozinkaFormControl.value,
        korisnickoIme: this.korisnickoFormControl.value
      };
      this.loginService.registerUser(formData).subscribe(
        (response) => {
          this.messageService.add({ severity: 'success', summary: 'Uspesna registracija', detail: 'Korisnik uspesno registrovan.' });
          this.ponistiFormu();
          setTimeout(() => {
            this.isLoading = false;
            this.router.navigate(['/']);
          }, 1000); 
        },
        (error) => {
          this.messageService.add({ severity: 'error', summary: 'Registracija neuspesna', detail: error.error.message || 'Proverite unesene podatke.' });
          this.isLoading = false;
          this.ponistiFormu();
        }
      );
    } else {
      this.isLoading = false;
      this.korisnickoFormControl.markAllAsTouched();
      this.emailFormControl.markAllAsTouched();
      this.lozinkaFormControl.markAllAsTouched();
      this.messageService.add({ severity: 'error', summary: 'Registracija neuspesna', detail: 'Proverite unesene podatke.' });
    }
  }

  ponistiFormu(){
    this.korisnickoFormControl.reset();
    this.emailFormControl.reset();
    this.lozinkaFormControl.reset();
  }
}
