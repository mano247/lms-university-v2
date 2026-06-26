import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { CourseMaterial } from '../../model/academic/course-material';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CourseMaterialService } from '../../services/course-material.service';
import { DividerModule } from 'primeng/divider';
import { NgFor } from '@angular/common';
import { FacultyService } from '../../services/faculty.service';
import { CourseService } from '../../services/course.service';
import { StudyProgramService } from '../../services/study-program.service';
import { Course } from '../../model/academic/course';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-course-material',
  standalone: true,
  imports: [RouterModule, DividerModule, NgFor],
  templateUrl: './course-material.component.html',
  styleUrl: './course-material.component.css'
})
export class CourseMaterialComponent implements OnInit{
  facultyCode: string | null = null;
  programCode: string | null = null;
  courseCode: string | null = null;
  materialName: string | null = null;

  facultyName: string | null = null;
  programName: string | null = null;
  course: Course = {
    courseCode: '',
    name: '',
    ects: 0,
    startDate: new Date(),
    endDate: new Date(),
    description: '',
    teachingMaterials: [],
    examAttempts: [],
    teacher: undefined,
  };

  material: CourseMaterial | null = null;

  constructor(
    private route: ActivatedRoute,
    private facultyService: FacultyService,
    private courseService: CourseService,
    private studyProgramService: StudyProgramService
  ){}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.facultyCode = params.get('facultyCode');
      this.programCode = params.get('programCode');
      this.courseCode = params.get('courseCode');
      this.materialName = params.get('materialName');
    });
    this.loadFaculty();
    this.loadProgram();
    this.loadCourse();
  }

  loadFaculty(){
    if(this.facultyCode !== null){
      this.facultyService.getBySifra(this.facultyCode).subscribe(x=>{
        this.facultyName = x.name;
      });
    }
  }

  loadCourse(){
    if(this.courseCode !== null){
      this.courseService.getBySifra(this.courseCode).subscribe(x=>{
        this.course = x;
        if (this.materialName && this.course.teachingMaterials) {
          this.material = this.course.teachingMaterials.find(m => m.title === this.materialName) || null;
        } else {
          this.material = null;
        }
      });
    }
  }

  loadProgram(){
    if(this.programCode !== null){
      this.studyProgramService.getBySifra(this.programCode).subscribe(x=>{
        this.programName = x.name;
      });
    }
  }
}
