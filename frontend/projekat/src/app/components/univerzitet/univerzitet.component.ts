import { Component, Input, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { University } from '../../model/academic/univerzitet';
import { UniversityService } from '../../services/univerzitet.service';
import { FacultyService } from '../../services/fakultet.service';
import { Faculty } from '../../model/academic/fakultet';
import { NgFor } from '@angular/common';
import { DividerModule } from 'primeng/divider';
import { Router, RouterModule } from '@angular/router';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-univerzitet',
  standalone: true,
  imports: [NgFor, DividerModule, RouterModule],
  templateUrl: './univerzitet.component.html',
  styleUrl: './univerzitet.component.css'
})
export class UniverzitetComponent implements OnInit {
  private universityId: number = 1;

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

  @Input()
  faculties: Faculty[] = [];

  constructor(
    private universityService: UniversityService,
    private facultyService: FacultyService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getUniversity();
    this.getFaculties();
  }

  getUniversity() {
    return this.universityService.getById(this.universityId).subscribe(x => {
      this.university = x;
    });
  }

  getFaculties() {
    return this.facultyService.getAll().subscribe(x => {
      this.faculties = x;
    });
  }

  getFoundingDate(): string {
    const date = new Date(this.university.foundingDate);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${day}. ${month}. ${year}`;
  }
}
