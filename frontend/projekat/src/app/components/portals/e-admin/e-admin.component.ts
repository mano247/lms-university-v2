import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { TabViewModule } from 'primeng/tabview';
import { RegistryComponent } from "../../admin-components/registry/registry.component";
import { ARegisteredUsersComponent } from "../../admin-components/a-registered-users/a-registered-users.component";
import { AStudyProgramsComponent } from "../../admin-components/a-study-programs/a-study-programs.component";
import { AOrganizationComponent } from "../../admin-components/a-organization/a-organization.component";
import { AEmployeesComponent } from "../../admin-components/a-employees/a-employees.component";

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-e-admin',
  standalone: true,
  imports: [TabViewModule, RegistryComponent, ARegisteredUsersComponent, AStudyProgramsComponent, AOrganizationComponent, AEmployeesComponent],
  templateUrl: './e-admin.component.html',
  styleUrl: './e-admin.component.css'
})
export class EAdminComponent implements OnInit{

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
