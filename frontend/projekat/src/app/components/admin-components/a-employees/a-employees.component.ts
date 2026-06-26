import { NgClass, NgIf } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { Teacher } from '../../../model/users/teacher';
import { StudentOffice } from '../../../model/users/student-office';
import { Administrator } from '../../../model/users/administrator';
import { TeacherService } from '../../../services/teacher.service';
import { StudentOfficeService } from '../../../services/student-office.service';
import { AdministratorService } from '../../../services/administrator.service';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { InputGroupModule } from 'primeng/inputgroup';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-a-employees',
  standalone: true,
  imports: [DropdownModule, FormsModule, NgIf, TableModule, NgClass, ButtonModule, DialogModule, ToastModule, ConfirmPopupModule, InputGroupModule],
  templateUrl: './a-employees.component.html',
  styleUrl: './a-employees.component.css',
  providers: [MessageService, ConfirmationService]
})
export class AEmployeesComponent implements OnInit{
  teachers: Teacher[] = [];
  officeStaff: StudentOffice[] = [];
  admins: Administrator[] = [];

  filteredTeachers: Teacher[] = [];
  filteredOfficeStaff: StudentOffice[] = [];
  filteredAdmins: Administrator[] = [];

  editTeacherVisible: boolean = false;

  editOfficeVisible: boolean = false;

  editAdminVisible: boolean = false;

  filterTeacher:any = {};
  filterOffice:any = {};
  filterAdmin:any = {};

  newTeacher:any = {};
  newOfficeEmployee:any = {};
  newAdmin:any = {};

  employeeCategories: { label: string, value: string }[] = [
    { label: 'Teachers', value: 'teachers' },
    { label: 'Student Office', value: 'officeStaff' },
    { label: 'Administration', value: 'admins' }
  ];

  selected: string = "teachers"

  constructor(private TeacherService: TeacherService, private officeStaffService: StudentOfficeService, 
    private adminService: AdministratorService, private messageService: MessageService, private confirmationService: ConfirmationService){}

  ngOnInit(): void {
    this.getteachers();
    this.getofficeStaff();
    this.loadAdmins();
  }

  loadAllData(){
    this.getteachers();
    this.getofficeStaff();
    this.loadAdmins();
  }

  getteachers(){
    this.TeacherService.getAll().subscribe(x=>{
      this.teachers = x;
      this.filteredTeachers = this.teachers;
      // console.log(this.teachers);
    })
  }

  getofficeStaff(){
    this.officeStaffService.getAll().subscribe(x=>{
      this.officeStaff = x;
      // console.log(this.officeStaff);
      this.filteredOfficeStaff = this.officeStaff;
    })
  }

  loadAdmins(){
    this.adminService.getAll().subscribe(x=>{
      this.admins = x;
      console.log(this.admins);
      this.filteredAdmins = this.admins;
    })
  }


  closeTeacherDialog(){
    this.editTeacherVisible = false;
  }

  openEditTeacher(profesor: Teacher){
    this.newTeacher = {...profesor};
    console.log(this.newTeacher);
    this.editTeacherVisible = true;
  }

