import { TestBed } from '@angular/core/testing';

import { NastavniMaterijalService } from './teaching-material.service';

describe('NastavniMaterijalService', () => {
  let service: NastavniMaterijalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NastavniMaterijalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
