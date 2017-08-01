(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('ProductCategoryDialogController', ProductCategoryDialogController);

    ProductCategoryDialogController.$inject = ['$state', '$timeout', '$scope', '$stateParams', '$window', 'entity', 'ProductCategory'];

    function ProductCategoryDialogController ($state, $timeout, $scope, $stateParams, $window, entity, ProductCategory) {
        var vm = this;

        vm.productCategory = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $window.history.back();
        }

        function save () {
            vm.isSaving = true;
            if (vm.productCategory.id !== null) {
                ProductCategory.update(vm.productCategory, onSaveSuccess, onSaveError);
            } else {
                ProductCategory.save(vm.productCategory, onSaveSuccess, onSaveError);
            }
            $state.go('product-category', {}, { reload: true});
        }

        function onSaveSuccess (result) {
            $scope.$emit('whatscoverApp:productCategoryUpdate', result);
            $state.reload()
            //$uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
