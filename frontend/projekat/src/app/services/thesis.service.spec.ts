import { TestBed } from '@angular/core/testing';

import { ZavrsniRadService } from './thesis.service';

describe('ZavrsniRadService', () => {
  let service: ZavrsniRadService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ZavrsniRadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
