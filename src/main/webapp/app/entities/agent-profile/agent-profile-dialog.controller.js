(function() {
    'use strict';

    angular
        .module('whatscoverApp')
        .controller('AgentProfileDialogController', AgentProfileDialogController);

    AgentProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$window', '$q', 'entity', 'AgentProfile', 'User', 'InsuranceCompany', 'InsuranceAgency', 'AgentProfileSendEmail', 'UploadService', '$state', '$rootScope', '$http'];

    function AgentProfileDialogController ($timeout, $scope, $stateParams, $window, $q, entity, AgentProfile, User, InsuranceCompany, InsuranceAgency, AgentProfileSendEmail, UploadService, $state, $rootScope, $http) {
        var vm = this;

        vm.agentProfile = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.sendEmail = sendEmail;
        vm.users = User.query();
        vm.insuranceCompanyState = $state.current.name + '.dialog-find-company';
        vm.insuranceAgencyState = $state.current.name + '.dialog-find-agency';
        vm.fileData = null;
        vm.newFileName = '';
        vm.currentFile = vm.agentProfile.photo_dir;
        vm.tempFileName = vm.agentProfile.photo_dir;
        vm.uploadFile = uploadFile;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            //$uibModalInstance.dismiss('cancel');
       	 	$state.go('agent-profile', {}, { reload: true});
        }
        
        $scope.init = function () {
        	var filePath = vm.agentProfile.photo_dir;
            
        	if (filePath) {
    			$http.get('api/load-file', {params: {"filePath": filePath}}).then(
					function(data, status, headers) {
						var image = $('#previewImg');
						image.css({
							'background-image': 'url(\'data:image/jpeg;base64,' + data.data + '\')',
							'background-size' : '100px 100px'
						});

						document.getElementById("fileName").innerHTML = nameFromFilePath(data.config.params.filePath);
						document.getElementById("fileSize").innerHTML = getFileSize(data.data.length);
	    			}).catch(function(error) {
	    				console.log(error);
	    			});            
        	}
        }

        function save () {
            vm.isSaving = true;
            if (vm.agentProfile.id !== null) {
            	var filePath = '';
            	if ((!vm.currentFile && vm.newFileName) || 
            			(nameFromFilePath(vm.currentFile) != vm.newFileName && vm.newFileName)) {
	            	UploadService.upload(vm.fileData).then(function (data) {
	            		filePath = data;
	                	AgentProfile.update(vm.agentProfile, onSaveSuccess, onSaveError);
	                });
            	} else {
            		AgentProfile.update(vm.agentProfile, onSaveSuccess, onSaveError);
            	}
            	
            } else {
                AgentProfile.save(vm.agentProfile, onSaveSuccess, onSaveError);
            }
        }
        
        function nameFromFilePath(filePath) {
        	var result = filePath;
        
        	if (filePath) {
	        	var idxSlash = filePath.lastIndexOf('\\') + 1,
	        	fileLength = filePath.length;	        	
	            result = filePath.substr(idxSlash, fileLength);
        	}
        		
    		return result;
        }
        
        function uploadFile(){
            var fileinput = document.getElementById("fileInput");
            fileinput.click();
        }
        
        $scope.fileNameChanged = function(element){
        	var fileSize = 0,
        	file = document.getElementById("fileInput").files[0];
        	//set file size
        	if (file) {
        		fileSize = getFileSize(file.size);
        	}
        	
        	document.getElementById("fileName").innerHTML = file.name;
		    document.getElementById("fileSize").innerHTML = fileSize;
		    
		    // Upload file
		    var formData = new FormData();
		    formData.append('files', file);
		    vm.fileData = formData;
		    vm.newFileName = file.name;
		    
		    // Preview file
		    previewFile(file);
        }
        
        function previewFile(file) {            

            var preview = document.getElementById('previewImg');
            var reader  = new FileReader();
            
        	if (file) {	            
	            reader.readAsDataURL(file);
            }
           
            reader.onload = function (event) {
            	preview.src = event.target.result;
            };
        }

        function getFileSize(size) {
        	var fileSize = 0;
        	
        	if(size > 0 && size != undefined) {
        		fileSize = size;
        	}
        	
		    var fileSizeStr = fileSize + " bytes";
		    // optional code for multiples approximation
		    for (var aMultiples = ["KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB"], nMultiple = 0, nApprox = fileSize / 1024; nApprox > 1; nApprox /= 1024, nMultiple++) {
		    	fileSizeStr = nApprox.toFixed(3) + " " + aMultiples[nMultiple];
		    }
		    
		    return fileSizeStr;
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
    }
})();
