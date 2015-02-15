'use strict';

/**
 * @ngdoc function
 * @name bnifscApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the bnifscApp
 */
angular.module('bnifscApp')
	.controller('MainCtrl', function($scope, $window, bnifsc,$routeParams,$location) {
		console.log("Controller loaded");
		$scope.bnifsc = bnifsc;		
		$scope.viewObj={};
		$scope.bank=$routeParams.bank;
		$scope.state=$routeParams.state;
		$scope.district=$routeParams.district;
		$scope.branch = $routeParams.branch;
		$scope.keyString = $routeParams.keyString;		
		$scope.branchProps;
		$scope.data={};
		$scope.panelTitle;
		function init() {
			if($scope.keyString){			
				bnifsc.getBranchByKey($scope.keyString,function(resp){
				$scope.branchProps = resp;	
				$scope.$apply($scope.branchProps);
				});
			}
			else if($scope.district){
				$scope.panelTitle = $routeParams.bank+' > '+$routeParams.state +' > '+ $routeParams.district +' Branches List'
				bnifsc.branches($routeParams.bank,$routeParams.state,$routeParams.district, function(resp) {	
				$scope.viewObj.itemsList = resp.items;
				$scope.$apply($scope.viewObj.itemsList);
				});
			}else if($scope.state){
				$scope.panelTitle = $routeParams.bank+' > '+$routeParams.state +' Districts List'
				bnifsc.districts($routeParams.bank,$routeParams.state, function(resp) {	
				$scope.viewObj.itemsList = resp.items;
				$scope.$apply($scope.viewObj.itemsList);
				});
			}else if($scope.bank){
				$scope.panelTitle = $routeParams.bank+' States List'
				bnifsc.states($routeParams.bank,function(resp) {	
				$scope.viewObj.itemsList = resp.items;
				$scope.$apply($scope.viewObj.itemsList);
				});
			}else{
				$scope.panelTitle = 'IFSC Banks List'
				bnifsc.banks(function(resp) {	
				$scope.viewObj.itemsList = resp.items;
				$scope.$apply($scope.viewObj.itemsList);
				});
			}			
		}

		$scope.onSelect = function(){
			console.log($location.path())
			$location.path($location.path()+'/'+$scope.data.search)
			console.log($scope.data.search)
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