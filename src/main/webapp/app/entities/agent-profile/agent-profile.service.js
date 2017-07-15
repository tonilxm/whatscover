(function() {
    'use strict';
    angular
        .module('whatscoverApp')
        .factory('AgentProfile', AgentProfile);

    AgentProfile.$inject = ['$resource', 'DateUtils'];

    function AgentProfile ($resource, DateUtils) {
        var resourceUrl =  'api/agent-profiles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dob = DateUtils.convertLocalDateFromServer(data.dob);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dob = DateUtils.convertLocalDateToServer(copy.dob);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dob = DateUtils.convertLocalDateToServer(copy.dob);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
