import { Component, Input, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { University } from '../../model/academic/university';
import { UniversityService } from '../../services/university.service';
import { FacultyService } from '../../services/faculty.service';
import { Faculty } from '../../model/academic/faculty';
import { NgFor } from '@angular/common';
import { DividerModule } from 'primeng/divider';
import { RouterModule } from '@angular/router';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-university',
  standalone: true,
  imports: [NgFor, DividerModule, RouterModule],
  templateUrl: './university.component.html',
  styleUrl: './university.component.css'
})
export class UniversityComponent implements OnInit {
  private universityId: number = 1;

  university: University = {
    name: '',
    foundingDate: new Date(),
    contact: '',
    description: '',
    image: '',
    address: ''
  };

  @Input()
  faculties: Faculty[] = [];

  constructor(
    private universityService: UniversityService,
    private facultyService: FacultyService
  ) {}

  ngOnInit(): void {
    this.loadUniversity();
    this.loadFaculties();
  }

  loadUniversity() {
    this.universityService.getById(this.universityId).subscribe(x => {
      this.university = x;
    });
  }

  loadFaculties() {
    this.facultyService.getAll().subscribe(x => {
      this.faculties = x;
    });
  }

  getDatum(): string {
    const d = new Date(this.university.foundingDate);
    const day = String(d.getDate()).padStart(2, '0');
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const year = d.getFullYear();
    return `${day}. ${month}. ${year}`;
  }
}
