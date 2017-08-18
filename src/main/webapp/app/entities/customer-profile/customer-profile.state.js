(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('customer-profile', {
            parent: 'entity',
            url: '/customer-profile?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'whatscoverApp.customerProfile.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-profile/customer-profiles.html',
                    controller: 'CustomerProfileController',
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
                    $translatePartialLoader.addPart('customerProfile');
                    $translatePartialLoader.addPart('gender');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('customer-profile-detail', {
            parent: 'customer-profile',
            url: '/customer-profile/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whatscoverApp.customerProfile.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-profile/customer-profile-detail.html',
                    controller: 'CustomerProfileDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customerProfile');
                    $translatePartialLoader.addPart('gender');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CustomerProfile', function($stateParams, CustomerProfile) {
                    return CustomerProfile.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'customer-profile',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('customer-profile-detail.edit', {
            parent: 'customer-profile-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-profile/customer-profile-dialog.html',
                    controller: 'CustomerProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomerProfile', function(CustomerProfile) {
                            return CustomerProfile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer-profile.new', {
            parent: 'customer-profile',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-profile/customer-profile-dialog.html',
                    controller: 'CustomerProfileDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customerProfile');
                    $translatePartialLoader.addPart('gender');
                    return $translate.refresh();
                }],
                entity: function () {
                    return {
                        firstName: null,
                        middleName: null,
                        lastName: null,
                        gender: null,
                        email: null,
                        dob: null,
                        id: null
                    };
                },
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'customer-profile',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
            /*
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-profile/customer-profile-dialog.html',
                    controller: 'CustomerProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                middleName: null,
                                lastName: null,
                                gender: null,
                                email: null,
                                dob: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('customer-profile', null, { reload: 'customer-profile' });
                }, function() {
                    $state.go('customer-profile');
                });
            }] */
        })
        .state('customer-profile.edit', {
            parent: 'customer-profile',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-profile/customer-profile-dialog.html',
                    controller: 'CustomerProfileDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customerProfile');
                    $translatePartialLoader.addPart('gender');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CustomerProfile', function($stateParams, CustomerProfile) {
                    return CustomerProfile.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'customer-profile',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
            /*
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-profile/customer-profile-dialog.html',
                    controller: 'CustomerProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomerProfile', function(CustomerProfile) {
                            return CustomerProfile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-profile', null, { reload: 'customer-profile' });
                }, function() {
                    $state.go('^');
                });
            }]
            */
        })
        .state('customer-profile.delete', {
            parent: 'customer-profile',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', '$location', function($stateParams, $state, $uibModal, $location) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-profile/customer-profile-delete-dialog.html',
                    controller: 'CustomerProfileDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CustomerProfile', function(CustomerProfile) {
                            return CustomerProfile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
//                    $state.go('customer-profile', null, { reload: 'customer-profile' });
                	$location.url('/customer-profile');
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
