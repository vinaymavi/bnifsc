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
    'ngMaterial'
  ])
  .config(function($routeProvider) {
    $routeProvider
      .when('/:bank?/:state?/:district?/:branch?/:keyString?', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });

function init() {
  var ROOT = 'https://bnifsc.appspot.com/_ah/api';
  gapi.client.load('bnifsc', 'v1', function() {
    window.init();
  }, ROOT);
}