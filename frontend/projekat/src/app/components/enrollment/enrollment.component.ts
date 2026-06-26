import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DividerModule } from 'primeng/divider';
import { UniversityService } from '../../services/university.service';
import { RouterModule } from '@angular/router';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-enrollment',
  standalone: true,
  imports: [DividerModule, RouterModule],
  templateUrl: './enrollment.component.html',
  styleUrl: './enrollment.component.css'
})
export class EnrollmentComponent implements OnInit{
  private universityId = 1;
  universityContact: string | undefined;
  universityName: string | undefined;
  universityAddress: string | undefined;

  constructor(private universityService: UniversityService) {}

  ngOnInit(): void {
    this.universityService.getById(this.universityId).subscribe(x=>{
      this.universityContact = x.contact;
      this.universityName = x.name;
      this.universityAddress = x.address;
    });
  }
}
