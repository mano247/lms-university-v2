import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FacultyService } from '../../services/fakultet.service';
import { StudyProgramService } from '../../services/studijski-program.service';
import { StudyProgram } from '../../model/academic/studijskiProgram';
import { Faculty } from '../../model/academic/fakultet';
import { DividerModule } from 'primeng/divider';
import { Course } from '../../model/academic/predmet';
import { CourseService } from '../../services/predmet.service';
import { NgFor } from '@angular/common';
import { map, switchMap, tap } from 'rxjs';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-studijski-program',
  standalone: true,
  imports: [DividerModule, RouterModule, NgFor],
  templateUrl: './studijski-program.component.html',
  styleUrl: './studijski-program.component.css'
})
export class StudijskiProgramComponent implements OnInit {
  facultyCode: string | null = null;
  studyProgramCode: string | null = null;
  studyProgram: StudyProgram | null = null;
  courses: Course[] = [];

  faculty: Faculty = {
    name: '',
    contact: '',
    description: '',
    dean: '',
    image: '',
    address: '',
    facultyCode: ''
  };

  constructor(
    private route: ActivatedRoute,
    private studyProgramService: StudyProgramService,
    private courseService: CourseService,
    private facultyService: FacultyService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.pipe(
      switchMap(params => {
        this.facultyCode = params.get('sifraFakulteta');
        this.studyProgramCode = params.get('sifraSP');

        if (this.facultyCode) {
          return this.facultyService.getByCode(this.facultyCode).pipe(
            tap(faculty => this.faculty = faculty),
            switchMap(() => {
              if (this.studyProgramCode) {
                return this.studyProgramService.getByCode(this.studyProgramCode).pipe(
                  tap(studyProgram => {
                    this.studyProgram = studyProgram;
                    this.getCourses();
                  })
                );
              } else {
                return [];
              }
            })
          );
        } else {
          return [];
        }
      })
    ).subscribe();
  }

  getFaculty() {
    if (this.facultyCode !== null) {
      this.facultyService.getByCode(this.facultyCode).subscribe(x => {
        this.faculty = x;
      });
    }
  }

  getStudyProgram() {
    if (this.studyProgramCode !== null) {
      this.studyProgramService.getByCode(this.studyProgramCode).subscribe(x => {
        this.studyProgram = x;
      });
    }
  }

  getCourses() {
    this.courseService.getAll().pipe(
      map(courses => courses.filter(c => c.studyProgram.name === this.studyProgram?.name))
    ).subscribe(filteredCourses => {
      this.courses = filteredCourses;
    });
  }
}
