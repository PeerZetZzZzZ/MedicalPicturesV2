/* This is global configuration file for some properties */
MedicalPictures.config(function ($translateProvider) {
    $translateProvider.translations('en', {
        'LOGIN_VIEW_HEAD_NAME': 'MedicalPictures Login View',
        'ADMIN_VIEW_HEAD_NAME': 'MedicalPictures Admin View',
        'USERNAME': 'Username',
        'PASSWORD': 'Password',
        'LOG_IN': 'Login',
        'PROVIDE_CREDENTIALS': 'Provide your credentials',
        'INCORRECT_EMAIL': 'Given username (e-mail address) is incorrect',
        'PASSWORD_TOO_SHORT': 'Password is too short!',
        'PASSWORD_TOO_LONG': 'Password is too long!',
        'MENU': 'Menu',
        'SETTINGS': 'Settings',
        'LOGOUT': 'Logout',
        'MANAGE_USERS': 'Manage users',
        'MANAGE_BODY_PARTS': 'Manage body parts',
        'MANAGE_PICTURE_TYPES': 'Manage pictures types',
        'ADD_NEW_USER': 'Add new user',
        'DELETE_USER': 'Delete user',
        'MARK_ALL': 'Mark all',
        'UNMARK_ALL': 'Unmark all',
        'ACCOUNT_TYPE': 'Account type',
        'BACK': 'Go back',
        'CHANGE_YOUR_SETTINGS': 'Change your settings',
        'NAME': 'Name',
        'SURNAME': 'Surname',
        'YOUR_USERNAME': 'Your username',
        'YOUR_NAME': 'Your name',
        'YOUR_SURNAME': 'Your surname',
        'YOUR_PASSWORD': 'Your password',
        'USER_ALREADY_LOGGED_OUTSIDE':'Given username is already logged somewhere!',
        'USER_ALREADY_LOGGED_LOCALLY':'Hey you are already logged! Click here to go to the main page!',
        'AUTHENTICATION_FAILED':'Authentication failed for given credentials.',
        'LOGGED_AS':'Logged as: ',
        'USERNAME_NAME':'Username',
        'AGE':'Age',
        'ADDING_NEW_USER':'Adding new user',
        'ADD_USER':'Add user',
        'USER_ADDED_SUCCESSFULLY':"User added successfully!",
        'USER_ADDING_FAILED':"User adding failed! User already exists.",
        'INTERNAL_PROBLEM_OCCURRED':'Internal problem occurred...!',
        'EDIT':'Edit',
        'CHECK':'Check',
        'MODIFY_USER':'Modify existing user',
        'UPDATE_USER':'Update user',
        'RESET_PASSWORD':'Reset password',
        'USER_EDITED_SUCCESSFULLY':'User has been updated successfully.',
        'PICTURE_TYPES':'Picture types',
        'ADD_BODY_PART':'Add new body part',
        'ADD':'Add'
    });
    $translateProvider.translations('pl', {
        'LOGIN_VIEW_HEAD_NAME': 'MedicalPictures Panel Logowania',
        'ADMIN_VIEW_HEAD_NAME': 'MedicalPictures Panel Administratora',
        'USERNAME': 'Użytkownik',
        'LOG_IN': 'Zaloguj',
        'PASSWORD': 'Hasło',
        'PROVIDE_CREDENTIALS': 'Podaj swój login oraz hasło',
        'INCORRECT_EMAIL': 'Podana nazwa użytkownika (adres e-mail) jest niepoprawny',
        'PASSWORD_TOO_SHORT': 'Hasło jest za krótkie!',
        'PASSWORD_TOO_LONG': 'Hasło jest za długie!',
        'MENU': 'Menu',
        'SETTINGS': 'Ustawienia',
        'LOGOUT': 'Wyloguj',
        'MANAGE_USERS': 'Zarządzaj użytkownikami',
        'MANAGE_BODY_PARTS': 'Zarządzaj częściami ciała',
        'MANAGE_PICTURE_TYPES': 'Zarządzaj typami zdjęć',
        'ADD_NEW_USER': 'Dodaj nowego użytkownika',
        'DELETE_USER': 'Usuń użytkownika',
        'MARK_ALL': 'Zaznacz wszystkich',
        'UNMARK_ALL': 'Odznacz wszytkich',
        'ACCOUNT_TYPE': 'Typ konta',
        'BACK': 'Powrót',
        'CHANGE_YOUR_SETTINGS': 'Zmień swoje ustawienia',
        'NAME': 'Imię',
        'SURNAME': 'Nazwisko',
        'YOUR_USERNAME': 'Twoja nazwa użytkownika',
        'YOUR_NAME': 'Twoje imię',
        'YOUR_SURNAME': 'Twoje nazwisko',
        'YOUR_PASSWORD': 'Twoje hasło',
        'USER_ALREADY_LOGGED_OUTSIDE':'Podany użytkownik jest już zalogowany gdzieś indziej!',
        'USER_ALREADY_LOGGED_LOCALLY':'Hej, jesteś już zalogowany! Kliknij tu, aby przejść do okna głównego.',
        'AUTHENTICATION_FAILED':'Logowanie nie powiodło się.',
        'LOGGED_AS':'Zalogowany jako: ',
        'USERNAME_NAME':'Nazwa użytkownika',
        'AGE':'Wiek',
        'ADDING_NEW_USER':'Dodawanie nowego użytkownika',
        'ADD_USER':'Dodaj użytkownika',
        'USER_ADDED_SUCCESSFULLY':"Użytkownik dodany!",
        'USER_ADDING_FAILED':"Nie udało się dodać użytkownika. Użytkownik już istnieje!",
        'INTERNAL_PROBLEM_OCCURRED':'Wystąpił problem w systemie!',
        'EDIT':'Edytuj',
        'CHECK':'Zaznacz',
        'MODIFY_USER':'Edytuj istniejącego użytkownika',
        'UPDATE_USER':'Aktualizuj użytkownika',
        'RESET_PASSWORD':'Resetuj hasło',
        'USER_EDITED_SUCCESSFULLY':'Dane użytkownika zostały zaktualizowane.',
        'PICTURE_TYPES':'Typy zdjęć',
        'ADD_BODY_PART':'Dodaj część ciała',
        'ADD':'Dodaj'
    });
    $translateProvider.preferredLanguage('pl');
});
