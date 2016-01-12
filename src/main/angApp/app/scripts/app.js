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
        $stateProvider
            .state("admin", {
                url: "/admin",
                templateUrl: "views/admin.html",
                controller: "AdminCtrl"
            })
            .state("admin.bank", {
                url: "/bank/:name",
                templateUrl: "views/bank.html"  ,
                controller: "BankCtrl"
            })
            .state("login", {
                url: "/login",
                templateUrl: "views/login.html",
                controller: "LoginCtrl"
            })


        // Set hashbang mode mode.
        $locationProvider.hashPrefix('!');
    });


function init() {
    var ROOT = 'https://bnifsc-beta-01.appspot.com/_ah/api';
    gapi.client.load('bnifsc', 'v1', function () {
        window.init();
    }, ROOT);
}
