'use strict';

angular.module('bnifscApp')
  .directive('quickHref', function ($location, $anchorScroll) {
    return {
      template: '<div></div>',
      restrict: 'E',
      link: function postLink(scope, element, attrs) {
        var quickLinksStr = attrs['quickLinks'];
        console.log(scope[quickLinksStr]);
        scope.$watch(quickLinksStr, (newVal)=> {
          render(newVal);
        });
        /**
         * create quick links.
         * @param linksArr {Array} of links.
         */
        function render(linksArr) {
          if (!linksArr) {
            return 0;
          }
          var len = linksArr.length;
          var html = [];
          html.push('<div class="btn-group col-lg-12" style="margin:-35px 0px" role="group" aria-label="...">');
          linksArr.forEach(function (val, ind) {
            html.push(`<button type="button" class="btn btn-default" style="width:calc(100% / ${len})">${val}</button>`);
          })
          html.push('</div>');
          element.html(html.join(''));
          /*TODO explore right method to bind click function.*/
          angular.element('button', angular.element(element)).on("click", function () {
            var id = angular.element(this).text();
            $location.hash(id);
            $anchorScroll();
          });
          console.log("Render");
        }
      }
    };
  });
