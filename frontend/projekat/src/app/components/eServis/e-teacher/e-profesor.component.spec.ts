import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EProfesorComponent } from './e-profesor.component';

describe('EProfesorComponent', () => {
  let component: EProfesorComponent;
  let fixture: ComponentFixture<EProfesorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EProfesorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EProfesorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
