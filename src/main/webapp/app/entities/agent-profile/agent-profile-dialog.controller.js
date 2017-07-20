(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('AgentProfileDialogController', AgentProfileDialogController);

    AgentProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'AgentProfile', 'User', 'InsuranceCompany', 'InsuranceAgency', '$state', '$rootScope'];

    function AgentProfileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, AgentProfile, User, InsuranceCompany, InsuranceAgency, $state, $rootScope) {
        var vm = this;

        vm.agentProfile = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.insurancecompanies = InsuranceCompany.query();
        vm.insuranceagencies = InsuranceAgency.query();
        vm.childState = $state.current.name + '.dialog-find-agency';

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.agentProfile.id !== null) {
                AgentProfile.update(vm.agentProfile, onSaveSuccess, onSaveError);
            } else {
                AgentProfile.save(vm.agentProfile, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('whatscoverApp:agentProfileUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dob = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        
        var unsubscribe = $rootScope.$on('whatscoverApp:agentProfileAgencyUpdate', function(event, result) {
//        	vm.agentProfile.insuranceCompanyId = result.id;
//            vm.agentProfile.insuranceCompanyName = result.name;
            vm.agentProfile.insuranceAgencyId = result.id;
            vm.agentProfile.insuranceAgencyName = result.name;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
