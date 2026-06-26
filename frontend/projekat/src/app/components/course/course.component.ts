import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { StudyProgramService } from '../../services/study-program.service';
import { StudyProgram } from '../../model/academic/study-program';
import { CourseService } from '../../services/course.service';
import { Course } from '../../model/academic/course';
import { DividerModule } from 'primeng/divider';
import { Faculty } from '../../model/academic/faculty';
import { FacultyService } from '../../services/faculty.service';
import { NgFor } from '@angular/common';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-course',
  standalone: true,
  imports: [RouterModule, DividerModule, NgFor],
  templateUrl: './course.component.html',
  styleUrl: './course.component.css'
})
export class CourseComponent implements OnInit{
  facultyCode: string | null = null;
  programCode: string | null = null;
  courseCode: string | null = null;

  faculty: Faculty | null = null;
  course: Course | null = null;
  studyProgram: StudyProgram | null = null;

  constructor(
    private route: ActivatedRoute,
    private studyProgramService: StudyProgramService,
    private facultyService: FacultyService,
    private courseService: CourseService
  ){}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.facultyCode = params.get('facultyCode');
      this.programCode = params.get('programCode');
      this.courseCode = params.get('courseCode');

      this.loadCourse();
      this.loadProgram();
      this.loadFaculty();
    });
  }

  loadCourse(){
    if(this.courseCode !== null){
      this.courseService.getBySifra(this.courseCode).subscribe(x=>{
        this.course = x;
      });
    }
  }

  loadFaculty(){
    if(this.facultyCode !== null){
      this.facultyService.getBySifra(this.facultyCode).subscribe(x=>{
        this.faculty = x;
      });
    }
  }

  loadProgram(){
    if(this.programCode !== null){
      this.studyProgramService.getBySifra(this.programCode).subscribe(x=>{
        this.studyProgram = x;
      });
    }
  }

  formatSyllabus(syllabus: string | undefined): string{
    if(!syllabus) return '';
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
    if(!date) return '';
    const d = new Date(date);
    const year = d.getUTCFullYear();
    const month = (d.getUTCMonth() + 1).toString().padStart(2, '0');
    const day = d.getUTCDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}
