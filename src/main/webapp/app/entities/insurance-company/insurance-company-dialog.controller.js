(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceCompanyDialogController', InsuranceCompanyDialogController);

    InsuranceCompanyDialogController.$inject = ['$state', '$timeout', '$scope', '$stateParams', '$window', 'entity', 'InsuranceCompany'];

    function InsuranceCompanyDialogController ($state, $timeout, $scope, $stateParams, $window, entity, InsuranceCompany) {
        var vm = this;

        vm.insuranceCompany = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $state.go('insurance-company', {}, { reload: true});
        }

        function save () {
            vm.isSaving = true;
            if (vm.insuranceCompany.id !== null) {
                InsuranceCompany.update(vm.insuranceCompany, onSaveSuccess, onSaveError);
            } else {
                InsuranceCompany.save(vm.insuranceCompany, onSaveSuccess, onSaveError);
            }

            $state.go('insurance-company', {}, { reload: true});
        }

        function onSaveSuccess (result) {
            $scope.$emit('whatscoverApp:insuranceCompanyUpdate', result);
            $state.reload();
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
