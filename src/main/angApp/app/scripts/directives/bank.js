'use strict';

angular.module('bnifscApp')
    .directive('bank', function ($filter, $compile,$state) {
        return {
            scope: {
                items: '=',
                bankFilter: '='
            },
            restrict: 'EA',
            link: function postLink(scope, element, attrs) {

                if (scope.banks) {
                    render(scope.items);
                }

                scope.$watch("items", function (banks) {                    
                    render(banks, scope.bankFilter);
                });

                scope.$watch("bankFilter", function (bankFilter) {                    
                    render(scope.items, bankFilter);
                });

                function render(items, filterStr) {
                    var html = [];
                    var filterItems = $filter('filter')(items, filterStr);
                    html.push("<!--Bank Start-->")
                    angular.forEach(filterItems, function (value, index) {                        
                        html.push("<div class='col-lg-12'><a href='"+$state.href("admin.bank",{name:value.name})+"'>" + value.name + "</a></div>")
                    });
                    html.push("<!--Bank End-->")
                    element.html(html.join(''));                    
                }

            }
        };
    });
