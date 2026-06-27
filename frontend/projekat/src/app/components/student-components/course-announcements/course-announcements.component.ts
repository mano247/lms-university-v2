import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Notification } from '../../../model/announcement';
import { GlobalNotification } from '../../../model/global-announcement';
import { NotificationService } from '../../../services/announcement.service';
import { GlobalNotificationsService } from '../../../services/global-announcements.service';

type Filter = 'all' | 'global' | 'course';

@Component({
  selector: 'app-course-announcements',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './course-announcements.component.html',
  styleUrl: './course-announcements.component.css',
})
export class CourseAnnouncementsComponent implements OnInit {
  courseAnnouncements: Notification[] = [];
  globalAnnouncements: GlobalNotification[] = [];
  isLoading = true;
  filter: Filter = 'all';

  constructor(
    private notificationService: NotificationService,
    private globalService: GlobalNotificationsService,
  ) {}

  ngOnInit(): void {
    let pending = 2;
    const done = () => { if (--pending === 0) this.isLoading = false; };

    this.notificationService.getAll().subscribe({
      next: items => { this.courseAnnouncements = items ?? []; done(); },
      error: () => done(),
    });
    this.globalService.getAll().subscribe({
      next: items => { this.globalAnnouncements = items ?? []; done(); },
      error: () => done(),
    });
  }

  get filtered(): Array<{ type: 'global' | 'course'; title: string; content: string; date: Date | null; badge?: string }> {
    const result: Array<{ type: 'global' | 'course'; title: string; content: string; date: Date | null; badge?: string }> = [];

    if (this.filter !== 'course') {
      for (const g of this.globalAnnouncements) {
        result.push({ type: 'global', title: g.title, content: g.content, date: g.startDate ? new Date(g.startDate) : null });
      }
    }
    if (this.filter !== 'global') {
      for (const n of this.courseAnnouncements) {
        result.push({ type: 'course', title: n.title, content: n.content, date: n.startDate ? new Date(n.startDate) : null, badge: (n.course as any)?.name });
      }
    }

    return result.sort((a, b) => (b.date?.getTime() ?? 0) - (a.date?.getTime() ?? 0));
  }

  setFilter(f: Filter): void { this.filter = f; }

  get globalCount(): number { return this.globalAnnouncements.length; }
  get courseCount(): number { return this.courseAnnouncements.length; }

  formatDate(d: Date | null): string {
    if (!d) return '';
    return d.toLocaleDateString('en-GB', { year: 'numeric', month: 'short', day: 'numeric' });
  }
}
