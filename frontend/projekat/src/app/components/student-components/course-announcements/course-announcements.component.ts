import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DividerModule } from 'primeng/divider';
import { DataViewModule } from 'primeng/dataview';
import { NgFor, NgIf } from '@angular/common';
import { Notification } from '../../../model/notification';
import { DropdownModule } from 'primeng/dropdown';
import { Course } from '../../../model/academic/course';
import { CourseService } from '../../../services/course.service';
import { CourseAnnouncementService } from '../../../services/course-announcement.service';
import { ButtonModule } from 'primeng/button';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { StudentService } from '../../../services/student.service';
import { LoginService } from '../../../services/auth/login.service';
import { FormsModule } from '@angular/forms';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-course-announcements',
  standalone: true,
  imports: [NgFor, DataViewModule, DividerModule, DropdownModule, ButtonModule, ConfirmPopupModule, ToastModule, DialogModule, NgIf, FormsModule],
  templateUrl: './course-announcements.component.html',
  styleUrl: './course-announcements.component.css',
  providers: [ConfirmationService, MessageService]
})
export class CourseAnnouncementsComponent implements OnInit {
  visible: boolean = false;

  activeCourses: Course[] | undefined;
  announcements: Notification[] = [];
  filteredAnnouncements: Notification[] = [];
  selectedCourseId: number | undefined;

  constructor(
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private studentService: StudentService,
    private courseAnnouncementService: CourseAnnouncementService
  ) {}

  ngOnInit(): void {
    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      const id = parsedUser.id;
      this.loadActiveCourses(id);
    }
    this.loadAnnouncements();
  }

  loadAnnouncements() {
    this.courseAnnouncementService.getAll().subscribe(x => {
      this.announcements = x;
    });
  }

  loadActiveCourses(id: number) {
    this.studentService.getAktivnicoursei(id).subscribe(x => {
      const unique = x.filter((c, index, self) =>
        index === self.findIndex(t => t.id === c.id)
      );
      this.activeCourses = unique;
      if (this.activeCourses && this.activeCourses.length > 0) {
        this.selectedCourseId = this.activeCourses[0].id;
        setTimeout(() => { this.filterAnnouncements(); }, 0);
      }
    });
  }

  onCourseChange(event: any) {
    if (event.value) {
      this.selectedCourseId = event.value;
      this.filterAnnouncements();
    }
  }

  filterAnnouncements() {
    if (this.selectedCourseId !== undefined) {
      this.filteredAnnouncements = this.announcements.filter(x =>
        x.course?.id === this.selectedCourseId
      );
    } else {
      this.filteredAnnouncements = [];
    }
  }

  deleteAnnouncement(event: Event, notification: Notification) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure you want to remove this announcement?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.messageService.add({ severity: 'info', summary: 'Removed!', detail: 'Announcement successfully removed.', life: 3000 });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled!', detail: 'Removal cancelled.', life: 3000 });
      }
    });
  }

  openAddDialog() {
    this.visible = true;
  }

  addAnnouncement() {
    this.visible = false;
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
    if (isNaN(d.getTime())) return '';
    const hours = d.getUTCHours().toString().padStart(2, '0');
    const minutes = d.getUTCMinutes().toString().padStart(2, '0');
    const day = d.getUTCDate().toString().padStart(2, '0');
    const month = (d.getUTCMonth() + 1).toString().padStart(2, '0');
    const year = d.getUTCFullYear();
    return `${hours}:${minutes}, ${day}-${month}-${year}`;
  }
}
