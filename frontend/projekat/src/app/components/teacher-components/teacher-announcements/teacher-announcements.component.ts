import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DividerModule } from 'primeng/divider';
import { DataViewModule } from 'primeng/dataview';
import { NgFor, NgIf } from '@angular/common';
import { Notification } from '../../../model/notification';
import { DropdownModule } from 'primeng/dropdown';
import { Course } from '../../../model/academic/course';
import { CourseAnnouncementService } from '../../../services/course-announcement.service';
import { ButtonModule } from 'primeng/button';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { LoginService } from '../../../services/auth/login.service';
import { TeacherService } from '../../../services/teacher.service';
import { FormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { Teacher } from '../../../model/users/teacher';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-teacher-announcements',
  standalone: true,
  imports: [NgFor, DataViewModule, DividerModule, DropdownModule, ButtonModule, ConfirmPopupModule, ToastModule, DialogModule, NgIf, FormsModule, CalendarModule],
  templateUrl: './teacher-announcements.component.html',
  styleUrl: './teacher-announcements.component.css',
  providers: [ConfirmationService, MessageService]
})
export class TeacherAnnouncementsComponent implements OnInit {
  visible: boolean = false;

  myCourses: Course[] | undefined;
  filteredAnnouncements: Notification[] | undefined;
  courseAnnouncements: Notification[] = [];
  selectedCourseId: number | undefined;
  selectedCourse: Course | undefined;
  teacher: Teacher | undefined;

  newAnnouncement: Notification = {
    content: '',
    title: '',
    image: '',
    startDate: new Date(),
    endDate: new Date(),
    course: undefined
  };

  constructor(
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private teacherService: TeacherService,
    private loginService: LoginService,
    private announcementService: CourseAnnouncementService
  ) {}

  ngOnInit(): void {
    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      const id = parsedUser.id;
      this.loadCourses(id);
    }
  }

  loadCourses(id: number): void {
    this.teacherService.mojicoursei(id).subscribe(courses => {
      this.myCourses = courses;
      if (this.myCourses && this.myCourses.length > 0) {
        this.selectedCourseId = this.myCourses[0].id;
        this.loadAnnouncements();
      }
    });
  }

  onCourseChange(event: any) {
    if (event.value) {
      this.selectedCourseId = event.value;
      this.filterAnnouncements();
    }
  }

  loadTeacher(id: number) {
    this.teacherService.getById(id).subscribe(x => {
      this.teacher = x;
    });
  }

  loadAnnouncements() {
    this.announcementService.getAll().subscribe(x => {
      this.courseAnnouncements = x;
      this.filterAnnouncements();
    });
  }

  filterAnnouncements(): void {
    if (this.selectedCourseId !== undefined) {
      this.filteredAnnouncements = this.courseAnnouncements.filter(n =>
        n.course?.id === this.selectedCourseId
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
        if (notification.id) {
          this.announcementService.delete(notification.id).subscribe(() => {
            this.messageService.add({ severity: 'info', summary: 'Removed!', detail: 'Announcement successfully removed.', life: 3000 });
            this.loadAnnouncements();
          });
        }
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
    this.announcementService.create(this.newAnnouncement).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Announcement successfully added!' });
        this.loadAnnouncements();
        this.visible = false;
        this.resetForm();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while adding the announcement.' });
        this.resetForm();
      }
    });
  }

  resetForm() {
    this.newAnnouncement = {
      content: '',
      title: '',
      image: '',
      startDate: new Date(),
      endDate: new Date(),
      course: undefined
    };
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
