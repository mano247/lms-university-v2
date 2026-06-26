import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudyHistoryComponent } from './study-history.component';

describe('StudyHistoryComponent', () => {
  let component: StudyHistoryComponent;
  let fixture: ComponentFixture<StudyHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudyHistoryComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(StudyHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
