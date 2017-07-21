(function() {
    'use strict';
    angular
        .module('whatscoverApp')
        .factory('AgentProfileSendEmail', AgentProfileSendEmail);

    AgentProfileSendEmail.$inject = ['$resource', 'DateUtils'];

    function AgentProfileSendEmail ($resource, DateUtils) {
        var resourceUrl =  'api/send-email-profiles';

        return $resource(resourceUrl, {}, {
            'email': {
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
