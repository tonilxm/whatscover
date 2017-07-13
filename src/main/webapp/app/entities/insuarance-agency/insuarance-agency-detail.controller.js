(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuaranceAgencyDetailController', InsuaranceAgencyDetailController);

    InsuaranceAgencyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'InsuaranceAgency'];

    function InsuaranceAgencyDetailController($scope, $rootScope, $stateParams, previousState, entity, InsuaranceAgency) {
        var vm = this;

        vm.insuaranceAgency = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('whatscoverApp:insuaranceAgencyUpdate', function(event, result) {
            vm.insuaranceAgency = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
