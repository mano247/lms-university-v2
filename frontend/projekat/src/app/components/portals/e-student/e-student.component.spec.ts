import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EStudentComponent } from './e-student.component';

describe('EStudentComponent', () => {
  let component: EStudentComponent;
  let fixture: ComponentFixture<EStudentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EStudentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EStudentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
