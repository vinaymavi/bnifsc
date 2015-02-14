"use strict";function init(){var a="https://bnifsc.appspot.com/_ah/api";gapi.client.load("bnifsc","v1",function(){window.init()},a)}angular.module("bnifscApp",["ngAnimate","ngAria","ngCookies","ngMessages","ngResource","ngRoute","ngSanitize","ngTouch","ngMaterial","ui.select"]).config(["$routeProvider","$locationProvider",function(a,b){a.when("/:bank?/:state?/:district?/:branch?/:keyString?",{templateUrl:"views/main.html",controller:"MainCtrl"}).when("/about",{templateUrl:"views/about.html",controller:"AboutCtrl"}).otherwise({redirectTo:"/"}),b.hashPrefix("!")}]),angular.module("bnifscApp").controller("MainCtrl",["$scope","$window","bnifsc","$routeParams","$location",function(a,b,c,d,e){function f(){a.keyString?c.getBranchByKey(a.keyString,function(b){a.branchProps=b.properties,a.$apply(a.branchProps)}):a.district?(a.panelTitle=d.bank+" > "+d.state+" > "+d.district+" Branches List",c.branches(d.bank,d.state,d.district,function(b){a.viewObj.itemsList=b.items,a.$apply(a.viewObj.itemsList)})):a.state?(a.panelTitle=d.bank+" > "+d.state+" Districts List",c.districts(d.bank,d.state,function(b){a.viewObj.itemsList=b.items,a.$apply(a.viewObj.itemsList)})):a.bank?(a.panelTitle=d.bank+" States List",c.states(d.bank,function(b){a.viewObj.itemsList=b.items,a.$apply(a.viewObj.itemsList)})):(a.panelTitle="IFSC Banks List",c.banks(function(b){a.viewObj.itemsList=b.items,a.$apply(a.viewObj.itemsList)}))}console.log("Controller loaded"),a.bnifsc=c,a.viewObj={},a.bank=d.bank,a.state=d.state,a.district=d.district,a.branch=d.branch,a.keyString=d.keyString,a.branchProps,a.data={},a.panelTitle,a.onSelect=function(){console.log(e.path()),e.path(e.path()+"/"+a.data.search),console.log(a.data.search)},c.appLoaded()&&f(),b.init=function(){console.log("window init calling"),c.appLoaded(!0),f()}}]),angular.module("bnifscApp").controller("AboutCtrl",["$scope",function(a){a.awesomeThings=["HTML5 Boilerplate","AngularJS","Karma"]}]),angular.module("bnifscApp").service("bnifsc",function(){var a=this;a.banksArr;var b=!1;return a.banks=function(b){gapi.client.bnifsc.banks().execute(function(c){console.log(c),a.banksArr=c.items,b(c)})},a.states=function(a,b){gapi.client.bnifsc.states({bankName:a}).execute(function(a){console.log(a),b(a)})},a.districts=function(a,b,c){gapi.client.bnifsc.districts({bankName:a,stateName:b}).execute(function(a){console.log(a),c(a)})},a.branches=function(a,b,c,d){gapi.client.bnifsc.branches({bankName:a,stateName:b,districtName:c}).execute(function(a){console.log(a),d(a)})},a.getBranchByKey=function(a,b){gapi.client.bnifsc.getBranchByKey({keyString:a}).execute(function(a){b(a)})},a.appLoaded=function(a){return a?void(b=a):b},a});