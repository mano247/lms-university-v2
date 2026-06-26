import { Component, Input, NO_ERRORS_SCHEMA } from '@angular/core';
import { University } from '../../../model/academic/university';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-footer',
  standalone: true,
  imports: [],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})
export class FooterComponent {
  currentYear: number = new Date().getFullYear();

  @Input() univerzitet: University = {
    name: '',
    foundingDate: new Date(),
    contact: '',
    description: '',
    image: '',
    address: ''
  };
}
