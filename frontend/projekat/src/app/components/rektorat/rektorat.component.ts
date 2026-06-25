import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { RektoratService } from '../../services/rektorat.service';
import { UniverzitetService } from '../../services/univerzitet.service';
import { DividerModule } from 'primeng/divider';
import { Rektorat } from '../../model/rektorat';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-rektorat',
  standalone: true,
  imports: [DividerModule],
  templateUrl: './rektorat.component.html',
  styleUrl: './rektorat.component.css'
})
export class RektoratComponent implements OnInit{
  private rektorat_id = 1;
  rektorat: Rektorat = {
    naziv: '',
    kontakt: '',
    slika: '',
    adresa: '',
    univerziteti: [],
    ime_rektora: ''
  }
  

  constructor(private rektoratService: RektoratService) {}

  ngOnInit(): void {
    this.getRektorat();
  }

  getRektorat(){
    return this.rektoratService.getById(this.rektorat_id).subscribe(x=>{
      this.rektorat = x;
    })
  }

  
}
