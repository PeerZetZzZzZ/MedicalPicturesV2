'use strict';
/* Contains global settings accessible for all controllers */
var MedicalPictures = angular.module('MedicalPictures', ['pascalprecht.translate']).constant('MedicalPicturesGlobal', {
    GLOBAL_APP_NAME: "MedicalPictures ver.1.0",
    MIN_PASSWORD_LENGTH: "3",
    MAX_PASSWORD_LENGTH: "30"
});

/* LoginView Controller */
MedicalPictures.controller('LoginController', function ($scope, $http, $location, MedicalPicturesGlobal) {
    $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
    $scope.minPasswordLength = MedicalPicturesGlobal.MIN_PASSWORD_LENGTH;
    $scope.maxPasswordLength = MedicalPicturesGlobal.MAX_PASSWORD_LENGTH;
    $scope.password = "";
    $scope.username = "";
    $scope.loginClicked = function () {
        $http({
          url:'/MedicalPictures/LoginServlet',
          method:'POST',
          headers:{'Content-Type':'application/json'},
          data: {'username':$scope.username, 'password':$scope.password}
        }).
            success(function (data, status, header, config) {
                console.log(status);
                $scope.appName=data;
            }).error(function (response) {
                console.log(status);
            });
    };
});

/* AdminView Controller */
MedicalPictures.controller('AdminViewController', function ($scope, MedicalPicturesGlobal) {
    $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
});

/* UserSettings Controller */
MedicalPictures.controller('UserSettingsController', function ($scope, MedicalPicturesGlobal) {
    $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;

})