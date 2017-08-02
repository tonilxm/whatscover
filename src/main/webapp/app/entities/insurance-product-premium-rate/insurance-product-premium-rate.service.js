(function() {
    'use strict';
    angular
        .module('whatscoverApp')
        .factory('InsuranceProductPremiumRate', InsuranceProductPremiumRate);

    InsuranceProductPremiumRate.$inject = ['$resource'];

    function InsuranceProductPremiumRate ($resource) {
        var resourceUrl =  'api/insurance-product-premium-rates/:id';

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
