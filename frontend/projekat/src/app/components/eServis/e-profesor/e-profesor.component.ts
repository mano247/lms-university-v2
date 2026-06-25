import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { TabViewModule } from 'primeng/tabview';
import { AngazovaniPredmetiComponent } from "../../profesor-components/angazovani-predmeti/angazovani-predmeti.component";
import { PredmetnaObavestenjaComponent } from "../../student-components/predmetna-obavestenja/predmetna-obavestenja.component";
import { SpisakStudenataComponent } from "../../profesor-components/spisak-studenata/spisak-studenata.component";
import { UnosOcenaComponent } from "../../profesor-components/unos-ocena/unos-ocena.component";
import { ObavestenjaProfComponent } from "../../profesor-components/obavestenja-prof/obavestenja-prof.component";

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-e-profesor',
  standalone: true,
  imports: [TabViewModule, AngazovaniPredmetiComponent, PredmetnaObavestenjaComponent, SpisakStudenataComponent, UnosOcenaComponent, ObavestenjaProfComponent],
  templateUrl: './e-profesor.component.html',
  styleUrl: './e-profesor.component.css'
})
export class EProfesorComponent implements OnInit{

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
