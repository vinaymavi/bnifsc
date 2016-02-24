'use strict';

angular.module('bnifscApp')
  .controller('CityCtrl', function ($scope, $window, bnifsc, $stateParams, $timeout) {
    $scope.panelTitle = '';
    $scope.bank = '';
    $scope.state = '';
    $scope.viewObj = {};
    $scope.quickLinks=[];
    function init() {
      $scope.bank = $stateParams.bank;
      $scope.state = $stateParams.state;
      $scope.panelTitle = ' > ' + $scope.state;
      bnifsc.districts($scope.bank, $scope.state, (resp)=> {
        $timeout(function () {
          $scope.ajaxrequest = false;
          $scope.viewObj.itemsList = bnifsc.groupByAlphabetical(resp.items, 'district');
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
