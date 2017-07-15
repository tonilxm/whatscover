(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceAgencyDetailController', InsuranceAgencyDetailController);

    InsuranceAgencyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'InsuranceAgency', 'InsuranceCompany'];

    function InsuranceAgencyDetailController($scope, $rootScope, $stateParams, previousState, entity, InsuranceAgency, InsuranceCompany) {
        var vm = this;

        vm.insuranceAgency = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('whatscoverApp:insuranceAgencyUpdate', function(event, result) {
            vm.insuranceAgency = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
