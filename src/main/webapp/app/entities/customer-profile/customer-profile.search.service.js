(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .factory('CustomerProfileSearch', CustomerProfileSearch);

    CustomerProfileSearch.$inject = ['$resource'];

    function CustomerProfileSearch($resource) {
        var resourceUrl =  'api/_search-name/customer-profiles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
