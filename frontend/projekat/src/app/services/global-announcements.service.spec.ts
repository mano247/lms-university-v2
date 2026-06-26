import { TestBed } from '@angular/core/testing';

import { GlobalAnnouncementsService } from './global-announcements.service';

describe('GlobalAnnouncementsService', () => {
  let service: GlobalAnnouncementsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GlobalAnnouncementsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
