import { NgIf } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { Form, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { RegistrovaniKorisnikService } from '../../services/registrovani-korisnik.service';
import { RegistrovaniKorisnik } from '../../model/users/registrovaniKorisnik';
import { StudentiService } from '../../services/studenti.service';
import { NastavnikService } from '../../services/nastavnik.service';
import { StudentskaSluzbaService } from '../../services/studentska-sluzba.service';
import { AdministratorService } from '../../services/administrator.service';
import { LoginService } from '../../services/auth/login.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-profil',
  standalone: true,
  imports: [DividerModule, ButtonModule, ReactiveFormsModule, ToastModule, NgIf, DialogModule, ReactiveFormsModule],
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.css'],
  providers: [MessageService]
})
export class ProfilComponent implements OnInit{
  visible: boolean = false;
  profilForm: FormGroup = this.fb.group({});
  korisnik: RegistrovaniKorisnik | undefined;
  korisnikId: number | undefined;
  roles: string[] = [];

  constructor(private fb: FormBuilder, private messageService: MessageService, 
    private korisnikService: RegistrovaniKorisnikService, private studentService: StudentiService,
    private nastavnikService: NastavnikService, private sluzbaService: StudentskaSluzbaService, 
    private  adminService: AdministratorService, private loginService: LoginService
  ) {}

  ngOnInit(): void {
    this.createForm();

    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      this.korisnikId= parsedUser.id;
      if(this.korisnikId !== undefined){
        this.getPodaci(this.korisnikId);
        this.roles = this.loginService.getUserRole();
        // console.log(this.roles)
      }
    }
  }

  getPodaci(id: number){
    this.korisnikService.getById(id).subscribe(x=>{
      this.korisnik = x;
      console.log(this.korisnik);
    })
  }

  postaviZaIzmenu(){
    if (this.korisnik) {
      this.profilForm.patchValue(this.korisnik);
    }
  }

  editDialog(){
    this.visible = true;
    this.postaviZaIzmenu();
  }

  createForm() {
    this.profilForm = this.fb.group({
      ime: [this.korisnik?.ime],
      prezime: [this.korisnik?.prezime],
      email: [this.korisnik?.email, [Validators.required, Validators.email]],
      korisnickoIme: [this.korisnik?.korisnickoIme, Validators.required],
      lozinka: [this.korisnik?.lozinka, Validators.required] 
    });
  }

  sacuvajIzmene() {
    if (this.profilForm.valid && this.korisnikId) {
      const updatedKorisnik: any = {
        ...this.korisnik,
        ...this.profilForm.value
      };
  
  
      let updateObservable;
  
      if (this.roles.includes('ADMINISTRATOR_PERMISSION')) {
        updateObservable = this.adminService.update(this.korisnikId, updatedKorisnik);
      } else if (this.roles.includes('STUDENTSKASLUZBA_PERMISSION')) {
        updateObservable = this.sluzbaService.update(this.korisnikId, updatedKorisnik);
      }else if (this.roles.includes('NASTAVNIK_PERMISSION')) {
        updateObservable = this.nastavnikService.update(this.korisnikId, updatedKorisnik);
      } else if (this.roles.includes('STUDENT_PERMISSION')) {
        updateObservable = this.studentService.update(this.korisnikId, updatedKorisnik);
      } else if (this.roles.includes('KORISNIK_PERMISSION')) {
        updateObservable = this.korisnikService.update(this.korisnikId, updatedKorisnik);
      } else {
        this.messageService.add({ severity: 'error', summary: 'Greška!', detail: 'Nepoznata uloga korisnika.' });
        return;
      }
  
      updateObservable.subscribe(
        () => {
          this.visible = false;
          this.messageService.add({ severity: 'success', summary: 'Profil uređen!', detail: 'Podaci su uspešno izmenjeni.' });
          if (this.korisnikId !== undefined) {
            this.getPodaci(this.korisnikId);
          }
        },
        error => {
          this.messageService.add({ severity: 'error', summary: 'Greška!', detail: 'Došlo je do greške prilikom izmene podataka.' });
        }
      );
    } else {
      this.messageService.add({ severity: 'warning', summary: 'Net tačni podaci!', detail: 'Proverite unesene podatke!' });
    }
  }
  
}
