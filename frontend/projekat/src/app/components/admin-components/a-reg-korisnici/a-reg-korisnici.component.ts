import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { RegisteredUserService } from '../../../services/registrovani-korisnik.service';
import { TableModule } from 'primeng/table';
import { InputGroupModule } from 'primeng/inputgroup';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { RegisteredUser } from '../../../model/users/registrovaniKorisnik';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { DialogModule } from 'primeng/dialog';
import { NgIf } from '@angular/common';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { DropdownModule } from 'primeng/dropdown';
import { AdministratorService } from '../../../services/administrator.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-a-reg-korisnici',
  standalone: true,
  imports: [TableModule, InputGroupModule, FormsModule, ButtonModule, ToastModule, DialogModule, NgIf, ConfirmPopupModule, DropdownModule],
  templateUrl: './a-reg-korisnici.component.html',
  styleUrl: './a-reg-korisnici.component.css',
  providers: [ConfirmationService, MessageService]
})
export class ARegKorisniciComponent implements OnInit {
  users: any[] = [];
  filteredUsers: any[] = [];
  selectedUser: any;

  addUserDialog: boolean = false;
  visible: boolean = false;
  typeDialog: boolean = false;

  userForTypeChange: any = {};
  selectedType: any;

  types = [
    { label: 'student', value: 'student_premission' },
    { label: 'teacher', value: 'nastavnik_premission' },
    { label: 'office', value: 'studentskaSluzba_premission' },
    { label: 'admin', value: 'administrator_premission' }
  ];

  newUser: any = {};

  search = {
    username: '',
    email: ''
  };

  constructor(
    private registeredUserService: RegisteredUserService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private adminService: AdministratorService
  ) {}

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers() {
    this.registeredUserService.getAll().subscribe(x => {
      this.users = x;
      this.filteredUsers = this.users;
    });
  }

  searchUsers() {
    this.filteredUsers = this.users.filter(u =>
      (this.search.username ? (u.username || '').toLowerCase().includes(this.search.username.toLowerCase()) : true) &&
      (this.search.email ? (u.email || '').toLowerCase().includes(this.search.email.toLowerCase()) : true)
    );
  }

  clearSearch() {
    this.search = { username: '', email: '' };
    this.filteredUsers = this.users;
  }

  removeUser(id: number, event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure you want to remove this user?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.registeredUserService.delete(id).subscribe({
          next: () => {
            this.getUsers();
            this.messageService.add({ severity: 'info', summary: 'Removed', detail: 'User removed successfully.' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while removing the user.' });
          }
        });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled', detail: 'Removal cancelled.', life: 3000 });
      }
    });
  }

  openTypeDialog(user: any) {
    this.typeDialog = true;
    this.userForTypeChange = user;
  }

  changeType() {
    this.adminService.assignStatus(this.selectedType.value, this.userForTypeChange).subscribe(() => {
      this.getUsers();
      this.cancelTypeDialog();
    });
  }

  cancelTypeDialog() {
    this.typeDialog = false;
    this.userForTypeChange = {};
  }

  closeAddUserDialog() {
    this.addUserDialog = false;
  }

  openAddUserDialog() {
    this.addUserDialog = true;
  }

  addUser() {
    this.registeredUserService.create(this.newUser).subscribe({
      next: () => {
        this.getUsers();
        this.closeAddUserDialog();
        this.newUser = {};
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'New user added successfully.' });
      },
      error: () => {
        this.closeAddUserDialog();
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while adding the user.' });
      }
    });
  }
}
