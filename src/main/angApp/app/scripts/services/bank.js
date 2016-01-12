'use strict';

angular.module('bnifscApp')
    .factory('bank', function () {
        function loadByName(name, cb) {
            gapi.client.bnifsc.admin.bank.loadByName({'name': name}).execute(function (resp) {
                cb(resp);
            });
        }

        function save(bank, cb) {
            gapi.client.bnifsc.admin.addBank(bank).execute(function (resp) {
                cb(resp);
            });
        }

        // Public API here
        return {
            loadByName: loadByName,
            save: save
        };
    });
