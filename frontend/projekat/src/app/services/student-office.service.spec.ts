import { TestBed } from '@angular/core/testing';

import { StudentOfficeService } from './student-office.service';

describe('StudentOfficeService', () => {
  let service: StudentOfficeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StudentOfficeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
