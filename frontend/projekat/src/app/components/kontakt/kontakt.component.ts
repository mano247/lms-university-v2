import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { UniversityService } from '../../services/univerzitet.service';
import { DividerModule } from 'primeng/divider';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-kontakt',
  standalone: true,
  imports: [DividerModule],
  templateUrl: './kontakt.component.html',
  styleUrl: './kontakt.component.css'
})
export class KontaktComponent implements OnInit {
  private universityId = 1;
  universityContact: string | undefined;
  universityAddress: string | undefined;

  constructor(private universityService: UniversityService) {}

  ngOnInit(): void {
    this.universityService.getById(this.universityId).subscribe(x => {
      this.universityAddress = x.address;
      this.universityContact = x.contact;
    });
  }
}
