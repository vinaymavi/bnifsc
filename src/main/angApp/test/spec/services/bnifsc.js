'use strict';

describe('Service: bnifsc', function () {

  // load the service's module
  beforeEach(module('bnifscApp'));

  // instantiate service
  var bnifsc;
  beforeEach(inject(function (_bnifsc_) {
    bnifsc = _bnifsc_;
  }));

  it('should do something', function () {
    expect(!!bnifsc).toBe(true);
  });

});
