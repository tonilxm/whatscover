(function() {
    'use strict';
    angular
        .module('whatscoverApp')
        .factory('UploadService', UploadService);

    UploadService.$inject = ['$http'];

    function UploadService($http) {
    	
        return {
            'upload': function(data) {
            	return $http.post('api/upload-file', data, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                }).then(function(response) { 
                	return response.data;	// Get file name from response data
                });/*.success(function (response) {
                	return response.data;
                }).error(function (response) {
                	//
                	console.log(response);
                });*/
			}/*,
        	'load': function(filePath) {
        		 return $http.get('api/load-file', {params: {"filePath": filePath}})
        		 			.then(function(data, status, headers) { return data.data; });
        	}*/
        }
    }
})();
