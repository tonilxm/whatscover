(function() {
    'use strict';
    angular
        .module('whatscoverApp')
        .factory('Upload', Upload);

    Upload.$inject = ['$resource'];

    function Upload ($resource) {
        var resourceUrl =  'api/upload-file';

        return $resource(resourceUrl, {}, {
            'upload': {
                /*method: 'POST',
                transformRequest: function (data) {
                    return data;
                }*/
				method: 'POST', isArray: true
			}
        });
    }
})();
