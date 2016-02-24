'use strict';

angular.module('bnifscApp')
  .controller('IfscCtrl', function ($scope, $window, bnifsc, $stateParams, $timeout) {
    $scope.branchProps = {};
    $scope.ajaxrequest=true;
    function init() {
      var ifsc = $stateParams.ifsc;
      bnifsc.branchByIFSC(ifsc, (resp)=> {
        $timeout(()=> {
          $scope.ajaxrequest=false;
          console.log(resp);
          $scope.branchProps = resp;
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
