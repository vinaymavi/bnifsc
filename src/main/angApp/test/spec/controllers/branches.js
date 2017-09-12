'use strict';

describe('Controller: BranchesCtrl', function () {

  // load the controller's module
  beforeEach(module('bnifscApp'));

  var BranchesCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    BranchesCtrl = $controller('BranchesCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
