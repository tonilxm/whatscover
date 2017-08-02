(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceProductPremiumRateDialogController', InsuranceProductPremiumRateDialogController);

    InsuranceProductPremiumRateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InsuranceProductPremiumRate', 'InsuranceProduct'];

    function InsuranceProductPremiumRateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InsuranceProductPremiumRate, InsuranceProduct) {
        var vm = this;

        vm.insuranceProductPremiumRate = entity;
        vm.clear = clear;
        vm.save = save;
        vm.insuranceproducts = InsuranceProduct.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.insuranceProductPremiumRate.id !== null) {
                InsuranceProductPremiumRate.update(vm.insuranceProductPremiumRate, onSaveSuccess, onSaveError);
            } else {
                InsuranceProductPremiumRate.save(vm.insuranceProductPremiumRate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('whatscoverApp:insuranceProductPremiumRateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
