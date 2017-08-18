(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('insurance-product-premium-rate', {
            parent: 'entity',
            url: '/insurance-product-premium-rate?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whatscoverApp.insuranceProductPremiumRate.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insurance-product-premium-rate/insurance-product-premium-rates.html',
                    controller: 'InsuranceProductPremiumRateController',
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
                    $translatePartialLoader.addPart('insuranceProductPremiumRate');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('insurance-product-premium-rate-detail', {
            parent: 'insurance-product-premium-rate',
            url: '/insurance-product-premium-rate/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whatscoverApp.insuranceProductPremiumRate.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insurance-product-premium-rate/insurance-product-premium-rate-detail.html',
                    controller: 'InsuranceProductPremiumRateDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('insuranceProductPremiumRate');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InsuranceProductPremiumRate', function($stateParams, InsuranceProductPremiumRate) {
                    return InsuranceProductPremiumRate.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'insurance-product-premium-rate',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('insurance-product-premium-rate-detail.edit', {
            parent: 'insurance-product-premium-rate-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-product-premium-rate/insurance-product-premium-rate-dialog.html',
                    controller: 'InsuranceProductPremiumRateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InsuranceProductPremiumRate', function(InsuranceProductPremiumRate) {
                            return InsuranceProductPremiumRate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('insurance-product-premium-rate.new', {
            parent: 'insurance-product-premium-rate',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-product-premium-rate/insurance-product-premium-rate-dialog.html',
                    controller: 'InsuranceProductPremiumRateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                entryAge: null,
                                malePremiumRate: null,
                                femalePremiumRate: null,
                                plan: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('insurance-product-premium-rate', null, { reload: 'insurance-product-premium-rate' });
                }, function() {
                    $state.go('insurance-product-premium-rate');
                });
            }]
        })
        .state('insurance-product-premium-rate.edit', {
            parent: 'insurance-product-premium-rate',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-product-premium-rate/insurance-product-premium-rate-dialog.html',
                    controller: 'InsuranceProductPremiumRateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InsuranceProductPremiumRate', function(InsuranceProductPremiumRate) {
                            return InsuranceProductPremiumRate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('insurance-product-premium-rate', null, { reload: 'insurance-product-premium-rate' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('insurance-product-premium-rate.delete', {
            parent: 'insurance-product-premium-rate',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', '$location', function($stateParams, $state, $uibModal, $location) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-product-premium-rate/insurance-product-premium-rate-delete-dialog.html',
                    controller: 'InsuranceProductPremiumRateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InsuranceProductPremiumRate', function(InsuranceProductPremiumRate) {
                            return InsuranceProductPremiumRate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
//                    $state.go('insurance-product-premium-rate', null, { reload: 'insurance-product-premium-rate' });
                	$location.url('/insurance-product-premium-rate');
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
