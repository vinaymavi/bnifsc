'use strict';

describe('Directive: bank', function () {

  // load the directive's module
  beforeEach(module('bnifscApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<bank></bank>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the bank directive');
  }));
});
