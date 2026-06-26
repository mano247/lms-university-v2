import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FacultyService } from '../../services/faculty.service';
import { StudyProgramService } from '../../services/study-program.service';
import { StudyProgram } from '../../model/academic/study-program';
import { Faculty } from '../../model/academic/faculty';
import { DividerModule } from 'primeng/divider';
import { Course } from '../../model/academic/course';
import { CourseService } from '../../services/course.service';
import { NgFor } from '@angular/common';
import { map, switchMap, tap } from 'rxjs';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-study-program',
  standalone: true,
  imports: [DividerModule, RouterModule, NgFor],
  templateUrl: './study-program.component.html',
  styleUrl: './study-program.component.css'
})
export class StudyProgramComponent implements OnInit {
  facultyCode: string | null = null;
  programCode: string | null = null;
  studyProgram: StudyProgram | null = null;
  courses: Course[] = [];

  faculty: Faculty = {
    facultyCode: '',
    name: '',
    contact: '',
    description: '',
    image: '',
    address: ''
  };

  constructor(
    private route: ActivatedRoute,
    private spService: StudyProgramService,
    private courseService: CourseService,
    private facultyService: FacultyService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.pipe(
      switchMap(params => {
        this.facultyCode = params.get('facultyCode');
        this.programCode = params.get('programCode');

        if (this.facultyCode) {
          return this.facultyService.getBySifra(this.facultyCode).pipe(
            tap(faculty => { this.faculty = faculty; }),
            switchMap(() => {
              if (this.programCode) {
                return this.spService.getBySifra(this.programCode).pipe(
                  tap(sp => {
                    this.studyProgram = sp;
                    this.loadCourses();
                  })
                );
              }
              return [];
            })
          );
        }
        return [];
      })
    ).subscribe();
  }

  loadCourses() {
    this.courseService.getAll().pipe(
      map(courses => courses.filter(c => c.studyProgram?.name === this.studyProgram?.name))
    ).subscribe(filtered => {
      this.courses = filtered;
    });
  }
}
