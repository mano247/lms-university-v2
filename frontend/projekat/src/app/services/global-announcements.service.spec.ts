import { TestBed } from '@angular/core/testing';

import { GlobalnaObavestenjaService } from './global-announcements.service';

describe('GlobalnaObavestenjaService', () => {
  let service: GlobalnaObavestenjaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GlobalnaObavestenjaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
