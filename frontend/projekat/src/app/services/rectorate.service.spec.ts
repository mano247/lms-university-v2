import { TestBed } from '@angular/core/testing';

import { RectorateService } from './rectorate.service';

describe('RectorateService', () => {
  let service: RectorateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RectorateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

