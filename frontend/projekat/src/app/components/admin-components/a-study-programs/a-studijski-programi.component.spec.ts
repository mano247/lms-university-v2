import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AStudijskiProgramiComponent } from './a-studijski-programi.component';

describe('AStudijskiProgramiComponent', () => {
  let component: AStudijskiProgramiComponent;
  let fixture: ComponentFixture<AStudijskiProgramiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AStudijskiProgramiComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AStudijskiProgramiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
