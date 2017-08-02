(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .factory('InsuranceProductPremiumRateSearch', InsuranceProductPremiumRateSearch);

    InsuranceProductPremiumRateSearch.$inject = ['$resource'];

    function InsuranceProductPremiumRateSearch($resource) {
        var resourceUrl =  'api/_search/insurance-product-premium-rates/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
