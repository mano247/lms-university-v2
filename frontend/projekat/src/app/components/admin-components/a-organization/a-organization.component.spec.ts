import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AOrganizationComponent } from './a-organization.component';

describe('AOrganizationComponent', () => {
  let component: AOrganizationComponent;
  let fixture: ComponentFixture<AOrganizationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AOrganizationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AOrganizationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
