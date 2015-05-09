'use strict';

/**
 * @ngdoc function
 * @name bnifscApp.controller:FeedbackCtrl
 * @description
 * # FeedbackCtrl
 * Controller of the bnifscApp
 */
angular.module('bnifscApp')
    .controller('FeedbackCtrl', function ($scope, $mdDialog, bnifsc, $mdToast) {
        var DELAY = 2 * 1000;
        var feedbackWelcome = $mdToast.simple()
            .content("Thank you. For valuable feedback.")
            .position("top right")
            .hideDelay(DELAY)
        $scope.feedback;
        $scope.cancel = function () {
            bnifsc.feedback = false;
            $mdDialog.cancel();
        }

        $scope.submit = function () {
            bnifsc.feedback = false;
            console.log($scope.feedback);
            //TODO api call for feedback.
            $mdDialog.hide();
            bnifsc.addFeedback($scope.feedback, function (resp) {
                console.log(resp);
                $mdToast.show(feedbackWelcome);
            });
        }
    });
