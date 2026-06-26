import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { UniversityService } from '../../services/university.service';
import { DividerModule } from 'primeng/divider';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-contact',
  standalone: true,
  imports: [DividerModule],
  templateUrl: './contact.component.html',
  styleUrl: './contact.component.css'
})
export class ContactComponent implements OnInit{
  private university_id = 1;
  universityContact: string | undefined;
  universityAddress: string | undefined;

  constructor(private universityService: UniversityService) {}

  ngOnInit(): void {
    this.universityService.getById(this.university_id).subscribe(x => {
      this.universityAddress = x.address;
      this.universityContact = x.contact;
    });
  }

}

