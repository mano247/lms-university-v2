import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UniversityService } from '../../services/university.service';

@Component({
  selector: 'app-enrollment',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './enrollment.component.html',
  styleUrl: './enrollment.component.css',
})
export class EnrollmentComponent implements OnInit {
  universityContact = '';
  universityName = 'University of Ashford';
  universityAddress = '';

  constructor(private universityService: UniversityService) {}

  ngOnInit(): void {
    this.universityService.getById(1).subscribe({
      next: (x) => {
        this.universityContact = x.contact ?? '';
        this.universityName = x.name ?? 'University of Ashford';
        this.universityAddress = x.address ?? '';
      },
    });
  }
}
