(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('ProductCategoryDeleteController',ProductCategoryDeleteController);

    ProductCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProductCategory'];

    function ProductCategoryDeleteController($uibModalInstance, entity, ProductCategory) {
        var vm = this;

        vm.productCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProductCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
