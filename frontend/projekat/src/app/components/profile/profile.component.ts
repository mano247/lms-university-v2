import { NgIf } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { RegisteredUserService } from '../../services/registered-user.service';
import { RegisteredUser } from '../../model/users/registered-user';
import { StudentService } from '../../services/student.service';
import { TeacherService } from '../../services/teacher.service';
import { StudentOfficeService } from '../../services/student-affairs.service';
import { AdministratorService } from '../../services/administrator.service';
import { LoginService } from '../../services/auth/login.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-profile',
  standalone: true,
  imports: [DividerModule, ButtonModule, ReactiveFormsModule, ToastModule, NgIf, DialogModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
  providers: [MessageService]
})
export class ProfileComponent implements OnInit {
  visible: boolean = false;
  profileForm: FormGroup = this.fb.group({});
  currentUser: RegisteredUser | undefined;
  userId: number | undefined;
  roles: string[] = [];

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService,
    private registeredUserService: RegisteredUserService,
    private studentService: StudentService,
    private teacherService: TeacherService,
    private studentOfficeService: StudentOfficeService,
    private adminService: AdministratorService,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    this.createForm();

    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      this.userId = parsedUser.id;
      if (this.userId !== undefined) {
        this.getUserData(this.userId);
        this.roles = this.loginService.getUserRole();
      }
    }
  }

  getUserData(id: number) {
    this.registeredUserService.getById(id).subscribe(x => {
      this.currentUser = x;
    });
  }

  prepareForEdit() {
    if (this.currentUser) {
      this.profileForm.patchValue(this.currentUser);
    }
  }

  openEditDialog() {
    this.visible = true;
    this.prepareForEdit();
  }

  createForm() {
    this.profileForm = this.fb.group({
      firstName: [this.currentUser?.firstName],
      lastName: [this.currentUser?.lastName],
      email: [this.currentUser?.email, [Validators.required, Validators.email]],
      username: [this.currentUser?.username, Validators.required],
      password: [this.currentUser?.password, Validators.required]
    });
  }

  saveChanges() {
    if (this.profileForm.valid && this.userId) {
      const updatedUser: any = {
        ...this.currentUser,
        ...this.profileForm.value
      };

      let updateObservable;

      if (this.roles.includes('ADMINISTRATOR_PERMISSION')) {
        updateObservable = this.adminService.update(this.userId, updatedUser);
      } else if (this.roles.includes('STUDENTSKASLUZBA_PERMISSION')) {
        updateObservable = this.studentOfficeService.update(this.userId, updatedUser);
      } else if (this.roles.includes('NASTAVNIK_PERMISSION')) {
        updateObservable = this.teacherService.update(this.userId, updatedUser);
      } else if (this.roles.includes('STUDENT_PERMISSION')) {
        updateObservable = this.studentService.update(this.userId, updatedUser);
      } else if (this.roles.includes('KORISNIK_PERMISSION')) {
        updateObservable = this.registeredUserService.update(this.userId, updatedUser);
      } else {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Unknown user role.' });
        return;
      }

      updateObservable.subscribe(
        () => {
          this.visible = false;
          this.messageService.add({ severity: 'success', summary: 'Profile updated!', detail: 'Your data has been saved successfully.' });
          if (this.userId !== undefined) {
            this.getUserData(this.userId);
          }
        },
        () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while updating your profile.' });
        }
      );
    } else {
      this.messageService.add({ severity: 'warning', summary: 'Invalid data', detail: 'Please check the entered data.' });
    }
  }
}
