import { TestBed } from '@angular/core/testing';

import { InputValidatorService } from './input-validator.service';

describe('InputValidatorService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: InputValidatorService = TestBed.get(InputValidatorService);
    expect(service).toBeTruthy();
  });
});
