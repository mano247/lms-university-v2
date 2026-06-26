import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { RegisteredUserService } from '../../../services/registered-user.service';
import { TableModule } from 'primeng/table';
import { InputGroupModule } from 'primeng/inputgroup';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { RegisteredUser } from '../../../model/users/registered-user';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { DialogModule } from 'primeng/dialog';
import { NgIf } from '@angular/common';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { DropdownModule } from 'primeng/dropdown';
import { AdministratorService } from '../../../services/administrator.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-a-registered-users',
  standalone: true,
  imports: [TableModule, InputGroupModule, FormsModule, ButtonModule, ToastModule, DialogModule, NgIf, ConfirmPopupModule, DropdownModule],
  templateUrl: './a-registered-users.component.html',
  styleUrl: './a-registered-users.component.css',
  providers: [ConfirmationService, MessageService]
})
export class ARegisteredUsersComponent implements OnInit{
  users: any[] = [];
  filteredUsers: any[] = [];

  selectedUser: any;

  addUserVisible: boolean = false;

  visible: boolean = false;
  tipDialog: boolean = false;

  userForTypeChange: any = {};
  tip: any;

  tipovi = [
    { name: "Student", tip: "student_premission" },
    { name: "Teacher", tip: "teacher_premission" },
    { name: "Student Office", tip: "student-office_premission" },
    { name: "Administrator", tip: "administrator_premission" },
  ]

  newUser: any = {};

  userFilter = {
    username: "",
    email: ""
  }

  constructor(private rkService: RegisteredUserService, private confirmationService: ConfirmationService, 
    private messageService: MessageService, private adminService: AdministratorService){}

  ngOnInit(): void {
    this.getusers();
  }
  
  getusers(){
    this.rkService.getAll().subscribe(x=>{
      this.users = x;
      this.filteredUsers = this.users;
    })
  }
  

  searchUsers() {
    this.filteredUsers = this.users.filter(k => 
      (this.userFilter.username ? (k.username || '').toLowerCase().includes(this.userFilter.username.toLowerCase()) : true) &&
      (this.userFilter.email ? (k.email || '').toLowerCase().includes(this.userFilter.email.toLowerCase()) : true)
    );
  }

  clearFilter(){
    this.userFilter = {
      username: "",
      email: ""
    }
    this.filteredUsers = this.users;
  }

  deleteUser(id: number, event: Event) {
    this.confirmationService.confirm({
        target: event.target as EventTarget,
        message: 'Zelite da uklonite izabranog korisnika?',
        icon: 'pi pi-info-circle',
        acceptButtonStyleClass: 'p-button-danger p-button-sm',
        accept: () => {
          this.rkService.delete(id).subscribe({
                 next: () => {
                   this.getusers();
                   this.messageService.add({ severity: 'info', summary: 'Uspešno uklonjeno', detail: 'currentUser uklonjen' });
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




  openChangeType(currentUser: any){
    this.tipDialog = true;
    this.userForTypeChange = currentUser;
    console.log(this.userForTypeChange);
  }

  saveTypeChange(){
    this.adminService.dodelaStatusa(this.tip.tip, this.userForTypeChange).subscribe(x=>{
      this.getusers();
      this.closeTypeDialog();
    })
  }

  closeTypeDialog(){
    this.tipDialog = false;
    this.userForTypeChange = {};
  }


  closeUserDialog(){
    this.addUserVisible = false;
  }

  openAddUserDialog(){
    this.addUserVisible = true;
  }

  addUser(){
    this.rkService.create(this.newUser).subscribe({
      next: () => {
        this.getusers();
        this.closeUserDialog();
        this.newUser = {};
        this.messageService.add({ severity: 'success', summary: 'Uspešno dodato', detail: 'Novi currentUser je uspesno dodat' });
      },
      error: err => {
        this.closeUserDialog();
        this.messageService.add({ severity: 'error', summary: 'Greška pri dodavanju', detail: 'Došlo je do greške.' });
      }
    });
  }



  
}



