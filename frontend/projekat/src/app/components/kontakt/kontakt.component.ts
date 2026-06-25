import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { UniverzitetService } from '../../services/univerzitet.service';
import { DividerModule } from 'primeng/divider';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-kontakt',
  standalone: true,
  imports: [DividerModule],
  templateUrl: './kontakt.component.html',
  styleUrl: './kontakt.component.css'
})
export class KontaktComponent implements OnInit{
  private univerzitet_id = 1;
  univerzitetKontakt: string | undefined;
  univerzitetAdresa: string | undefined;

  constructor(private univerzitetService: UniverzitetService) {}

  ngOnInit(): void {
    this.univerzitetService.getById(this.univerzitet_id).subscribe(x => {
      this.univerzitetAdresa = x.adresa;
      this.univerzitetKontakt = x.kontakt;
    })
  }

}
