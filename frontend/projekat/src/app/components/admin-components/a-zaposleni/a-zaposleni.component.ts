import { NgClass, NgIf } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { Teacher } from '../../../model/users/teacher';
import { StudentOffice } from '../../../model/users/student-affairs';
import { Administrator } from '../../../model/users/administrator';
import { TeacherService } from '../../../services/teacher.service';
import { StudentOfficeService } from '../../../services/student-affairs.service';
import { AdministratorService } from '../../../services/administrator.service';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { InputGroupModule } from 'primeng/inputgroup';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-a-zaposleni',
  standalone: true,
  imports: [DropdownModule, FormsModule, NgIf, TableModule, NgClass, ButtonModule, DialogModule, ToastModule, ConfirmPopupModule, InputGroupModule],
  templateUrl: './a-zaposleni.component.html',
  styleUrl: './a-zaposleni.component.css',
  providers: [MessageService, ConfirmationService]
})
export class AZaposleniComponent implements OnInit {
  teachers: Teacher[] = [];
  officeStaff: StudentOffice[] = [];
  administrators: Administrator[] = [];

  filteredTeachers: Teacher[] = [];
  filteredOfficeStaff: StudentOffice[] = [];
  filteredAdministrators: Administrator[] = [];

  editTeacherVisible: boolean = false;
  editOfficeVisible: boolean = false;
  editAdminVisible: boolean = false;

  teacherSearch: any = {};
  officeSearch: any = {};
  adminSearch: any = {};

  selectedTeacher: any = {};
  selectedOffice: any = {};
  selectedAdmin: any = {};

  employeeTypes: { label: string; value: string }[] = [
    { label: 'Teachers', value: 'teachers' },
    { label: 'Student Office', value: 'officeStaff' },
    { label: 'Administrators', value: 'administrators' }
  ];

  selectedCategory: string = 'teachers';

  constructor(
    private teacherService: TeacherService,
    private studentOfficeService: StudentOfficeService,
    private adminService: AdministratorService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll() {
    this.getTeachers();
    this.getOfficeStaff();
    this.getAdministrators();
  }

  getTeachers() {
    this.teacherService.getAll().subscribe(x => {
      this.teachers = x;
      this.filteredTeachers = this.teachers;
    });
  }

  getOfficeStaff() {
    this.studentOfficeService.getAll().subscribe(x => {
      this.officeStaff = x;
      this.filteredOfficeStaff = this.officeStaff;
    });
  }

  getAdministrators() {
    this.adminService.getAll().subscribe(x => {
      this.administrators = x;
      this.filteredAdministrators = this.administrators;
    });
  }

  closeTeacherDialog() {
    this.editTeacherVisible = false;
  }

  openEditTeacherDialog(teacher: Teacher) {
    this.selectedTeacher = { ...teacher };
    this.editTeacherVisible = true;
  }

  updateTeacher() {
    this.teacherService.update(this.selectedTeacher.id, this.selectedTeacher).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Teacher updated successfully.' });
        this.selectedTeacher = {};
        this.loadAll();
        this.closeTeacherDialog();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while updating teacher.' });
      }
    });
  }

  removeTeacher(id: number, event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure you want to remove this teacher?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.teacherService.delete(id).subscribe({
          next: () => {
            this.loadAll();
            this.messageService.add({ severity: 'info', summary: 'Removed', detail: 'Teacher removed successfully.' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while removing.' });
          }
        });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled', detail: 'Removal cancelled.', life: 3000 });
      }
    });
  }

  closeOfficeDialog() {
    this.editOfficeVisible = false;
  }

  openEditOfficeDialog(office: StudentOffice) {
    this.selectedOffice = { ...office };
    this.editOfficeVisible = true;
  }

  updateOfficeStaff() {
    this.studentOfficeService.update(this.selectedOffice.id, this.selectedOffice).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Office staff updated successfully.' });
        this.selectedOffice = {};
        this.loadAll();
        this.closeOfficeDialog();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while updating.' });
      }
    });
  }

  removeOfficeStaff(id: number, event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure you want to remove this office staff member?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.studentOfficeService.delete(id).subscribe({
          next: () => {
            this.loadAll();
            this.messageService.add({ severity: 'info', summary: 'Removed', detail: 'Office staff removed successfully.' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while removing.' });
          }
        });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled', detail: 'Removal cancelled.', life: 3000 });
      }
    });
  }

  closeAdminDialog() {
    this.editAdminVisible = false;
  }

  openEditAdminDialog(admin: Administrator) {
    this.selectedAdmin = { ...admin };
    this.editAdminVisible = true;
  }

  updateAdmin() {
    this.adminService.update(this.selectedAdmin.id, this.selectedAdmin).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Administrator updated successfully.' });
        this.selectedAdmin = {};
        this.loadAll();
        this.closeAdminDialog();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while updating.' });
      }
    });
  }

  removeAdmin(id: number, event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure you want to remove this administrator?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.adminService.delete(id).subscribe({
          next: () => {
            this.loadAll();
            this.messageService.add({ severity: 'info', summary: 'Removed', detail: 'Administrator removed successfully.' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while removing.' });
          }
        });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled', detail: 'Removal cancelled.', life: 3000 });
      }
    });
  }

  searchTeachers() {
    this.filteredTeachers = this.teachers.filter(p =>
      (this.teacherSearch.firstName ? p.firstName.toLowerCase().includes(this.teacherSearch.firstName.toLowerCase()) : true) &&
      (this.teacherSearch.lastName ? p.lastName.toLowerCase().includes(this.teacherSearch.lastName.toLowerCase()) : true) &&
      (this.teacherSearch.email ? p.email.toLowerCase().includes(this.teacherSearch.email.toLowerCase()) : true)
    );
  }

  searchOfficeStaff() {
    this.filteredOfficeStaff = this.officeStaff.filter(s =>
      (this.officeSearch.firstName ? s.firstName.toLowerCase().includes(this.officeSearch.firstName.toLowerCase()) : true) &&
      (this.officeSearch.lastName ? s.lastName.toLowerCase().includes(this.officeSearch.lastName.toLowerCase()) : true) &&
      (this.officeSearch.email ? s.email.toLowerCase().includes(this.officeSearch.email.toLowerCase()) : true)
    );
  }

  searchAdministrators() {
    this.filteredAdministrators = this.administrators.filter(a =>
      (this.adminSearch.firstName ? a.firstName.toLowerCase().includes(this.adminSearch.firstName.toLowerCase()) : true) &&
      (this.adminSearch.lastName ? a.lastName.toLowerCase().includes(this.adminSearch.lastName.toLowerCase()) : true)
    );
  }

  clearSearch() {
    this.teacherSearch = {};
    this.officeSearch = {};
    this.adminSearch = {};
    this.filteredTeachers = this.teachers;
    this.filteredOfficeStaff = this.officeStaff;
    this.filteredAdministrators = this.administrators;
  }
}
