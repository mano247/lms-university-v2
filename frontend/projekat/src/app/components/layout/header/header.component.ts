import { Component, Input, NO_ERRORS_SCHEMA } from '@angular/core';
import { Univerzitet } from '../../../model/academic/univerzitet';
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
  univerzitet: Univerzitet = {
    naziv: '',
    datumOsnivanja: new Date,
    kontakt: '',
    opis: '',
    slika: '',
    adresa: '',
    rektorat: {
      naziv: '',
      kontakt: '',
      slika: '',
      adresa: '',
      univerziteti: [],
      ime_rektora: ''
    }
  };

  items: MenuItem[];

  constructor(private router: Router, private loginService: LoginService){
    this.items = [
      {
        label: 'Moj meni',
        icon: 'pi pi-bars',
        command: () => this.meni()
      },
      {
        label: 'Moj profil',
        icon: 'pi pi-user',
        command: () => this.mojProfil()
      },
      {
        label: 'Obavestenja',
        icon: 'pi pi-bell',
        command: () => this.obavestenja()
      },
      {
        label: 'Odjava',
        icon: 'pi pi-sign-out',
        command: () => this.logout()
      }
    ];
  }
  
  meni(){
    this.router.navigate(['menu'])
  }

  mojProfil(){
    this.router.navigate(['moj-profil'])
  }

  obavestenja(){
    this.router.navigate(['sva_obavestenja'])
  }

  logout(){
    this.loginService.logout();
  }

  isLoggedIn(): boolean {
    return this.loginService.loggedIn();
  }

  prijavaLink(){
    this.router.navigate(['/login']);
  }
  
}
