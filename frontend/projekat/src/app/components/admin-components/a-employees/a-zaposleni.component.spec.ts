import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AZaposleniComponent } from './a-zaposleni.component';

describe('AZaposleniComponent', () => {
  let component: AZaposleniComponent;
  let fixture: ComponentFixture<AZaposleniComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AZaposleniComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AZaposleniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
