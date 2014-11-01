 'use strict'

 var MedicalPictures = angular.module('MedicalPictures',['pascalprecht.translate']);
MedicalPictures.GLOBAL_APP_NAME="MedicalPictures ver.1.0";
 MedicalPictures.controller('LoginController',function($scope){
   $scope.name='Kon';
   $scope.minPasswordLength="3";
   $scope.maxPasswordLength="30";
   $scope.prefered='en;'
 });
 MedicalPictures.config(function($translateProvider){
  $translateProvider.translations('en',{
    'LOGIN_VIEW_HEAD_NAME':'MedicalPictures Login View',
    'PASSWORD':'Password',
    'LOG_IN':'Login',
    'PROVIDE_CREDENTIALS':'Provide your credentials',
    'INCORRECT_EMAIL':'Given username (e-mail address) is incorrect',
    'PASSWORD_TOO_SHORT':'Password is too short!',
    'PASSWORD_TOO_LONG':'Password is too long!',

  });
  $translateProvider.translations('pl',{
    'LOGIN_VIEW_HEAD_NAME':'MedicalPictures Panel Logowania',
    'USERNAME':'Użytkownik',
    'LOG_IN':'Zaloguj',
    'PASSWORD':'Hasło',
    'PROVIDE_CREDENTIALS':'Podaj swój login oraz hasło',
    'INCORRECT_EMAIL':'Podana nazwa użytkownika (adres e-mail) jest niepoprawny',
    'PASSWORD_TOO_SHORT':'Hasło jest za krótkie!',
    'PASSWORD_TOO_LONG':'Hasło jest za długie!',

  });
  $translateProvider.preferredLanguage('pl');
 });
