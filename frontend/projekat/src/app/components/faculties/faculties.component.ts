import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FacultyService } from '../../services/faculty.service';
import { Faculty } from '../../model/academic/faculty';
import { NgFor } from '@angular/common';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';


@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-faculties',
  standalone: true,
  imports: [NgFor, CardModule, ButtonModule],
  templateUrl: './faculties.component.html',
  styleUrl: './faculties.component.css'
})
export class FacultiesComponent implements OnInit{
  faculties: Faculty[] = [];

  constructor(private facultyService: FacultyService, private router: Router){}

  ngOnInit(): void {
    this.facultyService.getAll().subscribe(x => {
      this.faculties = x;
    })
  }

  truncateDescription(description: string, maxLength: number = 110): string{
    if(description.length > maxLength){
      return description.substring(0, maxLength) + '...';
    }
    return description;
  }

  viewDetails(faculty: Faculty){
    this.router.navigate([`faculty/${faculty.facultyCode}`])
  }

}
