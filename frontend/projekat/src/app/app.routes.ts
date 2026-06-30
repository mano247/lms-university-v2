import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./components/university/university.component').then(m => m.UniversityComponent),
  },
  {
    path: 'enrollment',
    loadComponent: () => import('./components/enrollment/enrollment.component').then(m => m.EnrollmentComponent),
  },
  {
    path: 'contact',
    loadComponent: () => import('./components/contact/contact.component').then(m => m.ContactComponent),
  },
  {
    path: 'rectorate',
    loadComponent: () => import('./components/rectorate/rectorate.component').then(m => m.RectorateComponent),
  },
  {
    path: 'login',
    loadComponent: () => import('./components/login/login.component').then(m => m.LoginComponent),
  },
  {
    path: 'register',
    loadComponent: () => import('./components/register/register.component').then(m => m.RegisterComponent),
  },
  {
    path: 'faculties',
    loadComponent: () => import('./components/faculties/faculties.component').then(m => m.FacultiesComponent),
  },
  {
    path: 'faculty/:facultyCode',
    loadComponent: () => import('./components/faculty/faculty.component').then(m => m.FacultyComponent),
  },
  {
    path: 'faculty/:facultyCode/study-program/:studyProgramCode',
    loadComponent: () => import('./components/study-program/study-program.component').then(m => m.StudyProgramComponent),
  },
  {
    path: 'faculty/:facultyCode/study-program/:studyProgramCode/course/:courseCode',
    loadComponent: () => import('./components/course/course.component').then(m => m.CourseComponent),
  },
  {
    path: 'faculty/:facultyCode/study-program/:studyProgramCode/course/:courseCode/course-material/:materialTitle',
    loadComponent: () => import('./components/teaching-material/teaching-material.component').then(m => m.TeachingMaterialComponent),
  },
  {
    path: 'my-profile',
    loadComponent: () => import('./components/profile/profile.component').then(m => m.ProfileComponent),
    canActivate: [authGuard],
  },
  {
    path: 'announcements',
    loadComponent: () => import('./components/announcements/announcements.component').then(m => m.AnnouncementsComponent),
    canActivate: [authGuard],
  },
  {
    path: 'menu',
    loadComponent: () => import('./components/dashboard/dashboard.component').then(m => m.DashboardComponent),
    canActivate: [authGuard],
  },
  {
    path: 'all-announcements',
    loadComponent: () => import('./components/global-announcements/global-announcements.component').then(m => m.GlobalAnnouncementsComponent),
  },
  {
    path: 'eStudent',
    loadComponent: () => import('./components/eServis/e-student/e-student.component').then(m => m.EStudentComponent),
    canActivate: [authGuard, roleGuard],
    data: { allowedPermissions: ['STUDENT_PERMISSION'] },
  },
  {
    path: 'eAdmin',
    loadComponent: () => import('./components/eServis/e-admin/e-admin.component').then(m => m.EAdminComponent),
    canActivate: [authGuard, roleGuard],
    data: { allowedPermissions: ['ADMINISTRATOR_PERMISSION'] },
  },
  {
    path: 'eTeacher',
    loadComponent: () => import('./components/eServis/e-teacher/e-teacher.component').then(m => m.ETeacherComponent),
    canActivate: [authGuard, roleGuard],
    data: { allowedPermissions: ['TEACHER_PERMISSION'] },
  },
  {
    path: 'eOffice',
    loadComponent: () => import('./components/eServis/e-office/e-office.component').then(m => m.EOfficeComponent),
    canActivate: [authGuard, roleGuard],
    data: { allowedPermissions: ['STUDENT_AFFAIRS_PERMISSION'] },
  },
  {
    path: 'unauthorized',
    loadComponent: () => import('./components/unauthorized/unauthorized.component').then(m => m.UnauthorizedComponent),
  },
  {
    path: '**',
    loadComponent: () => import('./components/not-found/not-found.component').then(m => m.NotFoundComponent),
  },
];
