import { Component, Input, NO_ERRORS_SCHEMA } from '@angular/core';
import { University } from '../../../model/academic/univerzitet';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-footer',
  standalone: true,
  imports: [],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})
export class FooterComponent {
  currentYear: Date = new Date();

  @Input()
  university: University = {
    name: '',
    foundingDate: new Date(),
    contact: '',
    description: '',
    image: '',
    address: '',
    rectorate: {
      name: '',
      contact: '',
      image: '',
      address: '',
      universities: [],
      rectorName: ''
    }
  };
}
