'use strict';

/**
 * @ngdoc function
 * @name bnifscApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the bnifscApp
 */
angular.module('bnifscApp')
  .controller('HomeCtrl', function ($scope, $q, $window, bnifsc, $routeParams, $location, $mdDialog, $timeout, $log) {
    var self = this;
    //TODO add comments.
    $scope.bnifsc = bnifsc;
    $scope.viewObj = {};
    $scope.branchProps;
    $scope.data = {};
    $scope.panelTitle;
    $scope.autocompleteData;
    $scope.ajaxrequest = true;
    $scope.quickLinks = [];

    /*This function has to much logic split logic to different controllers*/
    function init() {
      /*Main page*/
      $scope.ajaxrequest = true;
      $scope.panelTitle = 'All Indian Bank List.'
      bnifsc.banks(function (resp) {
        $timeout(function () {
          $scope.ajaxrequest = false;
          $scope.viewObj.itemsList = bnifsc.groupByAlphabetical(resp.items);
          $scope.quickLinks = $scope.quickLinks.concat(Object.keys($scope.viewObj.itemsList));
        }, 0);
      });
      /*Google analytics code*/
      ga('set', {page: $location.path(), title: $scope.panelTitle});
      /*Google analytics code*/
      ga('send', 'pageview');
    }


    // controller initialization
    if (bnifsc.appLoaded()) {
      init();
    }

    // controller initialization when apploaded successfully.
    $window.init = function () {
      console.log("window init calling");
      bnifsc.appLoaded(true);
      init();
    }
  });
