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
  .config(function ($routeProvider, $locationProvider, $stateProvider, $urlRouterProvider, $windowProvider) {
    $urlRouterProvider.otherwise("/");
    $stateProvider
      .state("admin", {
        url: "/admin",
        templateUrl: "views/admin.html",
        controller: "AdminCtrl"
      })
      .state("admin.bank", {
        url: "/bank/:name",
        templateUrl: "views/bank.html",
        controller: "BankCtrl"
      })
      .state("login", {
        url: "/login",
        templateUrl: "views/login.html",
        controller: "LoginCtrl"
      })
      .state("ifsc", {
        url: '/bank/ifsc-code/:ifsc',
        templateUrl: 'views/ifsc.html',
        controller: 'IfscCtrl'
      })
      .state("home", {
        url: "/",
        templateUrl: "views/home.html",
        "controller": "HomeCtrl"
      })
      .state("state", {
        url: '/:bank',
        templateUrl: 'views/state.html',
        controller: 'StateCtrl'
      })
      .state("city", {
        url: '/:bank/:state',
        templateUrl: 'views/city.html',
        controller: 'CityCtrl'
      })
      .state("branches", {
        url: '/:bank/:state/:city',
        templateUrl: 'views/branches.html',
        controller: 'BranchesCtrl'
      })


    // Set hashbang mode mode.
    $locationProvider.hashPrefix('!');
  });


function init() {
  var ROOT = 'https://bnifsc-beta.appspot.com/_ah/api';
  gapi.client.load('bnifsc', 'v1', function () {
    window.init();
  }, ROOT);
}
