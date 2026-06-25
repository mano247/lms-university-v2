import { Component, Input, NO_ERRORS_SCHEMA } from '@angular/core';
import { Univerzitet } from '../../../model/academic/univerzitet';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-footer',
  standalone: true,
  imports: [],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})
export class FooterComponent {
  datum: Date = new Date();

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

}
