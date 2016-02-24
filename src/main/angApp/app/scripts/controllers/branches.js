'use strict';

angular.module('bnifscApp')
  .controller('BranchesCtrl', function ($scope, $window, bnifsc, $stateParams, $timeout) {
    $scope.panelTitle = '';
    $scope.bank = '';
    $scope.state = '';
    $scope.city = '';
    $scope.viewObj = {};
    $scope.quickLinks = [];
    function init() {
      $scope.bank = $stateParams.bank;
      $scope.state = $stateParams.state;
      $scope.city = $stateParams.city;
      $scope.panelTitle = ' > ' + $scope.state;
      bnifsc.branches($scope.bank, $scope.state, $scope.city, (resp)=> {
        $timeout(function () {
          $scope.ajaxrequest = false;
          $scope.viewObj.itemsList = bnifsc.groupByAlphabetical(resp.items);
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
