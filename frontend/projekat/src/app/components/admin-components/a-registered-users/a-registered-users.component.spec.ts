import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ARegisteredUsersComponent } from './a-registered-users.component';

describe('ARegisteredUsersComponent', () => {
  let component: ARegisteredUsersComponent;
  let fixture: ComponentFixture<ARegisteredUsersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ARegisteredUsersComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ARegisteredUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
