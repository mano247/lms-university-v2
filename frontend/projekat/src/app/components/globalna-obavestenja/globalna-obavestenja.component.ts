import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DataViewModule } from 'primeng/dataview';
import { NgFor } from '@angular/common';
import { DividerModule } from 'primeng/divider';
import { GlobalNotification } from '../../model/gObavestenje';
import { DatePipe } from '@angular/common';
import { GlobalNotificationsService } from '../../services/globalna-obavestenja.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-globalna-obavestenja',
  standalone: true,
  imports: [NgFor, DataViewModule, DividerModule],
  templateUrl: './globalna-obavestenja.component.html',
  styleUrl: './globalna-obavestenja.component.css',
  providers: [DatePipe]
})
export class GlobalnaObavestenjaComponent implements OnInit {
  announcements: GlobalNotification[] = [];

  constructor(
    private globalNotificationsService: GlobalNotificationsService,
    private datePipe: DatePipe
  ) {}

  ngOnInit(): void {
    this.getAnnouncements();
  }

  getAnnouncements() {
    this.globalNotificationsService.getAll().subscribe(x => {
      this.announcements = x;
    });
  }

  formatDate(date: Date): string {
    const formatted = this.datePipe.transform(date, 'HH:mm, d. MMM yyyy.');
    return formatted ?? '';
  }
}
