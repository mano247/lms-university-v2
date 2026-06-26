import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { UpisStudenataComponent } from "../../ssluzba-components/upis-studenata/upis-studenata.component";
import { IzdavanjeDokumenataComponent } from "../../ssluzba-components/izdavanje-dokumenata/izdavanje-dokumenata.component";
import { ObavestenjaComponent } from "../../obavestenja/obavestenja.component";
import { SsBibliotekaComponent } from "../../ssluzba-components/ss-biblioteka/ss-biblioteka.component";
import { SsKancComponent } from "../../ssluzba-components/ss-kanc/ss-kanc.component";
import { TabViewModule } from 'primeng/tabview';
import { RasporediComponent } from "../../ssluzba-components/rasporedi/rasporedi.component";

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-e-sluzba',
  standalone: true,
  imports: [UpisStudenataComponent, IzdavanjeDokumenataComponent, ObavestenjaComponent, SsBibliotekaComponent, SsKancComponent, TabViewModule, RasporediComponent],
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
