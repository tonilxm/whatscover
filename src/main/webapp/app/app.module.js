(function() {
    'use strict';

    angular
        .module('whatscoverApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'ui.router.tabs',
            'infinite-scroll',
            'xeditable',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar'
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler', 'editableOptions'];

    function run(stateHandler, translationHandler, editableOptions) {
        stateHandler.initialize();
        translationHandler.initialize();
        editableOptions.theme = 'bs3';
    }
})();
