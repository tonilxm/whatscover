(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('InsuranceProductRegistrationController', InsuranceProductRegistrationController);

    InsuranceProductRegistrationController.$inject = ['$timeout', '$scope', '$stateParams', 'entity', 'InsuranceProduct', 'InsuranceCompany', '$state', '$rootScope', 'InsuranceProductPremiumRate', 'InsuranceProductPremiumRateSearch', 'AlertService', 'paginationConstants', 'pagingParams', 'ParseLinks'];

    function InsuranceProductRegistrationController ($timeout, $scope, $stateParams, entity, InsuranceProduct, InsuranceCompany, $state, $rootScope, InsuranceProductPremiumRate, InsuranceProductPremiumRateSearch, AlertService, paginationConstants, pagingParams, ParseLinks) {
        var vm = this;
       
        vm.insuranceProduct = entity;
        vm.clear = clear;
        vm.save = save;
        //vm.childState = $state.current.name + '.dialog-find-company';
        vm.childState = 'insurance-product.registration.dialog-find-company';

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
        vm.savePremiumRate = savePremiumRate;
        
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
        			//console.log(data.id + " --- " +data.index);
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
        		//vm.insuranceProductPremiumRates.splice(index, 1);
        		//vm.insuranceProductPremiumRates[vm.insuranceProductPremiumRates.findIndex(el => el.id === id)].status = "DELETE";
        		vm.insuranceProductPremiumRates = $.grep(vm.insuranceProductPremiumRates, function(element, index){return element.status !== "DELETE" || element.id !== null});
        		console.log(vm.insuranceProductPremiumRates);
        		
        		//vm.isSaving = true;
                if (vm.insuranceProduct.id !== null) {
                    //InsuranceProduct.update(vm.insuranceProduct, onSaveSuccess, onSaveError);
                } else {
                    //InsuranceProduct.save(vm.insuranceProduct, onSaveSuccess, onSaveError);
                }
        	}
        	
        	
        	function triggerEForm(id, index, name){
        		vm.isValid = false;
        		$state.go('insurance-product.registration.premiumrate');
        		
        		$timeout(function() {
				    angular.element('#button'+ name + '-' + id + index).triggerHandler('click');
    				//console.log(angular.element('form button[type="submit"]'));
				}, 0);
				
				//console.log('#button-' + data.id + data.index);
        	}
        }

        function onSaveSuccess (result) {
            $scope.$emit('whatscoverApp:insuranceProductUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        
        var unsubscribe = $rootScope.$on('whatscoverApp:insuranceProductCompanyUpdate', function(event, result) {
        	vm.insuranceProduct.insuranceCompanyId = result.id;
            vm.insuranceProduct.insuranceCompanyName = result.name;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.initialise = function() {

            $scope.go = function(state) {
              $state.go(state);
            };

            $scope.tabData   = [
              {
                heading: '<i>General</i>',
                route:   'insurance-product.registration.general'
              },
              {
                heading: '<i>Premium Rate</i>',
                route:   'insurance-product.registration.premiumrate'
              }
            ];
          };

          $scope.initialise();
          
          loadInsuranceProductPremiumRate();

          function loadInsuranceProductPremiumRate () {
          	if (pagingParams.search) {
          		InsuranceProductPremiumRateSearch.query({
                      query: pagingParams.search,
                      page: pagingParams.page - 1,
                      size: vm.itemsPerPage,
                      sort: sort()
                  }, onSuccess, onError);
              } else {
            	  InsuranceProductPremiumRate.query({
                      //page: pagingParams.page - 1,
                      //size: vm.itemsPerPage,
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
        	 // console.log(vm.insuranceProductPremiumRates);
          }
          
          function removeRow(id, index){
        	  if(id != null){
        		  //vm.insuranceProductPremiumRates[index].status = "DELETE";
        		  vm.insuranceProductPremiumRates[vm.insuranceProductPremiumRates.findIndex(el => el.id === id)].status = "DELETE";
        	  }else{
        		  console.log(index);
        		  //vm.insuranceProductPremiumRates.splice(index, 1);
        		  vm.insuranceProductPremiumRates[index].status = "DELETE";
        	  }
        	  
        	 // vm.removedEntity.push(vm.insuranceProductPremiumRates[index]);
        	 // vm.insuranceProductPremiumRates.slice(index, 0);
        	//  console.log(vm.insuranceProductPremiumRates);
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
          
          function savePremiumRate(){
        	  console.log('tes');
          }
    }
})();
