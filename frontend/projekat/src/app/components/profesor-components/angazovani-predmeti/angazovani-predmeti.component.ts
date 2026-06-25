import { ChangeDetectorRef, Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DataViewModule } from 'primeng/dataview';
import { NgFor } from '@angular/common';
import { DividerModule } from 'primeng/divider';
import { Predmet } from '../../../model/academic/predmet';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { FormsModule} from '@angular/forms';
import { NastavnikService } from '../../../services/nastavnik.service';
import { PredmetService } from '../../../services/predmet.service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-angazovani-predmeti',
  standalone: true,
  imports: [NgFor, DataViewModule, DividerModule, ButtonModule, DialogModule, InputTextareaModule, FormsModule, ToastModule],
  templateUrl: './angazovani-predmeti.component.html',
  styleUrl: './angazovani-predmeti.component.css',
  providers: [MessageService]
})
export class AngazovaniPredmetiComponent implements OnInit{
  visible: boolean = false;

  predmeti: Predmet[] = [];

  predmetSilabus: String | undefined;
  profId: any;

  selectedPredmet: any = null;

  constructor(private nastavnikService: NastavnikService, private predmetService: PredmetService, private messageService: MessageService){}

  ngOnInit(): void {
    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      const id = parsedUser.id;
      this.profId = parsedUser.id;
      this.getPredmeti(id);
    }
  }

  getPredmeti(id: number){
    this.nastavnikService.mojiPredmeti(id).subscribe(x=>{
      this.predmeti = x;
    })
  }

  silabusDialog(predmet: Predmet){
    this.selectedPredmet = predmet;
    this.predmetSilabus = predmet.silabus;
    this.visible = true;
  }

  urediSilabus(){
    if (this.selectedPredmet) {
      const updatedPredmet: Predmet = {
        ...this.selectedPredmet,
        silabus: this.predmetSilabus
      };

      if (updatedPredmet.id !== undefined) {
        this.nastavnikService.izmenaSilabusa(updatedPredmet.id, updatedPredmet).subscribe({
          next: () => {
            this.visible = false;
            this.predmetSilabus = undefined;
            this.getPredmeti(this.profId);

            this.messageService.add({severity:'success', summary: 'Uspeh', detail: 'Silabus uspešno ažuriran'});
          },
          error: () => {
            this.messageService.add({severity:'error', summary: 'Greška', detail: 'Greška prilikom ažuriranja silabusa'});
            this.predmetSilabus = undefined;
          }
        });
      }

    }
  }

  ponistiDialog(){
    this.visible = false;
    this.predmetSilabus = undefined;
  }
}
