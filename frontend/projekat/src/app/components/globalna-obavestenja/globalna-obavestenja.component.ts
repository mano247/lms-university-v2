import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DataViewModule } from 'primeng/dataview';
import { NgFor } from '@angular/common';
import { ObavestenjeService } from '../../services/obavestenje.service';
import { DividerModule } from 'primeng/divider';
import { GObavestenje } from '../../model/gObavestenje';
import { DatePipe } from '@angular/common';
import { GlobalnaObavestenjaService } from '../../services/globalna-obavestenja.service';



@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-globalna-obavestenja',
  standalone: true,
  imports: [NgFor, DataViewModule, DividerModule],
  templateUrl: './globalna-obavestenja.component.html',
  styleUrl: './globalna-obavestenja.component.css',
  providers: [DatePipe]
})
export class GlobalnaObavestenjaComponent implements OnInit{

  obavestenja: GObavestenje[] = [];

  constructor(private globalObavestenjeService: GlobalnaObavestenjaService, private datePipe: DatePipe){}

  ngOnInit(): void {
    this.getObavestenja();
  }

  getObavestenja(){
    this.globalObavestenjeService.getAll().subscribe(x=>{
      this.obavestenja = x;
    })
  }

  formatDatuma(date: Date): string {
    const formattedDate = this.datePipe.transform(date, 'HH:mm, d. MMM yyyy.');
    return formattedDate ?? '';
  }


}
