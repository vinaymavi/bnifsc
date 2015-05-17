'use strict';

/**
 * @ngdoc directive
 * @name bnifscApp.directive:clickCopy
 * @description
 * # clickCopy
 * register click event and copy text to clipboard.
 */
angular.module('bnifscApp')
  .directive('clickCopy', function () {
    return {
      template: '<div></div>',
      restrict: 'E',
      link: function postLink(scope, element, attrs) {
        element.text('this is the clickCopy directive');
      }
    };
  });
