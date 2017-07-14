(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceCompanyDetailController', InsuranceCompanyDetailController);

    InsuranceCompanyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'InsuranceCompany'];

    function InsuranceCompanyDetailController($scope, $rootScope, $stateParams, previousState, entity, InsuranceCompany) {
        var vm = this;

        vm.insuranceCompany = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('whatscoverApp:insuranceCompanyUpdate', function(event, result) {
            vm.insuranceCompany = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
