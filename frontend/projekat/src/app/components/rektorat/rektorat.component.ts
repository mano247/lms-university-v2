import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { RectorateService } from '../../services/rektorat.service';
import { DividerModule } from 'primeng/divider';
import { Rectorate } from '../../model/rektorat';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-rektorat',
  standalone: true,
  imports: [DividerModule],
  templateUrl: './rektorat.component.html',
  styleUrl: './rektorat.component.css'
})
export class RektoratComponent implements OnInit {
  private rectorateId = 1;

  rectorate: Rectorate = {
    name: '',
    contact: '',
    image: '',
    address: '',
    universities: [],
    rectorName: ''
  };

  constructor(private rectorateService: RectorateService) {}

  ngOnInit(): void {
    this.getRectorate();
  }

  getRectorate() {
    return this.rectorateService.getById(this.rectorateId).subscribe(x => {
      this.rectorate = x;
    });
  }
}
