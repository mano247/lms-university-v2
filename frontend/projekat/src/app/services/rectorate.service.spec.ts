import { TestBed } from '@angular/core/testing';

import { RektoratService } from './rectorate.service';

describe('RektoratService', () => {
  let service: RektoratService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RektoratService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
