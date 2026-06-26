import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { CourseMaterial } from '../../model/academic/teaching-material';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CourseMaterialService } from '../../services/teaching-material.service';
import { DividerModule } from 'primeng/divider';
import { NgFor } from '@angular/common';
import { FacultyService } from '../../services/faculty.service';
import { CourseService } from '../../services/course.service';
import { StudyProgramService } from '../../services/study-program.service';
import { Course } from '../../model/academic/course';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-teaching-material',
  standalone: true,
  imports: [RouterModule, DividerModule, NgFor],
  templateUrl: './teaching-material.component.html',
  styleUrl: './teaching-material.component.css'
})
export class TeachingMaterialComponent implements OnInit {
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
      this.facultyCode = params.get('facultyCode');
      this.studyProgramCode = params.get('studyProgramCode');
      this.courseCode = params.get('courseCode');
      this.materialTitle = params.get('materialTitle');
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
