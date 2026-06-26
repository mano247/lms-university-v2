import { Component, OnInit } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { FacultyService } from '../../services/fakultet.service';
import { Faculty } from '../../model/academic/fakultet';

interface CardColor { bg: string; text: string; icon: string; }

@Component({
  selector: 'app-fakulteti',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './fakulteti.component.html',
  styleUrl: './fakulteti.component.css'
})
export class FakultetiComponent implements OnInit {
  faculties: Faculty[] = [];
  isLoading = true;
  searchQuery = '';

  private readonly CARD_COLORS: CardColor[] = [
    { bg: '#dbeafe', text: '#1e3a8a', icon: 'school' },
    { bg: '#fef3c7', text: '#78350f', icon: 'gavel' },
    { bg: '#dcfce7', text: '#14532d', icon: 'science' },
    { bg: '#fce7f3', text: '#831843', icon: 'brush' },
    { bg: '#ede9fe', text: '#4c1d95', icon: 'calculate' },
    { bg: '#fee2e2', text: '#7f1d1d', icon: 'health_and_safety' },
    { bg: '#e0f2fe', text: '#0c4a6e', icon: 'public' },
    { bg: '#fef9c3', text: '#713f12', icon: 'history_edu' },
    { bg: '#f0fdf4', text: '#052e16', icon: 'psychology' },
    { bg: '#fdf4ff', text: '#581c87', icon: 'biotech' },
  ];

  constructor(private facultyService: FacultyService, private router: Router) {}

  ngOnInit(): void {
    this.facultyService.getAll().subscribe({
      next: data => { this.faculties = data; this.isLoading = false; },
      error: () => { this.isLoading = false; }
    });
  }

  get filteredFaculties(): Faculty[] {
    const q = this.searchQuery.trim().toLowerCase();
    if (!q) return this.faculties;
    return this.faculties.filter(f =>
      f.name.toLowerCase().includes(q) || f.facultyCode.toLowerCase().includes(q)
    );
  }

  getFacultyInitials(name: string): string {
    return name.split(' ').filter(w => w.length > 2).slice(0, 2).map(w => w[0].toUpperCase()).join('');
  }

  getCardColor(index: number): CardColor {
    return this.CARD_COLORS[index % this.CARD_COLORS.length];
  }

  navigateToFaculty(code: string, event?: Event): void {
    event?.stopPropagation();
    this.router.navigate(['/fakultet', code]);
  }
}
