(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceProductDetailController', InsuranceProductDetailController);

    InsuranceProductDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'InsuranceProduct', 'InsuranceCompany', 'ProductCategory'];

    function InsuranceProductDetailController($scope, $rootScope, $stateParams, previousState, entity, InsuranceProduct, InsuranceCompany, ProductCategory) {
        var vm = this;

        vm.insuranceProduct = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('whatscoverApp:insuranceProductUpdate', function(event, result) {
            vm.insuranceProduct = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
