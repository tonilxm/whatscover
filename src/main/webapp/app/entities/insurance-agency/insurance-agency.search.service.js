(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .factory('InsuranceAgencySearch', InsuranceAgencySearch);

    InsuranceAgencySearch.$inject = ['$resource'];

    function InsuranceAgencySearch($resource) {
        var resourceUrl =  'api/_search-name/insurance-agencies/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
