'use strict';

describe('Controller Tests', function() {

    describe('InsuranceProductPremiumRate Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockInsuranceProductPremiumRate, MockInsuranceProduct;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockInsuranceProductPremiumRate = jasmine.createSpy('MockInsuranceProductPremiumRate');
            MockInsuranceProduct = jasmine.createSpy('MockInsuranceProduct');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'InsuranceProductPremiumRate': MockInsuranceProductPremiumRate,
                'InsuranceProduct': MockInsuranceProduct
            };
            createController = function() {
                $injector.get('$controller')("InsuranceProductPremiumRateDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'whatscoverApp:insuranceProductPremiumRateUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
