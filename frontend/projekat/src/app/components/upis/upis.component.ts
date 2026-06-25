import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DividerModule } from 'primeng/divider';
import { UniverzitetService } from '../../services/univerzitet.service';
import { RouterModule } from '@angular/router';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-upis',
  standalone: true,
  imports: [DividerModule, RouterModule],
  templateUrl: './upis.component.html',
  styleUrl: './upis.component.css'
})
export class UpisComponent implements OnInit{
  private univerzitet_id = 1;
  univerzitetKontakt: string | undefined;
  univerzitetNaziv: string | undefined;
  univerzitetAdresa: string | undefined;
  

  constructor(private univerzitetService: UniverzitetService,) {}

  ngOnInit(): void {
    this.univerzitetService.getById(this.univerzitet_id).subscribe(x=>{
      this.univerzitetKontakt = x.kontakt;
      this.univerzitetNaziv = x.naziv;
      this.univerzitetAdresa = x.adresa;
    })
  }

}
