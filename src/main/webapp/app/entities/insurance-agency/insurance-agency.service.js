(function() {
    'use strict';
    angular
        .module('whatscoverApp')
        .factory('InsuranceAgency', InsuranceAgency);

    InsuranceAgency.$inject = ['$resource'];

    function InsuranceAgency ($resource) {
        var resourceUrl =  'api/insurance-agencies/:id';

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
