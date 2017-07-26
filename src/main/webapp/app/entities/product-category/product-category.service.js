(function() {
    'use strict';
    angular
        .module('whatscoverApp')
        .factory('ProductCategory', ProductCategory);

    ProductCategory.$inject = ['$resource'];

    function ProductCategory ($resource) {
        var resourceUrl =  'api/product-categories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
