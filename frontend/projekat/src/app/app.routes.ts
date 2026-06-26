import { Routes } from '@angular/router';
import { UniversityComponent } from './components/university/university.component';
import { EnrollmentComponent } from './components/enrollment/enrollment.component';
import { ContactComponent } from './components/contact/contact.component';
import { RectorateComponent } from './components/rectorate/rectorate.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { FacultiesComponent } from './components/faculties/faculties.component';
import { FacultyComponent } from './components/faculty/faculty.component';
import { StudyProgramComponent } from './components/study-program/study-program.component';
import { CourseComponent } from './components/course/course.component';
import { CourseMaterialComponent } from './components/course-material/course-material.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AnnouncementsComponent } from './components/announcements/announcements.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { GlobalAnnouncementsComponent } from './components/global-announcements/global-announcements.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from './guards/auth.guard';
import { EStudentComponent } from './components/portals/e-student/e-student.component';
import { EProfessorComponent } from './components/portals/e-professor/e-professor.component';
import { EOfficeComponent } from './components/portals/e-office/e-office.component';
import { EAdminComponent } from './components/portals/e-admin/e-admin.component';

export const routes: Routes = [
    {
        path: '',
        component: UniversityComponent
    },
    {
        path: 'enrollment',
        component: EnrollmentComponent
    },
    {
        path: 'contact',
        component: ContactComponent
    },
    {
        path: 'rectorate',
        component: RectorateComponent
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'registration',
        component: RegistrationComponent
    },
    {
        path: 'faculties',
        component: FacultiesComponent
    },
    {
        path: 'faculty/:facultyCode',
        component: FacultyComponent
    },
    {
        path: 'faculty/:facultyCode/study-program/:programCode',
        component: StudyProgramComponent
    },
    {
        path: 'faculty/:facultyCode/study-program/:programCode/course/:courseCode',
        component: CourseComponent
    },
    {
        path: 'faculty/:facultyCode/study-program/:programCode/course/:courseCode/material/:materialName',
        component: CourseMaterialComponent
    },
    {
        path: 'my-profile',
        component: ProfileComponent
    },
    {
        path: 'announcements',
        component: AnnouncementsComponent,
    },
    {
        path: 'menu',
        component: DashboardComponent,
        canActivate: [AuthGuard],
        data: {
            allowedPermissions: [
                'ADMINISTRATOR_PERMISSION',
                'STUDENT_PERMISSION',
                'NASTAVNIK_PERMISSION',
                'STUDENTSKAofficeStaff_PERMISSION'
            ]
        }
    },
    {
        path: 'all-announcements',
        component: GlobalAnnouncementsComponent
    },
    {
        path: 'eStudent',
        component: EStudentComponent,
        canActivate: [AuthGuard],
        data: {
            allowedPermissions: [
                'ADMINISTRATOR_PERMISSION',
                'STUDENT_PERMISSION',
                'NASTAVNIK_PERMISSION',
                'STUDENTSKAofficeStaff_PERMISSION'
            ]
        }
    },
    {
        path: 'eAdmin',
        component: EAdminComponent,
        canActivate: [AuthGuard],
        data: {
            allowedPermissions: [
                'ADMINISTRATOR_PERMISSION'
            ]
        }
    },
    {
        path: 'eProfesor',
        component: EProfessorComponent,
        canActivate: [AuthGuard],
        data: {
            allowedPermissions: [
                'NASTAVNIK_PERMISSION'
            ]
        }
    },
    {
        path: 'eSofficeStaff',
        component: EOfficeComponent,
        canActivate: [AuthGuard],
        data: {
            allowedPermissions: [
                'STUDENTSKAofficeStaff_PERMISSION'
            ]
        }
    }
];
