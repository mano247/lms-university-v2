import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { map, switchMap, tap } from 'rxjs';
import { FacultyService } from '../../services/faculty.service';
import { StudyProgramService } from '../../services/study-program.service';
import { CourseService } from '../../services/course.service';
import { StudyProgram } from '../../model/academic/study-program';
import { Faculty } from '../../model/academic/faculty';
import { Course } from '../../model/academic/course';

@Component({
  selector: 'app-study-program',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './study-program.component.html',
  styleUrl: './study-program.component.css'
})
export class StudyProgramComponent implements OnInit {
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
        this.facultyCode = params.get('facultyCode');
        this.studyProgramCode = params.get('studyProgramCode');
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

  getDegreeType(): string {
    const name = (this.studyProgram?.name ?? '').toLowerCase();
    const code = (this.studyProgram?.programCode ?? '').toLowerCase();
    if (name.includes('phd') || name.includes('doctor') || code.includes('phd')) return 'Doctor of Philosophy';
    if (name.includes('master') || name.includes(' msc') || name.includes(' ma ')
        || code.includes('-ma') || code.includes('-msc') || name.includes('postgrad')) return 'Master of Science';
    return 'Bachelor of Arts';
  }

  get totalSemesters(): number {
    return this.availableYears.length * 2;
  }
}
