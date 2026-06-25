import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NgFor } from '@angular/common';
import { HeaderComponent } from "./components/layout/header/header.component";
import { FooterComponent } from "./components/layout/footer/footer.component";
import { University } from './model/academic/univerzitet';
import { UniversityService } from './services/univerzitet.service';
import { MenubarModule } from 'primeng/menubar';
import { FacultyService } from './services/fakultet.service';
import { Faculty } from './model/academic/fakultet';
import { MenuBarComponent } from "./components/layout/menu-bar/menu-bar.component";
import { Title } from '@angular/platform-browser';
import { StudyProgramService } from './services/studijski-program.service';
import { StudyProgram } from './model/academic/studijskiProgram';
import { TabViewModule } from 'primeng/tabview';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgFor, HeaderComponent, FooterComponent, MenubarModule, MenuBarComponent, TabViewModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = "LMS";

  university: University = {
    name: '',
    foundingDate: new Date(),
    contact: '',
    description: '',
    image: '',
    address: '',
    rectorate: {
      name: '',
      contact: '',
      image: '',
      address: '',
      universities: [],
      rectorName: ''
    }
  };

  studyPrograms: StudyProgram[] = [];
  faculties: Faculty[] = [];

  constructor(
    private universityService: UniversityService,
    private facultyService: FacultyService,
    private titleService: Title,
    private studyProgramService: StudyProgramService
  ) {}

  ngOnInit(): void {
    this.titleService.setTitle("LMS University");
    this.getUniversity();
    this.getFaculties();
    this.getStudyPrograms();
  }

  getUniversity() {
    return this.universityService.getAll().subscribe(x => {
      this.university = x[0];
    });
  }

  getFaculties() {
    return this.facultyService.getAll().subscribe(f => {
      this.faculties = f;
    });
  }

  getStudyPrograms() {
    return this.studyProgramService.getAll().subscribe(x => {
      this.studyPrograms = x;
    });
  }
}
