import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { GlobalNotification } from '../../model/gObavestenje';
import { DataViewModule } from 'primeng/dataview';
import { NgFor } from '@angular/common';
import { DividerModule } from 'primeng/divider';
import { GlobalNotificationsService } from '../../services/globalna-obavestenja.service';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { FormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-obavestenja',
  standalone: true,
  imports: [NgFor, DataViewModule, DividerModule, ButtonModule, DialogModule, FormsModule, CalendarModule, ToastModule],
  templateUrl: './obavestenja.component.html',
  styleUrl: './obavestenja.component.css',
  providers: [MessageService]
})
export class ObavestenjaComponent implements OnInit {
  announcements: GlobalNotification[] = [];
  displayDialog: boolean = false;

  newAnnouncement: GlobalNotification = {
    date: new Date(),
    content: '',
    title: '',
    image: '',
    startDate: new Date(),
    endDate: new Date()
  };

  constructor(
    private globalNotificationsService: GlobalNotificationsService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.getAnnouncements();
  }

  getAnnouncements() {
    this.globalNotificationsService.getAll().subscribe(x => {
      this.announcements = x;
    });
  }

  formatDate(date: any): string {
    let d: Date;

    if (typeof date === 'string') {
      d = new Date(date);
    } else if (date instanceof Date) {
      d = date;
    } else {
      return '';
    }

    if (isNaN(d.getTime())) {
      return '';
    }

    const hours = d.getUTCHours().toString().padStart(2, '0');
    const minutes = d.getUTCMinutes().toString().padStart(2, '0');
    const day = d.getUTCDate().toString().padStart(2, '0');
    const month = (d.getUTCMonth() + 1).toString().padStart(2, '0');
    const year = d.getUTCFullYear();

    return `${hours}:${minutes}, ${day}-${month}-${year}`;
  }

  openDialog() {
    this.displayDialog = true;
  }

  addAnnouncement() {
    this.globalNotificationsService.create(this.newAnnouncement).subscribe({
      next: () => {
        this.getAnnouncements();
        this.resetForm();
        this.closeDialog();
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Announcement added successfully.'
        });
      },
      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'An error occurred while adding the announcement.'
        });
      }
    });
  }

  closeDialog() {
    this.displayDialog = false;
  }

  resetForm() {
    this.newAnnouncement = {
      date: new Date(),
      content: '',
      title: '',
      image: '',
      startDate: new Date(),
      endDate: new Date()
    };
  }
}
