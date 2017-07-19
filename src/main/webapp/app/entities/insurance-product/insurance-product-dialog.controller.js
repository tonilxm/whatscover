(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceProductDialogController', InsuranceProductDialogController);

    InsuranceProductDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InsuranceProduct', 'InsuranceCompany', '$state', '$rootScope'];

    function InsuranceProductDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InsuranceProduct, InsuranceCompany, $state, $rootScope) {
        var vm = this;
       
        vm.insuranceProduct = entity;
        vm.clear = clear;
        vm.save = save;
        vm.insurancecompanies = InsuranceCompany.query();
        vm.childState = $state.current.name + '.dialog-find-company';

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.insuranceProduct.id !== null) {
                InsuranceProduct.update(vm.insuranceProduct, onSaveSuccess, onSaveError);
            } else {
                InsuranceProduct.save(vm.insuranceProduct, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('whatscoverApp:insuranceProductUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        
        var unsubscribe = $rootScope.$on('whatscoverApp:insuranceProductCompanyUpdate', function(event, result) {
        	vm.insuranceProduct.insuranceCompanyId = result.id;
            vm.insuranceProduct.insuranceCompanyName = result.name;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
