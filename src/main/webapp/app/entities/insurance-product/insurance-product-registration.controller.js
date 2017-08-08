(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceProductRegistrationController', InsuranceProductRegistrationController);

    InsuranceProductRegistrationController.$inject = ['$timeout', '$scope', '$stateParams', 'entity', 'InsuranceProduct', 'InsuranceCompany', '$state', '$rootScope', 'InsuranceProductPremiumRate', 'InsuranceProductPremiumRateSearch', 'AlertService', 'paginationConstants', 'pagingParams', 'ParseLinks', 'ProductCategory', 'isReadOnly'];

    function InsuranceProductRegistrationController ($timeout, $scope, $stateParams, entity, InsuranceProduct, InsuranceCompany, $state, $rootScope, InsuranceProductPremiumRate, InsuranceProductPremiumRateSearch, AlertService, paginationConstants, pagingParams, ParseLinks, ProductCategory, isReadOnly) {
        var vm = this;
       
        vm.insuranceProduct = entity;
        //console.log(entity);
        vm.insuranceProductPremiumRates = [];
        vm.clear = clear;
        vm.save = save;
        vm.childStateCompany = $state.current.parent + '.dialog-find-company';
        vm.childStateProductCategory = $state.current.parent + '.dialog-find-product-category';

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.search = search;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;
        
        vm.addRow = addRow;
        vm.removeRow = removeRow;
        vm.removedEntity = [];
        vm.onUpdateField = onUpdateField;
        vm.checkEmptyData = checkEmptyData;
        
        vm.cancel = cancel;
        vm.isReadOnly = isReadOnly;
        vm.isShowBtns = !isReadOnly;
        console.log("readonly : "+vm.isReadOnly+ " -- btns : "+vm.isShowBtns);
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
         	vm.isValid = true;
        	vm.insuranceProductPremiumRates.every(function(data){
        		if(data.status === 'NEW'){
        			if(data.entryAge === null){
        				triggerEForm(data.id, data.index, "EntryAgeForm");
        				return false;
        			}else if(data.malePremiumRate === null){
        				triggerEForm(data.id, data.index, "MaleRateForm");
        				return false;
        			}else if(data.femalePremiumRate === null){
        				triggerEForm(data.id, data.index, "FemaleRateForm");
        				return false;
        			}
        		}
        		return true;
        	});
        	
        	if(vm.isValid){
        		vm.insuranceProductPremiumRates = $.grep(vm.insuranceProductPremiumRates, function(element, index){return (element.status !== "DELETE" || element.id !== null) && element.status !== "DEFAULT"});
        		console.log(vm.insuranceProductPremiumRates);
        		
        		vm.isSaving = true;
        		vm.insuranceProduct["premiumRates"] = vm.insuranceProductPremiumRates;
        		if (vm.insuranceProduct.id !== null) {
                    InsuranceProduct.updateInsuranceProduct(vm.insuranceProduct, onSaveSuccess, onSaveError);
                } else {
                    InsuranceProduct.saveInsuranceProduct(vm.insuranceProduct, onSaveSuccess, onSaveError);
                }
        	}
        	
        	
        	function triggerEForm(id, index, name){
        		vm.isValid = false;
        		$state.go($state.current.parent + '.premiumrate');
        		
        		$timeout(function() {
				    angular.element('#button'+ name + '-' + id + index).triggerHandler('click');
				}, 0);
				
        	}
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            vm.cancel();
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        
        var unsubscribeCompany = $rootScope.$on('whatscoverApp:insuranceProductCompanyUpdate', function(event, result) {
        	vm.insuranceProduct.insuranceCompanyId = result.id;
            vm.insuranceProduct.insuranceCompanyName = result.name;
        });
        $scope.$on('$destroy', unsubscribeCompany);
        
        var unsubscribeProductCategory = $rootScope.$on('whatscoverApp:insuranceProductCategoryUpdate', function(event, result) {
        	vm.insuranceProduct.productCategoryId = result.id;
            vm.insuranceProduct.productCategoryName = result.name;
        });
        $scope.$on('$destroy', unsubscribeProductCategory);

        $scope.initialise = function() {

            $scope.go = function(state) {
              $state.go(state);
            };
            console.log($state.current.parent);
            $scope.tabData   = [
              {
                heading: '<i>General</i>',
                route:   $state.current.parent + '.general'
              },
              {
                heading: '<i>Premium Rate</i>',
                route:   $state.current.parent + '.premiumrate'
              }
            ];
          };

          $scope.initialise();
          
          loadInsuranceProductPremiumRate();

          function loadInsuranceProductPremiumRate () {
        	  if(vm.insuranceProduct.id != null){
        		  InsuranceProductPremiumRateSearch.searchByProductId({
            		  query: vm.insuranceProduct.id,
                      sort: sort()
                  }, onSuccess, onError);
        	  }
        	  
              function sort() {
                  var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                  if (vm.predicate !== 'id') {
                      result.push('id');
                  }
                  return result;
              }
              function onSuccess(data, headers) {
                  vm.links = ParseLinks.parse(headers('link'));
                  vm.totalItems = headers('X-Total-Count');
                  vm.queryCount = vm.totalItems;
                  vm.insuranceProductPremiumRates = data;
                  vm.page = pagingParams.page;
              }
              function onError(error) {
                  AlertService.error(error.data.message);
              }
          }

          function loadPage(page) {
              vm.page = page;
              vm.transition();
          }
          
          function transition() {
          	pagingParams.page = vm.page;
          	pagingParams.sort = vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc');
          	pagingParams.search = vm.currentSearch;
          	loadInsuranceProductPremiumRate();
          }

          function search(searchQuery) {
	          if (!searchQuery){
	              return vm.clear();
	          }
	          vm.links = null;
	          vm.page = 1;
	          vm.predicate = '_score';
	          vm.reverse = false;
	          vm.currentSearch = searchQuery;
	          vm.transition();
          }

          function clear() {
              vm.links = null;
              vm.page = 1;
              vm.predicate = 'id';
              vm.reverse = true;
              vm.currentSearch = null;
              vm.transition();
          }
          
          function addRow(){
        	  var newPremiumRate = {
    			  "id" : null,
    			  "entryAge" : null,
    			  "malePremiumRate" : null,
    			  "femalePremiumRate" : null,
    			  "plan" : null,
    			  "insuranceProductId" : null,
    			  "status" : "NEW",
    			  "createdBy" : null,
    			  "createdDate" : null,
    			  "lastModifiedBy" : null,
    			  "lastModifiedDate" : null,	
    			  "index" : vm.insuranceProductPremiumRates.length
        	  }
        	  vm.insuranceProductPremiumRates.push(newPremiumRate);
          }
          
          function removeRow(id, index){
        	  if(id != null){
        		  vm.insuranceProductPremiumRates[vm.insuranceProductPremiumRates.findIndex(el => el.id === id)].status = "DELETE";
        	  }else{
        		  //console.log(index);
        		  vm.insuranceProductPremiumRates[index].status = "DELETE";
        	  }
        	  
          }
          
          function onUpdateField(id){
        	  if(id != null){
        		  vm.insuranceProductPremiumRates[vm.insuranceProductPremiumRates.findIndex(el => el.id === id)].status = "UPDATE";
        	  }
        	  
        	  console.log(vm.insuranceProductPremiumRates);
          };
          
          function checkEmptyData(data){
        	  if (data === '' && data === null) {
        	      return "Please fill the field";
        	  }
          };
          
          function cancel(){
        	  $state.go('insurance-product');
          }
    }
})();
