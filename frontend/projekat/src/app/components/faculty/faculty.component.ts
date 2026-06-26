import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { Faculty } from '../../model/academic/faculty';
import { FacultyService } from '../../services/faculty.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { DividerModule } from 'primeng/divider';
import { NgFor, NgIf } from '@angular/common';
import { StudyProgram } from '../../model/academic/study-program';
import { StudyProgramService } from '../../services/study-program.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-faculty',
  standalone: true,
  imports: [DividerModule, NgIf, NgFor, RouterModule],
  templateUrl: './faculty.component.html',
  styleUrl: './faculty.component.css'
})
export class FacultyComponent implements OnInit{
  faculty: Faculty | undefined;
  facultyCode: string | null = null;
  studyPrograms: StudyProgram[] = [];

  constructor(
    private facultyService: FacultyService,
    private router: Router,
    private route: ActivatedRoute,
    private spService: StudyProgramService
  ){}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.facultyCode = params.get('facultyCode');
      this.loadFaculty();
    });
  }

  loadFaculty(){
    if(this.facultyCode){
      this.facultyService.getBySifra(this.facultyCode).subscribe(x=>{
        this.faculty = x;
        this.loadPrograms();
      });
    }
  }

  loadPrograms(){
    this.spService.getAll().subscribe(x => {
      this.studyPrograms = x.filter(program => program.faculty?.name === this.faculty?.name);
    });
  }
}
