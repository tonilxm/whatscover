(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .factory('AppointmentSearch', AppointmentSearch);

    AppointmentSearch.$inject = ['$resource'];

    function AppointmentSearch($resource) {
        var resourceUrl =  'api/_search/appointments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
