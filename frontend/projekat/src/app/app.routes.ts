import { Routes } from '@angular/router';
import { UniverzitetComponent } from './components/univerzitet/univerzitet.component';
import { UpisComponent } from './components/upis/upis.component';
import { KontaktComponent } from './components/kontakt/kontakt.component';
import { RektoratComponent } from './components/rektorat/rektorat.component';
import { RegisterComponent } from './components/register/register.component';
import { FakultetiComponent } from './components/fakulteti/fakulteti.component';
import { FakultetComponent } from './components/fakultet/fakultet.component';
import { StudijskiProgramComponent } from './components/studijski-program/studijski-program.component';
import { PredmetComponent } from './components/predmet/predmet.component';
import { NastavniMaterijalComponent } from './components/nastavni-materijal/nastavni-materijal.component';
import { ProfilComponent } from './components/profil/profil.component';
import { ObavestenjaComponent } from './components/obavestenja/obavestenja.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { GlobalnaObavestenjaComponent } from './components/globalna-obavestenja/globalna-obavestenja.component';
import { LoginComponent } from './components/login/login.component';
import { UnauthorizedComponent } from './components/unauthorized/unauthorized.component';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';
import { EStudentComponent } from './components/eServis/e-student/e-student.component';
import { EProfesorComponent } from './components/eServis/e-profesor/e-profesor.component';
import { ESluzbaComponent } from './components/eServis/e-sluzba/e-sluzba.component';
import { EAdminComponent } from './components/eServis/e-admin/e-admin.component';

export const routes: Routes = [
  { path: '', component: UniverzitetComponent },
  { path: 'upis', component: UpisComponent },
  { path: 'kontakt', component: KontaktComponent },
  { path: 'rektorat', component: RektoratComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'fakulteti', component: FakultetiComponent },
  { path: 'fakultet/:sifraFakulteta', component: FakultetComponent },
  {
    path: 'fakultet/:sifraFakulteta/studijski-program/:sifraSP',
    component: StudijskiProgramComponent,
  },
  {
    path: 'fakultet/:sifraFakulteta/studijski-program/:sifraSP/predmet/:sifraPredmeta',
    component: PredmetComponent,
  },
  {
    path: 'fakultet/:sifraFakulteta/studijski-program/:sifraSP/predmet/:sifraPredmeta/nastavni-materijal/:nMaterijalNaziv',
    component: NastavniMaterijalComponent,
  },
  {
    path: 'moj-profil',
    component: ProfilComponent,
    canActivate: [authGuard],
  },
  {
    path: 'obavestenja',
    component: ObavestenjaComponent,
    canActivate: [authGuard],
  },
  {
    path: 'menu',
    component: DashboardComponent,
    canActivate: [authGuard],
  },
  { path: 'sva_obavestenja', component: GlobalnaObavestenjaComponent },
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
    path: 'eProfesor',
    component: EProfesorComponent,
    canActivate: [authGuard, roleGuard],
    data: { allowedPermissions: ['TEACHER_PERMISSION'] },
  },
  {
    path: 'eSSluzba',
    component: ESluzbaComponent,
    canActivate: [authGuard, roleGuard],
    data: { allowedPermissions: ['STUDENT_AFFAIRS_PERMISSION'] },
  },
  { path: 'unauthorized', component: UnauthorizedComponent },
  { path: '**', redirectTo: '' },
];
