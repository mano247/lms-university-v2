import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { UniversityService } from '../../../services/university.service';
import { University } from '../../../model/academic/university';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { FormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { RectorateService } from '../../../services/rectorate.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-a-organizacija',
  standalone: true,
  imports: [ButtonModule, DialogModule, FormsModule, CalendarModule, ToastModule],
  templateUrl: './a-organizacija.component.html',
  styleUrl: './a-organizacija.component.css',
  providers: [MessageService]
})
export class AOrganizacijaComponent implements OnInit {
  university: University | undefined;
  rectorate: any;
  universityForEdit: any = {};
  visible: boolean = false;

  constructor(
    private universityService: UniversityService,
    private messageService: MessageService,
    private rectorateService: RectorateService
  ) {}

  ngOnInit(): void {
    this.getUniversity();
  }

  getUniversity() {
    this.universityService.getById(1).subscribe(x => {
      this.university = x;
    });
  }

  getRectorate() {
    this.rectorateService.getById(1).subscribe(x => {
      this.rectorate = x;
    });
  }

  openEditDialog() {
    this.visible = true;
    this.universityForEdit = { ...this.university };
  }

  saveChanges() {
    const universityData = { ...this.universityForEdit, rectorate: this.universityForEdit.rectorate.id };
    this.universityService.update(this.universityForEdit.id, universityData).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'University data updated successfully.' });
        this.visible = false;
        this.getUniversity();
        this.universityForEdit = {};
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while updating university data.' });
      }
    });
  }

  cancelDialog() {
    this.visible = false;
    this.universityForEdit = {};
  }
}
