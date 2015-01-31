'use strict';

/**
 * @ngdoc function
 * @name bnifscApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the bnifscApp
 */
angular.module('bnifscApp')
	.controller('MainCtrl', function($scope, $window, bnifsc) {
		console.log("Controller loaded");
		$scope.bnifsc = bnifsc;
		$scope.name="vinay kumar";
		$scope.arr=[1,2,3,4,5];
		$window.init = function() {
			console.log("window init calling");
			bnifsc.banks(function(){
				$scope.$apply($scope.bnifsc);
			});
		}
	});