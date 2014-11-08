'use strict';
        /* Contains global settings accessible for all controllers */
        var MedicalPictures = angular.module('MedicalPictures', ['pascalprecht.translate','ngSanitize']).constant('MedicalPicturesGlobal', {
            GLOBAL_APP_NAME: "MedicalPictures ver.1.0",
            MIN_PASSWORD_LENGTH: "3",
            MAX_PASSWORD_LENGTH: "30"
        });
        /* LoginView Controller */
        MedicalPictures.controller('LoginController', function ($scope, $http, $window,$translate, $location, MedicalPicturesGlobal) {
                $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
                $scope.minPasswordLength = MedicalPicturesGlobal.MIN_PASSWORD_LENGTH;
                $scope.maxPasswordLength = MedicalPicturesGlobal.MAX_PASSWORD_LENGTH;
                $scope.password = "pass";
                $scope.username = "userOrm@gmail.com";
                $scope.userAlreadyLogged= ""; //it contain the name of user which couldn't log because of already being logged
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
                              data: {'username': $scope.username, 'password': $scope.password}
                      }).
                        success(function (data, status, header, config) {
                            console.log(status);
                            if (data.username === $scope.username && data.status === "true"){//if login successful
                                $window.location.href = "ManageUsers";
                            }
                            else{//else print error message
                                        switch(data.reason){
                                            case "alreadyLogged":
                                                $translate('USER_ALREADY_LOGGED').then(function (translation) {
                                                    showAlertMessage(translation ,data.username);
                                                });
                                                break;
                                            case "authenticationFailed":
                                                $translate('AUTHENTICATION_FAILED').then(function (translation) {
                                                    showAlertMessage(translation,data.username);
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
        function showAlertMessage(message,username){
            var alert = document.getElementById("alertMessageDiv");
            alert.style.visibility="visible";//We show error message
            alert.innerHTML = "<div data-alert class=\"alert-box alert round\">"+
                username+" : " + message +
                "<a href=\"#\" class=\"close\">&times;</a>"+
                "</div>";
        }
        /* AdminView Controller */
        MedicalPictures.controller('AdminViewController', function ($scope, MedicalPicturesGlobal) {
        $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
                });
        /* UserSettings Controller */
        MedicalPictures.controller('UserSettingsController', function ($scope, MedicalPicturesGlobal) {
        $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
                })
