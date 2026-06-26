import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AOrganizacijaComponent } from './a-organizacija.component';

describe('AOrganizacijaComponent', () => {
  let component: AOrganizacijaComponent;
  let fixture: ComponentFixture<AOrganizacijaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AOrganizacijaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AOrganizacijaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
