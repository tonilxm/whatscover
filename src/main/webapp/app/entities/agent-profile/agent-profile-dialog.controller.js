(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('AgentProfileDialogController', AgentProfileDialogController);

    AgentProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$window', '$q', 'entity', 'AgentProfile', 'User', 'InsuranceCompany', 'InsuranceAgency', 'AgentProfileSendEmail', 'UploadService', '$state', '$rootScope', '$sce'];

    function AgentProfileDialogController ($timeout, $scope, $stateParams, $window, $q, entity, AgentProfile, User, InsuranceCompany, InsuranceAgency, AgentProfileSendEmail, UploadService, $state, $rootScope, $sce) {
        var vm = this;

        vm.agentProfile = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.sendEmail = sendEmail;
//        vm.uploadFile = uploadFile;
//        vm.validateFileType = validateFileType;
        vm.users = User.query();
        vm.insuranceCompanyState = $state.current.name + '.dialog-find-company';
        vm.insuranceAgencyState = $state.current.name + '.dialog-find-agency';
        vm.fileData = null;
        vm.currentFile = vm.agentProfile.photo_dir;
        vm.newFileName = '';

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
            	var filePath = '';            	
            	if ($scope.nameFromFilePath(vm.currentFile) != vm.newFileName) {
	            	UploadService.upload(vm.fileData).then(function (data) {
	            		filePath = data;
	                    // set file path here
	                    vm.agentProfile.photo_dir = filePath;
	                    AgentProfile.update(vm.agentProfile, onSaveSuccess, onSaveError);
	                });
            	}
            	
            	if (!filePath) {
                    AgentProfile.update(vm.agentProfile, onSaveSuccess, onSaveError);
            	}
            } else {
                AgentProfile.save(vm.agentProfile, onSaveSuccess, onSaveError);
            }
        }
        
        $scope.trustSrc = function(src) {
	    	console.log('test ==>' + src);
	        return $sce.trustAsResourceUrl(src);
        }
        
        $scope.nameFromFilePath = function(filePath) {
        	var idxSlash = filePath.lastIndexOf('\\') + 1,
        	fileLength = filePath.length;
        	
            return vm.currentFile.substr(idxSlash, fileLength);
        }

        $scope.fileNameChanged = function(element){
        	var fileSize = 0,
        	file = document.getElementById("fileInput").files[0];
        	
        	if (file) {
        		fileSize = file.size;
        	}
		
		    var sOutput = fileSize + " bytes";
		    // optional code for multiples approximation
		    for (var aMultiples = ["KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB"], nMultiple = 0, nApprox = fileSize / 1024; nApprox > 1; nApprox /= 1024, nMultiple++) {
		        sOutput = nApprox.toFixed(3) + " " + aMultiples[nMultiple] + " (" + fileSize + " bytes)";
		    }
		    // end of optional code
		
		    document.getElementById("fileSize").innerHTML = sOutput;
		    
		    // Upload file
		    var formData = new FormData();
		    formData.append('files', file);
		    vm.fileData = formData;
		    vm.newFileName = file.name;
		    
		    // preview file
		    previewFile();
        }
        
        function previewFile() {
            var preview = document.getElementById('previewImg'),
            file = document.querySelector('input[type=file]').files[0],
            reader  = new FileReader();

            if (file) {
              reader.readAsDataURL(file);
            }
            
            reader.onload = function (event) {
            	preview.src = event.target.result;
            };
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

        /*function validateFileType(){
            var fileName = document.getElementById("field_photo_dir").value;
            var idxDot = fileName.lastIndexOf(".") + 1;
            var extFile = fileName.substr(idxDot, fileName.length).toLowerCase();
            if (extFile=="jpg" || extFile=="jpeg" || extFile=="png"){
                //TO DO
            }else{
                alert("Only jpg/jpeg and png files are allowed!");
            }   
        }*/
        
        /*function uploadFile(){
            var fileinput = document.getElementById("browse");
            fileinput.click();
        }*/

        /*$scope.fileNameChanged = function(element){
            document.getElementById("tmp_photo_dir").value =  document.getElementById("browse").value;
       	     var uploadForm = new FormData();

       	     $scope.$apply(function(scope) {
                 var photofile = element.files[0];
                 var reader = new FileReader();
                 reader.onload = function(e) {
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
                             Upload.upload({uploadFile: event.target.result}, true, false);
                         }else{
                         	alert('Please upload correct File Name, File extension should be .jpg');
                         }
                     }
                 };
                 reader.readAsDataURL(photofile);
            });
       }*/
    }
})();
