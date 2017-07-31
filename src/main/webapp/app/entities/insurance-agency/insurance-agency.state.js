(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('insurance-agency', {
            parent: 'entity',
            url: '/insurance-agency/?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whatscoverApp.insuranceAgency.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insurance-agency/insurance-agencies.html',
                    controller: 'InsuranceAgencyController',
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
                    $translatePartialLoader.addPart('insuranceAgency');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('insurance-agency-detail', {
            parent: 'insurance-agency',
            url: '/insurance-agency/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whatscoverApp.insuranceAgency.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insurance-agency/insurance-agency-detail.html',
                    controller: 'InsuranceAgencyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('insuranceAgency');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InsuranceAgency', function($stateParams, InsuranceAgency) {
                    return InsuranceAgency.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'insurance-agency',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('insurance-agency-detail.edit', {
            parent: 'insurance-agency-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-agency/insurance-agency-dialog.html',
                    controller: 'InsuranceAgencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InsuranceAgency', function(InsuranceAgency) {
                            return InsuranceAgency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('insurance-agency.new', {
            parent: 'insurance-agency',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insurance-agency/insurance-agency-dialog.html',
                    controller: 'InsuranceAgencyDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('insuranceAgency');
                    return $translate.refresh();
                }],
                entity: function () {
                    return {
                        code: null,
                        name: null,
                        description: null,
                        id: null
                    };
                }
            }
            /*
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-agency/insurance-agency-dialog.html',
                    controller: 'InsuranceAgencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                name: null,
                                address_1: null,
                                address_2: null,
                                address_3: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('insurance-agency', null, { reload: 'insurance-agency' });
                }, function() {
                    $state.go('insurance-agency');
                });
            }]
            */
        })
        .state('insurance-agency.edit', {
            parent: 'insurance-agency',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insurance-agency/insurance-agency-dialog.html',
                    controller: 'InsuranceAgencyDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('insuranceAgency');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InsuranceAgency', function($stateParams, InsuranceAgency) {
                    return InsuranceAgency.get({id : $stateParams.id}).$promise;
                }]
            }
            /*
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-agency/insurance-agency-dialog.html',
                    controller: 'InsuranceAgencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InsuranceAgency', function(InsuranceAgency) {
                            return InsuranceAgency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('insurance-agency', null, { reload: 'insurance-agency' });
                }, function() {
                    $state.go('^');
                });
            }]
            */
        })
        .state('insurance-agency.delete', {
            parent: 'insurance-agency',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-agency/insurance-agency-delete-dialog.html',
                    controller: 'InsuranceAgencyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InsuranceAgency', function(InsuranceAgency) {
                            return InsuranceAgency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('insurance-agency', null, { reload: 'insurance-agency' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state(generateFindCompanyStateObj('insurance-agency.edit.dialog-find-company', 'insurance-agency.edit'))
        .state(generateFindCompanyStateObj('insurance-agency.new.dialog-find-company', 'insurance-agency.new'));
    }
    
   /**
    * generate State for company find dialog 
    */ 
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
	                    	 return 'insuranceAgencyCompanyUpdate';
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
	                 $state.go('insurance-agency', null, { reload: 'insurance-agency' });
	             }, function() {
	                 $state.go('^');
	             });
	        }]
    	};
    	
    	return obj;
    }

})();
