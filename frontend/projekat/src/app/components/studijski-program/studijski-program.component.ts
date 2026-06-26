import { Component, OnInit } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { map, switchMap, tap } from 'rxjs';
import { FacultyService } from '../../services/fakultet.service';
import { StudyProgramService } from '../../services/studijski-program.service';
import { CourseService } from '../../services/predmet.service';
import { StudyProgram } from '../../model/academic/studijskiProgram';
import { Faculty } from '../../model/academic/fakultet';
import { Course } from '../../model/academic/predmet';

@Component({
  selector: 'app-studijski-program',
  standalone: true,
  imports: [NgFor, NgIf, RouterModule],
  templateUrl: './studijski-program.component.html',
  styleUrl: './studijski-program.component.css'
})
export class StudijskiProgramComponent implements OnInit {
  facultyCode: string | null = null;
  studyProgramCode: string | null = null;
  faculty: Faculty | null = null;
  studyProgram: StudyProgram | null = null;
  courses: Course[] = [];
  isLoading = true;
  selectedYear = 1;

  constructor(
    private route: ActivatedRoute,
    private facultyService: FacultyService,
    private studyProgramService: StudyProgramService,
    private courseService: CourseService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.pipe(
      switchMap(params => {
        this.facultyCode = params.get('sifraFakulteta');
        this.studyProgramCode = params.get('sifraSP');
        this.isLoading = true;

        if (!this.facultyCode) return [];
        return this.facultyService.getByCode(this.facultyCode).pipe(
          tap(f => { this.faculty = f; }),
          switchMap(() => {
            if (!this.studyProgramCode) return [];
            return this.studyProgramService.getByCode(this.studyProgramCode).pipe(
              tap(sp => {
                this.studyProgram = sp;
                this.selectedYear = 1;
              }),
              switchMap(() =>
                this.courseService.getAll().pipe(
                  map(all => all.filter(c =>
                    c.studyProgram?.programCode === this.studyProgramCode ||
                    c.studyProgram?.name === this.studyProgram?.name
                  ))
                )
              )
            );
          })
        );
      })
    ).subscribe({
      next: courses => {
        this.courses = courses as Course[];
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  get availableYears(): number[] {
    const years = new Set(this.courses.map(c => c.studyYear?.yearNumber ?? 1));
    if (years.size === 0) return [1];
    return Array.from(years).sort((a, b) => a - b);
  }

  getCoursesForYear(year: number): Course[] {
    if (this.availableYears.length <= 1 && !this.courses.some(c => c.studyYear)) {
      return this.courses;
    }
    return this.courses.filter(c => (c.studyYear?.yearNumber ?? 1) === year);
  }

  get totalEcts(): number {
    return this.courses.reduce((sum, c) => sum + (c.ects ?? 0), 0);
  }

  getDirectorName(): string {
    const d = this.studyProgram?.programDirector;
    if (!d) return '—';
    if (typeof d === 'string') return d;
    return `${d.firstName ?? ''} ${d.lastName ?? ''}`.trim() || '—';
  }

  getTeacherName(course: Course): string {
    const t = course.teacher;
    if (!t) return '—';
    return `${t.firstName ?? ''} ${t.lastName ?? ''}`.trim() || '—';
  }
}
