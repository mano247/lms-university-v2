import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { UniverzitetService } from '../../../services/univerzitet.service';
import { Univerzitet } from '../../../model/academic/univerzitet';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { FormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { RektoratService } from '../../../services/rektorat.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-a-organizacija',
  standalone: true,
  imports: [ButtonModule, DialogModule, FormsModule, CalendarModule, ToastModule],
  templateUrl: './a-organizacija.component.html',
  styleUrl: './a-organizacija.component.css',
  providers: [MessageService]
})
export class AOrganizacijaComponent implements OnInit{
  univerzitet: Univerzitet | undefined;
  rektorat: any;

  univerzitetZaIzmenu: any = {};

  visible: boolean = false;


  constructor(private univerzitetService: UniverzitetService, private messageService: MessageService, private rektoratService: RektoratService){}

  ngOnInit(): void {
    this.getUniverzitet();
  }

  getUniverzitet(){
    this.univerzitetService.getById(1).subscribe(x=>{
      this.univerzitet = x;
      console.log(this.univerzitet);
    })
  }

  getRektorat(){
    this.rektoratService.getById(1).subscribe(x=>{
      this.rektorat = x;
    })
  }

  izmeniDialog(){
    this.visible = true;
    this.univerzitetZaIzmenu = {...this.univerzitet};
  }

  izmena(){
    const uniZaIzmenu = {...this.univerzitetZaIzmenu, rektorat: this.univerzitetZaIzmenu.rektorat.id}
    console.log(uniZaIzmenu);
    this.univerzitetService.update(this.univerzitetZaIzmenu.id, uniZaIzmenu).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Uspešno', detail: 'Podaci o univerzitetu su uspešno izmenjeni.' });
        this.visible = false;
        this.getUniverzitet();
        this.univerzitetZaIzmenu = {};
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Greška', detail: 'Došlo je do greške prilikom izmene podataka o univerzitetu.' });
      }
    })

  }

  ponistiDialog(){
    this.visible = false;
    this.univerzitetZaIzmenu = {};
  }



}
