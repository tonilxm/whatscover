(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('AgentProfileController', AgentProfileController);

    AgentProfileController.$inject = ['AgentProfile', 'AgentProfileSearch', 'ParseLinks', 'AlertService', 'paginationConstants'];

    function AgentProfileController(AgentProfile, AgentProfileSearch, ParseLinks, AlertService, paginationConstants) {

        var vm = this;

        vm.agentProfiles = [];
        vm.queryData = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        vm.clear = clear;
        vm.loadAll = loadAll;
        vm.search = search;

        loadAll();

        function loadAll () {
            if (vm.queryData && vm.queryData.length == 5) 
            {
                AgentProfileSearch.query({
                	queryData: vm.queryData,
                    page: vm.page,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                AgentProfile.query({
                    page: vm.page,
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
                for (var i = 0; i < data.length; i++) {
                    vm.agentProfiles.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.agentProfiles = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
        
        function pushData(searchQueryByFirstName, searchQueryByMiddleName,
        		searchQueryByLastName, searchQueryByCompanyName, searchQueryByAgentName) 
        {
        	if (!searchQueryByFirstName) { searchQueryByFirstName = ""; } 
        	if (!searchQueryByMiddleName) { searchQueryByMiddleName = ""; }
        	if (!searchQueryByLastName) { searchQueryByLastName = ""; } 
        	if (!searchQueryByCompanyName) { searchQueryByCompanyName = ""; }
        	if (!searchQueryByAgentName) { searchQueryByAgentName = ""; } 
        	
        	vm.queryData = [];
        	vm.queryData.push(searchQueryByFirstName, searchQueryByMiddleName, 
        			searchQueryByLastName, searchQueryByCompanyName, searchQueryByAgentName);
    	}
        
        function clear () {
            vm.agentProfiles = [];
            vm.links = {
                last: 0
            };
            vm.page = 0;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.queryData = [];
            vm.loadAll();
        }

        function search (searchQueryByFirstName, searchQueryByMiddleName,
        		searchQueryByLastName, searchQueryByCompanyName, searchQueryByAgentName) {
            if (!searchQueryByFirstName && !searchQueryByMiddleName 
            		&& !searchQueryByLastName && !searchQueryByCompanyName
            		&& !searchQueryByAgentName){
                return vm.clear();
            } 
            vm.agentProfiles = [];
            vm.links = {
                last: 0
            };
            vm.page = 0;
            vm.predicate = '_score';
            vm.reverse = false;
            pushData(searchQueryByFirstName, searchQueryByMiddleName, searchQueryByLastName,
            		searchQueryByCompanyName, searchQueryByAgentName);
            vm.loadAll();
        }
    }
})();
