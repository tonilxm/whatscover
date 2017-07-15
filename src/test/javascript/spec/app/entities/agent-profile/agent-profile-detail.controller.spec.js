'use strict';

describe('Controller Tests', function() {

    describe('AgentProfile Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAgentProfile, MockUser, MockInsuranceCompany;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAgentProfile = jasmine.createSpy('MockAgentProfile');
            MockUser = jasmine.createSpy('MockUser');
            MockInsuranceCompany = jasmine.createSpy('MockInsuranceCompany');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AgentProfile': MockAgentProfile,
                'User': MockUser,
                'InsuranceCompany': MockInsuranceCompany
            };
            createController = function() {
                $injector.get('$controller')("AgentProfileDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'whatscoverApp:agentProfileUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
