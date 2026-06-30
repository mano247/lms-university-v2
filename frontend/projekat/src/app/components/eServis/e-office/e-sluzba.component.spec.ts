import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ESluzbaComponent } from './e-sluzba.component';

describe('ESluzbaComponent', () => {
  let component: ESluzbaComponent;
  let fixture: ComponentFixture<ESluzbaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ESluzbaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ESluzbaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
