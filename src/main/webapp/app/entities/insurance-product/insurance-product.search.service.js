(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .factory('InsuranceProductSearch', InsuranceProductSearch);

    InsuranceProductSearch.$inject = ['$resource'];

    function InsuranceProductSearch($resource) {
        var resourceUrl =  'api/_search/insurance-products/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
