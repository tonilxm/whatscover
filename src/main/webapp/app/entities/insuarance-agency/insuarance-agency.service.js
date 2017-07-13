(function() {
    'use strict';
    angular
        .module('whatscoverApp')
        .factory('InsuaranceAgency', InsuaranceAgency);

    InsuaranceAgency.$inject = ['$resource'];

    function InsuaranceAgency ($resource) {
        var resourceUrl =  'api/insuarance-agencies/:id';

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
