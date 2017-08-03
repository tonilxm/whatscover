(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .factory('ProductCategorySearch', ProductCategorySearch);

    ProductCategorySearch.$inject = ['$resource'];

    function ProductCategorySearch($resource) {
        var resourceUrl =  'api/_search-name/product-categories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
