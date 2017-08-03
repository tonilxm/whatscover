(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceAgencyDialogController', InsuranceAgencyDialogController);

    InsuranceAgencyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$window', 'entity', 'InsuranceAgency', 'InsuranceCompany', '$rootScope', '$state'];

    function InsuranceAgencyDialogController ($timeout, $scope, $stateParams, $window, entity, InsuranceAgency, InsuranceCompany, $rootScope, $state) {
        var vm = this;

        vm.insuranceAgency = entity;
        vm.clear = clear;
        vm.save = save;
        vm.childState = $state.current.name + '.dialog-find-company';

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
//        	$window.history.back();
        	 $state.go('insurance-agency', {}, { reload: true});
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
            $state.reload();
//            $uibModalInstance.close(result);
            vm.isSaving = false;
            $state.go('insurance-agency', {}, { reload: true});
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        var unsubscribe = $rootScope.$on('whatscoverApp:insuranceAgencyCompanyUpdate', function(event, result) {
        	vm.insuranceAgency.insuranceCompanyId = result.id;
            vm.insuranceAgency.insuranceCompanyName = result.name;
        });
        $scope.$on('$destroy', unsubscribe);
        
    }
})();
