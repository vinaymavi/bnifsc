'use strict';

/**
 * @ngdoc directive
 * @name bnifscApp.directive:clickCopy
 * @description
 * # clickCopy
 * register click event and copy text to clipboard.
 */
angular.module('bnifscApp')
  .directive('clickCopy', function ($window) {
    return {
      restrict: 'E',
      link: function postLink(scope, element, attrs) {
          element.click(function($event){
              console.log("click event");
              console.log(scope);
              console.log(element.text());
              console.log($window);
              console.log($event);
          })
        console.log(element);
      }
    };
  });
