import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AStudyProgramsComponent } from './a-study-programs.component';

describe('AStudyProgramsComponent', () => {
  let component: AStudyProgramsComponent;
  let fixture: ComponentFixture<AStudyProgramsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AStudyProgramsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AStudyProgramsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
