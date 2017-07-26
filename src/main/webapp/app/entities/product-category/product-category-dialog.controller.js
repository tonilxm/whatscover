(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('ProductCategoryDialogController', ProductCategoryDialogController);

    ProductCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProductCategory'];

    function ProductCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProductCategory) {
        var vm = this;

        vm.productCategory = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.productCategory.id !== null) {
                ProductCategory.update(vm.productCategory, onSaveSuccess, onSaveError);
            } else {
                ProductCategory.save(vm.productCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('whatscoverApp:productCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
