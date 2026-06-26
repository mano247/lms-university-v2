import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GlobalAnnouncementsComponent } from './global-announcements.component';

describe('GlobalAnnouncementsComponent', () => {
  let component: GlobalAnnouncementsComponent;
  let fixture: ComponentFixture<GlobalAnnouncementsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GlobalAnnouncementsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GlobalAnnouncementsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
