(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('insurance-product', {
            parent: 'entity',
            url: '/insurance-product?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whatscoverApp.insuranceProduct.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insurance-product/insurance-products.html',
                    controller: 'InsuranceProductController',
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
                    $translatePartialLoader.addPart('insuranceProduct');
                    $translatePartialLoader.addPart('gender');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('insurance-product-detail', {
            parent: 'insurance-product',
            url: '/insurance-product/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whatscoverApp.insuranceProduct.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insurance-product/insurance-product-detail.html',
                    controller: 'InsuranceProductDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('insuranceProduct');
                    $translatePartialLoader.addPart('gender');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InsuranceProduct', function($stateParams, InsuranceProduct) {
                    return InsuranceProduct.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'insurance-product',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('insurance-product-detail.edit', {
            parent: 'insurance-product-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-product/insurance-product-dialog.html',
                    controller: 'InsuranceProductDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InsuranceProduct', function(InsuranceProduct) {
                            return InsuranceProduct.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('insurance-product.new', {
            parent: 'insurance-product',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-product/insurance-product-dialog.html',
                    controller: 'InsuranceProductDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                name: null,
                                entryAgeLastBday: null,
                                gender: null,
                                premiumTerm: null,
                                policyTerm: null,
                                premiumRate: null,
                                sumAssuredDeath: null,
                                sumAssuredTPD: null,
                                sumAssuredADD: null,
                                sumAssuredHospIncome: null,
                                sumAssuredCI: null,
                                sumAssuredMedic: null,
                                sumAssuredCancer: null,
                                productWeightDeath: null,
                                productWeightPA: null,
                                productWeightHospIncome: null,
                                productWeightCI: null,
                                productWeightMedic: null,
                                productWeightCancer: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('insurance-product', null, { reload: 'insurance-product' });
                }, function() {
                    $state.go('insurance-product');
                });
            }]
        })
        .state('insurance-product.edit', {
            parent: 'insurance-product',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-product/insurance-product-dialog.html',
                    controller: 'InsuranceProductDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InsuranceProduct', function(InsuranceProduct) {
                            return InsuranceProduct.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('insurance-product', null, { reload: 'insurance-product' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('insurance-product.delete', {
            parent: 'insurance-product',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurance-product/insurance-product-delete-dialog.html',
                    controller: 'InsuranceProductDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InsuranceProduct', function(InsuranceProduct) {
                            return InsuranceProduct.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('insurance-product', null, { reload: 'insurance-product' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state(generateFindCompanyStateObj('insurance-product.edit.dialog-find-company', 'insurance-product.edit'))
        .state(generateFindCompanyStateObj('insurance-product.new.dialog-find-company', 'insurance-product.new'));
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
	                    	 return 'insuranceProductCompanyUpdate';
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
	                 $state.go('insurance-product', null, { reload: 'insurance-product' });
	             }, function() {
	                 $state.go('^');
	             });
	        }]
    	};
    	
    	return obj;
    }

})();
