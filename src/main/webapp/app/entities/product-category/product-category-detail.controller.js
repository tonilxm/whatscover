(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('ProductCategoryDetailController', ProductCategoryDetailController);

    ProductCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProductCategory'];

    function ProductCategoryDetailController($scope, $rootScope, $stateParams, previousState, entity, ProductCategory) {
        var vm = this;

        vm.productCategory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('whatscoverApp:productCategoryUpdate', function(event, result) {
            vm.productCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
