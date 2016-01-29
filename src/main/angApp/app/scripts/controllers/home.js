'use strict';

/**
 * @ngdoc function
 * @name bnifscApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the bnifscApp
 */
angular.module('bnifscApp')
    .controller('HomeCtrl', function ($scope, $q, $window, bnifsc, $routeParams, $location, $mdDialog, $timeout, $log) {
        var self = this;
        var FEEDBACK_DELAY = 10 * 1000;
        //TODO add comments.
        $scope.bnifsc = bnifsc;
        $scope.viewObj = {};
        $scope.branchProps;
        $scope.data = {};
        $scope.panelTitle;
        $scope.autocompleteData;
        $scope.ajaxrequest = true;

        /*This function has to much logic split logic to different controllers*/
        function init() {
            /*Main page*/
            $scope.ajaxrequest = true;
            $scope.panelTitle = 'IFSC Banks List'
            bnifsc.banks(function (resp) {
                $timeout(function () {
                    $scope.viewObj.itemsList = groupByAlfabatical(resp.items);
                }, 0)
            });
            /*Google analytics code*/
            ga('set', {page: $location.path(), title: $scope.panelTitle});
            /*Google analytics code*/
            ga('send', 'pageview');
            /*Feedback form*/
            //if (bnifsc.feedback) {
            //    $timeout(function () {
            //        $mdDialog.show({
            //            controller: "FeedbackCtrl",
            //            templateUrl: 'views/feedback.html'
            //        })
            //    }, FEEDBACK_DELAY)
            //}

        }

        function groupByAlfabatical(items) {
            $scope.ajaxrequest = false;
            $scope.autocompleteData = items;
            var data = {},
                key = "";
            items.forEach(function (value, index, arr) {
                if (key !== value.name[0]) {
                    key = value.name[0];
                    data[key] = [];
                }
                data[key].push(value);
            });
            return data;
        }

        // controller initialization
        if (bnifsc.appLoaded()) {
            init();
        }

        // controller initialization when apploaded successfully.
        $window.init = function () {
            console.log("window init calling");
            bnifsc.appLoaded(true);
            init();
        }
    });
