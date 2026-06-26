import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentIssuanceComponent } from './document-issuance.component';

describe('DocumentIssuanceComponent', () => {
  let component: DocumentIssuanceComponent;
  let fixture: ComponentFixture<DocumentIssuanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DocumentIssuanceComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DocumentIssuanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
