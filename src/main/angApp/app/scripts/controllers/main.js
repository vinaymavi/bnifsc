'use strict';

/**
 * @ngdoc function
 * @name bnifscApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the bnifscApp
 */
angular.module('bnifscApp')
	.controller('MainCtrl', function($scope, $window, bnifsc,$routeParams) {
		console.log("Controller loaded");
		$scope.bnifsc = bnifsc;		
		$scope.viewObj={};
		$scope.bank=$routeParams.bank;
		$scope.state=$routeParams.state;
		$scope.district=$routeParams.district;		
		function init() {
			if($scope.district){
				bnifsc.branches($routeParams.bank,$routeParams.state,$routeParams.district, function(resp) {	
				$scope.viewObj.itemsList = resp.items;
				$scope.$apply($scope.bnifsc);
				});
			}else if($scope.state){
				bnifsc.districts($routeParams.bank,$routeParams.state, function(resp) {	
				$scope.viewObj.itemsList = resp.items;
				$scope.$apply($scope.bnifsc);
				});
			}else if($scope.bank){
				bnifsc.states($routeParams.bank,function(resp) {	
				$scope.viewObj.itemsList = resp.items;
				$scope.$apply($scope.bnifsc);
				});
			}else{
				bnifsc.banks(function(resp) {	
				$scope.viewObj.itemsList = resp.items;
				$scope.$apply($scope.bnifsc);
				});
			}			
		}

		// controller initialization 
		if (bnifsc.appLoaded()) {			
			init();
		}

		// controller initialization when apploaded successfully.
		$window.init = function() {
			console.log("window init calling");
			bnifsc.appLoaded(true);			
			init();
		}
	});