'use strict';

angular.module('bnifscApp')
    .controller('BankCtrl', function ($scope, $window, $stateParams, admin, bank, $timeout) {
        function init() {
            if (admin.appLoaded) {
                console.log($stateParams.name);
                $scope.name = $stateParams.name;
                bank.loadByName($scope.name, function (resp) {
                    console.log(resp);
                    $timeout(function () {
                        $scope.bank = resp;
                        $scope.bank.email = resp.email.email;
                        $scope.bank.image = "http://www.apnaatmsite.com/images/bank.jpg";
                    }, 0);

                });
            }
        }

        $scope.save = function () {
            console.log("save calling");
            bank.save($scope.bank, function (resp) {
                console.log(resp);
            });
        };

        /*Initialization of function*/
        $window.init = function () {
            console.log("init calling");
            admin.appLoaded = true;
            $scope.$emit("appLoaded");
        };
        $scope.$on("appLoaded", function () {
            init();
        });
        init();
    });
