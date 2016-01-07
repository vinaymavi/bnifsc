'use strict';
/**
 * @ngdoc overview
 * @name bnifscApp
 * @description
 * # bnifscApp
 *
 * Main module of the application.
 */
angular
    .module('bnifscApp', [
        'ngAnimate',
        'ngAria',
        'ngCookies',
        'ngMessages',
        'ngResource',
        'ngRoute',
        'ngSanitize',
        'ngTouch',
        'ngMaterial',
        'ui.router'
    ])
    .config(function ($routeProvider, $locationProvider, $stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/");
        $stateProvider.state("admin", {
            url: "/admin",
            templateUrl: "views/admin.html",
            controller: "AdminCtrl"
        })

        // Set hashbang mode mode.
        $locationProvider.hashPrefix('!');
    });


function init() {
    var ROOT = 'https://bnifsc-beta.appspot.com/_ah/api';
    gapi.client.load('bnifsc', 'v2', function () {
        window.init();
    }, ROOT);
}
