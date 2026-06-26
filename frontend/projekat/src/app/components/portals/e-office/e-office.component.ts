import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { StudentEnrollmentComponent } from "../../student-office-components/student-enrollment/student-enrollment.component";
import { DocumentIssuanceComponent } from "../../student-office-components/document-issuance/document-issuance.component";
import { AnnouncementsComponent } from "../../announcements/announcements.component";
import { LibraryComponent } from "../../student-office-components/library/library.component";
import { OfficeSuppliesComponent } from "../../student-office-components/office-supplies/office-supplies.component";
import { TabViewModule } from 'primeng/tabview';
import { SchedulesComponent } from "../../student-office-components/schedules/schedules.component";

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-e-office',
  standalone: true,
  imports: [StudentEnrollmentComponent, DocumentIssuanceComponent, AnnouncementsComponent, LibraryComponent, OfficeSuppliesComponent, TabViewModule, SchedulesComponent],
  templateUrl: './e-office.component.html',
  styleUrl: './e-office.component.css'
})
export class EOfficeComponent implements OnInit{

  selectedTabIndex: number = 0;

  ngOnInit(): void {
    const savedIndex = localStorage.getItem('selectedTabIndex');
    if (savedIndex) {
      this.selectedTabIndex = +savedIndex;
    }else{
      this.selectedTabIndex = 0;
    }
  }

  onTabChange(event: any) {
    localStorage.setItem('selectedTabIndex', event.index);
  }

}
