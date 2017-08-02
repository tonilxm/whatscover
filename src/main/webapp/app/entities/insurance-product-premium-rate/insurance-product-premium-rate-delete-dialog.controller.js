(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceProductPremiumRateDeleteController',InsuranceProductPremiumRateDeleteController);

    InsuranceProductPremiumRateDeleteController.$inject = ['$uibModalInstance', 'entity', 'InsuranceProductPremiumRate'];

    function InsuranceProductPremiumRateDeleteController($uibModalInstance, entity, InsuranceProductPremiumRate) {
        var vm = this;

        vm.insuranceProductPremiumRate = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InsuranceProductPremiumRate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
