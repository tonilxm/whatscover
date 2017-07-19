(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('AppointmentDetailController', AppointmentDetailController);

    AppointmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Appointment', 'CustomerProfile', 'AgentProfile'];

    function AppointmentDetailController($scope, $rootScope, $stateParams, previousState, entity, Appointment, CustomerProfile, AgentProfile) {
        var vm = this;

        vm.appointment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('whatscoverApp:appointmentUpdate', function(event, result) {
            vm.appointment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
