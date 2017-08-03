(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('product-category', {
            parent: 'entity',
            url: '/product-category?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'whatscoverApp.productCategory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-category/product-categories.html',
                    controller: 'ProductCategoryController',
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
                    $translatePartialLoader.addPart('productCategory');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('product-category-detail', {
            parent: 'product-category',
            url: '/product-category/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whatscoverApp.productCategory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-category/product-category-detail.html',
                    controller: 'ProductCategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('productCategory');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProductCategory', function($stateParams, ProductCategory) {
                    return ProductCategory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'product-category',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('product-category-detail.edit', {
            parent: 'product-category-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-category/product-category-dialog.html',
                    controller: 'ProductCategoryDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('productCategory');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProductCategory', function($stateParams, ProductCategory) {
                    return ProductCategory.get({id : $stateParams.id}).$promise;
                }]
            }
            /*
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-category/product-category-dialog.html',
                    controller: 'ProductCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProductCategory', function(ProductCategory) {
                            return ProductCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }] */
        })
        .state('product-category.new', {
            parent: 'product-category',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-category/product-category-dialog.html',
                    controller: 'ProductCategoryDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('productCategory');
                    return $translate.refresh();
                }],
                entity: function () {
                    return {
                        name: null,
                        id: null
                    };
                },
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'product-category',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
            /*
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-category/product-category-dialog.html',
                    controller: 'ProductCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('product-category', null, { reload: 'product-category' });
                }, function() {
                    $state.go('product-category');
                });
            }] */
        })
        .state('product-category.edit', {
            parent: 'product-category',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-category/product-category-dialog.html',
                    controller: 'ProductCategoryDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('productCategory');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProductCategory', function($stateParams, ProductCategory) {
                    return ProductCategory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'product-category',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
            /*
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-category/product-category-dialog.html',
                    controller: 'ProductCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProductCategory', function(ProductCategory) {
                            return ProductCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-category', null, { reload: 'product-category' });
                }, function() {
                    $state.go('^');
                });
            }]
            */
        })
        .state('product-category.delete', {
            parent: 'product-category',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-category/product-category-delete-dialog.html',
                    controller: 'ProductCategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProductCategory', function(ProductCategory) {
                            return ProductCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-category', null, { reload: 'product-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
