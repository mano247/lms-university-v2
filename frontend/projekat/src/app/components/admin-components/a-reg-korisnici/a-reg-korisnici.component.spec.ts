import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ARegKorisniciComponent } from './a-reg-korisnici.component';

describe('ARegKorisniciComponent', () => {
  let component: ARegKorisniciComponent;
  let fixture: ComponentFixture<ARegKorisniciComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ARegKorisniciComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ARegKorisniciComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
