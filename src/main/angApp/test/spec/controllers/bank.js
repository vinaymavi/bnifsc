'use strict';

describe('Controller: BankCtrl', function () {

  // load the controller's module
  beforeEach(module('bnifscApp'));

  var BankCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    BankCtrl = $controller('BankCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
