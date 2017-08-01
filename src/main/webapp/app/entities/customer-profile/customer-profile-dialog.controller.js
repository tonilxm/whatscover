(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('CustomerProfileDialogController', CustomerProfileDialogController);

    CustomerProfileDialogController.$inject = ['$state', '$timeout', '$scope', '$stateParams', '$q', '$window' ,'entity', 'CustomerProfile', 'User'];

    function CustomerProfileDialogController ($state, $timeout, $scope, $stateParams, $q, $window, entity, CustomerProfile, User) {
        var vm = this;

        vm.customerProfile = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            //$uibModalInstance.dismiss('cancel');
            $state.go('customer-profile', {}, { reload: true});
        }

        function save () {
            vm.isSaving = true;
            if (vm.customerProfile.id !== null) {
                CustomerProfile.update(vm.customerProfile, onSaveSuccess, onSaveError);
            } else {
                CustomerProfile.save(vm.customerProfile, onSaveSuccess, onSaveError);
            }
            //$window.history.back();
            $state.go('customer-profile', {}, { reload: true});
        }

        function onSaveSuccess (result) {
            $scope.$emit('whatscoverApp:customerProfileUpdate', result);
            $state.reload();
            //$uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dob = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
