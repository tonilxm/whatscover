(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('entity', {
            abstract: true,
            parent: 'app'
        });
    }
})();
