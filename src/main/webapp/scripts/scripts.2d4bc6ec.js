"use strict";function init(){var a="https://bnifsc.appspot.com/_ah/api";gapi.client.load("bnifsc","v1",function(){window.init()},a)}angular.module("bnifscApp",["ngAnimate","ngAria","ngCookies","ngMessages","ngResource","ngRoute","ngSanitize","ngTouch","ngMaterial"]).config(["$routeProvider","$locationProvider",function(a,b){a.when("/:bank?/:state?/:district?/:branch?/:keyString?",{templateUrl:"views/main.html",controller:"MainCtrl"}).when("/about",{templateUrl:"views/about.html",controller:"AboutCtrl"}).otherwise({redirectTo:"/"}),b.hashPrefix("!")}]),angular.module("bnifscApp").controller("MainCtrl",["$scope","$window","bnifsc","$routeParams","$location","$mdDialog","$timeout",function(a,b,c,d,e,f,g){function h(){a.keyString?(a.ajaxrequest=!0,a.panelTitle=d.bank+" > "+d.state+" > "+d.district+" Branche",c.getBranchByKey(a.keyString,function(b){a.branchProps=b,a.ajaxrequest=!1,a.$apply(a.branchProps)}),ga("set",{page:e.path(),title:a.panelTitle})):a.district?(a.ajaxrequest=!0,a.panelTitle=d.bank+" > "+d.state+" > "+d.district+" Branches List",c.branches(d.bank,d.state,d.district,function(b){a.viewObj.itemsList=j(b.items),a.$apply(a)}),ga("set",{page:e.path(),title:a.panelTitle})):a.state?(a.ajaxrequest=!0,a.panelTitle=d.bank+" > "+d.state+" Districts List",c.districts(d.bank,d.state,function(b){a.viewObj.itemsList=i(b.items),a.$apply(a)}),ga("set",{page:e.path(),title:a.panelTitle})):a.bank?(a.ajaxrequest=!0,a.panelTitle=d.bank+" States List",c.states(d.bank,function(b){a.viewObj.itemsList=i(b.items),a.$apply(a)}),ga("set",{page:e.path(),title:a.panelTitle})):(a.ajaxrequest=!0,a.panelTitle="IFSC Banks List",c.banks(function(b){a.viewObj.itemsList=i(b.items),a.$apply(a)}),ga("set",{page:e.path(),title:a.panelTitle})),ga("send","pageview"),c.feedback&&g(function(){f.show({controller:"FeedbackCtrl",templateUrl:"views/feedback.html"})},k)}function i(b){a.ajaxrequest=!1,a.autocompleteData=b;var c={},d="";return b.forEach(function(a,b,e){d!==a[0]&&(d=a[0],c[d]=[]),c[d].push(a)}),c}function j(b){a.ajaxrequest=!1;var c={},d="";return b.forEach(function(a,b,e){d!==a.branchName[0]&&(d=a.branchName[0],c[d]=[]),c[d].push(a)}),c}var k=1e4;a.bnifsc=c,a.viewObj={},a.bank=d.bank,a.state=d.state,a.district=d.district,a.branch=d.branch,a.keyString=d.keyString,a.branchProps,a.data={},a.panelTitle,a.autocompleteData,a.ajaxrequest=!0,a.onSelect=function(){console.log(e.path()),e.path(e.path()+"/"+a.data.search),console.log(a.data.search)},c.appLoaded()&&h(),b.init=function(){console.log("window init calling"),c.appLoaded(!0),h()}}]),angular.module("bnifscApp").controller("AboutCtrl",["$scope",function(a){a.awesomeThings=["HTML5 Boilerplate","AngularJS","Karma"]}]),angular.module("bnifscApp").service("bnifsc",function(){var a=this;a.banksArr;var b=!1;return a.banks=function(b){gapi.client.bnifsc.banks().execute(function(c){console.log(c),a.banksArr=c.items,b(c)})},a.feedback=!0,a.states=function(a,b){gapi.client.bnifsc.states({bankName:a}).execute(function(a){console.log(a),b(a)})},a.districts=function(a,b,c){gapi.client.bnifsc.districts({bankName:a,stateName:b}).execute(function(a){console.log(a),c(a)})},a.branches=function(a,b,c,d){gapi.client.bnifsc.branches({bankName:a,stateName:b,districtName:c}).execute(function(a){console.log(a),d(a)})},a.getBranchByKey=function(a,b){gapi.client.bnifsc.getBranchByKey({keyString:a}).execute(function(a){b(a)})},a.addFeedback=function(a,b){gapi.client.bnifsc.feedback({feedback:a}).execute(function(a){b(a)})},a.appLoaded=function(a){return a?void(b=a):b},a}),angular.module("bnifscApp").controller("FeedbackCtrl",["$scope","$mdDialog","bnifsc","$mdToast",function(a,b,c,d){var e=2e3,f=d.simple().content("Thank you. For valuable feedback.").position("top right").hideDelay(e);a.feedback,a.cancel=function(){c.feedback=!1,b.cancel()},a.submit=function(){c.feedback=!1,console.log(a.feedback),b.hide(),c.addFeedback(a.feedback,function(a){console.log(a),d.show(f)})}}]);