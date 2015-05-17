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
  .config(function($routeProvider,$locationProvider) {
    $routeProvider
      .when('/:bank?/:state?/:district?/:branch?/:ifsc?', {
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

      // Set hashbang mode mode.
      $locationProvider.hashPrefix('!');
  });



function init() {
  var ROOT = 'https://bnifsc-beta.appspot.com/_ah/api';
  gapi.client.load('bnifsc', 'v2', function() {
    window.init();
  }, ROOT);
}
