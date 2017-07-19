(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('AppointmentDialogController', AppointmentDialogController);

    AppointmentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Appointment', 'CustomerProfile', 'AgentProfile'];

    function AppointmentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Appointment, CustomerProfile, AgentProfile) {
        var vm = this;

        vm.appointment = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.customerprofiles = CustomerProfile.query();
        vm.agentprofiles = AgentProfile.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.appointment.id !== null) {
                Appointment.update(vm.appointment, onSaveSuccess, onSaveError);
            } else {
                Appointment.save(vm.appointment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('whatscoverApp:appointmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.datetime = false;
        vm.datePickerOpenStatus.assignedDatetime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
