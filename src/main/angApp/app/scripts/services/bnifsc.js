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
		self.banks = function(cb) {
			gapi.client.bnifsc.banks().execute(function(resp) {
				console.log(resp);
				self.banksArr = resp.items;				
				cb();
			});
		};
		return self;
	});