'use strict';

describe('Controller Tests', function() {

    describe('InsuranceAgency Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockInsuranceAgency, MockInsuranceCompany;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockInsuranceAgency = jasmine.createSpy('MockInsuranceAgency');
            MockInsuranceCompany = jasmine.createSpy('MockInsuranceCompany');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'InsuranceAgency': MockInsuranceAgency,
                'InsuranceCompany': MockInsuranceCompany
            };
            createController = function() {
                $injector.get('$controller')("InsuranceAgencyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'whatscoverApp:insuranceAgencyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
