import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { StudyProgramService } from '../../services/study-program.service';
import { StudyProgram } from '../../model/academic/study-program';
import { CourseService } from '../../services/course.service';
import { Course } from '../../model/academic/course';
import { Faculty } from '../../model/academic/faculty';
import { FacultyService } from '../../services/faculty.service';

@Component({
  selector: 'app-course',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './course.component.html',
  styleUrl: './course.component.css',
})
export class CourseComponent implements OnInit {
  facultyCode: string | null = null;
  studyProgramCode: string | null = null;
  courseCode: string | null = null;

  faculty: Faculty | null = null;
  course: Course | null = null;
  studyProgram: StudyProgram | null = null;
  isLoading = true;

  constructor(
    private route: ActivatedRoute,
    private studyProgramService: StudyProgramService,
    private facultyService: FacultyService,
    private courseService: CourseService,
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.facultyCode = params.get('facultyCode');
      this.studyProgramCode = params.get('studyProgramCode');
      this.courseCode = params.get('courseCode');
      this.isLoading = true;
      this.loadAll();
    });
  }

  private loadAll(): void {
    if (this.courseCode) {
      this.courseService.getByCode(this.courseCode).subscribe({
        next: x => { this.course = x; this.isLoading = false; },
        error: () => (this.isLoading = false),
      });
    }
    if (this.facultyCode) {
      this.facultyService.getByCode(this.facultyCode).subscribe({
        next: x => (this.faculty = x),
      });
    }
    if (this.studyProgramCode) {
      this.studyProgramService.getByCode(this.studyProgramCode).subscribe({
        next: x => (this.studyProgram = x),
      });
    }
  }

  formatSyllabus(syllabus: string | undefined): string {
    if (!syllabus) return '';
    return syllabus
      .replace(/"/g, '')
      .replace(/\n\n/g, '</p><p>')
      .replace(/\n/g, '<br>')
      .replace(/\[([^\]]+)\]/g, (_m, p1) =>
        p1.split(';').map((item: string) => `<p>${item.trim()}</p>`).join(''),
      );
  }

  formatDate(date: Date | undefined): string {
    if (!date) return '—';
    const d = new Date(date);
    return isNaN(d.getTime()) ? '—' : d.toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' });
  }

  get teacherName(): string {
    const t = this.course?.teacher as any;
    if (!t) return '—';
    return [t.firstName, t.lastName].filter(Boolean).join(' ') || t.username || '—';
  }
}
