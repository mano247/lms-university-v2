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
import { StudentOfficeService } from '../../services/student-office.service';
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
  korisnikId: number | undefined;
  roles: string[] = [];

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService,
    private registeredUserService: RegisteredUserService,
    private studentService: StudentService,
    private teacherService: TeacherService,
    private officeStaffService: StudentOfficeService,
    private adminService: AdministratorService,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    this.createForm();
    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      this.korisnikId = parsedUser.id;
      if (this.korisnikId !== undefined) {
        this.loadAllData(this.korisnikId);
        this.roles = this.loginService.getUserRole();
      }
    }
  }

  loadAllData(id: number) {
    this.registeredUserService.getById(id).subscribe(x => {
      this.currentUser = x;
    });
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

  editDialog() {
    this.visible = true;
    if (this.currentUser) {
      this.profileForm.patchValue(this.currentUser);
    }
  }

  saveChanges() {
    if (this.profileForm.valid && this.korisnikId) {
      const updatedUser: any = {
        ...this.currentUser,
        ...this.profileForm.value
      };

      let updateObservable;

      if (this.roles.includes('ADMINISTRATOR_PERMISSION')) {
        updateObservable = this.adminService.update(this.korisnikId, updatedUser);
      } else if (this.roles.includes('student-office_PERMISSION')) {
        updateObservable = this.officeStaffService.update(this.korisnikId, updatedUser);
      } else if (this.roles.includes('teacher_PERMISSION')) {
        updateObservable = this.teacherService.update(this.korisnikId, updatedUser);
      } else if (this.roles.includes('STUDENT_PERMISSION')) {
        updateObservable = this.studentService.update(this.korisnikId, updatedUser);
      } else if (this.roles.includes('KORISNIK_PERMISSION')) {
        updateObservable = this.registeredUserService.update(this.korisnikId, updatedUser);
      } else {
        this.messageService.add({ severity: 'error', summary: 'Error!', detail: 'Unknown user role.' });
        return;
      }

      updateObservable.subscribe(
        () => {
          this.visible = false;
          this.messageService.add({ severity: 'success', summary: 'Profile updated!', detail: 'Your information has been saved.' });
          if (this.korisnikId !== undefined) {
            this.loadAllData(this.korisnikId);
          }
        },
        () => {
          this.messageService.add({ severity: 'error', summary: 'Error!', detail: 'An error occurred while updating your profile.' });
        }
      );
    } else {
      this.messageService.add({ severity: 'warning', summary: 'Invalid data!', detail: 'Please check the entered information.' });
    }
  }
}
