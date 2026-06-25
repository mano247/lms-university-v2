import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DataViewModule } from 'primeng/dataview';
import { NgFor } from '@angular/common';
import { DividerModule } from 'primeng/divider';
import { Course } from '../../../model/academic/predmet';
import { StudentService } from '../../../services/studenti.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-moji-predmeti',
  standalone: true,
  imports: [NgFor, DataViewModule, DividerModule],
  templateUrl: './moji-predmeti.component.html',
  styleUrl: './moji-predmeti.component.css'
})
export class MojiPredmetiComponent implements OnInit {
  myCourses: Course[] = [];
  passed: Course[] = [];
  failed: Course[] = [];

  constructor(private studentService: StudentService) {}

  ngOnInit(): void {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      const id = parsedUser.id;
      this.getMyCourses(id);
    }
  }

  getMyCourses(id: number) {
    this.studentService.getActiveCourses(id).subscribe(x => {
      this.myCourses = x;
    });
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
