'use strict';

angular.module('bnifscApp')
  .controller('StateCtrl', function ($scope, $window, bnifsc, $stateParams, $timeout) {
    $scope.quickLinks = [];
    $scope.viewObj = {};
    $scope.panelTitle = "";
    $scope.bank;
    function init() {
      console.log($stateParams.bank);
      $scope.bank = $stateParams.bank;
      $scope.panelTitle = " > " + $scope.bank;
      bnifsc.states($scope.bank, function (resp) {
        $timeout(function () {
          $scope.ajaxrequest = false;
          $scope.viewObj.itemsList = bnifsc.groupByAlphabetical(resp.items, 'state');
          $scope.quickLinks = $scope.quickLinks.concat(Object.keys($scope.viewObj.itemsList));
        }, 0);
      });
    }

    /*controller initialization*/
    if (bnifsc.appLoaded()) {
      init();
    }
    $window.init = ()=> {
      console.log("window init calling");
      bnifsc.appLoaded(true);
      init();
    }
  });
