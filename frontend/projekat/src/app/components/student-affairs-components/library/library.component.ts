import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { CourseMaterial } from '../../../model/academic/teaching-material';
import { StudentOfficeService } from '../../../services/student-affairs.service';
import { TableModule } from 'primeng/table';
import { NgClass, NgIf } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { CourseMaterialService } from '../../../services/teaching-material.service';
import { InputGroupModule } from 'primeng/inputgroup';
import { FormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { CourseService } from '../../../services/course.service';
import { Course } from '../../../model/academic/course';
import { DropdownModule } from 'primeng/dropdown';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-library',
  standalone: true,
  imports: [TableModule, NgClass, ButtonModule, InputGroupModule, FormsModule, NgIf, DialogModule, DropdownModule, ToastModule],
  templateUrl: './library.component.html',
  styleUrl: './library.component.css',
  providers: [MessageService]
})
export class LibraryComponent implements OnInit {
  textbooks: CourseMaterial[] = [];
  filteredTextbooks: CourseMaterial[] = [];

  visible: boolean = false;
  addDialog: boolean = false;

  textbookForEdit: any;
  additionalQuantity = 0;
  courses: Course[] = [];
  newTextbook: any = {};

  search: any = {
    title: '',
    author: ''
  };

  constructor(
    private studentOfficeService: StudentOfficeService,
    private textbookService: CourseMaterialService,
    private courseService: CourseService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.getTextbooks();
    this.getCourses();
  }

  getTextbooks() {
    this.studentOfficeService.getTextbooks().subscribe(x => {
      this.textbooks = x;
      this.textbooks.sort((a, b) => {
        if (a.quantity <= 10 && b.quantity > 10) {
          return -1;
        } else if (a.quantity > 10 && b.quantity <= 10) {
          return 1;
        } else {
          return a.quantity - b.quantity;
        }
      });
      this.filteredTextbooks = this.textbooks;
    });
  }

  getCourses() {
    this.courseService.getAll().subscribe(x => {
      this.courses = x;
    });
  }

  issueTextbook(textbook: CourseMaterial) {
    if (textbook.quantity <= 0) {
      return;
    } else {
      const updated = { ...textbook, quantity: textbook.quantity - 1 };
      if (updated.id) {
        this.textbookService.update(updated.id, updated).subscribe(() => {
          this.getTextbooks();
        });
      }
    }
  }

  requestTextbook(textbook: CourseMaterial) {
    this.visible = true;
    this.textbookForEdit = textbook;
  }

  processRequest() {
    const newQuantity = Number(this.textbookForEdit.quantity) + Number(this.additionalQuantity);
    const updated = { ...this.textbookForEdit, quantity: newQuantity };
    this.textbookService.update(updated.id, updated).subscribe(() => {
      this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Quantity increased.' });
      this.visible = false;
      this.getTextbooks();
      this.additionalQuantity = 0;
    });
  }

  closeDialog() {
    this.visible = false;
  }

  openAddDialog() {
    this.addDialog = true;
  }

  addTextbook() {
    this.textbookService.create(this.newTextbook).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Added', detail: 'Textbook added successfully.' });
        this.newTextbook = {};
        this.getTextbooks();
        this.closeAddDialog();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while adding textbook.' });
      }
    });
  }

  closeAddDialog() {
    this.addDialog = false;
  }

  searchTextbooks() {
    this.filteredTextbooks = this.textbooks.filter(u =>
      (this.search.title ? u.title.toLowerCase().includes(this.search.title.toLowerCase()) : true) &&
      (this.search.author ? u.authors.toLowerCase().includes(this.search.author.toLowerCase()) : true)
    );
  }

  clearSearch() {
    this.search = { title: '', author: '' };
    this.filteredTextbooks = this.textbooks;
  }
}
