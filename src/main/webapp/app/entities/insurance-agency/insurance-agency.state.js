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
            url: '/insurance-agency',
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
            resolve: {
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
        })
        .state('insurance-agency.edit', {
            parent: 'insurance-agency',
            url: '/{id}/edit',
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
                    $state.go('insurance-agency', null, { reload: 'insurance-agency' });
                }, function() {
                    $state.go('^');
                });
            }]
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
        });
    }

})();