(function () {
    'use strict';

    angular
        .module('whatscoverApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
