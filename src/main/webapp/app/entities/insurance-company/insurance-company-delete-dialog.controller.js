(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceCompanyDeleteController',InsuranceCompanyDeleteController);

    InsuranceCompanyDeleteController.$inject = ['$uibModalInstance', 'entity', 'InsuranceCompany'];

    function InsuranceCompanyDeleteController($uibModalInstance, entity, InsuranceCompany) {
        var vm = this;

        vm.insuranceCompany = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InsuranceCompany.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
