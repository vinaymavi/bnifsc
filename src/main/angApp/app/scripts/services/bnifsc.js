'use strict';
/**
 * @ngdoc service
 * @name bnifscApp.bnifsc
 * @description
 * # bnifsc
 * Service in the bnifscApp.
 */
angular.module('bnifscApp')
	.service('bnifsc', function() {
		var self = this;
		self.banksArr;
		var appLoaded = false;

		self.banks = function(cb) {
			gapi.client.bnifsc.banks()
				.execute(function(resp) {
					console.log(resp);
					self.banksArr = resp.items;
					cb(resp);
				});
		};

		self.states = function(bank, cb) {
			gapi.client.bnifsc.states({
					'bankName': bank
				})
				.execute(function(resp) {
					console.log(resp);
					cb(resp);
				});
		};

		self.districts = function(bank, state, cb) {
			gapi.client.bnifsc.districts({
					'bankName': bank,
					'stateName': state
				})
				.execute(function(resp) {
					console.log(resp);
					cb(resp);
				})
		};

		self.branches = function(bank, state, district, cb) {
			gapi.client.bnifsc.branches({
					'bankName': bank,
					'stateName': state,
					'districtName': district
				})
				.execute(function(resp) {
					console.log(resp);
					cb(resp);
				})
		};

		self.getBranchByKey = function(key, cb) {
			gapi.client.bnifsc.getBranchByKey({
					'keyString': key
				})
				.execute(function(resp) {
					cb(resp);
				})
		};
		// @app = Boolean
		self.appLoaded = function(app) {
			if (app) {
				appLoaded = app;
			} else {
				return appLoaded;
			}
		}
		
		return self;
	});