import { TestBed } from '@angular/core/testing';

import { KancelarijskiMaterijalService } from './office-supplies.service';

describe('KancelarijskiMaterijalService', () => {
  let service: KancelarijskiMaterijalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KancelarijskiMaterijalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
