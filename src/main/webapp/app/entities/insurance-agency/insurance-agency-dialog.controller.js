(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceAgencyDialogController', InsuranceAgencyDialogController);

    InsuranceAgencyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InsuranceAgency', 'InsuranceCompany'];

    function InsuranceAgencyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InsuranceAgency, InsuranceCompany) {
        var vm = this;

        vm.insuranceAgency = entity;
        vm.clear = clear;
        vm.save = save;
        vm.insurancecompanies = InsuranceCompany.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.insuranceAgency.id !== null) {
                InsuranceAgency.update(vm.insuranceAgency, onSaveSuccess, onSaveError);
            } else {
                InsuranceAgency.save(vm.insuranceAgency, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('whatscoverApp:insuranceAgencyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
