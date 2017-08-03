(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('AgentProfileDialogController', AgentProfileDialogController);

    AgentProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$window', '$q', 'entity', 'AgentProfile', 'User', 'InsuranceCompany', 'InsuranceAgency', 'AgentProfileSendEmail', '$state', '$rootScope'];

    function AgentProfileDialogController ($timeout, $scope, $stateParams, $window, $q, entity, AgentProfile, User, InsuranceCompany, InsuranceAgency, AgentProfileSendEmail, $state, $rootScope) {
        var vm = this;

        vm.agentProfile = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.sendEmail = sendEmail;
        vm.uploadFile = uploadFile;
        vm.validateFileType = validateFileType;
        vm.users = User.query();
        vm.insuranceCompanyState = $state.current.name + '.dialog-find-company';
        vm.insuranceAgencyState = $state.current.name + '.dialog-find-agency';

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            //$uibModalInstance.dismiss('cancel');
       	 	$state.go('agent-profile', {}, { reload: true});
        }

        function save () {
            vm.isSaving = true;
            if (vm.agentProfile.id !== null) {
                AgentProfile.update(vm.agentProfile, onSaveSuccess, onSaveError);
            } else {
                AgentProfile.save(vm.agentProfile, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('whatscoverApp:agentProfileUpdate', result);
            //$uibModalInstance.close(result);
            vm.isSaving = false;
            $state.go('agent-profile', {}, { reload: true});
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function onSendEmailError () {
            vm.isSendEmail = false;
        }

        vm.datePickerOpenStatus.dob = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        
        var agencyUnsubscribe = $rootScope.$on('whatscoverApp:agentProfileAgencyUpdate', function(event, result) {
        	vm.agentProfile.insuranceAgencyId = result.id;
            vm.agentProfile.insuranceAgencyName = result.name;
        });
        $scope.$on('$destroy', agencyUnsubscribe);
        
        var companyUnsubscribe = $rootScope.$on('whatscoverApp:agentProfileCompanyUpdate', function(event, result) {
        	vm.agentProfile.insuranceCompanyId = result.id;
            vm.agentProfile.insuranceCompanyName = result.name;
        });
        $scope.$on('$destroy', companyUnsubscribe);

        function onSendEmailSuccess () {
            vm.isSendEmail = true;
        }

        function sendEmail(){
        	vm.isSendEmail = true;
			if (vm.agentProfile.id !== null) {
				alert("We've sent notification to your email");
			}else{
				AgentProfileSendEmail.email(vm.agentProfile, onSendEmailSuccess, onSendEmailError);
			}
        }

        function validateFileType(){
            var fileName = document.getElementById("field_photo_dir").value;
            var idxDot = fileName.lastIndexOf(".") + 1;
            var extFile = fileName.substr(idxDot, fileName.length).toLowerCase();
            if (extFile=="jpg" || extFile=="jpeg" || extFile=="png"){
                //TO DO
            }else{
                alert("Only jpg/jpeg and png files are allowed!");
            }   
        }
        
        function uploadFile(){
            var fileinput = document.getElementById("browse");
            fileinput.click();
            //document.getElementById("field_photo_dir").value = fileinput.value;
            //vm.agentProfile.photo_dir = document.getElementById("field_photo_dir").value;
        }
        
        $scope.fileNameChanged = function() {
        	console.log("select file");
            document.getElementById("tmp_photo_dir").value =  document.getElementById("browse").value;
            var tempValue = $("#tmp_photo_dir").val().trim();
            var tempLength = tempValue.length;

            if(!tempLength < 1)
            {
            	var dIndex = tempValue.lastIndexOf(".");
                var fileName = tempValue.substring(dIndex, tempLength);
                if( fileName == '.jpg'){
                	var sIndex = tempValue.lastIndexOf('\\');
            		tempValue = tempValue.substr(sIndex + 1, tempLength);
                    document.getElementById("field_photo_dir").value = tempValue;
                    vm.agentProfile.photo_dir = tempValue;
                }else{
                	alert('Please upload correct File Name, File extension should be .jpg');
                }
            }
        }
    }
})();
