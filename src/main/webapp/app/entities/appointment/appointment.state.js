(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('appointment', {
            parent: 'entity',
            url: '/appointment?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whatscoverApp.appointment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/appointment/appointments.html',
                    controller: 'AppointmentController',
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
                    $translatePartialLoader.addPart('appointment');
                    $translatePartialLoader.addPart('appointmentStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('appointment-detail', {
            parent: 'appointment',
            url: '/appointment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whatscoverApp.appointment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/appointment/appointment-detail.html',
                    controller: 'AppointmentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('appointment');
                    $translatePartialLoader.addPart('appointmentStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Appointment', function($stateParams, Appointment) {
                    return Appointment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'appointment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('appointment-detail.edit', {
            parent: 'appointment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appointment/appointment-dialog.html',
                    controller: 'AppointmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Appointment', function(Appointment) {
                            return Appointment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('appointment.new', {
            parent: 'appointment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appointment/appointment-dialog.html',
                    controller: 'AppointmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                phone: null,
                                locationGeoLong: null,
                                locationGeoLat: null,
                                locationAddress: null,
                                datetime: null,
                                assignedDatetime: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('appointment', null, { reload: 'appointment' });
                }, function() {
                    $state.go('appointment');
                });
            }]
        })
        .state('appointment.edit', {
            parent: 'appointment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appointment/appointment-dialog.html',
                    controller: 'AppointmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Appointment', function(Appointment) {
                            return Appointment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('appointment', null, { reload: 'appointment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('appointment.delete', {
            parent: 'appointment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appointment/appointment-delete-dialog.html',
                    controller: 'AppointmentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Appointment', function(Appointment) {
                            return Appointment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('appointment', null, { reload: 'appointment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
