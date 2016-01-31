'use strict';

angular.module('bnifscApp')
  .controller('AdminCtrl', function ($scope, $window, bnifsc, admin, $timeout) {
    $scope.bankFilter = '';

    /*How many ajax request is active.*/
    function init() {
      if (admin.appLoaded) {
        bnifsc.banks(function (resp) {
          $timeout(function () {
            $scope.banks = resp.items;
          }, 0)
        });
      }
    }

    /*Initialization of function*/
    $window.init = function () {
      console.log("init calling");
      admin.appLoaded = true;
      $scope.$emit("appLoaded");
    }

    $scope.$on("appLoaded", function () {
      init();
    });

    init();
  });
