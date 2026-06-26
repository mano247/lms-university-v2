import { Routes } from '@angular/router';
import { UniversityComponent } from './components/university/university.component';
import { EnrollmentComponent } from './components/enrollment/enrollment.component';
import { ContactComponent } from './components/contact/contact.component';
import { RectorateComponent } from './components/rectorate/rectorate.component';
import { RegisterComponent } from './components/register/register.component';
import { FacultiesComponent } from './components/faculties/faculties.component';
import { FacultyComponent } from './components/faculty/faculty.component';
import { StudyProgramComponent } from './components/study-program/study-program.component';
import { CourseComponent } from './components/course/course.component';
import { TeachingMaterialComponent } from './components/teaching-material/teaching-material.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AnnouncementsComponent } from './components/announcements/announcements.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { GlobalAnnouncementsComponent } from './components/global-announcements/global-announcements.component';
import { LoginComponent } from './components/login/login.component';
import { UnauthorizedComponent } from './components/unauthorized/unauthorized.component';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';
import { EStudentComponent } from './components/eServis/e-student/e-student.component';
import { EProfesorComponent } from './components/eServis/e-profesor/e-profesor.component';
import { ESluzbaComponent } from './components/eServis/e-sluzba/e-sluzba.component';
import { EAdminComponent } from './components/eServis/e-admin/e-admin.component';

export const routes: Routes = [
  { path: '', component: UniversityComponent },
  { path: 'enrollment', component: EnrollmentComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'rectorate', component: RectorateComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'faculties', component: FacultiesComponent },
  { path: 'faculty/:facultyCode', component: FacultyComponent },
  {
    path: 'faculty/:facultyCode/study-program/:studyProgramCode',
    component: StudyProgramComponent,
  },
  {
    path: 'faculty/:facultyCode/study-program/:studyProgramCode/course/:courseCode',
    component: CourseComponent,
  },
  {
    path: 'faculty/:facultyCode/study-program/:studyProgramCode/course/:courseCode/course-material/:materialTitle',
    component: TeachingMaterialComponent,
  },
  {
    path: 'my-profile',
    component: ProfileComponent,
    canActivate: [authGuard],
  },
  {
    path: 'announcements',
    component: AnnouncementsComponent,
    canActivate: [authGuard],
  },
  {
    path: 'menu',
    component: DashboardComponent,
    canActivate: [authGuard],
  },
  { path: 'all-announcements', component: GlobalAnnouncementsComponent },
  {
    path: 'eStudent',
    component: EStudentComponent,
    canActivate: [authGuard, roleGuard],
    data: { allowedPermissions: ['STUDENT_PERMISSION'] },
  },
  {
    path: 'eAdmin',
    component: EAdminComponent,
    canActivate: [authGuard, roleGuard],
    data: { allowedPermissions: ['ADMINISTRATOR_PERMISSION'] },
  },
  {
    path: 'eTeacher',
    component: EProfesorComponent,
    canActivate: [authGuard, roleGuard],
    data: { allowedPermissions: ['TEACHER_PERMISSION'] },
  },
  {
    path: 'eOffice',
    component: ESluzbaComponent,
    canActivate: [authGuard, roleGuard],
    data: { allowedPermissions: ['STUDENT_AFFAIRS_PERMISSION'] },
  },
  { path: 'unauthorized', component: UnauthorizedComponent },
  { path: '**', redirectTo: '' },
];
