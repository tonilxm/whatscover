(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('AgentProfileDetailController', AgentProfileDetailController);

    AgentProfileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AgentProfile', 'User', 'InsuranceCompany'];

    function AgentProfileDetailController($scope, $rootScope, $stateParams, previousState, entity, AgentProfile, User, InsuranceCompany) {
        var vm = this;

        vm.agentProfile = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('whatscoverApp:agentProfileUpdate', function(event, result) {
            vm.agentProfile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
