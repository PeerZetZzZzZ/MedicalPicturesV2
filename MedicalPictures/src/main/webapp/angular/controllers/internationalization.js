/* This is global configuration file for some properties */
MedicalPictures.config(function($translateProvider){
 $translateProvider.translations('en',{
   'LOGIN_VIEW_HEAD_NAME':'MedicalPictures Login View',
   'ADMIN_VIEW_HEAD_NAME':'MedicalPictures Admin View',
   'USERNAME':'Username',
   'PASSWORD':'Password',
   'LOG_IN':'Login',
   'PROVIDE_CREDENTIALS':'Provide your credentials',
   'INCORRECT_EMAIL':'Given username (e-mail address) is incorrect',
   'PASSWORD_TOO_SHORT':'Password is too short!',
   'PASSWORD_TOO_LONG':'Password is too long!',
   'MENU':'Menu',
   'SETTINGS':'Settings',
   'LOGOUT':'Logout',
   'MANAGE_USERS':'Manage users',
   'MANAGE_BODY_PARTS':'Manage body parts',
   'MANAGE_PICTURE_TYPES':'Manage pictures types',
   'ADD_NEW_USER':'Add new user',
   'DELETE_USER':'Delete user',
   'MARK_ALL':'Mark all',
   'UNMARK_ALL':'Unmark all',
   'ACCOUNT_TYPE':'Account type',
   'BACK':'Go back'
 });
 $translateProvider.translations('pl',{
   'LOGIN_VIEW_HEAD_NAME':'MedicalPictures Panel Logowania',
   'ADMIN_VIEW_HEAD_NAME':'MedicalPictures Panel Administratora',
   'USERNAME':'Użytkownik',
   'LOG_IN':'Zaloguj',
   'PASSWORD':'Hasło',
   'PROVIDE_CREDENTIALS':'Podaj swój login oraz hasło',
   'INCORRECT_EMAIL':'Podana nazwa użytkownika (adres e-mail) jest niepoprawny',
   'PASSWORD_TOO_SHORT':'Hasło jest za krótkie!',
   'PASSWORD_TOO_LONG':'Hasło jest za długie!',
   'MENU':'Menu',
   'SETTINGS':'Ustawienia',
   'LOGOUT':'Wyloguj',
   'MANAGE_USERS':'Zarządzaj użytkownikami',
   'MANAGE_BODY_PARTS':'Zarządzaj częściami ciała',
   'MANAGE_PICTURE_TYPES':'Zarządzaj typami zdjęć',
   'ADD_NEW_USER':'Dodaj nowego użytkownika',
   'DELETE_USER':'Usuń użytkownika',
   'MARK_ALL':'Zaznacz wszystkich',
   'UNMARK_ALL':'Odznacz wszytkich',
   'ACCOUNT_TYPE':'Typ konta',
   'BACK':'Powrót'
 });
 $translateProvider.preferredLanguage('en');
});
