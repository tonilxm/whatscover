'use strict';

describe('Controller Tests', function() {

    describe('Appointment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAppointment, MockCustomerProfile, MockAgentProfile;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAppointment = jasmine.createSpy('MockAppointment');
            MockCustomerProfile = jasmine.createSpy('MockCustomerProfile');
            MockAgentProfile = jasmine.createSpy('MockAgentProfile');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Appointment': MockAppointment,
                'CustomerProfile': MockCustomerProfile,
                'AgentProfile': MockAgentProfile
            };
            createController = function() {
                $injector.get('$controller')("AppointmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'whatscoverApp:appointmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
