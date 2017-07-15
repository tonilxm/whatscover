(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceAgencyDeleteController',InsuranceAgencyDeleteController);

    InsuranceAgencyDeleteController.$inject = ['$uibModalInstance', 'entity', 'InsuranceAgency'];

    function InsuranceAgencyDeleteController($uibModalInstance, entity, InsuranceAgency) {
        var vm = this;

        vm.insuranceAgency = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InsuranceAgency.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
