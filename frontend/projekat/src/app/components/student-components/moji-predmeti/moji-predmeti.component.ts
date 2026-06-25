import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DataViewModule } from 'primeng/dataview';
import { NgFor } from '@angular/common';
import { DividerModule } from 'primeng/divider';
import { Predmet } from '../../../model/academic/predmet';
import { StudentiService } from '../../../services/studenti.service';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-moji-predmeti',
  standalone: true,
  imports: [NgFor, DataViewModule, DividerModule],
  templateUrl: './moji-predmeti.component.html',
  styleUrl: './moji-predmeti.component.css'
})
export class MojiPredmetiComponent  implements OnInit{
  mojiPredmeti: Predmet[] = [];

  polozeni: Predmet[] = [];
  nepolozeni: Predmet[] =[];


  constructor(private studentService: StudentiService){}

  ngOnInit(): void {
    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      const id = parsedUser.id;
      this.getMojiPredmeti(id);
    }


  }

  getMojiPredmeti(id: number){
    this.studentService.getAktivniPredmeti(id).subscribe(x=>{
      this.mojiPredmeti = x;
      console.log("mp", this.mojiPredmeti);
    })
  }


  formatirajDatum(vreme: Date | undefined){
    if(vreme){
      const datum = new Date(vreme);

      const godina = datum.getUTCFullYear();
      const mesec = (datum.getUTCMonth() + 1).toString().padStart(2, '0'); 
      const dan = datum.getUTCDate().toString().padStart(2, '0');

      return `${godina}-${mesec}-${dan}`;
    }else{
      return "";
    }
  }

}

