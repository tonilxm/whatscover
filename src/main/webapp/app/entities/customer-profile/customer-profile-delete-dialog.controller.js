(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('CustomerProfileDeleteController',CustomerProfileDeleteController);

    CustomerProfileDeleteController.$inject = ['$uibModalInstance', 'entity', 'CustomerProfile'];

    function CustomerProfileDeleteController($uibModalInstance, entity, CustomerProfile) {
        var vm = this;

        vm.customerProfile = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CustomerProfile.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
