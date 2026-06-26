import { Component, OnInit } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { FacultyService } from '../../services/fakultet.service';
import { StudyProgramService } from '../../services/studijski-program.service';
import { Faculty } from '../../model/academic/fakultet';

interface CardColor { bg: string; text: string; icon: string; }

@Component({
  selector: 'app-univerzitet',
  standalone: true,
  imports: [NgFor, NgIf, RouterModule],
  templateUrl: './univerzitet.component.html',
  styleUrl: './univerzitet.component.css'
})
export class UniverzitetComponent implements OnInit {
  faculties: Faculty[] = [];
  studyProgramCount = 0;
  isLoading = true;

  private readonly CARD_COLORS: CardColor[] = [
    { bg: '#dbeafe', text: '#1e3a8a', icon: 'school' },
    { bg: '#fef3c7', text: '#78350f', icon: 'gavel' },
    { bg: '#d1fae5', text: '#064e3b', icon: 'biotech' },
    { bg: '#ede9fe', text: '#4c1d95', icon: 'engineering' },
    { bg: '#fce7f3', text: '#831843', icon: 'palette' },
    { bg: '#e0f2fe', text: '#0c4a6e', icon: 'monitoring' },
    { bg: '#fef9c3', text: '#713f12', icon: 'history_edu' },
    { bg: '#f0fdf4', text: '#14532d', icon: 'eco' },
    { bg: '#fff1f2', text: '#881337', icon: 'medical_services' },
    { bg: '#f0f9ff', text: '#0369a1', icon: 'computer' },
  ];

  constructor(
    private facultyService: FacultyService,
    private studyProgramService: StudyProgramService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.facultyService.getAll().subscribe({
      next: f => {
        this.faculties = f;
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });

    this.studyProgramService.getAll().subscribe({
      next: sp => { this.studyProgramCount = sp.length; },
      error: () => {}
    });
  }

  getCardColor(index: number): CardColor {
    return this.CARD_COLORS[index % this.CARD_COLORS.length];
  }

  getFacultyInitials(name: string): string {
    return name.split(' ')
      .filter(w => w.length > 2)
      .slice(0, 2)
      .map(w => w[0])
      .join('')
      .toUpperCase() || name.substring(0, 2).toUpperCase();
  }

  navigateToFaculty(code: string, event: Event) {
    event.stopPropagation();
    this.router.navigate(['/fakultet', code]);
  }
}
