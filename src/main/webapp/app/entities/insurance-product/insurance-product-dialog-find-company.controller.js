(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceProductDialogFindCompanyController', InsuranceProductDialogFindCompanyController);

    InsuranceProductDialogFindCompanyController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InsuranceProduct', 'InsuranceCompany', 'InsuranceCompanySearch', 'paginationConstants', 'pagingParams', 'ParseLinks', '$state', 'AlertService', '$uibModal'];

    function InsuranceProductDialogFindCompanyController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InsuranceProduct, InsuranceCompany, InsuranceCompanySearch, paginationConstants, pagingParams, ParseLinks, $state, AlertService, $uibModal) {
        var vm = this;
        vm.loadPage = loadPage;
        vm.clear = clear;
        vm.choose = choose;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.search = search;
        vm.loadInsuranceCompanies = loadInsuranceCompanies;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;
        vm.cancel = cancel;
        
        loadInsuranceCompanies();

        function loadInsuranceCompanies () {
        	if (pagingParams.search) {
                InsuranceCompanySearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                InsuranceCompany.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.insuranceCompanies = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }
        
        function transition() {
        	pagingParams.page = vm.page;
        	pagingParams.sort = vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc');
        	pagingParams.search = vm.currentSearch;
        	loadInsuranceCompanies();
        }

        function search(searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear() {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }
        
        function cancel(){
        	$uibModalInstance.dismiss('cancel');
        }

        function choose (id) {
        	$uibModal.open({
                templateUrl: 'app/entities/insurance-product/insurance-product-choose-company-dialog.html',
                controller: 'InsuranceCompanyChooseController',
                controllerAs: 'vm',
                size: 'md',
                resolve: {
                    entity: ['InsuranceCompany', function(InsuranceCompany) {
                        return InsuranceCompany.get({id : id}).$promise;
                    }],
                    parentModal: $uibModalInstance,
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('insuranceCompany');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
        }

    }
})();
