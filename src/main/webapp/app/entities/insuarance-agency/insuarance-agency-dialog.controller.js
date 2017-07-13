(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuaranceAgencyDialogController', InsuaranceAgencyDialogController);

    InsuaranceAgencyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InsuaranceAgency'];

    function InsuaranceAgencyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InsuaranceAgency) {
        var vm = this;

        vm.insuaranceAgency = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.insuaranceAgency.id !== null) {
                InsuaranceAgency.update(vm.insuaranceAgency, onSaveSuccess, onSaveError);
            } else {
                InsuaranceAgency.save(vm.insuaranceAgency, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('whatscoverApp:insuaranceAgencyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
