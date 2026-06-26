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
  selector: 'app-a-organization',
  standalone: true,
  imports: [ButtonModule, DialogModule, FormsModule, CalendarModule, ToastModule],
  templateUrl: './a-organization.component.html',
  styleUrl: './a-organization.component.css',
  providers: [MessageService]
})
export class AOrganizationComponent implements OnInit{
  university: University | undefined;
  editUniversity: any = {};
  visible: boolean = false;

  constructor(private universityService: UniversityService, private messageService: MessageService, private rectorateService: RectorateService){}

  ngOnInit(): void {
    this.getUniversity();
  }

  getUniversity(){
    this.universityService.getById(1).subscribe(x=>{
      this.university = x;
    })
  }

  openEditDialog(){
    this.visible = true;
    this.editUniversity = {...this.university};
  }

  saveChanges(){
    const payload = {...this.editUniversity, rectorate: this.editUniversity.rectorate?.id};
    this.universityService.update(this.editUniversity.id, payload).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'University data updated successfully.' });
        this.visible = false;
        this.getUniversity();
        this.editUniversity = {};
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while updating university data.' });
      }
    });
  }

  closeDialog(){
    this.visible = false;
    this.editUniversity = {};
  }



}
