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
		function init() {
			if($routeParams.district){
				console.log("branchBy")
			}else if($routeParams.state){
				console.log("districtByStateName");
			}else if($routeParams.bank){
				console.log("statesByBankName");
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