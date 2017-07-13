(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('insuarance-agency', {
            parent: 'entity',
            url: '/insuarance-agency',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whatscoverApp.insuaranceAgency.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insuarance-agency/insuarance-agencies.html',
                    controller: 'InsuaranceAgencyController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('insuaranceAgency');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('insuarance-agency-detail', {
            parent: 'insuarance-agency',
            url: '/insuarance-agency/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whatscoverApp.insuaranceAgency.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insuarance-agency/insuarance-agency-detail.html',
                    controller: 'InsuaranceAgencyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('insuaranceAgency');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InsuaranceAgency', function($stateParams, InsuaranceAgency) {
                    return InsuaranceAgency.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'insuarance-agency',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('insuarance-agency-detail.edit', {
            parent: 'insuarance-agency-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insuarance-agency/insuarance-agency-dialog.html',
                    controller: 'InsuaranceAgencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InsuaranceAgency', function(InsuaranceAgency) {
                            return InsuaranceAgency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('insuarance-agency.new', {
            parent: 'insuarance-agency',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insuarance-agency/insuarance-agency-dialog.html',
                    controller: 'InsuaranceAgencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                insuarance_company_id: null,
                                name: null,
                                address_1: null,
                                address_2: null,
                                address_3: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('insuarance-agency', null, { reload: 'insuarance-agency' });
                }, function() {
                    $state.go('insuarance-agency');
                });
            }]
        })
        .state('insuarance-agency.edit', {
            parent: 'insuarance-agency',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insuarance-agency/insuarance-agency-dialog.html',
                    controller: 'InsuaranceAgencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InsuaranceAgency', function(InsuaranceAgency) {
                            return InsuaranceAgency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('insuarance-agency', null, { reload: 'insuarance-agency' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('insuarance-agency.delete', {
            parent: 'insuarance-agency',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insuarance-agency/insuarance-agency-delete-dialog.html',
                    controller: 'InsuaranceAgencyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InsuaranceAgency', function(InsuaranceAgency) {
                            return InsuaranceAgency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('insuarance-agency', null, { reload: 'insuarance-agency' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
