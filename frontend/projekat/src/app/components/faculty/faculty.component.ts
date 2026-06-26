import { Component, OnInit } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Faculty } from '../../model/academic/faculty';
import { FacultyService } from '../../services/faculty.service';
import { StudyProgram } from '../../model/academic/study-program';
import { StudyProgramService } from '../../services/study-program.service';

@Component({
  selector: 'app-faculty',
  standalone: true,
  imports: [NgFor, NgIf, RouterModule],
  templateUrl: './faculty.component.html',
  styleUrl: './faculty.component.css'
})
export class FacultyComponent implements OnInit {
  faculty: Faculty | null = null;
  studyPrograms: StudyProgram[] = [];
  isLoading = true;

  constructor(
    private facultyService: FacultyService,
    private studyProgramService: StudyProgramService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const code = params.get('facultyCode');
      if (code) this.loadFaculty(code);
    });
  }

  private loadFaculty(code: string): void {
    this.isLoading = true;
    this.facultyService.getByCode(code).subscribe({
      next: f => {
        this.faculty = f;
        this.loadPrograms();
      },
      error: () => { this.isLoading = false; }
    });
  }

  private loadPrograms(): void {
    this.studyProgramService.getAll().subscribe({
      next: all => {
        this.studyPrograms = all.filter(
          p => p.faculty?.facultyCode === this.faculty?.facultyCode
        );
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  getProgramBadge(program: StudyProgram): { label: string; cls: string } {
    const name = (program.name + ' ' + program.programCode).toLowerCase();
    if (name.includes('phd') || name.includes('doct')) return { label: 'Doctorate', cls: 'badge-gold' };
    if (name.includes('master') || name.includes(' ma ') || name.includes(' msc') || name.includes('-ma') || name.includes('postgrad')) return { label: 'Postgraduate', cls: 'badge-gold' };
    return { label: 'Undergraduate', cls: 'badge-blue' };
  }

  getDeanName(): string {
    const dean = this.faculty?.dean;
    if (!dean) return '—';
    if (typeof dean === 'string') return dean;
    return `${dean.firstName ?? ''} ${dean.lastName ?? ''}`.trim() || '—';
  }
}
