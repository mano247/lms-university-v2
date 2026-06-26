import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DividerModule } from 'primeng/divider';
import { DataViewModule } from 'primeng/dataview';
import { NgFor, NgIf } from '@angular/common';
import { Notification } from '../../../model/obavestenje';
import { DropdownModule } from 'primeng/dropdown';
import { Course } from '../../../model/academic/predmet';
import { CourseService } from '../../../services/predmet.service';
import { NotificationService } from '../../../services/obavestenje.service';
import { ButtonModule } from 'primeng/button';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { LoginService } from '../../../services/auth/login.service';
import { TeacherService } from '../../../services/nastavnik.service';
import { FormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { Teacher } from '../../../model/users/nastavnik';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-obavestenja-prof',
  standalone: true,
  imports: [NgFor, DataViewModule, DividerModule, DropdownModule, ButtonModule, ConfirmPopupModule, ToastModule, DialogModule, NgIf, FormsModule, CalendarModule],
  templateUrl: './obavestenja-prof.component.html',
  styleUrl: './obavestenja-prof.component.css',
  providers: [ConfirmationService, MessageService]
})
export class ObavestenjaProfComponent implements OnInit {
  visible: boolean = false;

  myCourses: Course[] | undefined;
  courseAnnouncements: Notification[] = [];
  filteredAnnouncements: Notification[] | undefined;

  selectedCourseId: number | undefined;
  selectedCourse: Course | undefined;
  teacher: Teacher | undefined;

  newAnnouncement: Notification = {
    date: new Date(),
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
    private notificationService: NotificationService,
    private courseService: CourseService
  ) {}

  ngOnInit(): void {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      const id = parsedUser.id;
      this.getCourses(id);
    }
  }

  getCourses(id: number): void {
    this.teacherService.getMyCourses(id).subscribe(courses => {
      this.myCourses = courses;

      if (this.myCourses && this.myCourses.length > 0) {
        this.selectedCourseId = this.myCourses[0].id;
        this.getAnnouncements();
      }
    });
  }

  onCourseChange(event: any) {
    if (event.value) {
      this.selectedCourseId = event.value;
      this.filterAnnouncements();
    }
  }

  getTeacher(id: number) {
    this.teacherService.getById(id).subscribe(x => {
      this.teacher = x;
    });
  }

  getAnnouncements() {
    this.notificationService.getAll().subscribe(x => {
      this.courseAnnouncements = x;
      this.filterAnnouncements();
    });
  }

  filterAnnouncements(): void {
    if (this.selectedCourseId !== undefined) {
      this.filteredAnnouncements = this.courseAnnouncements.filter(a =>
        a.course?.id === this.selectedCourseId
      );
    } else {
      this.filteredAnnouncements = [];
    }
  }

  removeAnnouncement(event: Event, announcement: Notification) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure you want to remove this announcement?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        if (announcement.id) {
          this.notificationService.delete(announcement.id).subscribe(() => {
            this.messageService.add({ severity: 'info', summary: 'Removed', detail: 'Announcement removed successfully.', life: 3000 });
            this.getAnnouncements();
          });
        }
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled', detail: 'Removal cancelled.', life: 3000 });
      }
    });
  }

  openAnnouncementDialog() {
    this.visible = true;
  }

  addAnnouncement() {
    this.notificationService.create(this.newAnnouncement).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Announcement added successfully!' });
        this.getAnnouncements();
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
      date: new Date(),
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
}
