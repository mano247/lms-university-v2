import { TestBed } from '@angular/core/testing';

import { NastavnikService } from './teacher.service';

describe('NastavnikService', () => {
  let service: NastavnikService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NastavnikService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
