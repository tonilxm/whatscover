(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('CustomerProfileDialogController', CustomerProfileDialogController);

    CustomerProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$q', '$window' ,'entity', 'CustomerProfile', 'User'];

    function CustomerProfileDialogController ($timeout, $scope, $stateParams, $q, $window, entity, CustomerProfile, User) {
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
            $window.history.back();
        }

        function save () {
            vm.isSaving = true;
            if (vm.customerProfile.id !== null) {
                CustomerProfile.update(vm.customerProfile, onSaveSuccess, onSaveError);
            } else {
                CustomerProfile.save(vm.customerProfile, onSaveSuccess, onSaveError);
            }
            $window.history.back();
        }

        function onSaveSuccess (result) {
            $scope.$emit('whatscoverApp:customerProfileUpdate', result);
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
