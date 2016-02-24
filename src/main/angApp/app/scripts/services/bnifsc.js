'use strict';
/**
 * @ngdoc service
 * @name bnifscApp.bnifsc
 * @description
 * # bnifsc
 * Service in the bnifscApp.
 */
angular.module('bnifscApp')
  .service('bnifsc', function () {
    var self = this;
    self.banksArr;
    var appLoaded = false;
    //TODO add comments.
    self.banks = function (cb) {
      gapi.client.bnifsc.public.banks()
        .execute(function (resp) {
          console.log(resp);
          self.banksArr = resp.items;
          cb(resp);
        });
    };
    //feedback flag.
    self.feedback = true;

    self.states = function (bank, cb) {
      gapi.client.bnifsc.public.states({
          'bankName': bank
        })
        .execute(function (resp) {
          console.log(resp);
          cb(resp);
        });
    };
    /**
     * Convert given array to object and bind all details to first
     * character of given key
     * @param items {{Array}}
     * @param mapKey {{String}} default name
     * @return {{Object}}
     */
    self.groupByAlphabetical = function (items, mapKey) {
      if (!mapKey) {
        mapKey = 'name'
      }
      if (!items) {
        return {};
      }
      var data = {},
        key = "";
      items.forEach(function (value, index, arr) {
        if (key !== value[mapKey][0]) {
          key = value[mapKey][0];
          data[key] = [];
        }
        data[key].push(value);
      });
      return data;
    }
    self.districts = function (bank, state, cb) {
      gapi.client.bnifsc.public.districts({
          'bankName': bank,
          'stateName': state
        })
        .execute(function (resp) {
          console.log(resp);
          cb(resp);
        })
    };

    self.branches = function (bank, state, district, cb) {
      gapi.client.bnifsc.public.branches({
          'bankName': bank,
          'stateName': state,
          'districtName': district
        })
        .execute(function (resp) {
          console.log(resp);
          cb(resp);
        })
    };
    /**
     * @description Branch by IFSC code.
     * @param {String} ifsc
     * @param {function} cb
     */
    self.branchByIFSC = function (ifsc, cb) {
      gapi.client.bnifsc.public.branchByIFSC({
          "ifsc": ifsc
        })
        .execute(function (resp) {
          cb(resp);
        })
    }

    /*@ngdoc method
     * @description
     * #feedback
     * Add feedback
     * @params{String} feedback
     * @params{Function} cb*/
    self.addFeedback = function (feedback, cb) {
      gapi.client.bnifsc.public.feedback({
          'feedback': feedback
        })
        .execute(function (resp) {
          cb(resp);
        })
    }
    // @app = Boolean
    self.appLoaded = function (app) {
      if (app) {
        appLoaded = app;
      } else {
        return appLoaded;
      }
    }
    /**
     * Search Result with query.
     * @param query {String}
     * @param cb {Function}
     */
    self.search = function (query, cb) {
      gapi.client.bnifsc.public.search({'query': query}).execute(function (resp) {
        console.info(resp);
        cb(resp);
      });
    }
    return self;
  });
