(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .factory('AgentProfileSearch', AgentProfileSearch);

    AgentProfileSearch.$inject = ['$resource'];

    function AgentProfileSearch($resource) {
    //    var resourceUrl =  'api/_search/agent-profiles/:id';
        var resourceUrl =  'api/_search-name/agent-profiles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
