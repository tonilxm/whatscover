(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('AgentProfileDialogController', AgentProfileDialogController);

    AgentProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'AgentProfile', 'User', 'InsuranceCompany', 'InsuranceAgency', 'AgentProfileSendEmail', '$state', '$rootScope'];

    function AgentProfileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, AgentProfile, User, InsuranceCompany, InsuranceAgency, AgentProfileSendEmail, $state, $rootScope) {
        var vm = this;

        vm.agentProfile = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.sendEmail = sendEmail;
        vm.users = User.query();
        vm.insuranceCompanyState = $state.current.name + '.dialog-find-company';
        vm.insuranceAgencyState = $state.current.name + '.dialog-find-agency';

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

        function onSendEmailError () {
            vm.isSendEmail = false;
        }

        vm.datePickerOpenStatus.dob = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        
        var agencyUnsubscribe = $rootScope.$on('whatscoverApp:agentProfileAgencyUpdate', function(event, result) {
        	vm.agentProfile.insuranceAgencyId = result.id;
            vm.agentProfile.insuranceAgencyName = result.name;
        });
        $scope.$on('$destroy', agencyUnsubscribe);
        
        var companyUnsubscribe = $rootScope.$on('whatscoverApp:agentProfileCompanyUpdate', function(event, result) {
        	vm.agentProfile.insuranceCompanyId = result.id;
            vm.agentProfile.insuranceCompanyName = result.name;
        });
        $scope.$on('$destroy', companyUnsubscribe);

        function onSendEmailSuccess () {
            vm.isSendEmail = true;
        }

        function sendEmail(){
        	vm.isSendEmail = true;
			if (vm.agentProfile.id !== null) {
				alert("We've sent notification to your email");
			}else{
				AgentProfileSendEmail.email(vm.agentProfile, onSendEmailSuccess, onSendEmailError);
			}
        }
    }
})();
