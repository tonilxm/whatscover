(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('CustomerProfileDetailController', CustomerProfileDetailController);

    CustomerProfileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CustomerProfile', 'User'];

    function CustomerProfileDetailController($scope, $rootScope, $stateParams, previousState, entity, CustomerProfile, User) {
        var vm = this;

        vm.customerProfile = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('whatscoverApp:customerProfileUpdate', function(event, result) {
            vm.customerProfile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
