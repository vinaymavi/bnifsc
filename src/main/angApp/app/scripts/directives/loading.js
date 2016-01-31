'use strict';

angular.module('bnifscApp')
  .directive('loading', function ($timeout) {
    return {
      restrict: 'C',
      link: function postLink(scope, element, attrs) {
        var numOfAjaxReq = 0;
        /*Ajax req listener*/
        scope.$on("ajaxReq", function () {
          numOfAjaxReq++;
          console.log("ajax request ");

        });

        /*Ajax resp listener*/
        scope.$on("ajaxResp", function () {
          $timeout(()=> {
            numOfAjaxReq--;
          }, 0);
          console.log("ajax resp");
        });

        scope.isLoading = function () {
          console.log("Num Of Ajax Req=" + numOfAjaxReq);
          return numOfAjaxReq === 0
        };

        scope.$watch(scope.isLoading, (v) => {
          if (v) {
            element.hide();
          } else {
            element.show();
          }
        })


      }
    };
  });
