(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('AgentProfileController', AgentProfileController);

    AgentProfileController.$inject = ['$state', 'AgentProfile', 'AgentProfileSearch', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function AgentProfileController($state, AgentProfile, AgentProfileSearch, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.agentProfiles = [];
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.queryData = pagingParams.search;
        vm.currentSearch = pagingParams.search;

        loadAll();

        function loadAll () {
            if (pagingParams.search) {
                AgentProfileSearch.query({
                	queryData: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                AgentProfile.query({
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
                for (var i = 0; i < data.length; i++) {
                    vm.agentProfiles.push(data[i]);
                }
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
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search (queryData) {
            if (!queryData){
                return vm.clear();
            } 
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = queryData;
            vm.transition();
        }
        function clear () {
    		vm.agentProfiles = [];
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }
    }
})();
