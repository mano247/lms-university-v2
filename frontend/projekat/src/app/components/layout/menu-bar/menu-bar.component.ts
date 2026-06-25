import { Component, Input, OnChanges, OnInit, SimpleChanges, NO_ERRORS_SCHEMA } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { MenubarModule } from 'primeng/menubar';
import { Fakultet } from '../../../model/academic/fakultet';
import { Router } from '@angular/router';
import { FakultetService } from '../../../services/fakultet.service';
import { StudijskiProgram } from '../../../model/academic/studijskiProgram';
import { LoginService } from '../../../services/auth/login.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-menu-bar',
  standalone: true,
  imports: [MenubarModule],
  templateUrl: './menu-bar.component.html',
  styleUrl: './menu-bar.component.css'
})
export class MenuBarComponent implements OnChanges{
@Input()
fakulteti: Fakultet[] = [];

@Input()
studijskiProgrami: StudijskiProgram[] = [];

items: MenuItem[]|undefined;


constructor(private router: Router, private fakultetService: FakultetService, private loginService: LoginService) {}

ngOnChanges(changes: SimpleChanges): void {
  if (changes['fakulteti'] || changes['studijskiProgrami']) {
    this.updateMenuItems();
  }
}

updateMenuItems(){
  this.items = [
    {
      label: 'Univerzitet',
      icon: 'pi pi-graduation-cap',
      command: () => this.onUniverzitetClick()
    },
    {
      label: 'Fakulteti',
      icon: 'pi pi-building-columns',
      command: () => this.onFakultetiClick(),
      items: this.fakulteti.map(fakultet => ({
        label: fakultet.naziv,
        icon: 'pi pi-building',
        command: () => this.onFakultetClick(fakultet.sifraFakulteta)
      }))
    },
    {
      label: 'Upis',
      icon: 'pi pi-pen-to-square',
      command: () => this.onUpisClick()
    },
    {
      label: 'Obavestenja',
      icon: 'pi pi-bell',
      command: () => this.onObavestenjaClick()
    },
    {
      label: 'Rektorat',
      icon: 'pi pi-users',
      command: () => this.onRektoratClick()
    },
    {
      label: 'Kontakt',
      icon: 'pi pi-envelope',
      command: () => this.onKontaktClick()
    }
  ]
}

onUniverzitetClick(){
  this.router.navigate(['']);
}

onFakultetClick(sifraFakulteta: string){
  this.router.navigate([`fakultet/${sifraFakulteta}`]);
}

onUpisClick(){
  this.router.navigate(['upis']);
}

onObavestenjaClick(){
  this.router.navigate(['sva_obavestenja']);
}

onRektoratClick(){
  this.router.navigate(['rektorat']);
}

onKontaktClick(){
  this.router.navigate(['kontakt']);
}

onLoginClick(){
  this.router.navigate(['login']);
}

onFakultetiClick(){
  this.router.navigate(['fakulteti'])
}


onMenuMouseEnter() {

}

onMenuMouseLeave() {

}


}



