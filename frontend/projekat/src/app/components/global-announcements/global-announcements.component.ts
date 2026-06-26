import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DataViewModule } from 'primeng/dataview';
import { NgFor } from '@angular/common';
import { DividerModule } from 'primeng/divider';
import { GlobalNotification } from '../../model/global-notification';
import { DatePipe } from '@angular/common';
import { GlobalAnnouncementsService } from '../../services/global-announcements.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-global-announcements',
  standalone: true,
  imports: [NgFor, DataViewModule, DividerModule],
  templateUrl: './global-announcements.component.html',
  styleUrl: './global-announcements.component.css',
  providers: [DatePipe]
})
export class GlobalAnnouncementsComponent implements OnInit{
  announcements: GlobalNotification[] = [];

  constructor(private announcementService: GlobalAnnouncementsService, private datePipe: DatePipe){}

  ngOnInit(): void {
    this.loadAnnouncements();
  }

  loadAnnouncements(){
    this.announcementService.getAll().subscribe(x=>{
      this.announcements = x;
    });
  }

  formatDate(date: Date | undefined): string {
    return this.datePipe.transform(date ?? null, 'HH:mm, d. MMM yyyy.') ?? '';
  }
}
