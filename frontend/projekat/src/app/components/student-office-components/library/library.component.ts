import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { CourseMaterial } from '../../../model/academic/course-material';
import { StudentOfficeService } from '../../../services/student-office.service';
import { TableModule } from 'primeng/table';
import { NgClass, NgIf } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { CourseMaterialService } from '../../../services/course-material.service';
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
  materials: CourseMaterial[] = [];
  filteredMaterials: CourseMaterial[] = [];

  visible: boolean = false;
  addVisible: boolean = false;

  editMaterial: any;
  newQuantity = 0;
  courses: Course[] = [];
  newMaterial: any = {};

  search: any = {
    title: '',
    author: ''
  };

  constructor(
    private officeStaffService: StudentOfficeService,
    private materialService: CourseMaterialService,
    private courseService: CourseService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadMaterials();
    this.loadCourses();
  }

  loadMaterials() {
    this.officeStaffService.getUdzbenici().subscribe(x => {
      this.materials = x;
      this.materials.sort((a, b) => {
        const qa = a.quantity ?? 0;
        const qb = b.quantity ?? 0;
        if (qa <= 10 && qb > 10) return -1;
        if (qa > 10 && qb <= 10) return 1;
        return qa - qb;
      });
      this.filteredMaterials = this.materials;
    });
  }

  loadCourses() {
    this.courseService.getAll().subscribe(x => {
      this.courses = x;
    });
  }

  issueTextbook(material: CourseMaterial) {
    if ((material.quantity ?? 0) <= 0) return;
    const updated = { ...material, quantity: (material.quantity ?? 0) - 1 };
    if (updated.id) {
      this.materialService.update(updated.id, updated).subscribe(() => {
        this.loadMaterials();
      });
    }
  }

  openRequisitionDialog(material: CourseMaterial) {
    this.visible = true;
    this.editMaterial = material;
  }

  submitRequisition() {
    const newQty = Number(this.editMaterial.quantity ?? 0) + Number(this.newQuantity);
    const updated = { ...this.editMaterial, quantity: newQty };
    this.materialService.update(updated.id, updated).subscribe(() => {
      this.messageService.add({ severity: 'success', summary: 'Quantity updated', detail: 'Quantity increased.' });
      this.visible = false;
      this.loadMaterials();
      this.newQuantity = 0;
    });
  }

  closeDialog() {
    this.visible = false;
  }

  openAddForm() {
    this.addVisible = true;
  }

  addMaterial() {
    this.materialService.create(this.newMaterial).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Textbook added', detail: 'Textbook successfully added.' });
        this.newMaterial = {};
        this.loadMaterials();
        this.closeAddDialog();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while adding the textbook.' });
      }
    });
  }

  closeAddDialog() {
    this.addVisible = false;
  }

  searchMaterials() {
    this.filteredMaterials = this.materials.filter(u =>
      (this.search.title ? u.title.toLowerCase().includes(this.search.title.toLowerCase()) : true) &&
      (this.search.author ? u.authors.toLowerCase().includes(this.search.author.toLowerCase()) : true)
    );
  }

  clearFilter() {
    this.search = { title: '', author: '' };
    this.filteredMaterials = this.materials;
  }
}
