import { TestBed } from '@angular/core/testing';

import { PredmetnoObavestenjeService } from './course-announcement.service';

describe('PredmetnoObavestenjeService', () => {
  let service: PredmetnoObavestenjeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PredmetnoObavestenjeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
