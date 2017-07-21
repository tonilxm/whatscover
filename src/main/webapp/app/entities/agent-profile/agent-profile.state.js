(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('agent-profile', {
            parent: 'entity',
            url: '/agent-profile?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whatscoverApp.agentProfile.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agent-profile/agent-profiles.html',
                    controller: 'AgentProfileController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('agentProfile');
                    $translatePartialLoader.addPart('gender');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('agent-profile-detail', {
            parent: 'agent-profile',
            url: '/agent-profile/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whatscoverApp.agentProfile.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agent-profile/agent-profile-detail.html',
                    controller: 'AgentProfileDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('agentProfile');
                    $translatePartialLoader.addPart('gender');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AgentProfile', function($stateParams, AgentProfile) {
                    return AgentProfile.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'agent-profile',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('agent-profile-detail.edit', {
            parent: 'agent-profile-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-profile/agent-profile-dialog.html',
                    controller: 'AgentProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AgentProfile', function(AgentProfile) {
                            return AgentProfile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agent-profile.new', {
            parent: 'agent-profile',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-profile/agent-profile-dialog.html',
                    controller: 'AgentProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                agent_code: null,
                                first_name: null,
                                middle_name: null,
                                last_name: null,
                                gender: null,
                                email: null,
                                dob: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('agent-profile', null, { reload: 'agent-profile' });
                }, function() {
                    $state.go('agent-profile');
                });
            }]
        })
        .state('agent-profile.edit', {
            parent: 'agent-profile',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-profile/agent-profile-dialog.html',
                    controller: 'AgentProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AgentProfile', function(AgentProfile) {
                            return AgentProfile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agent-profile', null, { reload: 'agent-profile' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agent-profile.delete', {
            parent: 'agent-profile',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-profile/agent-profile-delete-dialog.html',
                    controller: 'AgentProfileDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AgentProfile', function(AgentProfile) {
                            return AgentProfile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agent-profile', null, { reload: 'agent-profile' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state(generateFindCompanyStateObj('agent-profile.edit.dialog-find-company', 'agent-profile.edit'))
        .state(generateFindCompanyStateObj('agent-profile.new.dialog-find-company', 'agent-profile.new'))
        .state(generateFindAgencyStateObj('agent-profile.edit.dialog-find-agency', 'agent-profile.edit'))
        .state(generateFindAgencyStateObj('agent-profile.new.dialog-find-agency', 'agent-profile.new'));
    }
  
    function generateFindCompanyStateObj(name, parent){
    	var obj = {
			name: name,	
			parent: parent,
	        url: '/findCompany?page&sort&search',
	        data: {
	            authorities: ['ROLE_USER']
	        },
	        params: {
	        	page: '1',
	            sort: 'id,asc',
	            search: null
	        },
	        onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
	        	 $uibModal.open({
	                 templateUrl: 'app/entities/common-ui/common-dialog-find-company.html',
	                 controller: 'CommonDialogFindCompanyController',
	                 controllerAs: 'vm',
	                 backdrop: 'static',
	                 size: 'lg',
	                 resolve: {
	                     entity: null,
	                     emitName: function(){
	                    	 return 'agentProfileCompanyUpdate';
	                     },
	                     pagingParams: ['PaginationUtil', function (PaginationUtil) {
	                         return {
	                             page: PaginationUtil.parsePage(obj.params.page),
	                             sort: obj.params.sort,
	                             predicate: PaginationUtil.parsePredicate(obj.params.sort),
	                             ascending: PaginationUtil.parseAscending(obj.params.sort),
	                             search: obj.params.search
	                         };
	                     }],
	                     translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
	                         $translatePartialLoader.addPart('insuranceCompany');
	                         $translatePartialLoader.addPart('global');
	                         return $translate.refresh();
	                     }]
	                 }
	             }).result.then(function() {
	                 $state.go('agent-profile', null, { reload: 'agent-profile' });
	             }, function() {
	                 $state.go('^');
	             });
	        }]
    	};
    	
    	return obj;
    }
    
    function generateFindAgencyStateObj(name, parent){
    	var obj = {
			name: name,	
			parent: parent,
	        url: '/findAgency?page&sort&search',
	        data: {
	            authorities: ['ROLE_USER']
	        },
	        params: {
	            page: '1',
	            sort: 'id,asc',
	            search: null
	        },
	        onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
	        	 $uibModal.open({
	                 templateUrl: 'app/entities/common-ui/common-dialog-find-agency.html',
	                 controller: 'CommonDialogFindAgencyController',
	                 controllerAs: 'vm',
	                 backdrop: 'static',
	                 size: 'lg',
	                 resolve: {
	                	 entity: null,
	                     emitName: function(){
	                    	 return 'agentProfileAgencyUpdate';
	                     },
	                     pagingParams: ['PaginationUtil', function (PaginationUtil) {
	                         return {
	                             page: PaginationUtil.parsePage(obj.page),
	                             sort: obj.params.sort,
	                             predicate: PaginationUtil.parsePredicate(obj.params.sort),
	                             ascending: PaginationUtil.parseAscending(obj.params.sort),
	                             search: obj.params.search
	                         };
	                     }],
	                     translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
	                         $translatePartialLoader.addPart('insuranceAgency');
	                         $translatePartialLoader.addPart('global');
	                         return $translate.refresh();
	                     }]
	                 }
	             }).result.then(function() {
	                 $state.go('agent-profile', null, { reload: 'agent-profile' });
	             }, function() {
	                 $state.go('^');
	             });
	        }]
    	};
    	
    	return obj;
    }

})();
