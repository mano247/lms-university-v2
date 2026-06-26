import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EOfficeComponent } from './e-office.component';

describe('EOfficeComponent', () => {
  let component: EOfficeComponent;
  let fixture: ComponentFixture<EOfficeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EOfficeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EOfficeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
