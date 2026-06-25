import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { StudyProgramService } from '../../services/studijski-program.service';
import { StudyProgram } from '../../model/academic/studijskiProgram';
import { CourseService } from '../../services/predmet.service';
import { Course } from '../../model/academic/predmet';
import { DividerModule } from 'primeng/divider';
import { Faculty } from '../../model/academic/fakultet';
import { FacultyService } from '../../services/fakultet.service';
import { CourseMaterialService } from '../../services/nastavni-materijal.service';
import { NgFor } from '@angular/common';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-predmet',
  standalone: true,
  imports: [RouterModule, DividerModule, NgFor],
  templateUrl: './predmet.component.html',
  styleUrl: './predmet.component.css'
})
export class PredmetComponent implements OnInit {
  facultyCode: string | null = null;
  studyProgramCode: string | null = null;
  courseCode: string | null = null;

  faculty: Faculty | null = null;
  course: Course | null = null;
  studyProgram: StudyProgram | null = null;

  constructor(
    private route: ActivatedRoute,
    private courseMaterialService: CourseMaterialService,
    private studyProgramService: StudyProgramService,
    private facultyService: FacultyService,
    private courseService: CourseService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.facultyCode = params.get('sifraFakulteta');
      this.studyProgramCode = params.get('sifraSP');
      this.courseCode = params.get('sifraPredmeta');

      this.getCourse();
      this.getStudyProgram();
      this.getFaculty();
    });
  }

  getCourse() {
    if (this.courseCode !== null) {
      this.courseService.getByCode(this.courseCode).subscribe(x => {
        this.course = x;
      });
    }
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

  formatSyllabus(syllabus: string | undefined): string {
    if (!syllabus) return 'syllabus';

    let formatted = syllabus
      .replace(/"/g, '')
      .replace(/\n\n/g, '</p><p><br>')
      .replace(/\n/g, '<br>');

    formatted = formatted.replace(/\[([^\]]+)\]/g, (match, p1) => {
      const items = p1.split(';').map((item: string) => `<p>${item.trim()}</p>`).join('');
      return `<div>${items}</div>`;
    });

    return formatted;
  }

  formatDate(date: Date | undefined): string {
    if (date) {
      const d = new Date(date);
      const year = d.getUTCFullYear();
      const month = (d.getUTCMonth() + 1).toString().padStart(2, '0');
      const day = d.getUTCDate().toString().padStart(2, '0');
      return `${year}-${month}-${day}`;
    }
    return '';
  }
}
