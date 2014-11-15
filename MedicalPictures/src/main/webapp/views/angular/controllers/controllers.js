'use strict';
        /* Contains global settings accessible for all controllers */
        var MedicalPictures = angular.module('MedicalPictures', ['pascalprecht.translate']).constant('MedicalPicturesGlobal', {
            GLOBAL_APP_NAME: "MedicalPictures ver.1.0",
            MIN_PASSWORD_LENGTH: "3",
            MAX_PASSWORD_LENGTH: "30",
            MAX_NAME_SURNAME_LENGTH: "255",
            MAX_USERNAME_LENGTH:"100",
            MIN_AGE:"1",
            MAX_AGE:"150",
            NAME_REGEXP_PATTERN:/[a-zA-Z]/,
            ADMIN_VIEW_MAIN_WINDOW:'/MedicalPictures/AdminViewManageUsers',
            DOCTOR_VIEW_MAIN_WINDOW:'/MedicalPictures/DoctorView',
            TECHNICIAN_VIEW_MAIN_WINDOW:'/MedicalPictures/TechnicianView',
            PATIENT_VIEW_MAIN_WINDOW:'/MedicalPictures/PatientView',
            ACCOUNT_TYPES:['ADMIN','DOCTOR','PATIENT','TECHNICIAN'],
        });

        var userMainWindow = "";
        MedicalPictures.factory('MedicalPicturesCommon', function () {
            return { username:'' };
        });
        /* LoginView Controller */
        MedicalPictures.controller('LoginController', function ($scope, $http, $window,$translate, $location, MedicalPicturesGlobal) {
                $scope.appName =  MedicalPicturesGlobal.GLOBAL_APP_NAME;
                $scope.minPasswordLength = MedicalPicturesGlobal.MIN_PASSWORD_LENGTH;
                $scope.maxPasswordLength = MedicalPicturesGlobal.MAX_PASSWORD_LENGTH;
                $scope.password = "";
                $scope.username="";//we share this username globally to later can print in in other windows
                $scope.userAlreadyLogged= ""; //it contain the name of user which couldn't log because of already being logged
                $scope.userMainWindow="";// url to the main window for this user, if he will go back to the Login VIew being already logged
                document.getElementById("alertMessageDiv").style.visibility="hidden";
                /* This above is a small hack, because loading the html content like below - alert didnt react on clicking the close sign (x).
                The solution was 2:
                - have 1 alert already in HTML code ( not dynamically loaded)
                - have 1 alert already in HTML and hide it. I made it  */
                $scope.loginClicked = function () {
                    if(!angular.isUndefined($scope.username)&& !angular.isUndefined($scope.password)){
                      $http({
                              url: '/MedicalPictures/LoginView',
                              method: 'POST',
                              headers: {'Content-Type': 'application/json'},
                              data: {'username': $scope.username, 'password': CryptoJS.SHA512($scope.password).toString()}
                      }).
                        success(function (data, status, header, config) {
                            console.log(status);
                            if (data.username === $scope.username && data.status === "true"){//if login successful
                                switch(data.accountType){
                                  case "ADMIN":
                                      $window.location.href = "AdminViewManageUsers";
                                      break;
                                  case "PATIENT":
                                      $window.location.href = "PatientView";
                                      break;
                                  case "TECHNICIAN":
                                      $window.location.href = "TechnicianView";
                                      break;
                                  case "DOCTOR":
                                      $window.location.href = "DoctorView";
                                      break;
                                }
                            }
                            else{//else print error message
                                        switch(data.reason){
                                            case "alreadyLoggedOutside":
                                                $translate('USER_ALREADY_LOGGED_OUTSIDE').then(function (translation) {
                                                    showAlertMessageError(translation ,data.username);
                                                });
                                                break;
                                            case "authenticationFailed":
                                                $translate('AUTHENTICATION_FAILED').then(function (translation) {
                                                    showAlertMessageError(translation,data.username);
                                                });
                                                break;
                                            case "alreadyLoggedLocally":
                                                $translate('USER_ALREADY_LOGGED_LOCALLY').then(function (translation) {
                                                    var mainWindow;
                                                    switch(data.accountType){
                                                        case "DOCTOR":
                                                            mainWindow = MedicalPicturesGlobal.DOCTOR_VIEW_MAIN_WINDOW;
                                                            break;
                                                        case "ADMIN":
                                                            mainWindow = MedicalPicturesGlobal.ADMIN_VIEW_MAIN_WINDOW;
                                                            break;
                                                        case "PATIENT":
                                                            mainWindow = MedicalPicturesGlobal.PATIENT_VIEW_MAIN_WINDOW;
                                                            break;
                                                        case "TECHNICIAN":
                                                            mainWindow = MedicalPicturesGlobal.TECHNICIAN_VIEW_MAIN_WINDOW;
                                                            break;
                                                    }
                                                    showAlertMessageWarning(translation,data.username,mainWindow);
                                                });
                                                break;
                                         }
                           }
                        }).error(function (response) {
                            console.log(response);
                        });
                  };
              }
        });
        function showAlertMessageError(message,username){
            var alert = document.getElementById("alertMessageDiv");
            alert.style.visibility="visible";//We show error message
            alert.innerHTML = "<div data-alert class=\"alert-box alert round\">"+
                username+" : " + message +
                "<a class=\"close\">&times;</a>"+
                "</div>";
        }
        function showAlertMessageWarning(message,username, url){
            var alert = document.getElementById("alertMessageDiv");
            alert.style.visibility="visible";//We show error message
            alert.innerHTML = "<div data-alert class=\"alert-box warning round\">"+
                "<a href=\""+url+"\" >" + username+" : " + message +"</a>" +
                "<a class=\"close\">&times;</a>"+
                "</div>";
        }
        function showAlertMessageSuccess(message,username){
            var alert = document.getElementById("alertMessageDiv");
            alert.style.visibility="visible";//We show error message
            alert.innerHTML = "<div data-alert class=\"alert-box success radius\">"+
                username+" : " + message +
                "<a class=\"close\">&times;</a>"+
                "</div>";
        }



        /* AdminView Controller */
        MedicalPictures.controller('AdminViewManageUsersController', function ($scope,$http,$translate, MedicalPicturesGlobal) {
            $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
            $scope.accountTypes = MedicalPicturesGlobal.ACCOUNT_TYPES;
            $scope.minPasswordLength = MedicalPicturesGlobal.MIN_PASSWORD_LENGTH;
            $scope.maxPasswordLength = MedicalPicturesGlobal.MAX_PASSWORD_LENGTH;
            $scope.minAge = MedicalPicturesGlobal.MIN_AGE;
            $scope.maxAge = MedicalPicturesGlobal.MAX_AGE;
            $scope.nameRegexpPattern = MedicalPicturesGlobal.NAME_REGEXP_PATTERN;
            $scope.maxNameSurnameLength= MedicalPicturesGlobal.MAX_NAME_SURNAME_LENGTH;
            $scope.maxUsernameLength = MedicalPicturesGlobal.MAX_USERNAME_LENGTH;
            $scope.editingUsername="";
            $scope.editingName="";
            $scope.editingSurname="";
            $scope.editingAge=0;
            $scope.editingSelectedAccountType="";
            $scope.resetPasswordCheckbox = false;
            document.getElementById("alertMessageDiv").style.visibility="hidden";
              $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getLoggedUser').
              success(function(data, status, headers, config) {
                $scope.loggedUsername = data.username;
              }).
              error(function(data, status, headers, config) {
                  console.log(status);
              });
              $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllUsernames').
              success(function(data, status, headers, config) {
                  $scope.usernamesList = data.usernames;
              }).
              error(function(data, status, headers, config) {
                    console.log(status);
              });
              $scope.editUserClicked = function(usernameToEdit){
                $scope.resetPasswordCheckbox=false;
                  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getUserInfo/'+usernameToEdit).
                    success(function(data, status, headers, config) {
                        $scope.editingUsername=data.username;
                        $scope.editingPassword=data.password;
                        $scope.editingName=data.name;
                        $scope.editingSurname=data.surname;
                        $scope.editingAge=data.age;
                        $scope.editingSelectedAccountType=data.accountType;
                    }).
                    error(function(data, status, headers, config) {
                        console.log(status);
                    });
              };
              $scope.markAllUsersClicked = function(){
                 var i;
                  for(i=0;i<$scope.usernamesList.length;i++){
                      document.getElementById($scope.usernamesList[i].username).checked = true;
                  }
              };
              $scope.unmarkAllUsersClicked = function(){
                 var i;
                  for(i=0;i<$scope.usernamesList.length;i++){
                      document.getElementById($scope.usernamesList[i].username).checked = false;
                  }
              };
              $scope.deleteUsersClicked = function(){
                 var i;
                 var usersToDeleteList = [];
                  for(i=0;i<$scope.usernamesList.length;i++){
                      if(document.getElementById($scope.usernamesList[i].username).checked === true){
                          var index = usersToDeleteList.length;
                          usersToDeleteList[index]="{username:'"+$scope.usernamesList[i].username+"',accountType:'"
                          +$scope.usernamesList[i].accountType+"'}";
                      }
                  }
                  var usersToDelete ;
                  if(usersToDeleteList.length ===0){
                        usersToDelete= "{usernames:[]}";
                  } else{
                        usersToDelete = "{usernames:["+usersToDeleteList+"]}";
                  }
                  $scope.appName = usersToDelete;
                  $http({
                      url: '/MedicalPictures/AdminViewDeleteUsers',
                      method: 'POST',
                      headers: {'Content-Type': 'application/json'},
                      data: usersToDelete
                  }).
                  success(function (data, status, header, config) {
                        $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllUsernames').
                        success(function(data, status, headers, config) {
                            $scope.usernamesList = data.usernames;
                        }).
                        error(function(data, status, headers, config) {
                              console.log(status);
                        });
                  }).error(function(data, status, headers, config) {
                        console.log(status);
                  });
              };
              $scope.saveEditedUserClicked = function(){
                var resetPassword=false;
                document.getElementById("alertMessageDiv").style.visibility="hidden";//hide the message about success
                if(!angular.isUndefined($scope.editingUsername) &&
                !angular.isUndefined($scope.editingAge) && !angular.isUndefined($scope.editingName) &&
                !angular.isUndefined($scope.editingSurname) && !angular.isUndefined($scope.editingSelectedAccountType)){
                    if(document.getElementById($scope.resetPasswordCheckbox).checked === true){
                        resetPassword=true;
                    }
                      var userToEdit ={"username":$scope.editingUsername,"accountType":$scope.editingSelectedAccountType,
                      "name":$scope.editingName,"surname":$scope.editingSurname,"age":$scope.editingAge.toString(),"resetPassword":resetPassword.toString()}
                      $http({
                          url:'/MedicalPictures/AdminViewEditUser',
                          method:'POST',
                          headers:{'Content-Type':'application/json'},
                          data:userToEdit
                      }).success(function (data, status, header, config) {


                          $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllUsernames').
                          success(function(data, status, headers, config) {
                              $scope.usernamesList = data.usernames;
                              $translate('USER_EDITED_SUCCESSFULLY').then(function (translation){
                                  var userEdited = $scope.editingUsername;
                                  $scope.editingUsername=undefined;
                                  $scope.editingAge=undefined;
                                  $scope.editingName=undefined;
                                  $scope.editingSurname=undefined;
                                   $('.close-reveal-modal').click();//close the reveal-modal window, small hack
                                  showAlertMessageSuccess(translation , userEdited);
                              });
                          }).
                          error(function(data, status, headers, config) {
                                console.log(status);
                          });
                      }).error(function(data,status,headers,config){
                          console.log(status);
                      });
               }
            }
        });
        MedicalPictures.controller('AdminViewAddUserController',function($scope, $translate,$location, $http,MedicalPicturesGlobal){
            $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
            $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getLoggedUser').
              success(function(data, status, headers, config) {
                $scope.loggedUsername = data.username;
              }).
              error(function(data, status, headers, config) {
                  console.log(status);
              });
              document.getElementById("alertMessageDiv").style.visibility="hidden";
              $scope.minPasswordLength = MedicalPicturesGlobal.MIN_PASSWORD_LENGTH;
              $scope.minAge = MedicalPicturesGlobal.MIN_AGE;
              $scope.maxAge = MedicalPicturesGlobal.MAX_AGE;
              $scope.nameRegexpPattern = MedicalPicturesGlobal.NAME_REGEXP_PATTERN;
              $scope.maxNameSurnameLength= MedicalPicturesGlobal.MAX_NAME_SURNAME_LENGTH;
              $scope.maxUsernameLength = MedicalPicturesGlobal.MAX_USERNAME_LENGTH;
              $scope.username = "a@a.pl";
              $scope.age = 1;
              $scope.name = "name";
              $scope.surname = "surname";
              $scope.accountType = "ADMIN";
              $scope.accountTypes = MedicalPicturesGlobal.ACCOUNT_TYPES;
              $scope.addUserClicked = function(){
                  document.getElementById("alertMessageDiv").style.visibility="hidden";//we hide it if user clicks it after adding user previously
                  if(!angular.isUndefined($scope.username) &&
                  !angular.isUndefined($scope.age) && !angular.isUndefined($scope.name) &&
                  !angular.isUndefined($scope.surname) && !angular.isUndefined($scope.selectedAccountType)){
                   $http({
                          url: '/MedicalPictures/AdminViewAddUser',
                          method: 'POST',
                          headers: {'Content-Type': 'application/json'},
                          data: {'username': $scope.username, 'age':$scope.age.toString(),
                          'name':$scope.name, 'surname':$scope.surname,'accountType':$scope.selectedAccountType,'resetPassword':"true"}
                      }).
                      success(function(data, status, headers, config) {
                          if(data.result==="success"){
                              $scope.username=undefined;
                              $scope.age=undefined;
                              $scope.name=undefined;
                              $scope.surname=undefined;
                              $translate('USER_ADDED_SUCCESSFULLY').then(function (translation) {
                                $location.path('/MedicalPictures/AdminViewAddUsers');
                                $location.replace();
                                  showAlertMessageSuccess(translation ,data.username);
                              });
                        }
                        else if(data.result === "failed"){
                              if(data.username === ""){
                                  $translate('INTERNAL_PROBLEM_OCCURRED').then(function (translation) {
                                      showAlertMessageError(translation ,data.username);
                                  });
                              } else{
                                  $translate('USER_ADDING_FAILED').then(function (translation) {
                                      showAlertMessageError(translation ,data.username);
                                  });
                              }
                        }
                      }).
                      error(function(data, status, headers, config) {
                          $translate('INTERNAL_PROBLEM_OCCURRED').then(function (translation) {
                              showAlertMessageError(translation ,data.username);
                          });
                          console.log(status);
                      });
                  }
              };
        });
        MedicalPictures.controller('AdminViewManagePictureTypesController',function($scope, $translate,$location, $http,MedicalPicturesGlobal){
            $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
            $scope.pictureTypesList =[];
            $scope.newPictureType ="";
            $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getLoggedUser').
              success(function(data, status, headers, config) {
                $scope.loggedUsername = data.username;
              }).
              error(function(data, status, headers, config) {
                  console.log(status);
              });
            $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllPictureTypes').
              success(function(data, status, headers, config) {
                $scope.pictureTypesList = data.pictureTypes;
              }).
              error(function(data, status, headers, config) {
                  console.log(status);
              });
              document.getElementById("alertMessageDiv").style.visibility="hidden";
              $scope.addBodyPartClicked = function(){
                  $scope.appName=$scope.newPictureType;
              };

        });
        /* UserSettings Controller */
        MedicalPictures.controller('UserSettingsController', function ($scope, MedicalPicturesGlobal) {
        $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
      });
