import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { StudyProgramService } from '../../../services/study-program.service';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputGroupModule } from 'primeng/inputgroup';
import { FormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { StudyProgram } from '../../../model/academic/study-program';
import { DropdownModule } from 'primeng/dropdown';
import { Faculty } from '../../../model/academic/faculty';
import { FacultyService } from '../../../services/faculty.service';
import { ConfirmPopupModule } from 'primeng/confirmpopup';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-a-study-programs',
  standalone: true,
  imports: [TableModule, ButtonModule, InputGroupModule, FormsModule, DialogModule, ToastModule, DropdownModule, ConfirmPopupModule],
  templateUrl: './a-study-programs.component.html',
  styleUrl: './a-study-programs.component.css',
  providers: [MessageService, ConfirmationService]
})
export class AStudyProgramsComponent implements OnInit{
  studyPrograms: any[] = [];
  filteredStudyPrograms: any[] = [];
  faculties: Faculty[] = [];

  editVisible: boolean = false;
  addVisible: boolean = false;

  newStudyProgram: any = {};

  filter = {
    name: '',
    code: '',
    faculty: '',
    programDirector: ''
  };

  selectedProgram: any;
  editForm: any = {
    name: '',
    programDirector: '',
    programCode: ''
  };

  constructor(
    private studyProgramService: StudyProgramService,
    private messageService: MessageService,
    private facultyService: FacultyService,
    private confirmationService: ConfirmationService
  ){}

  ngOnInit(): void {
    this.loadStudyPrograms();
    this.loadFaculties();
  }

  loadStudyPrograms(){
    this.studyProgramService.getAll().subscribe(x=>{
      this.studyPrograms = x;
      this.filteredStudyPrograms = this.studyPrograms;
    });
  }

  loadFaculties(){
    this.facultyService.getAll().subscribe(x=>{
      this.faculties = x;
    });
  }

  openEditDialog(program: StudyProgram){
    this.editVisible = true;
    this.selectedProgram = {...program};
    this.editForm = {...program};
  }

  applyFilter(){
    this.filteredStudyPrograms = this.studyPrograms.filter(s =>
      (this.filter.name ? s.name?.toLowerCase().includes(this.filter.name.toLowerCase()) : true) &&
      (this.filter.code ? s.programCode?.toLowerCase().includes(this.filter.code.toLowerCase()) : true) &&
      (this.filter.faculty ? String(s.faculty?.name || '').toLowerCase().includes(this.filter.faculty.toLowerCase()) : true) &&
      (this.filter.programDirector ? String(s.programDirector || '').toLowerCase().includes(this.filter.programDirector.toLowerCase()) : true)
    );
  }

  clearFilter(){
    this.filter = { name: '', code: '', faculty: '', programDirector: '' };
    this.filteredStudyPrograms = this.studyPrograms;
  }

  saveEdit(){
    if (this.selectedProgram) {
      const payload = {
        ...this.selectedProgram,
        name: this.editForm.name,
        programCode: this.editForm.programCode,
        programDirector: this.editForm.programDirector
      };

      this.studyProgramService.update(payload.id, payload).subscribe({
        next: () => {
          this.loadStudyPrograms();
          this.closeEditDialog();
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Study program updated successfully.' });
        },
        error: () => {
          this.closeEditDialog();
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while updating.' });
        }
      });
    }
  }

  closeEditDialog(){
    this.editVisible = false;
  }

  openAddDialog(){
    this.addVisible = true;
  }

  addStudyProgram(){
    this.studyProgramService.create(this.newStudyProgram).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Added', detail: 'Study program added successfully.' });
        this.newStudyProgram = {};
        this.loadStudyPrograms();
        this.closeAddDialog();
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while adding study program.' });
        console.error(err);
      }
    });
  }

  closeAddDialog(){
    this.addVisible = false;
    this.newStudyProgram = {};
  }

  deleteStudyProgram(id: number, event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure you want to remove this study program?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.studyProgramService.delete(id).subscribe({
          next: () => {
            this.loadStudyPrograms();
            this.messageService.add({ severity: 'info', summary: 'Removed', detail: 'Study program removed.' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while removing.' });
          }
        });
      },
      reject: () => {
        this.messageService.add({ severity: 'warn', summary: 'Cancelled', detail: 'Removal cancelled.', life: 3000 });
      }
    });
  }
}
