import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NgFor } from '@angular/common';
import { HeaderComponent } from "./components/layout/header/header.component";
import { FooterComponent } from "./components/layout/footer/footer.component";
import { Univerzitet } from './model/academic/univerzitet';
import { UniverzitetService } from './services/univerzitet.service';
import { MenubarModule } from 'primeng/menubar';
import { FakultetService } from './services/fakultet.service';
import { Fakultet } from './model/academic/fakultet';
import { MenuBarComponent } from "./components/layout/menu-bar/menu-bar.component";
import { Title } from '@angular/platform-browser';
import { StudijskiProgramService } from './services/studijski-program.service';
import { StudijskiProgram } from './model/academic/studijskiProgram';
import { TabViewModule } from 'primeng/tabview';



@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgFor, HeaderComponent, FooterComponent, MenubarModule, MenuBarComponent, TabViewModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = "LMS";

  univerzitet: Univerzitet = {
    naziv: '',
    datumOsnivanja: new Date,
    kontakt: '',
    opis: '',
    slika: '',
    adresa: '',
    rektorat: {
      naziv: '',
      kontakt: '',
      slika: '',
      adresa: '',
      univerziteti: [],
      ime_rektora: ''
    }
  };

  studijskiProgrami: StudijskiProgram[] = [];
  fakulteti: Fakultet[] = [];

  constructor(private univerzitetService: UniverzitetService, private fakultetService: FakultetService, private titleService: Title, private spService: StudijskiProgramService){}

  ngOnInit(): void {
    this.titleService.setTitle("LMS Univerzitet");
    this.getUniverzitet();
    this.getFakulteti();
    this.getStudijskiProgrami();
  }

  getUniverzitet(){
    return this.univerzitetService.getAll().subscribe(x=>{
      this.univerzitet = x[0];
    })
  }

  getFakulteti(){
    return this.fakultetService.getAll().subscribe( f => {
      this.fakulteti = f;
    })
  }

  getStudijskiProgrami(){
    return this.spService.getAll().subscribe( x => {
      this.studijskiProgrami = x;
    })
  }

}
