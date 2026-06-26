import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RectorateComponent } from './rectorate.component';

describe('RectorateComponent', () => {
  let component: RectorateComponent;
  let fixture: ComponentFixture<RectorateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RectorateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RectorateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
