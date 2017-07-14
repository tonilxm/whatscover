(function() {
    'use strict';
    angular
        .module('whatscoverApp')
        .factory('InsuranceProduct', InsuranceProduct);

    InsuranceProduct.$inject = ['$resource'];

    function InsuranceProduct ($resource) {
        var resourceUrl =  'api/insurance-products/:id';

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
