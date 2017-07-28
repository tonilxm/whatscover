(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceCompanyDialogController', InsuranceCompanyDialogController);

    InsuranceCompanyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$window', 'entity', 'InsuranceCompany'];

    function InsuranceCompanyDialogController ($timeout, $scope, $stateParams, $window, entity, InsuranceCompany) {
        var vm = this;

        vm.insuranceCompany = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $window.history.back();
        }

        function save () {
            vm.isSaving = true;
            if (vm.insuranceCompany.id !== null) {
                InsuranceCompany.update(vm.insuranceCompany, onSaveSuccess, onSaveError);
            } else {
                InsuranceCompany.save(vm.insuranceCompany, onSaveSuccess, onSaveError);
            }
            $window.history.back();
        }

        function onSaveSuccess (result) {
            $scope.$emit('whatscoverApp:insuranceCompanyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
