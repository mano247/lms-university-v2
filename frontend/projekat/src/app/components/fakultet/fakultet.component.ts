import { NO_ERRORS_SCHEMA } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { MenuModule } from 'primeng/menu';
import { Faculty } from '../../model/academic/fakultet';
import { FacultyService } from '../../services/fakultet.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DividerModule } from 'primeng/divider';
import { NgFor, NgIf } from '@angular/common';
import { StudyProgram } from '../../model/academic/studijskiProgram';
import { StudyProgramService } from '../../services/studijski-program.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-fakultet',
  standalone: true,
  imports: [MenuModule, DividerModule, NgIf, NgFor],
  templateUrl: './fakultet.component.html',
  styleUrl: './fakultet.component.css'
})
export class FakultetComponent implements OnInit {
  faculty: Faculty | undefined;
  facultyCode: string | null = null;
  studyPrograms: StudyProgram[] = [];

  constructor(
    private facultyService: FacultyService,
    private router: Router,
    private route: ActivatedRoute,
    private studyProgramService: StudyProgramService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.facultyCode = params.get('sifraFakulteta');
      this.loadFaculty();
    });
  }

  loadFaculty() {
    if (this.facultyCode) {
      this.facultyService.getByCode(this.facultyCode).subscribe(x => {
        this.faculty = x;
        this.getStudyPrograms();
      });
    }
  }

  getStudyPrograms() {
    return this.studyProgramService.getAll().subscribe(x => {
      this.studyPrograms = x.filter(program => program.faculty.name === this.faculty?.name);
    });
  }
}
