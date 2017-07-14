(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .factory('InsuranceCompanySearch', InsuranceCompanySearch);

    InsuranceCompanySearch.$inject = ['$resource'];

    function InsuranceCompanySearch($resource) {
        var resourceUrl =  'api/_search/insurance-companies/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
