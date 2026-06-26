import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { CourseMaterial } from '../../model/academic/nastavniMaterijal';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CourseMaterialService } from '../../services/nastavni-materijal.service';
import { DividerModule } from 'primeng/divider';
import { NgFor } from '@angular/common';
import { FacultyService } from '../../services/fakultet.service';
import { CourseService } from '../../services/predmet.service';
import { StudyProgramService } from '../../services/studijski-program.service';
import { Course } from '../../model/academic/predmet';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-nastavni-materijal',
  standalone: true,
  imports: [RouterModule, DividerModule, NgFor],
  templateUrl: './nastavni-materijal.component.html',
  styleUrl: './nastavni-materijal.component.css'
})
export class NastavniMaterijalComponent implements OnInit {
  facultyCode: string | null = null;
  studyProgramCode: string | null = null;
  courseCode: string | null = null;
  materialTitle: string | null = null;

  facultyName: string | null = null;
  programName: string | null = null;
  course: Course = {
    syllabus: '',
    name: '',
    ects: 0,
    startDate: new Date(),
    endDate: new Date(),
    description: '',
    teachingMaterials: [],
    examAttempts: [],
    teacher: undefined,
    students: [],
    announcements: [],
    studyProgram: '',
    courseCode: ''
  };

  material: CourseMaterial | null = null;

  constructor(
    private route: ActivatedRoute,
    private facultyService: FacultyService,
    private courseService: CourseService,
    private studyProgramService: StudyProgramService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.facultyCode = params.get('sifraFakulteta');
      this.studyProgramCode = params.get('sifraSP');
      this.courseCode = params.get('sifraPredmeta');
      this.materialTitle = params.get('nMaterijalNaziv');
    });
    this.getFaculty();
    this.getStudyProgram();
    this.getCourse();
  }

  getFaculty() {
    if (this.facultyCode !== null) {
      this.facultyService.getByCode(this.facultyCode).subscribe(x => {
        this.facultyName = x.name;
      });
    }
  }

  getCourse() {
    if (this.courseCode !== null) {
      this.courseService.getByCode(this.courseCode).subscribe(x => {
        this.course = x;
        if (this.materialTitle) {
          this.material = this.course.teachingMaterials.find(m => m.title === this.materialTitle) || null;
        } else {
          this.material = null;
        }
      });
    }
  }

  getStudyProgram() {
    if (this.studyProgramCode !== null) {
      this.studyProgramService.getByCode(this.studyProgramCode).subscribe(x => {
        this.programName = x.name;
      });
    }
  }
}
