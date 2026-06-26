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
  selector: 'app-a-studijski-programi',
  standalone: true,
  imports: [TableModule, ButtonModule, InputGroupModule, FormsModule, DialogModule, ToastModule, DropdownModule, ConfirmPopupModule],
  templateUrl: './a-studijski-programi.component.html',
  styleUrl: './a-studijski-programi.component.css',
  providers: [MessageService, ConfirmationService]
})
export class AStudijskiProgramiComponent implements OnInit {
  studyPrograms: any[] = [];
  filteredStudyPrograms: any[] = [];
  faculties: Faculty[] = [];

  visible: boolean = false;
  addProgramDialog: boolean = false;

  newProgram: any = {};

  search = {
    name: '',
    code: '',
    faculty: '',
    director: ''
  };

  selectedProgram: any;
  programForEdit: any = {
    name: '',
    director: '',
    programCode: ''
  };

  constructor(
    private studyProgramService: StudyProgramService,
    private messageService: MessageService,
    private facultyService: FacultyService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.getStudyPrograms();
    this.getFaculties();
  }

  getStudyPrograms() {
    this.studyProgramService.getAll().subscribe(x => {
      this.studyPrograms = x;
      this.filteredStudyPrograms = this.studyPrograms;
    });
  }

  getFaculties() {
    this.facultyService.getAll().subscribe(x => {
      this.faculties = x;
    });
  }

  openEditDialog(program: StudyProgram) {
    this.visible = true;
    this.selectedProgram = { ...program };
    this.programForEdit = this.selectedProgram;
  }

  searchPrograms() {
    this.filteredStudyPrograms = this.studyPrograms.filter(s =>
      (this.search.name ? s.name.toLowerCase().includes(this.search.name.toLowerCase()) : true) &&
      (this.search.code ? s.programCode.toLowerCase().includes(this.search.code.toLowerCase()) : true) &&
      (this.search.faculty ? s.faculty.toLowerCase().includes(this.search.faculty.toLowerCase()) : true) &&
      (this.search.director ? s.programDirector.toLowerCase().includes(this.search.director.toLowerCase()) : true)
    );
  }

  clearSearch() {
    this.search = { name: '', code: '', faculty: '', director: '' };
    this.filteredStudyPrograms = this.studyPrograms;
  }

  saveChanges() {
    if (this.selectedProgram) {
      const updatedProgram = {
        ...this.selectedProgram,
        name: this.programForEdit.name,
        programCode: this.programForEdit.programCode,
        programDirector: this.programForEdit.director
      };

      this.studyProgramService.update(updatedProgram.id, updatedProgram).subscribe({
        next: () => {
          this.getStudyPrograms();
          this.cancelDialog();
          this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Study program updated successfully.' });
        },
        error: () => {
          this.cancelDialog();
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while updating.' });
        }
      });
    }
  }

  cancelDialog() {
    this.visible = false;
  }

  openAddDialog() {
    this.addProgramDialog = true;
  }

  addProgram() {
    this.studyProgramService.create(this.newProgram).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Added', detail: 'Study program added successfully.' });
        this.newProgram = {};
        this.getStudyPrograms();
        this.closeAddDialog();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while adding the program.' });
      }
    });
  }

  closeAddDialog() {
    this.addProgramDialog = false;
    this.newProgram = {};
  }

  removeProgram(id: number, event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure you want to remove this study program?',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      accept: () => {
        this.studyProgramService.delete(id).subscribe({
          next: () => {
            this.getStudyPrograms();
            this.messageService.add({ severity: 'info', summary: 'Removed', detail: 'Study program removed successfully.' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while removing.' });
          }
        });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Cancelled', detail: 'Removal cancelled.', life: 3000 });
      }
    });
  }
}
