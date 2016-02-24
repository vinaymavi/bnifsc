'use strict';

angular.module('bnifscApp')
  .controller('IfscCtrl', function ($scope, $window, bnifsc, $stateParams, $timeout) {
    $scope.branchProps = {};
    function init() {
      var ifsc = $stateParams.ifsc;
      bnifsc.branchByIFSC(ifsc, (resp)=> {
        $timeout(()=> {
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
