import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GlobalNotificationsService } from '../../services/global-announcements.service';

type FilterType = 'all' | 'global' | 'course';

@Component({
  selector: 'app-global-announcements',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './global-announcements.component.html',
  styleUrl: './global-announcements.component.css',
})
export class GlobalAnnouncementsComponent implements OnInit {
  announcements: any[] = [];
  filtered: any[] = [];
  activeFilter: FilterType = 'all';
  isLoading = true;

  readonly filters: { value: FilterType; label: string }[] = [
    { value: 'all',    label: 'All' },
    { value: 'global', label: 'Global' },
    { value: 'course', label: 'Course' },
  ];

  constructor(private globalNotificationsService: GlobalNotificationsService) {}

  ngOnInit(): void {
    this.globalNotificationsService.getAll().subscribe({
      next: (data) => {
        this.announcements = data;
        this.applyFilter();
        this.isLoading = false;
      },
      error: () => (this.isLoading = false),
    });
  }

  setFilter(f: FilterType): void {
    this.activeFilter = f;
    this.applyFilter();
  }

  private applyFilter(): void {
    if (this.activeFilter === 'all') {
      this.filtered = this.announcements;
    } else if (this.activeFilter === 'global') {
      this.filtered = this.announcements.filter(a => !a.course && !a.courseId);
    } else {
      this.filtered = this.announcements.filter(a => a.course || a.courseId);
    }
  }

  formatDate(d: any): string {
    if (!d) return '—';
    const date = new Date(d);
    if (isNaN(date.getTime())) return '—';
    return date.toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' });
  }

  announcementType(a: any): string {
    return a.course || a.courseId ? 'Course' : 'Global';
  }
}
