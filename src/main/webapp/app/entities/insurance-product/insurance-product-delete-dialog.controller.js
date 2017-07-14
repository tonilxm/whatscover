(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceProductDeleteController',InsuranceProductDeleteController);

    InsuranceProductDeleteController.$inject = ['$uibModalInstance', 'entity', 'InsuranceProduct'];

    function InsuranceProductDeleteController($uibModalInstance, entity, InsuranceProduct) {
        var vm = this;

        vm.insuranceProduct = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InsuranceProduct.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
