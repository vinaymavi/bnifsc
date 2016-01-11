'use strict';

angular.module('bnifscApp')
    .controller('AdminCtrl', function ($scope, $window, bnifsc, admin) {
        $window.init = function () {
            console.log("init calling");
        }
    });
