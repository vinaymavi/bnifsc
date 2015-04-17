'use strict';

/**
 * @ngdoc function
 * @name bnifscApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the bnifscApp
 */
angular.module('bnifscApp')
	.controller('MainCtrl', function($scope, $window, bnifsc, $routeParams, $location) {
		console.log("Controller loaded");
		$scope.bnifsc = bnifsc;
		$scope.viewObj = {};
		$scope.bank = $routeParams.bank;
		$scope.state = $routeParams.state;
		$scope.district = $routeParams.district;
		$scope.branch = $routeParams.branch;
		$scope.keyString = $routeParams.keyString;
		$scope.branchProps;
		$scope.data = {};
		$scope.panelTitle;
		$scope.autocompleteData;
		$scope.ajaxrequest=true;
		function init() {
			if ($scope.keyString) {
				$scope.ajaxrequest =true;
				bnifsc.getBranchByKey($scope.keyString, function(resp) {
					$scope.branchProps = resp;
					$scope.ajaxrequest =false;
					$scope.$apply($scope.branchProps);				
				});
			} else if ($scope.district) {
				$scope.ajaxrequest =true;
				$scope.panelTitle = $routeParams.bank + ' > ' + $routeParams.state + ' > ' + $routeParams.district + ' Branches List'
				bnifsc.branches($routeParams.bank, $routeParams.state, $routeParams.district, function(resp) {
					$scope.viewObj.itemsList = groupByAlfabaticalObj(resp.items);
					$scope.$apply($scope);
				});
			} else if ($scope.state) {
				$scope.ajaxrequest =true;
				$scope.panelTitle = $routeParams.bank + ' > ' + $routeParams.state + ' Districts List'
				bnifsc.districts($routeParams.bank, $routeParams.state, function(resp) {
					$scope.viewObj.itemsList = groupByAlfabatical(resp.items);
					$scope.$apply($scope);
				});
			} else if ($scope.bank) {
				$scope.ajaxrequest =true;
				$scope.panelTitle = $routeParams.bank + ' States List'
				bnifsc.states($routeParams.bank, function(resp) {
					$scope.viewObj.itemsList = groupByAlfabatical(resp.items);
					$scope.$apply($scope);
				});
			} else {
				$scope.ajaxrequest =true;
				$scope.panelTitle = 'IFSC Banks List'
				bnifsc.banks(function(resp) {
					$scope.viewObj.itemsList = groupByAlfabatical(resp.items);
					$scope.$apply($scope);
				});
			}
		}

		function groupByAlfabatical(items) {
			$scope.ajaxrequest =false;
			$scope.autocompleteData = items;
			var data = {},
				key = "";
			items.forEach(function(value, index, arr){
				if (key !== value[0]) {
					key = value[0];
					data[key] = [];
				}
				data[key].push(value);
			});

			return data;
		}
		
		function groupByAlfabaticalObj(items) {
			$scope.ajaxrequest =false;
			var data = {},
				key = "";
			items.forEach(function(value, index, arr){
				if (key !== value.branchName[0]) {
					key = value.branchName[0];
					data[key] = [];
				}
				data[key].push(value);
			});

			return data;
		}
		
		$scope.onSelect = function() {
				console.log($location.path())
				$location.path($location.path() + '/' + $scope.data.search)
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
