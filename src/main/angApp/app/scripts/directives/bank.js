'use strict';

angular.module('bnifscApp')
    .directive('bank', function ($filter) {
        return {
            scope: {
                banks: '=',
                bankFilter: '='
            },
            restrict: 'EA',
            link: function postLink(scope, element, attrs) {

                if (scope.banks) {
                    render(scope.banks);
                }

                scope.$watch("banks", function (banks) {
                    console.log(banks);
                    render(banks,scope.bankFilter);
                });

                scope.$watch("bankFilter", function (bankFilter) {
                    console.log(bankFilter);
                    render(scope.banks,bankFilter);
                });

                function render(items, filterStr) {
                    var html = [];
                    var filterItems = $filter('filter')(items,filterStr);
                    html.push("<!--Bank Start-->")
                    angular.forEach(filterItems, function (value, index) {
                        console.log(value.name);
                        html.push("<div class='col-lg-12'><a href='#'>" + value.name + "</a></div>")
                    });
                    html.push("<!--Bank End-->")
                    element.html(html.join(''));
                }

            }
        };
    });
