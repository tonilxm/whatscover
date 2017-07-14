'use strict';

describe('Controller Tests', function() {

    describe('InsuranceProduct Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockInsuranceProduct, MockInsuranceCompany;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockInsuranceProduct = jasmine.createSpy('MockInsuranceProduct');
            MockInsuranceCompany = jasmine.createSpy('MockInsuranceCompany');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'InsuranceProduct': MockInsuranceProduct,
                'InsuranceCompany': MockInsuranceCompany
            };
            createController = function() {
                $injector.get('$controller')("InsuranceProductDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'whatscoverApp:insuranceProductUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
