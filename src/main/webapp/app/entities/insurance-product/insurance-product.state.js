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
        .state(generateProductManagementState('insurance-product-detail', 'insurance-product',
        		'/insurance-product/{id}', ['$stateParams','InsuranceProduct', function($stateParams, InsuranceProduct) {
                    return InsuranceProduct.get({id : $stateParams.id}).$promise;
                }], true))
        .state(generateProductManagementTabState('insurance-product-detail.general', 'insurance-product-detail',
        		'/general', {isTab1 : true,isTab2 : false}))
        .state(generateProductManagementTabState('insurance-product-detail.premiumrate', 'insurance-product-detail',
        		'/premiumrate', {isTab1 : false,isTab2 : true})) 
        .state(generateProductManagementState('insurance-product.edit', 'insurance-product',
        		'/{id}/edit', ['$stateParams','InsuranceProduct', function($stateParams, InsuranceProduct) {
                    return InsuranceProduct.get({id : $stateParams.id}).$promise;
                }], false))
        .state(generateProductManagementTabState('insurance-product.edit.general', 'insurance-product.edit',
        		'/general', {isTab1 : true,isTab2 : false}))
        .state(generateProductManagementTabState('insurance-product.edit.premiumrate', 'insurance-product.edit',
        		'/premiumrate', {isTab1 : false,isTab2 : true}))          
        .state(generateProductManagementState('insurance-product.registration', 'insurance-product',
        		'/registration', function (){
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
				   }, false))
		.state(generateProductManagementTabState('insurance-product.registration.general', 'insurance-product.registration',
        		'/general', {isTab1 : true,isTab2 : false}))
        .state(generateProductManagementTabState('insurance-product.registration.premiumrate', 'insurance-product.registration',
        		'/premiumrate', {isTab1 : false,isTab2 : true}))
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
        		'insuranceProductCategoryUpdate', 'productCategory'))
        .state('insurance-product.delete', {
            parent: 'insurance-product',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', '$location', function($stateParams, $state, $uibModal, $location) {
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
//                    $state.go('insurance-product', null, { reload: 'insurance-product' });
                	$location.url('/insurance-product');
                }, function() {
                    $state.go('^');
                });
            }]
        });
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
    
    /**
     * generate Product Management State
     */
    function generateProductManagementState(name, parent, url, entity, isReadOnly){
    	var obj = {
    			name: name,
    			parent: parent,
                url: url,
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        template: '<tabs data="tabData" type="tabs"></tabs><div id="view-general"><div ui-view="view-general"></div></div><div id="view-premium-rate"><div ui-view="view-premium-rate"></div></div>',
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
                    entity: entity,
                    isReadOnly: function(){
                    	return isReadOnly; 
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
        	};

        	return obj;
    }
    
    /**
     * generate Product Management Tab State
     */
    function generateProductManagementTabState(name, parent, url, activeTabs){
    	var obj = {
			name: name,
			parent: parent,
            url: url,
            params:{
            	activeTabs : activeTabs
            },
            views: {
                "view-general": {
                    templateUrl: 'app/entities/insurance-product/insurance-product-registration.html',
                },
                "view-premium-rate": {
                    templateUrl: 'app/entities/insurance-product/premium-rate/premium-rates.html',
                }
            },
            onEnter: ['$stateParams', function($stateParams) {
	        	if($stateParams.activeTabs.isTab1){
	        		angular.element( document.querySelector( '#view-general' ) ).removeClass('hidden');
	        		angular.element( document.querySelector( '#view-premium-rate' ) ).addClass('hidden');
	        	}else{
	        		angular.element( document.querySelector( '#view-general' ) ).addClass('hidden');
	        		angular.element( document.querySelector( '#view-premium-rate' ) ).removeClass('hidden');
	        	}
	        }]
    	};

    	return obj;
    }

})();