  saveTeacher(){
    this.TeacherService.update(this.newTeacher.id, this.newTeacher).subscribe({
      next: (x) => {
        this.messageService.add({
          severity: 'success', 
          summary: 'Profesor izmenjen', 
          detail: 'Profesor je uspešno izmenjen.'
        });
        this.newTeacher = {}; 
        this.loadAllData();    
        this.closeTeacherDialog();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error', 
          summary: 'Greška', 
          detail: 'Došlo je do greške pri izmeni profesora.'
        });
        console.error('Greška:', err); 
      }
    })
  }

  deleteTeacher(id: number, event: Event){
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Zelite da uklonite izabranog profesora?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.TeacherService.delete(id).subscribe({
               next: () => {
                 this.loadAllData();
                 this.messageService.add({ severity: 'info', summary: 'Uspešno uklonjeno', detail: 'Profesor je uklonjen' });
               },
                error: err => {
                 this.messageService.add({ severity: 'error', summary: 'Greška pri uklanjanju', detail: 'Došlo je do greške.' });
               }
             });
      },
      reject: () => {
          this.messageService.add({ severity: 'error', summary: 'Ponisteno', detail: 'Uklanjanje ponisteno', life: 3000 });
      }
    });
  }



  closeOfficeDialog(){
    this.editOfficeVisible = false;
  }

  openEditOffice(officeStaff: StudentOffice){
    this.newOfficeEmployee = {...officeStaff};
    this.editOfficeVisible = true;
  }

  saveOfficeStaff(){
    this.officeStaffService.update(this.newOfficeEmployee.id, this.newOfficeEmployee).subscribe({
      next: (x) => {
        this.messageService.add({
          severity: 'success', 
          summary: 'officeStaff izmenjena', 
          detail: 'officeStaff je uspešno izmenjena.'
        });
        this.newOfficeEmployee = {}; 
        this.loadAllData();    
        this.closeOfficeDialog();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error', 
          summary: 'Greška', 
          detail: 'Došlo je do greške pri izmeni sluzbe.'
        });
        console.error('Greška:', err); 
      }
    })
  }

  deleteOfficeEmployee(id: number, event: Event){
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Zelite da uklonite izabranog zaposlenog sluzbe?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.officeStaffService.delete(id).subscribe({
               next: () => {
                 this.loadAllData();
                 this.messageService.add({ severity: 'info', summary: 'Uspešno uklonjeno', detail: 'Zaposleni je uklonjen' });
               },
                error: err => {
                 this.messageService.add({ severity: 'error', summary: 'Greška pri uklanjanju', detail: 'Došlo je do greške.' });
               }
             });
      },
      reject: () => {
          this.messageService.add({ severity: 'error', summary: 'Ponisteno', detail: 'Uklanjanje ponisteno', life: 3000 });
      }
    });
  }

  closeAdminDialog(){
    this.editAdminVisible = false;
  }

  openEditAdmin(admin: Administrator){
    this.newAdmin = {...admin};
    this.editAdminVisible = true;
  }

  saveAdmin(){
    this.adminService.update(this.newAdmin.id, this.newAdmin).subscribe({
      next: (x) => {
        this.messageService.add({
          severity: 'success', 
          summary: 'Admin izmenjen', 
          detail: 'Admin je uspešno izmenjen.'
        });
        this.newAdmin = {}; 
        this.loadAllData();    
        this.closeAdminDialog();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error', 
          summary: 'Greška', 
          detail: 'Došlo je do greške pri izmeni admina.'
        });
        console.error('Greška:', err); 
      }
    })
  }

  deleteAdmin(id: number, event: Event){
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Zelite da uklonite izabranog admina?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.adminService.delete(id).subscribe({
               next: () => {
                 this.loadAllData();
                 this.messageService.add({ severity: 'info', summary: 'Uspešno uklonjeno', detail: 'Admin je uklonjen' });
               },
                error: err => {
                 this.messageService.add({ severity: 'error', summary: 'Greška pri uklanjanju', detail: 'Došlo je do greške.' });
               }
             });
      },
      reject: () => {
          this.messageService.add({ severity: 'error', summary: 'Ponisteno', detail: 'Uklanjanje ponisteno', life: 3000 });
      }
    });
  }

  filterTeachers(){
    this.filteredTeachers = this.teachers.filter(p =>
      (this.filterTeacher.firstName ? p.firstName.toLowerCase().includes(this.filterTeacher.firstName.toLowerCase()) : true) &&
      (this.filterTeacher.lastName ? p.lastName.toLowerCase().includes(this.filterTeacher.lastName.toLowerCase()) : true) &&
      (this.filterTeacher.email ? p.email.toLowerCase().includes(this.filterTeacher.email.toLowerCase()) : true) 
    );
  }

  filterOfficeStaff(){
    this.filteredOfficeStaff = this.officeStaff.filter(s =>
      (this.filterOffice.firstName ? s.firstName.toLowerCase().includes(this.filterOffice.firstName.toLowerCase()) : true) &&
      (this.filterOffice.lastName ? s.lastName.toLowerCase().includes(this.filterOffice.lastName.toLowerCase()) : true) &&
      (this.filterOffice.email ? s.email.toLowerCase().includes(this.filterOffice.email.toLowerCase()) : true) 
    );
  }

  filterAdmins(){
    this.filteredAdmins = this.admins.filter(a =>
      (this.filterAdmin.firstName ? a.firstName.toLowerCase().includes(this.filterAdmin.firstName.toLowerCase()) : true) &&
      (this.filterAdmin.lastName ? a.lastName.toLowerCase().includes(this.filterAdmin.lastName.toLowerCase()) : true)
    );
  }

  clearFilter(){
    this.filterTeacher= {};
    this.filterOffice = {};
    this.filterAdmin = {};

    this.filteredTeachers = this.teachers;
    this.filteredOfficeStaff = this.officeStaff;
    this.filteredAdmins = this.admins;
  }

}



