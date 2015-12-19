'use strict';

/**
 * @ngdoc function
 * @name bnifscApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the bnifscApp
 */
angular.module('bnifscApp')
    .controller('MainCtrl', function ($scope, $q, $window, bnifsc, $routeParams, $location, $mdDialog, $timeout, $log) {
        var self = this;
        var FEEDBACK_DELAY = 10 * 1000;
        //TODO add comments.
        $scope.bnifsc = bnifsc;
        $scope.viewObj = {};
        $scope.bank = $routeParams.bank;
        $scope.state = $routeParams.state;
        $scope.district = $routeParams.district;
        $scope.branch = $routeParams.branch;
        $scope.ifsc = $routeParams.ifsc;
        $scope.branchProps;
        $scope.data = {};
        $scope.panelTitle;
        $scope.autocompleteData;
        $scope.ajaxrequest = true;

        self.simulateQuery = true;
        self.isDisabled = false;
        // list of `state` value/display objects
        self.statesAuto = loadAll();
        self.querySearch = querySearch;
        self.selectedItemChange = selectedItemChange;
        self.searchTextChange = searchTextChange;
        // ******************************
        // Internal methods
        // ******************************
        /**
         * Search for states... use $timeout to simulate
         * remote dataservice call.
         */
        function querySearch(query) {

            var results = query ? self.statesAuto.filter(createFilterFor(query)) : self.statesAuto,
                deferred;
            deferred = $q.defer();
            if (query.indexOf(' ') > -1) {
                $log.info("space found");
                bnifsc.search(query.substring(0, query.indexOf(' ')), function (resp) {
                    console.log(resp.branches);
                    deferred.resolve(resp.branches);
                });
                return deferred.promise;
            }else{
                return results;
            }


        }

        function searchTextChange(text) {
            $log.info('Text changed to ' + text);
            $log.info(text.indexOf(' '));
        }

        function selectedItemChange(item) {
            $log.info('Item changed to ' + JSON.stringify(item));
        }

        /**
         * Build `states` list of key/value pairs
         */
        function loadAll() {
            var allStates = '';
            return allStates.split(/, +/g).map(function (state) {
                return {
                    value: state.toLowerCase(),
                    display: state
                };
            });
        }

        /**
         * Create filter function for a query string
         */
        function createFilterFor(query) {
            var lowercaseQuery = angular.lowercase(query);
            return function filterFn(state) {
                return (state.value.indexOf(lowercaseQuery) === 0);
            };
        }

        /*This function has to much logic split logic to different controllers*/
        function init() {
            if ($scope.ifsc) {
                /*Branch page.*/
                $scope.ajaxrequest = true;
                $scope.panelTitle = $routeParams.bank + ' > ' + $routeParams.state + ' > ' + $routeParams.district + ' Branche'
                bnifsc.branchByIFSC($scope.ifsc, function (resp) {
                    $scope.branchProps = resp;
                    $scope.ajaxrequest = false;
                    $scope.$apply($scope.branchProps);
                });

                /*Google analytics code*/
                ga('set', {page: $location.path(), title: $scope.panelTitle});

            } else if ($scope.district) {
                /*Bank district page*/
                $scope.ajaxrequest = true;
                $scope.panelTitle = $routeParams.bank + ' > ' + $routeParams.state + ' > ' + $routeParams.district + ' Branches List'
                bnifsc.branches($routeParams.bank, $routeParams.state, $routeParams.district, function (resp) {
                    $scope.viewObj.itemsList = groupByAlfabaticalObj(resp.items);
                    $scope.$apply($scope);
                });

                /*Google analytics code*/
                ga('set', {page: $location.path(), title: $scope.panelTitle});
            } else if ($scope.state) {
                /*Bank State page*/
                $scope.ajaxrequest = true;
                $scope.panelTitle = $routeParams.bank + ' > ' + $routeParams.state + ' Districts List'
                bnifsc.districts($routeParams.bank, $routeParams.state, function (resp) {
                    $scope.viewObj.itemsList = groupByAlfabatical(resp.items);
                    $scope.$apply($scope);
                });
                /*Google analytics code*/
                ga('set', {page: $location.path(), title: $scope.panelTitle});
            } else if ($scope.bank) {
                /*Bank page*/
                $scope.ajaxrequest = true;
                $scope.panelTitle = $routeParams.bank + ' States List'
                bnifsc.states($routeParams.bank, function (resp) {
                    $scope.viewObj.itemsList = groupByAlfabatical(resp.items);
                    $scope.$apply($scope);
                });
                /*Google analytics code*/
                ga('set', {page: $location.path(), title: $scope.panelTitle});
            } else {
                /*Main page*/
                $scope.ajaxrequest = true;
                $scope.panelTitle = 'IFSC Banks List'
                bnifsc.banks(function (resp) {
                    $scope.viewObj.itemsList = groupByAlfabatical(resp.items);
                    $scope.$apply($scope);
                });
                /*Google analytics code*/
                ga('set', {page: $location.path(), title: $scope.panelTitle});
            }
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
                if (key !== value.bankName[0]) {
                    key = value.bankName[0];
                    data[key] = [];
                }
                data[key].push(value);
            });

            return data;
        }

        function groupByAlfabaticalObj(items) {
            $scope.ajaxrequest = false;
            var data = {},
                key = "";
            items.forEach(function (value, index, arr) {
                if (key !== value.branchName[0]) {
                    key = value.branchName[0];
                    data[key] = [];
                }
                data[key].push(value);
            });

            return data;
        }

        $scope.onSelect = function () {
            console.log($location.path())
            $location.path($location.path() + '/' + $scope.data.search)
            console.log($scope.data.search)
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
