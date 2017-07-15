(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('AgentProfileDeleteController',AgentProfileDeleteController);

    AgentProfileDeleteController.$inject = ['$uibModalInstance', 'entity', 'AgentProfile'];

    function AgentProfileDeleteController($uibModalInstance, entity, AgentProfile) {
        var vm = this;

        vm.agentProfile = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AgentProfile.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
