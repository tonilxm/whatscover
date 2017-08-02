(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceProductPremiumRateDetailController', InsuranceProductPremiumRateDetailController);

    InsuranceProductPremiumRateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'InsuranceProductPremiumRate', 'InsuranceProduct'];

    function InsuranceProductPremiumRateDetailController($scope, $rootScope, $stateParams, previousState, entity, InsuranceProductPremiumRate, InsuranceProduct) {
        var vm = this;

        vm.insuranceProductPremiumRate = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('whatscoverApp:insuranceProductPremiumRateUpdate', function(event, result) {
            vm.insuranceProductPremiumRate = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
