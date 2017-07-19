(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceCompanyChooseController',InsuranceCompanyChooseController);

    InsuranceCompanyChooseController.$inject = ['$uibModalInstance', 'entity', 'InsuranceCompany', '$scope', 'parentModal'];

    function InsuranceCompanyChooseController($uibModalInstance, entity, InsuranceCompany, $scope, parentModal) {
        var vm = this;

        vm.entity = entity;
        vm.clear = clear;
        vm.confirmChoose = confirmChoose;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmChoose () {
            $scope.$emit('whatscoverApp:insuranceProductCompanyUpdate',vm.entity);
        	$uibModalInstance.close(true);
        	parentModal.dismiss('cancel');
        }
    }
})();
