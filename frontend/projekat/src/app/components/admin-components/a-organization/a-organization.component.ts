import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UniversityService } from '../../../services/university.service';
import { RectorateService } from '../../../services/rectorate.service';

@Component({
  selector: 'app-a-organization',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './a-organization.component.html',
  styleUrl: './a-organization.component.css',
})
export class AOrganizationComponent implements OnInit {
  university: any = null;
  rectorate: any = null;

  showEditModal = false;
  form: any = {};

  toast: { msg: string; type: 'success' | 'error' } | null = null;

  constructor(
    private universityService: UniversityService,
    private rectorateService: RectorateService,
  ) {}

  ngOnInit(): void {
    this.universityService.getById(1).subscribe(x => (this.university = x));
    this.rectorateService.getById(1).subscribe(x => (this.rectorate = x));
  }

  openEdit() {
    this.form = { ...this.university };
    this.showEditModal = true;
  }

  closeModal() {
    this.showEditModal = false;
    this.form = {};
  }

  save() {
    const payload = { ...this.form };
    if (payload.rectorate && typeof payload.rectorate === 'object') {
      payload.rectorate = payload.rectorate.id;
    }
    this.universityService.update(this.form.id, payload).subscribe({
      next: () => {
        this.universityService.getById(1).subscribe(x => (this.university = x));
        this.closeModal();
        this.showToast('University data updated.', 'success');
      },
      error: () => this.showToast('Error saving changes.', 'error'),
    });
  }

  showToast(msg: string, type: 'success' | 'error') {
    this.toast = { msg, type };
    setTimeout(() => (this.toast = null), 4000);
  }
}

