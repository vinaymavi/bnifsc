'use strict';

describe('Controller: IfscCtrl', function () {

  // load the controller's module
  beforeEach(module('bnifscApp'));

  var IfscCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    IfscCtrl = $controller('IfscCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
