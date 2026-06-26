import { TestBed } from '@angular/core/testing';

import { PolaganjeService } from './exam-attempt.service';

describe('PolaganjeService', () => {
  let service: PolaganjeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PolaganjeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
