(function() {
    'use strict';
    angular
        .module('whatscoverApp')
        .factory('InsuranceCompany', InsuranceCompany);

    InsuranceCompany.$inject = ['$resource'];

    function InsuranceCompany ($resource) {
        var resourceUrl =  'api/insurance-companies/:id';

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
