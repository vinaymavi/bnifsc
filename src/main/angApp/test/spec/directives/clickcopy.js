'use strict';

describe('Directive: clickCopy', function () {

  // load the directive's module
  beforeEach(module('bnifscApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<click-copy></click-copy>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the clickCopy directive');
  }));
});
