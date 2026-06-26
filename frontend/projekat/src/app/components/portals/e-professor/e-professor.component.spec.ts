import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EProfessorComponent } from './e-professor.component';

describe('EProfessorComponent', () => {
  let component: EProfessorComponent;
  let fixture: ComponentFixture<EProfessorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EProfessorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EProfessorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
