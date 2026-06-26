import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { MojiPredmetiComponent } from "../../student-components/moji-predmeti/moji-predmeti.component";
import { PredmetnaObavestenjaComponent } from "../../student-components/predmetna-obavestenja/predmetna-obavestenja.component";
import { IstorijaStudiranjaComponent } from "../../student-components/istorija-studiranja/istorija-studiranja.component";
import { PrijavaIspitaComponent } from "../../student-components/prijava-ispita/prijava-ispita.component";
import { TabViewModule } from 'primeng/tabview';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-e-student',
  standalone: true,
  imports: [MojiPredmetiComponent, PredmetnaObavestenjaComponent, IstorijaStudiranjaComponent, PrijavaIspitaComponent, TabViewModule],
  templateUrl: './e-student.component.html',
  styleUrl: './e-student.component.css'
})
export class EStudentComponent implements OnInit {
  selectedTabIndex: number = 0;

  ngOnInit(): void {
    const savedIndex = localStorage.getItem('selectedTabIndex');
    this.selectedTabIndex = savedIndex ? +savedIndex : 0;
  }

  onTabChange(event: any) {
    localStorage.setItem('selectedTabIndex', event.index);
  }
}
