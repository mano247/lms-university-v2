import { Component, OnInit } from '@angular/core';
import { CourseMaterial } from '../../model/academic/teaching-material';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CourseService } from '../../services/course.service';
import { Course } from '../../model/academic/course';
import { FacultyService } from '../../services/faculty.service';
import { StudyProgramService } from '../../services/study-program.service';

@Component({
  selector: 'app-teaching-material',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './teaching-material.component.html',
  styleUrl: './teaching-material.component.css',
})
export class TeachingMaterialComponent implements OnInit {
  facultyCode: string | null = null;
  studyProgramCode: string | null = null;
  courseCode: string | null = null;
  materialTitle: string | null = null;

  facultyName: string | null = null;
  programName: string | null = null;
  course: Course | null = null;
  material: CourseMaterial | null = null;
  isLoading = true;

  constructor(
    private route: ActivatedRoute,
    private facultyService: FacultyService,
    private courseService: CourseService,
    private studyProgramService: StudyProgramService,
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.facultyCode = params.get('facultyCode');
      this.studyProgramCode = params.get('studyProgramCode');
      this.courseCode = params.get('courseCode');
      this.materialTitle = params.get('materialTitle');
      this.loadAll();
    });
  }

  private loadAll(): void {
    if (this.facultyCode) {
      this.facultyService.getByCode(this.facultyCode).subscribe({ next: x => (this.facultyName = x.name) });
    }
    if (this.studyProgramCode) {
      this.studyProgramService.getByCode(this.studyProgramCode).subscribe({ next: x => (this.programName = x.name) });
    }
    if (this.courseCode) {
      this.courseService.getByCode(this.courseCode).subscribe({
        next: x => {
          this.course = x;
          this.material = this.materialTitle
            ? (x.teachingMaterials.find((m: CourseMaterial) => m.title === this.materialTitle) ?? null)
            : null;
          this.isLoading = false;
        },
        error: () => (this.isLoading = false),
      });
    }
  }
}
