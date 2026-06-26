import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { StudentEnrollmentComponent } from "../../student-affairs-components/student-enrollment/student-enrollment.component";
import { DocumentIssuanceComponent } from "../../student-affairs-components/document-issuance/document-issuance.component";
import { AnnouncementsComponent } from "../../announcements/announcements.component";
import { LibraryComponent } from "../../student-affairs-components/library/library.component";
import { OfficeSuppliesComponent } from "../../student-affairs-components/office-supplies/office-supplies.component";
import { TabViewModule } from 'primeng/tabview';
import { SchedulesComponent } from "../../student-affairs-components/schedules/schedules.component";

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-e-sluzba',
  standalone: true,
  imports: [StudentEnrollmentComponent, DocumentIssuanceComponent, AnnouncementsComponent, LibraryComponent, OfficeSuppliesComponent, TabViewModule, SchedulesComponent],
  templateUrl: './e-sluzba.component.html',
  styleUrl: './e-sluzba.component.css'
})
export class ESluzbaComponent implements OnInit {
  selectedTabIndex: number = 0;

  ngOnInit(): void {
    const savedIndex = localStorage.getItem('selectedTabIndex');
    this.selectedTabIndex = savedIndex ? +savedIndex : 0;
  }

  onTabChange(event: any) {
    localStorage.setItem('selectedTabIndex', event.index);
  }
}
