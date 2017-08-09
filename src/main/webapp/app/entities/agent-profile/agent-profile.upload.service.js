(function() {
    'use strict';
    angular
        .module('whatscoverApp')
        .factory('UploadService', UploadService);

//    Upload.$inject = ['$resource'];
    UploadService.$inject = ['$http'];

//    function Upload ($resource) {
    function UploadService($http) {
        var resourceUrl =  'api/upload-file';

        return {
            'upload': function(data){
            	return $http.post(resourceUrl, data, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                }).then(function(response) { 
                	return response.data;	// Get file name from response data
                });
			}
        }
        
        /*return $resource(resourceUrl, {}, {
            'upload': {
                method: 'POST',
                transformRequest: function (data) {
                    return data;
                }
				method: 'POST', isArray: true
			}
        });*/
    }
})();
