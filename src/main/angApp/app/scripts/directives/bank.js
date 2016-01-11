'use strict';

angular.module('bnifscApp')
    .directive('bank', function () {
        return {
            scope: {
                banks: '='
            },
            restrict: 'EA',
            link: function postLink(scope, element, attrs) {

                if (scope.banks) {
                    render(banks);
                }

                scope.$watch("banks", function (banks) {
                    console.log(banks);
                    render(banks)
                });

                function render(banks) {
                    var html = [];
                    html.push("<!--Bank Start-->")
                    angular.forEach(banks, function (value, index) {
                        console.log(value.name);
                        html.push("<div class='col-lg-12'><a href='#'>" + value.name + "</a></div>")
                    });
                    html.push("<!--Bank End-->")
                    element.html(html.join(''));
                }

            }
        };
    });
