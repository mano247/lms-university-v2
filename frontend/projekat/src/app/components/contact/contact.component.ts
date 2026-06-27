import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UniversityService } from '../../services/university.service';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './contact.component.html',
  styleUrl: './contact.component.css',
})
export class ContactComponent implements OnInit {
  universityContact = '';
  universityAddress = '';

  contactForm = { name: '', email: '', subject: '', message: '' };
  toast: { msg: string; type: 'success' | 'error' } | null = null;

  constructor(private universityService: UniversityService) {}

  ngOnInit(): void {
    this.universityService.getById(1).subscribe({
      next: (x) => {
        this.universityAddress = x.address ?? '';
        this.universityContact = x.contact ?? '';
      },
    });
  }

  submitForm(): void {
    const { name, email, subject, message } = this.contactForm;
    if (!name || !email || !subject || !message) {
      this.showToast('Please fill in all fields.', 'error');
      return;
    }
    this.showToast('Message sent! We will get back to you soon.', 'success');
    this.contactForm = { name: '', email: '', subject: '', message: '' };
  }

  private showToast(msg: string, type: 'success' | 'error'): void {
    this.toast = { msg, type };
    setTimeout(() => (this.toast = null), 4000);
  }
}
