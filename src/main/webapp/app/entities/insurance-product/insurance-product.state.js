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
                authorities: ['ROLE_ADMIN'],
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
        .state('insurance-product.edit', {
            parent: 'insurance-product',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    template: '<tabs data="tabData" type="tabs"></tabs><div ui-view="view1"></div>',
                    controller: 'InsuranceProductRegistrationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('insuranceProduct');
                    $translatePartialLoader.addPart('gender');
                    $translatePartialLoader.addPart('insuranceProductPremiumRate');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }],
                entity: ['$stateParams','InsuranceProduct', function($stateParams, InsuranceProduct) {
                    return InsuranceProduct.get({id : $stateParams.id}).$promise;
                }],
                pagingParams: ['PaginationUtil', function (PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage('1'),
                        sort: 'id,asc',
                        predicate: PaginationUtil.parsePredicate('id,asc'),
                        ascending: PaginationUtil.parseAscending('id,asc'),
                        search: null
                    };
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
        .state('insurance-product.edit.general', {
        	parent: 'insurance-product.edit',
            url:         '/general',
            views: {
                "view1": {
                    //controller: "InsuranceProductRegistrationController as vm",
                    templateUrl: "app/entities/insurance-product/insurance-product-registration.html",
                }
            }
        })
        .state('insurance-product.edit.premiumrate', {
        	parent: 'insurance-product.edit',
            url:         '/premiumrate',
            views: {
                "view1": {
                    //controller: "InsuranceProductRegistrationController as vm",
                    templateUrl: "app/entities/insurance-product/premium-rate/premium-rates.html",
                }
            }
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
        .state('insurance-product.registration', {
            parent: 'insurance-product',
            url: '/registration',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    template: '<tabs data="tabData" type="tabs"></tabs><div ui-view="view1"></div>',
                    controller: 'InsuranceProductRegistrationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('insuranceProduct');
                    $translatePartialLoader.addPart('gender');
                    $translatePartialLoader.addPart('insuranceProductPremiumRate');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }],
                entity: function () {
                    return {
                        code: null,
                        name: null,
                        shortDescription: null,
                        longDescription: null,
                        minEntryAgeLastBday: null,
                        maxEntryAgeLastBday: null,
                        minSumAssured: null,
                        maxSumAssured: null,
                        premUnit: null,
                        prodWeightLife: null,
                        prodWeightMedical: null,
                        gender: null,
                        productWeightPA: null,
                        productWeightHospIncome: null,
                        productWeightCI: null,
                        productWeightCancer: null,
                        id: null
                    };
                },
                pagingParams: ['PaginationUtil', function (PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage('1'),
                        sort: 'id,asc',
                        predicate: PaginationUtil.parsePredicate('id,asc'),
                        ascending: PaginationUtil.parseAscending('id,asc'),
                        search: null
                    };
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
        .state('insurance-product.registration.general', {
        	parent: 'insurance-product.registration',
            url:         '/general',
            views: {
                "view1": {
                    //controller: "InsuranceProductRegistrationController as vm",
                    templateUrl: "app/entities/insurance-product/insurance-product-registration.html",
                }
            }
        })
        .state('insurance-product.registration.premiumrate', {
        	parent: 'insurance-product.registration',
            url:         '/premiumrate',
            views: {
                "view1": {
                    //controller: "InsuranceProductRegistrationController as vm",
                    templateUrl: "app/entities/insurance-product/premium-rate/premium-rates.html",
                }
            }
        })       
        .state(generateFindEntityStateObj('insurance-product.registration.dialog-find-company', 'insurance-product.registration.general',
        		'/findCompany?page&sort&search', 'app/entities/common-ui/common-dialog-find-company.html', 'CommonDialogFindCompanyController',
        		'insuranceProductCompanyUpdate', 'insuranceCompany'))
        .state(generateFindEntityStateObj('insurance-product.edit.dialog-find-company', 'insurance-product.edit.general',
        		'/findCompany?page&sort&search', 'app/entities/common-ui/common-dialog-find-company.html', 'CommonDialogFindCompanyController',
        		'insuranceProductCompanyUpdate', 'insuranceCompany'))
        .state(generateFindEntityStateObj('insurance-product.registration.dialog-find-product-category', 'insurance-product.registration.general',
        		'/findProductCategory?page&sort&search', 'app/entities/common-ui/common-dialog-find-product-category.html', 'CommonDialogFindProductCategoryController',
        		'insuranceProductCategoryUpdate', 'productCategory'))
        .state(generateFindEntityStateObj('insurance-product.edit.dialog-find-product-category', 'insurance-product.edit.general',
        		'/findProductCategory?page&sort&search', 'app/entities/common-ui/common-dialog-find-product-category.html', 'CommonDialogFindProductCategoryController',
        		'insuranceProductCategoryUpdate', 'productCategory'));
    }
    
    /**
     * generate find entity dialog state
     */
    function generateFindEntityStateObj(name, parent, url, templateUrl, controller, emitName, entityName){
    	var obj = {
			name: name,
			parent: parent,
	        url: url,
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
	                 templateUrl: templateUrl,
	                 controller: controller,
	                 controllerAs: 'vm',
	                 backdrop: 'static',
	                 size: 'lg',
	                 resolve: {
	                     entity: null,
	                     emitName: function(){
	                    	 return emitName;
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
	                         $translatePartialLoader.addPart(entityName);
	                         $translatePartialLoader.addPart('global');
	                         return $translate.refresh();
	                     }]
	                 }
	             }).result.then(function() {
	                 $state.go(parent, null, { reload: parent });
	             }, function() {
	                 $state.go('^');
	             });
	        }]
    	};

    	return obj;
    }

})();
