'use strict';

angular.module('bnifscApp')
  .factory('bank', function () {

    function emitReq(scope) {
      scope.$emit("ajaxReq");
    }

    function emitResp(scope) {
      scope.$emit("ajaxResp");
    }

    function loadByName(name, scope, cb) {
      emitReq(scope);
      gapi.client.bnifsc.admin.bank.loadByName({'name': name}).execute(function (resp) {
        emitResp(scope);
        cb(resp);
      });
    }

    function save(bank,scope, cb) {
      emitReq(scope);
      gapi.client.bnifsc.admin.addBank(bank).execute(function (resp) {
        emitResp(scope);
        cb(resp);
      });
    }

    // Public API here
    return {
      loadByName: loadByName,
      save: save
    };
  });
