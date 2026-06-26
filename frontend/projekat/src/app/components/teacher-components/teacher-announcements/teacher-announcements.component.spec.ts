import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeacherAnnouncementsComponent } from './teacher-announcements.component';

describe('TeacherAnnouncementsComponent', () => {
  let component: TeacherAnnouncementsComponent;
  let fixture: ComponentFixture<TeacherAnnouncementsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TeacherAnnouncementsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TeacherAnnouncementsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
