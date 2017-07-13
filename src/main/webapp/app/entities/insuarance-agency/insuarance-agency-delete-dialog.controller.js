(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuaranceAgencyDeleteController',InsuaranceAgencyDeleteController);

    InsuaranceAgencyDeleteController.$inject = ['$uibModalInstance', 'entity', 'InsuaranceAgency'];

    function InsuaranceAgencyDeleteController($uibModalInstance, entity, InsuaranceAgency) {
        var vm = this;

        vm.insuaranceAgency = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InsuaranceAgency.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
