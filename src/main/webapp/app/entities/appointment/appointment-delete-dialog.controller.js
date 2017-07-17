(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('AppointmentDeleteController',AppointmentDeleteController);

    AppointmentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Appointment'];

    function AppointmentDeleteController($uibModalInstance, entity, Appointment) {
        var vm = this;

        vm.appointment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Appointment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
