(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .factory('InsuaranceAgencySearch', InsuaranceAgencySearch);

    InsuaranceAgencySearch.$inject = ['$resource'];

    function InsuaranceAgencySearch($resource) {
        var resourceUrl =  'api/_search/insuarance-agencies/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
