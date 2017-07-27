(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('insurance-company', {
            parent: 'entity',
            url: '/insurance-company?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'whatscoverApp.insuranceCompany.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insurance-company/insurance-companies.html',
                    controller: 'InsuranceCompanyController',
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
                    $translatePartialLoader.addPart('insuranceCompany');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('insurance-company-detail', {
            parent: 'insurance-company',
            url: '/insurance-company/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whatscoverApp.insuranceCompany.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insurance-company/insurance-company-detail.html',
                    controller: 'InsuranceCompanyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('insuranceCompany');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InsuranceCompany', function($stateParams, InsuranceCompany) {
                    return InsuranceCompany.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'insurance-company',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('insurance-company-detail.edit', {
            parent: 'insurance-company-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-company/insurance-company-dialog.html',
                    controller: 'InsuranceCompanyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InsuranceCompany', function(InsuranceCompany) {
                            return InsuranceCompany.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('insurance-company.new', {
            parent: 'insurance-company',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-company/insurance-company-dialog.html',
                    controller: 'InsuranceCompanyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                name: null,
                                description: null,
                                address_1: null,
                                address_2: null,
                                address_3: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('insurance-company', null, { reload: 'insurance-company' });
                }, function() {
                    $state.go('insurance-company');
                });
            }]
        })
        .state('insurance-company.edit', {
            parent: 'insurance-company',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-company/insurance-company-dialog.html',
                    controller: 'InsuranceCompanyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InsuranceCompany', function(InsuranceCompany) {
                            return InsuranceCompany.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('insurance-company', null, { reload: 'insurance-company' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('insurance-company.delete', {
            parent: 'insurance-company',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-company/insurance-company-delete-dialog.html',
                    controller: 'InsuranceCompanyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InsuranceCompany', function(InsuranceCompany) {
                            return InsuranceCompany.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('insurance-company', null, { reload: 'insurance-company' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
