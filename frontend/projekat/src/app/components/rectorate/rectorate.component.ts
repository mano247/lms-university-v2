import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { RectorateService } from '../../services/rectorate.service';
import { DividerModule } from 'primeng/divider';
import { Rectorate } from '../../model/rectorate';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-rectorate',
  standalone: true,
  imports: [DividerModule],
  templateUrl: './rectorate.component.html',
  styleUrl: './rectorate.component.css'
})
export class RectorateComponent implements OnInit {
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
