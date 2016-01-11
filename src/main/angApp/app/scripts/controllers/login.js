'use strict';

angular.module('bnifscApp')
    .controller('LoginCtrl', function ($scope, bnifsc, admin, $window, $state) {
        $scope.login = function () {
            admin.login(function (resp) {
                console.log(resp);
                if (!resp['error']) {
                    $state.go('admin');
                }
            });
        };
        $window.init = function () {
            console.log("init calling");
            admin.appLoaded =true;
        };
    });
