'use strict';
/* Contains global settings accessible for all controllers */
var MedicalPictures = angular.module('MedicalPictures', ['angularFileUpload', 'pascalprecht.translate']).constant('MedicalPicturesGlobal', {
  GLOBAL_APP_NAME: "MedicalPictures ver.1.0",
  MIN_PASSWORD_LENGTH: "3",
  MAX_PASSWORD_LENGTH: "30",
  MAX_NAME_SURNAME_LENGTH: "255",
  MAX_PICTURE_DESCRIPTION_LENGTH: "1000",
  MAX_USERNAME_LENGTH: "100",
  MAX_BODY_PART_LENGTH: "255",
  MAX_PICTURE_TYPE_LENGTH: "255",
  MAX_DEFINED_PICTURE_DESCRIPTION_NAME_LENGTH: "100",
  MAX_DEFINED_PICTURE_DESCRIPTION_LENGTH: "1000",
  MIN_LENGTH: "2",
  MIN_AGE: "1",
  MAX_AGE: "150",
  NAME_REGEXP_PATTERN: /[a-zA-Z]/,
  ADMIN_VIEW_MAIN_WINDOW: '/MedicalPictures/AdminViewManageUsers',
  DOCTOR_VIEW_MAIN_WINDOW: '/MedicalPictures/DoctorViewManageDescriptions',
  TECHNICIAN_VIEW_MAIN_WINDOW: '/MedicalPictures/TechnicianViewManagePictures',
  PATIENT_VIEW_MAIN_WINDOW: '/MedicalPictures/PatientViewManagePictures',
  ACCOUNT_TYPES: ['ADMIN', 'DOCTOR', 'PATIENT', 'TECHNICIAN']
});
/* LoginView Controller */
MedicalPictures.controller('LoginController', function($scope, $http, $window, $translate, MedicalPicturesGlobal) {
  $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
  $scope.languages = ['pl', 'en'];
  $scope.minPasswordLength = MedicalPicturesGlobal.MIN_PASSWORD_LENGTH;
  $scope.maxPasswordLength = MedicalPicturesGlobal.MAX_PASSWORD_LENGTH;
  $scope.maxUsernameLength = MedicalPicturesGlobal.MAX_USERNAME_LENGTH;
  $translate.use('en');
  $scope.password = "";
  $scope.username = ""; //we share this username globally to later can print in in other windows
  $scope.userAlreadyLogged = ""; //it contain the name of user which couldn't log because of already being logged
  $scope.userMainWindow = ""; // url to the main window for this user, if he will go back to the Login VIew being already logged
  document.getElementById("alertMessageDiv").style.visibility = "hidden";
  /* This above is a small hack, because loading the html content like below - alert didnt react on clicking the close sign (x).
	 The solution was 2:
	 - have 1 alert already in HTML code ( not dynamically loaded)
	 - have 1 alert already in HTML and hide it. I made it  */
  $scope.languageChanged = function(newLanguage) {
    $translate.use(newLanguage);
  }
  $scope.loginClicked = function() {
    if (!angular.isUndefined($scope.username) && !angular.isUndefined($scope.password)) {
      $http({
        url: '/MedicalPictures/webresources/MedicalPicturesCommon/loginUser',
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        data: {
          'username': $scope.username,
          'password': $scope.password
        }
      }).success(function(data, status, header, config) {
        console.log(status);
        if (data.errorCode === 0) { //if login successful
          switch (data.accountType) {
            case "ADMIN":
              $window.location.href = "AdminViewManageUsers";
              break;
            case "PATIENT":
              $window.location.href = "PatientViewManagePictures";
              break;
            case "TECHNICIAN":
              $window.location.href = "TechnicianViewManagePictures";
              break;
            case "DOCTOR":
              $window.location.href = "DoctorViewManageDescriptions";
              break;
          }
        } else { //else print error message
          switch (data.errorCode) {
            case -1:
              $translate('AUTHENTICATION_FAILED').then(function(translation) {
                showAlertMessageError(translation, $scope.username);
              });
              break;
            case -3: //json parse
              $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
                showAlertMessageError(translation, '');
              });
              break;
            case -5:
              $translate('AUTHENTICATION_FAILED').then(function(translation) {
                showAlertMessageError(translation, $scope.username);
              });
              break;

          }
        }
      }).error(function(response) {
        $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
          showAlertMessageError(translation, '');
        });
        console.log(response);
      });
    };
  };
});

function showAlertMessageError(message, username) {
  var alert = document.getElementById("alertMessageDiv");
  alert.style.visibility = "visible"; //We show error message
  alert.innerHTML = "<div data-alert class=\"alert-box alert round\">" +
    username + " : " + message +
    "<a class=\"close\">&times;</a>" +
    "</div>";
}

function showAlertMessageWarningUrl(message, username, url) {
  var alert = document.getElementById("alertMessageDiv");
  alert.style.visibility = "visible"; //We show error message
  alert.innerHTML = "<div data-alert class=\"alert-box warning round\">" +
    "<a href=\"" + url + "\" >" + username + " : " + message + "</a>" +
    "<a class=\"close\">&times;</a>" +
    "</div>";
}

function showAlertMessageWarning(message) {
  var alert = document.getElementById("alertMessageDiv");
  alert.style.visibility = "visible"; //We show error message
  alert.innerHTML = "<div data-alert class=\"alert-box warning round\">" + message +
    "<a class=\"close\">&times;</a>" +
    "</div>";
}

function showAlertMessageSuccess(message, username) {
  var alert = document.getElementById("alertMessageDiv");
  alert.style.visibility = "visible"; //We show error message
  alert.innerHTML = "<div data-alert class=\"alert-box success radius\">" +
    username + " : " + message +
    "<a class=\"close\">&times;</a>" +
    "</div>";
}

function showAlertMessageSuccess(message) {
  var alert = document.getElementById("alertMessageDiv");
  alert.style.visibility = "visible"; //We show error message
  alert.innerHTML = "<div data-alert class=\"alert-box success radius\">" + message +
    "<a class=\"close\">&times;</a>" +
    "</div>";
}

/* AdminView Controller */
MedicalPictures.controller('AdminViewManageUsersController', function($scope, $http, $window, $translate, MedicalPicturesGlobal) {
  $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
  $scope.accountTypes = MedicalPicturesGlobal.ACCOUNT_TYPES;
  $scope.minPasswordLength = MedicalPicturesGlobal.MIN_PASSWORD_LENGTH;
  $scope.maxPasswordLength = MedicalPicturesGlobal.MAX_PASSWORD_LENGTH;
  $scope.minAge = MedicalPicturesGlobal.MIN_AGE;
  $scope.maxAge = MedicalPicturesGlobal.MAX_AGE;
  $scope.nameRegexpPattern = MedicalPicturesGlobal.NAME_REGEXP_PATTERN;
  $scope.maxNameSurnameLength = MedicalPicturesGlobal.MAX_NAME_SURNAME_LENGTH;
  $scope.maxUsernameLength = MedicalPicturesGlobal.MAX_USERNAME_LENGTH;
  $scope.applicationLanguages = ['pl', 'en'];
  $scope.editingUsername = "";
  $scope.editingSpecialization = "";
  $scope.editingName = "";
  $scope.editingSurname = "";
  $scope.editingAge = 0;
  $scope.editingSelectedAccountType = "";
  $scope.resetPasswordCheckbox = false;
  $scope.logoutUser = function() {
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/Logout').
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          $window.location.href = "/MedicalPictures/LoginView";
        case -1: //user unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
      }
    }).
    error(function(data, status, headers, config) {
      console.log(status);
    });
  };
  document.getElementById("alertMessageDiv").style.visibility = "hidden";
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getLoggedUser').
  success(function(data, status, headers, config) {
    $scope.loggedUsername = data.username;
    $translate.use(data.applicationLanguage);
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      $translate.use('en');
      showAlertMessageError(translation, data.username);
    });
    console.log(status);
  });
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllUsernames').
  success(function(data, status, headers, config) {
    switch (data.errorCode) {
      case 0:
        $scope.usernamesList = data.usernames;
        break;
      case -1: //unauthorized
        $window.location.href = "/MedicalPictures/UnauthorizedView";
        break;
      case -4: //not logged
        $window.location.href = "/MedicalPictures/LoginView";
        break;
      case -3: //json parse
        $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
          showAlertMessageError(translation, data.username);
        });
        break;
    }
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, data.username);
    });
    console.log(status);
  });
  $scope.editUserClicked = function(usernameToEdit) {
    $scope.resetPasswordCheckbox = false;
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getUserInfo/' + usernameToEdit).
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          {
            $scope.editingUsername = data.username;
            $scope.editingPassword = data.password;
            $scope.editingName = data.name;
            $scope.editingSurname = data.surname;
            $scope.editingAge = data.age;
            $scope.editingSelectedAccountType = data.accountType;
            $scope.editingSelectedLanguage = data.chosenLanguage;
            if ($scope.editingSelectedAccountType === 'DOCTOR') {
              $scope.editingSpecialization = data.specialization;
            }
            break;
          }
        case -1: //unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
        case -5: //user doesn't exist
          $translate('USER_DOESNT_EXISTS').then(function(translation) {
            showAlertMessageError(translation, $scope.editingUsername);
          });
          break;
        case -3: //json parse
          $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
            showAlertMessageError(translation, '');
          });
          break;
      }

    }).
    error(function(data, status, headers, config) {
      $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
        showAlertMessageError(translation, data.username);
      });
      console.log(status);
    });
  };
  $scope.markAllUsersClicked = function() {
    var i;
    for (i = 0; i < $scope.usernamesList.length; i++) {
      document.getElementById($scope.usernamesList[i].username).checked = true;
    }
  };
  $scope.unmarkAllUsersClicked = function() {
    var i;
    for (i = 0; i < $scope.usernamesList.length; i++) {
      document.getElementById($scope.usernamesList[i].username).checked = false;
    }
  };
  $scope.deleteUsersClicked = function() {
    var i;
    var usersToDeleteList = [];
    var usersToDeleteExist = false;
    for (i = 0; i < $scope.usernamesList.length; i++) {
      if (document.getElementById($scope.usernamesList[i].username).checked === true) {
        var index = usersToDeleteList.length;
        if ($scope.usernamesList[i].username !== $scope.loggedUsername) {
          usersToDeleteList[index] = "{username:'" + $scope.usernamesList[i].username + "',accountType:'" + $scope.usernamesList[i].accountType + "'}";
        }
      }
    }
    var usersToDelete;
    if (usersToDeleteList.length === 0) {
      $translate('NO_USERS_TO_REMOVE_SELECTED').then(function(translation) {
        showAlertMessageWarning(translation, '');
      });
    } else {
      usersToDelete = "{usernames:[" + usersToDeleteList + "]}";
      var usersToDeleteExist = true;
    }
    if (usersToDeleteExist) {
      $http({
        url: '/MedicalPictures/webresources/MedicalPicturesCommon/deleteUsers',
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        data: usersToDelete
      }).success(function(data, status, header, config) {
        switch (data.errorCode) {
          case 0:
            {
              $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllUsernames').
              success(function(data, status, headers, config) {
                switch (data.errorCode) {
                  case 0:
                    {
                      $scope.usernamesList = data.usernames;
                      $translate('USERS_SUCCESSFULY_REMOVED').then(function(translation) {
                        showAlertMessageSuccess(translation, '');
                      });
                      break;
                    }
                  case -1: //unauthorized
                    $window.location.href = "/MedicalPictures/UnauthorizedView";
                    break;
                  case -4: //not logged
                    $window.location.href = "/MedicalPictures/LoginView";
                    break;
                }
              }).
              error(function(data, status, headers, config) {
                $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
                  showAlertMessageError(translation, data.username);
                });
                console.log(status);
              });
              break;
            }
          case -1: //unauthorized
            $window.location.href = "/MedicalPictures/UnauthorizedView";
            break;
          case -4: //not logged
            $window.location.href = "/MedicalPictures/LoginView";
            break;
          case -3: //json parse
            $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
              showAlertMessageError(translation, '');
            });
            break;
        }
      }).error(function(data, status, headers, config) {
        $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
          showAlertMessageError(translation, data.username);
        });
        console.log(status);
      });
    }
  };
  $scope.saveEditedUserClicked = function() {
    var resetPassword = false;
    document.getElementById("alertMessageDiv").style.visibility = "hidden"; //hide the message about success
    if (!angular.isUndefined($scope.editingUsername) &&
      !angular.isUndefined($scope.editingAge) && !angular.isUndefined($scope.editingName) &&
      !angular.isUndefined($scope.editingSurname) && !angular.isUndefined($scope.editingSelectedAccountType)) {
      var allValuesArePresent = true;
      if ($scope.editingSelectedAccountType === "DOCTOR" && ($scope.editingSpecialization === undefined || $scope.editingSpecialization === '')) {
        allValuesArePresent = false; //specialization is undefined
      }
      if (allValuesArePresent) {
        if (document.getElementById($scope.resetPasswordCheckbox).checked === true) {
          resetPassword = true;
        }
        var userToEdit = {
          "username": $scope.editingUsername,
          "accountType": $scope.editingSelectedAccountType,
          "name": $scope.editingName,
          "surname": $scope.editingSurname,
          "specialization": $scope.editingSpecialization,
          "age": $scope.editingAge.toString(),
          "resetPassword": resetPassword.toString(),
          "chosenLanguage": $scope.editingSelectedLanguage
        };
        $http({
          url: '/MedicalPictures/webresources/MedicalPicturesCommon/editUser',
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          data: userToEdit
        }).success(function(data, status, header, config) {
          switch (data.errorCode) {
            case 0:
              {
                $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllUsernames').
                success(function(data, status, headers, config) {
                  switch (data.errorCode) {
                    case 0:
                      {
                        $scope.usernamesList = data.usernames;
                        $translate('USER_EDITED_SUCCESSFULLY').then(function(translation) {
                          var userEdited = $scope.editingUsername;
                          $scope.editingUsername = undefined;
                          $scope.editingAge = undefined;
                          $scope.editingName = undefined;
                          $scope.editingSurname = undefined;
                          $scope.editingSpecialization = undefined;
                          $('.close-reveal-modal').click(); //close the reveal-modal window, small hack
                          showAlertMessageSuccess(translation, userEdited);
                        });
                        break;
                      }
                    case -1: //unauthorized
                      $window.location.href = "/MedicalPictures/UnauthorizedView";
                      break;
                    case -4: //not logged
                      $window.location.href = "/MedicalPictures/LoginView";
                      break;
                    case -3: //json parse
                      $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
                        showAlertMessageError(translation, '');
                      });
                      break;

                  }
                }).
                error(function(data, status, headers, config) {
                  $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
                    showAlertMessageError(translation, data.username);
                  });
                  console.log(status);
                });
                break;
              }
            case -5: //user doesn't exist
              $translate('USER_DOESNT_EXISTS').then(function(translation) {
                showAlertMessageError(translation, $scope.editingUsername);
              });
              break;
            case -99: //internal server
              $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
                showAlertMessageError(translation, "");
              });
              break;
            case -1: //unauthorized
              $window.location.href = "/MedicalPictures/UnauthorizedView";
              break;
            case -4: //not logged
              $window.location.href = "/MedicalPictures/LoginView";
              break;
            case -3: //json parse
              $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
                showAlertMessageError(translation, '');
              });
              break;

          }
        }).error(function(data, status, headers, config) {
          console.log(status);
        });
      }
    }
  };
});
MedicalPictures.controller('AdminViewAddUserController', function($scope, $translate, $location, $window, $http, MedicalPicturesGlobal) {
  $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
  $scope.applicationLanguages = ['pl', 'en'];
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getLoggedUser').
  success(function(data, status, headers, config) {
    $scope.loggedUsername = data.username;
    $translate.use(data.applicationLanguage);
  }).
  error(function(data, status, headers, config) {
    console.log(status);
  });
  $scope.logoutUser = function() {
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/Logout').
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          $window.location.href = "/MedicalPictures/LoginView";
        case -1: //user unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
      }
    }).
    error(function(data, status, headers, config) {
      console.log(status);
    });
  }
  document.getElementById("alertMessageDiv").style.visibility = "hidden";
  $scope.minPasswordLength = MedicalPicturesGlobal.MIN_PASSWORD_LENGTH;
  $scope.minAge = MedicalPicturesGlobal.MIN_AGE;
  $scope.maxAge = MedicalPicturesGlobal.MAX_AGE;
  $scope.nameRegexpPattern = MedicalPicturesGlobal.NAME_REGEXP_PATTERN;
  $scope.maxNameSurnameLength = MedicalPicturesGlobal.MAX_NAME_SURNAME_LENGTH;
  $scope.maxUsernameLength = MedicalPicturesGlobal.MAX_USERNAME_LENGTH;
  $scope.username = "a@a.pl";
  $scope.age = 1;
  $scope.name = "name";
  $scope.surname = "surname";
  $scope.accountType = "ADMIN";
  $scope.accountTypes = MedicalPicturesGlobal.ACCOUNT_TYPES;
  $scope.addUserClicked = function() {
    document.getElementById("alertMessageDiv").style.visibility = "hidden"; //we hide it if user clicks it after adding user previously
    if (!angular.isUndefined($scope.username) &&
      !angular.isUndefined($scope.age) && !angular.isUndefined($scope.name) &&
      !angular.isUndefined($scope.surname) && !angular.isUndefined($scope.selectedAccountType)) {
      var allValuesArePresent = true;
      if ($scope.selectedAccountType === "DOCTOR" && ($scope.specialization === undefined || $scope.specialization === '')) {
        allValuesArePresent = false; //specialization is undefined
      }
      if (allValuesArePresent) {
        $http({
          url: '/MedicalPictures/webresources/MedicalPicturesCommon/addNewUser',
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          data: {
            'username': $scope.username,
            'specialization': $scope.specialization,
            'age': $scope.age.toString(),
            'name': $scope.name,
            'surname': $scope.surname,
            'chosenLanguage': $scope.selectedLanguage,
            'accountType': $scope.selectedAccountType,
            'resetPassword': "true"
          }
        }).success(function(data, status, headers, config) {
          switch (data.errorCode) {
            case 0:
              {
                $scope.username = undefined;
                $scope.age = undefined;
                $scope.name = undefined;
                $scope.specialization = undefined;
                $scope.surname = undefined;
                $translate('USER_ADDED_SUCCESSFULLY').then(function(translation) {
                  $location.path('/MedicalPictures/AdminViewAddUsers');
                  $location.replace();
                  showAlertMessageSuccess(translation, data.username);
                });
                break;
              }
            case -99: //internal server
              $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
                showAlertMessageError(translation, data.username);
              });
              break;
            case -1: //unauthorized
              $window.location.href = "/MedicalPictures/UnauthorizedView";
              break;
            case -4: //not logged
              $window.location.href = "/MedicalPictures/LoginView";
              break;
            case -3: //json parse
              $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
                showAlertMessageError(translation, '');
              });
              break;
            case -7: //already exists
              $translate('USER_DUPLICATION_ERROR').then(function(translation) {
                showAlertMessageError(translation, $scope.username);
              });
              break;
          }

        }).
        error(function(data, status, headers, config) {
          $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
            showAlertMessageError(translation, data.username);
          });
          console.log(status);
        });
      }
    }
  };
});
MedicalPictures.controller('AdminViewManagePictureTypesController', function($scope, $translate, $location, $window, $http, MedicalPicturesGlobal) {
  $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
  $scope.pictureTypesList = [];
  $scope.newPictureType = "";
  $scope.maxPictureTypeLength = MedicalPicturesGlobal.MAX_PICTURE_TYPE_LENGTH;
  $scope.logoutUser = function() {
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/Logout').
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          $window.location.href = "/MedicalPictures/LoginView";
        case -1: //user unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
      }
    }).
    error(function(data, status, headers, config) {
      console.log(status);
    });
  };
  document.getElementById("alertMessageDiv").style.visibility = "hidden";
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getLoggedUser').
  success(function(data, status, headers, config) {
    $scope.loggedUsername = data.username;
    $translate.use(data.applicationLanguage);
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, data.username);
    });
    console.log(status);
  });
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllPictureTypes').
  success(function(data, status, headers, config) {
    switch (data.errorCode) {
      case 0:
        {
          $scope.pictureTypesList = data.pictureTypes;
          break;
        }
      case -1: //unauthorized
        $window.location.href = "/MedicalPictures/UnauthorizedView";
        break;
      case -4: //not logged
        $window.location.href = "/MedicalPictures/LoginView";
        break;
      case -3: //json parse
        $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
          showAlertMessageError(translation, '');
        });
        break;
    }
  }).error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, data.username);
    });
    console.log(status);
  });
  $scope.addPictureTypeClicked = function() {
    if (!angular.isUndefined($scope.newPictureType)) {
      document.getElementById("alertMessageDiv").style.visibility = "hidden";
      var newPictureType = {
        'pictureType': $scope.newPictureType
      };
      $http({
        url: '/MedicalPictures/webresources/MedicalPicturesCommon/addPictureType',
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        data: newPictureType
      }).success(function(data, status, headers, config) {
        switch (data.errorCode) {
          case 0:
            $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllPictureTypes').
            success(function(data, status, headers, config) {
              switch (data.errorCode) {
                case 0:
                  $scope.pictureTypesList = data.pictureTypes;
                  $translate('PICTURE_TYPE_ADDED_SUCCESSFULLY').then(function(translation) {
                    showAlertMessageSuccess(translation, data.pictureType);
                  });
                  break;
                case -1: //unauthorized
                  $window.location.href = "/MedicalPictures/UnauthorizedView";
                  break;
                case -4: //not logged
                  $window.location.href = "/MedicalPictures/LoginView";
                  break;
              }
            }).
            error(function(data, status, headers, config) {
              $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
                showAlertMessageError(translation, "");
              });
              console.log(status);
            });
            break;
          case -1: //unauthorized
            $window.location.href = "/MedicalPictures/UnauthorizedView";
            break;
          case -4: //not logged
            $window.location.href = "/MedicalPictures/LoginView";
            break;
          case -3: //json parse
            $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
              showAlertMessageError(translation, '');
            });
            break;
          case -7: //already exists
            $translate('OBJECT_DUPLICATION_ERROR').then(function(translation) {
              showAlertMessageError(translation, $scope.newPictureType);
            });
            break;
        }
      }).error(function(data, status, headers, config) {
        console.log(status);
        $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
          showAlertMessageError(translation, "");
        });
      });
    } else {
      $translate('PICTURE_TYPE_IS_INCORRECT').then(function(translation) {
        showAlertMessageWarning(translation);
      });
    }
  };

});
MedicalPictures.controller('AdminViewManageDefinedPictureDescriptionsController', function($scope, $translate, $location, $window, $http, MedicalPicturesGlobal) {
  $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
  $scope.maxDefinedPictureDescriptionNameLength = MedicalPicturesGlobal.MAX_DEFINED_PICTURE_DESCRIPTION_NAME_LENGTH;
  $scope.maxDefinedPictureDescriptionLength = MedicalPicturesGlobal.MAX_DEFINED_PICTURE_DESCRIPTION_LENGTH;
  $scope.pictureTypesList = [];
  $scope.newPictureType = "";
  $scope.maxPictureTypeLength = MedicalPicturesGlobal.MAX_PICTURE_TYPE_LENGTH;
  document.getElementById("alertMessageDiv").style.visibility = "hidden";
  $scope.logoutUser = function() {
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/Logout').
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          $window.location.href = "/MedicalPictures/LoginView";
        case -1: //user unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
      }
    }).
    error(function(data, status, headers, config) {
      console.log(status);
    });
  };
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getLoggedUser').
  success(function(data, status, headers, config) {
    $scope.loggedUsername = data.username;
    $translate.use(data.applicationLanguage);
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, data.username);
    });
    console.log(status);
  });
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getDefinedPictureDescriptions').
  success(function(data, status, headers, config) {
    switch (data.errorCode) {
      case 0:
        {
          $scope.definedPictureDescriptionsList = data.definedPictureDescriptions;
          break;
        }
      case -1: //unauthorized
        $window.location.href = "/MedicalPictures/UnauthorizedView";
        break;
      case -4: //not logged
        $window.location.href = "/MedicalPictures/LoginView";
        break;
      case -3: //json parse
        $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
          showAlertMessageError(translation, '');
        });
        break;
    }
  }).error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, data.username);
    });
    console.log(status);
  });
  $scope.addDefinedPictureDescriptionClicked = function() {
    if (!angular.isUndefined($scope.newDefinedPictureDescriptionName) && !angular.isUndefined($scope.newDefinedPictureDescription)) {
      document.getElementById("alertMessageDiv").style.visibility = "hidden";
      var newDefinedPictureDescription = {
        'name': $scope.newDefinedPictureDescriptionName,
        'description': $scope.newDefinedPictureDescription,
      };
      $http({
        url: '/MedicalPictures/webresources/MedicalPicturesCommon/addDefinedPictureDescription',
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        data: newDefinedPictureDescription
      }).success(function(data, status, headers, config) {
        switch (data.errorCode) {
          case 0:
            $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getDefinedPictureDescriptions').
            success(function(data, status, headers, config) {
              switch (data.errorCode) {
                case 0:
                  $scope.definedPictureDescriptionsList = data.definedPictureDescriptions;
                  $translate('PICTURE_TYPE_ADDED_SUCCESSFULLY').then(function(translation) {
                    showAlertMessageSuccess(translation, $scope.newDefinedPictureDescriptionName);
                  });
                  break;
                case -1: //unauthorized
                  $window.location.href = "/MedicalPictures/UnauthorizedView";
                  break;
                case -4: //not logged
                  $window.location.href = "/MedicalPictures/LoginView";
                  break;
              }
            }).
            error(function(data, status, headers, config) {
              $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
                showAlertMessageError(translation, "");
              });
              console.log(status);
            });
            break;
          case -1: //unauthorized
            $translate('PICTURE_TYPE_ADDING_FAILED').then(function(translation) {
              showAlertMessageError(translation, data.pictureType);
            });
            break;
          case -4: //not logged
            $window.location.href = "/MedicalPictures/LoginView";
            break;
          case -3: //json parse
            $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
              showAlertMessageError(translation, '');
            });
            break;
          case -7:
            $translate('OBJECT_DUPLICATION_ERROR').then(function(translation) {
              showAlertMessageError(translation, $scope.newDefinedPictureDescription);
            });
            break;
        }
      }).error(function(data, status, headers, config) {
        console.log(status);
        $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
          showAlertMessageError(translation, "");
        });
      });
    } else {
      $translate('DEFINED_PICTURE_DESCRIPTION_IS_INCORRECT').then(function(translation) {
        showAlertMessageWarning(translation);
      });
    }
  };

});
MedicalPictures.controller('AdminViewManageBodyPartsController', function($scope, $translate, $location, $window, $http, MedicalPicturesGlobal) {
  $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
  $scope.bodyPartsList = [];
  $scope.maxBodyPartLength = MedicalPicturesGlobal.MAX_BODY_PART_LENGTH;
  $scope.newBodyPart = "";
  document.getElementById("alertMessageDiv").style.visibility = "hidden";
  $scope.logoutUser = function() {
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/Logout').
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          $window.location.href = "/MedicalPictures/LoginView";
        case -1: //user unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
      }
    }).
    error(function(data, status, headers, config) {
      console.log(status);
    });
  };
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getLoggedUser').
  success(function(data, status, headers, config) {
    $scope.loggedUsername = data.username;
    $translate.use(data.applicationLanguage);
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, "");
    });
    console.log(status);
  });
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllBodyParts').
  success(function(data, status, headers, config) {
    switch (data.errorCode) {
      case 0:
        $scope.bodyPartsList = data.bodyParts;
        break;
      case -1: //unauthorized
        $window.location.href = "/MedicalPictures/UnauthorizedView";
        break;
      case -4: //not logged
        $window.location.href = "/MedicalPictures/LoginView";
        break;
    }
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, "");
    });
    console.log(status);
  });
  $scope.addBodyPartClicked = function() {
    if (!angular.isUndefined($scope.newBodyPart) && $scope.newBodyPart !== '') {
      document.getElementById("alertMessageDiv").style.visibility = "hidden";
      var newBodyPart = {
        'bodyPart': $scope.newBodyPart
      };
      $http({
        url: '/MedicalPictures/webresources/MedicalPicturesCommon/addBodyPart',
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        data: newBodyPart
      }).success(function(data, status, headers, config) {
        switch (data.errorCode) {
          case 0:
            $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllBodyParts').
            success(function(data, status, headers, config) {
              $scope.bodyPartsList = data.bodyParts;
            }).
            error(function(data, status, headers, config) {
              $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
                showAlertMessageError(translation, "");
              });
              console.log(status);
            });
            $translate('BODY_PART_ADDED_SUCCESSFULLY').then(function(translation) {
              showAlertMessageSuccess(translation, data.bodyPart);
            });
            break;
          case -1: //unauthorized
            $window.location.href = "/MedicalPictures/UnauthorizedView";
            break;
          case -4: //not logged
            $window.location.href = "/MedicalPictures/LoginView";
            break;
          case -3: //json parse
            $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
              showAlertMessageError(translation, '');
            });
            break;
          case -99:
            $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
              showAlertMessageError(translation, "");
            });
            break;
          case -7: //already exists
            $translate('OBJECT_DUPLICATION_ERROR').then(function(translation) {
              showAlertMessageError(translation, $scope.newBodyPart);
            });
            break;
        }
      }).error(function(data, status, headers, config) {
        console.log(status);
        $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
          showAlertMessageError(translation, "");
        });
      });
    } else {
      $translate('BODY_PART_IS_INCORRECT').then(function(translation) {
        showAlertMessageWarning(translation);
      });
    }
  };

});
/* TechnicianView Controllers */
MedicalPictures.controller('TechnicianViewAddPicturesController', function($scope, $http, $translate, $window, FileUploader, MedicalPicturesGlobal) {
  $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
  $scope.accountTypes = MedicalPicturesGlobal.ACCOUNT_TYPES;
  $scope.allPatients = [];
  $scope.allBodyParts = [];
  $scope.allPictureType = [];
  $scope.selectedPatient;
  $scope.selectedBodyPart;
  $scope.selectedPictureType;
  $scope.logoutUser = function() {
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/Logout').
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          $window.location.href = "/MedicalPictures/LoginView";
        case -1: //user unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
      }
    }).
    error(function(data, status, headers, config) {
      console.log(status);
    });
  };
  var uploader = $scope.uploader = new FileUploader({
    url: '/MedicalPictures/TechnicianViewAddPictures'
  });

  uploader.filters.push({
    name: 'imageFilter',
    fn: function(item /*{File|FileLikeObject}*/ , options) {
      var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
      return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
    }
  });
  document.getElementById("alertMessageDiv").style.visibility = "hidden";
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getLoggedUser').
  success(function(data, status, headers, config) {
    $scope.loggedUsername = data.username;
    $translate.use(data.applicationLanguage);
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, "");
    });
    console.log(status);
  });
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllPatients').
  success(function(data, status, headers, config) {
    switch (data.errorCode) {
      case 0:
        var i = 0;
        var patients = data.patients;
        for (i = 0; i < patients.length; i++) {
          $scope.allPatients[i] = patients[i].username + " : " + patients[i].name + " " + patients[i].surname;
        }
        break;
      case -1: //unauthorized
        $window.location.href = "/MedicalPictures/UnauthorizedView";
        break;
      case -4: //not logged
        $window.location.href = "/MedicalPictures/LoginView";
        break;
      case -99:
        $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
          showAlertMessageError(translation, "");
        });
        break;

    }
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, "");
    });
    console.log(status);
  });
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllBodyParts').
  success(function(data, status, headers, config) {
    switch (data.errorCode) {
      case 0:
        $scope.allBodyParts = data.bodyParts;
        break;
      case -1: //unauthorized
        $window.location.href = "/MedicalPictures/UnauthorizedView";
        break;
      case -4: //not logged
        $window.location.href = "/MedicalPictures/LoginView";
        break;
      case -99:
        $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
          showAlertMessageError(translation, "");
        });
        break;
    }
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, "");
    });
    console.log(status);
  });
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllPictureTypes').
  success(function(data, status, headers, config) {
    switch (data.errorCode) {
      case 0:
        $scope.allPictureTypes = data.pictureTypes;
        break;
      case -1: //unauthorized
        $window.location.href = "/MedicalPictures/UnauthorizedView";
        break;
      case -4: //not logged
        $window.location.href = "/MedicalPictures/LoginView";
        break;
      case -99:
        $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
          showAlertMessageError(translation, "");
        });
        break;
    }
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, "");
    });
    console.log(status);
  });
  $scope.uploadPictures = function() {
    var i = 0;
    var flagOfUpload = true;
    for (i = 0; i < uploader.queue.length; i++) {
      if (angular.isUndefined(uploader.queue[i].selectedPatient) && uploader.queue[i].selectedPatient !== '') {
        var fileName = uploader.queue[i].file.name;
        $translate('NO_PATIENT_SELECTED_FOR_FILE').then(function(translation) {
          showAlertMessageError(translation, fileName);
        });
        flagOfUpload = false;
        break;
      } else if (angular.isUndefined(uploader.queue[i].selectedBodyPart) && uploader.queue[i].selectedBodyPart !== '') {
        var fileName = uploader.queue[i].file.name;
        $translate('NO_BODY_PART_SELECTED_FOR_FILE').then(function(translation) {
          showAlertMessageError(translation, fileName);
        });
        flagOfUpload = false;
        break;
      } else if (angular.isUndefined(uploader.queue[i].selectedPictureType) && uploader.queue[i].selectedPictureType !== '') {
        var fileName = uploader.queue[i].file.name;
        $translate('NO_PICTURE_TYPE_SELECTED_FOR_FILE').then(function(translation) {
          showAlertMessageError(translation, fileName);
        });
        flagOfUpload = false;
        break;
      }
    }
    if (flagOfUpload) {
      uploader.uploadAll();
    }
  };

  uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/ , filter, options) {
    $translate('ONLY_PICTURES_CAN_BE_UPLOADED').then(function(translation) {
      showAlertMessageWarning(translation);
    });
  };
  uploader.onAfterAddingFile = function(fileItem) {
    console.info('onAfterAddingFile', fileItem);
  };
  uploader.onAfterAddingAll = function(addedFileItems) {
    console.info('onAfterAddingAll', addedFileItems);
  };
  uploader.onBeforeUploadItem = function(item) {
    document.getElementById("alertMessageDiv").style.visibility = "hidden";
    console.info('onBeforeUploadItem', item);
    var pictureData = '{\'patient\':\'' + item.selectedPatient + '\', \'pictureName\':' + item.file.name + ',\'bodyPart\':\'' + item.selectedBodyPart + '\',\'pictureType\':\'' + item.selectedPictureType + '\'}';
    item.file.name = pictureData;
  };
  uploader.onProgressItem = function(fileItem, progress) {
    console.info('onProgressItem', fileItem, progress);
  };
  uploader.onProgressAll = function(progress) {
    console.info('onProgressAll', progress);
  };
  uploader.onSuccessItem = function(fileItem, response, status, headers) {
    console.info('onSuccessItem', fileItem, response, status, headers);
  };
  uploader.onErrorItem = function(fileItem, response, status, headers) {
    console.info('onErrorItem', fileItem, response, status, headers);
  };
  uploader.onCancelItem = function(fileItem, response, status, headers) {
    console.info('onCancelItem', fileItem, response, status, headers);
  };
  uploader.onCompleteItem = function(fileItem, response, status, headers) {
    console.info('onCompleteItem', fileItem, response, status, headers);
  };
  uploader.onCompleteAll = function() {
    $translate('ALL_PICTURES_UPLOADED').then(function(translation) {
      showAlertMessageSuccess(translation);
      $scope.uploader.clearQueue();
    });
  };

  console.info('uploader', uploader);
});
MedicalPictures.controller('TechnicianViewManagePicturesController', function($scope, $http, $translate, $window, MedicalPicturesGlobal) {
  $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
  $scope.pictures = [];
  $scope.allBodyParts = [];
  $scope.allPictureTypes = [];
  document.getElementById("alertMessageDiv").style.visibility = "hidden";
  $scope.logoutUser = function() {
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/Logout').
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          $window.location.href = "/MedicalPictures/LoginView";
        case -1: //user unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
      }
    }).
    error(function(data, status, headers, config) {
      console.log(status);
    });
  };
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getLoggedUser').
  success(function(data, status, headers, config) {
    $scope.loggedUsername = data.username;
    $translate.use(data.applicationLanguage);
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, "");
    });
    console.log(status);
  });
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllBodyParts').
  success(function(data, status, headers, config) {
    var i = 0;
    $scope.allBodyParts = data.bodyParts;
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllPictureTypes').
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          $scope.allPictureTypes = data.pictureTypes;
          $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllPictures').
          success(function(data, status, headers, config) {
            switch (data.errorCode) {
              case 0:
                $scope.pictures = data.pictures;
                if ($scope.pictures.length === 0) {
                  $translate('PICTURES_LIST_IS_EMPTY').then(function(translation) {
                    showAlertMessageWarning(translation);
                  });
                }
                break;
              case -1: //unauthorized
                $window.location.href = "/MedicalPictures/UnauthorizedView";
                break;
              case -4: //not logged
                $window.location.href = "/MedicalPictures/LoginView";
                break;
              case -99:
                $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
                  showAlertMessageError(translation, "");
                });
                break;
            }
          }).
          error(function(data, status, headers, config) {
            $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
              showAlertMessageError(translation, "");
            });
            console.log(status);
          });
          break;
        case -1: //unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
        case -99:
          break;
      }
    }).
    error(function(data, status, headers, config) {
      $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
        showAlertMessageError(translation, "");
      });
      console.log(status);
    });

  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, "");
    });
    console.log(status);
  });


  $scope.markAllPicturesClicked = function() {
    var i;
    for (i = 0; i < $scope.pictures.length; i++) {
      document.getElementById($scope.pictures[i].pictureId).checked = true;
    }
  };
  $scope.unmarkAllPicturesClicked = function() {
    var i;
    for (i = 0; i < $scope.pictures.length; i++) {
      document.getElementById($scope.pictures[i].pictureId).checked = false;
    }
  };
  $scope.deletePicturesClicked = function() {
    var i;
    var picturesToDelete = [];
    for (i = 0; i < $scope.pictures.length; i++) {
      if (document.getElementById($scope.pictures[i].pictureId).checked === true) {
        var index = picturesToDelete.length;
        picturesToDelete[index] = "{pictureId:'" + $scope.pictures[i].pictureId + "'}";
      }
    }
    var deletePictures;
    if (picturesToDelete.length === 0) {} else {
      deletePictures = "{pictures:[" + picturesToDelete + "]}";
      $http({
        url: '/MedicalPictures/webresources/MedicalPicturesCommon/deletePictures',
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        data: deletePictures
      }).success(function(data, status, header, config) {
        switch (data.errorCode) {
          case 0:
            $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllPictures').
            success(function(data, status, headers, config) {
              $scope.pictures = data.pictures;
              if ($scope.pictures.length === 0) {
                $translate('PICTURES_LIST_IS_EMPTY').then(function(translation) {
                  showAlertMessageWarning(translation);
                });
              } else {
                $translate('SUCCESSFULLY_REMOVED_PICTURE').then(function(translation) {
                  showAlertMessageSuccess(translation);
                });
              }
            }).
            error(function(data, status, headers, config) {
              $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
                showAlertMessageError(translation, "");
              });
              console.log(status);
            });
            break;
          case -1: //unauthorized
            $window.location.href = "/MedicalPictures/UnauthorizedView";
            break;
          case -4: //not logged
            $window.location.href = "/MedicalPictures/LoginView";
            break;
          case -3:
            $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
              showAlertMessageError(translation, '');
            });
            break;
          case -99:
            $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
              showAlertMessageError(translation, "");
            });
            break;
        }
      }).error(function(data, status, headers, config) {
        $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
          showAlertMessageError(translation, "");
        });
        console.log(status);
      });
    }
  };
  $scope.updatePicture = function(picture) {
    var pictureUpdateValues = '{pictureId:\'' + picture.pictureId + '\',bodyPart:\'' + picture.selectedBodyPart + '\',pictureType:\'' + picture.selectedPictureType + '\'}';
    $http({
      url: '/MedicalPictures/webresources/MedicalPicturesCommon/updatePicture',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      data: pictureUpdateValues
    }).success(function(data, status, header, config) {
      switch (data.errorCode) {
        case 0:
          $translate('SUCCESSFULLY_EDITED_PICTURE').then(function(translation) {
            showAlertMessageSuccess(translation, picture.pictureName);
          });
          break;
        case -6: //object doesn't exist
          $translate('ADD_TO_DB_FAILED').then(function(translation) {
            showAlertMessageError(translation, picture.pictureName);
          });
          break;
        case -1: //unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
        case -3: //json parse
          $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
            showAlertMessageError(translation, '');
          });
          break;
        case -99: //internal server error
          $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
            showAlertMessageError(translation, "");
          });
          break;
      }
    }).error(function(data, status, header, config) {
      $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
        showAlertMessageError(translation, "");
      });
      console.log(status);
    });
  };
});
/* Doctor Section */
MedicalPictures.controller('DoctorViewManageDescriptionsController', function($scope, $http, $window, $translate, MedicalPicturesGlobal) {
  $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
  $scope.patients = [];
  $scope.definedPictureDescriptions = [];
  document.getElementById("alertMessageDiv").style.visibility = "hidden";
  $scope.logoutUser = function() {
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/Logout').
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          $window.location.href = "/MedicalPictures/LoginView";
        case -1: //user unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
      }
    }).
    error(function(data, status, headers, config) {
      console.log(status);
    });
  };
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getLoggedUser').
  success(function(data, status, headers, config) {
    $scope.loggedUsername = data.username;
    $translate.use(data.applicationLanguage);
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, "");
    });
    console.log(status);
  });
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getDefinedPictureDescriptions').
  success(function(data, status, headers, config) {
    switch (data.errorCode) {
      case 0:
        $scope.definedPictureDescriptions = data.definedPictureDescriptions;
        break;
      case -1: //unauthorized
        $window.location.href = "/MedicalPictures/UnauthorizedView";
        break;
      case -4: //not logged
        $window.location.href = "/MedicalPictures/LoginView";
        break;
      case -99: //internal server error
        $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
          showAlertMessageError(translation, "");
        });
        break;
    }
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, "");
    });
    console.log(status);
  });
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getAllPatients').
  success(function(data, status, headers, config) {
    switch (data.errorCode) {
      case 0:
        $scope.patients = data.patients;
        if ($scope.patients.length === 0) {
          $translate('NO_PATIENTS_FOUND').then(function(translation) {
            showAlertMessageWarning(translation);
          });
        }
        break;
      case -1: //unauthorized
        $window.location.href = "/MedicalPictures/UnauthorizedView";
        break;
      case -4: //not logged
        $window.location.href = "/MedicalPictures/LoginView";
        break;
      case -99: //internal server error
        $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
          showAlertMessageError(translation, "");
        });
        break;
    }
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, "");
    });
    console.log(status);
  });
  $scope.getPatientPicturesNames = function(patient) {
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getPatientPicturesNames/' + patient.username).
    success(function(data, status, headers, config) {
      patient.pictures = data.pictures;
    }).
    error(function(data, status, headers, config) {
      $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
        showAlertMessageError(translation, "");
      });
      console.log(status);
    });
  };
  $scope.getPatientPictureWithThumbnail = function(picture) {
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getPatientPictureWithThumbnail/' + picture.pictureId).
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          $scope.selectedPicture = data;
          break;
        case -1: //unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
        case -99: //internal server error
          $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
            showAlertMessageError(translation, "");
          });
          break;
      }
    }).
    error(function(data, status, headers, config) {
      $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
        showAlertMessageError(translation, "");
      });
      console.log(status);
    });
  };
  $scope.getFullPictureData = function(picture) {
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getFullPictureData/' + picture.pictureId).
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          window.open(data.pictureData);
          break;
        case -6:
          $translate('ADD_TO_DB_FAILED').then(function(translation) {
            showAlertMessageError(translation, picture.pictureName);
          });
          break;
        case -1: //unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
        case -99: //internal server error
          $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
            showAlertMessageError(translation, "");
          });
          break;
      }
    }).
    error(function(data, status, headers, config) {
      $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
        showAlertMessageError(translation, "");
      });
      console.log(status);
    });
  };
  $scope.savePictureDescription = function(picture) {
    var pictureDescription;
    if (!angular.isUndefined(picture.definedPictureDescriptionId)) {
      if (picture.pictureDescriptionId !== '') { //if we update description , not add new
        pictureDescription = '{pictureId:\'' + picture.pictureId + '\', pictureDescriptionId:\'' + picture.pictureDescriptionId + '\',pictureDescription:\'\',definedPictureDescriptionId:\'' + picture.definedPictureDescriptionId + '\'}';
      } else {
        pictureDescription = '{pictureId:\'' + picture.pictureId + '\', pictureDescriptionId:\'\',pictureDescription:\'\',definedPictureDescriptionId:\'' + picture.definedPictureDescriptionId + '\'}';
      }
    } else {
      if (picture.pictureDescriptionId !== '') {
        pictureDescription = '{pictureId:\'' + picture.pictureId + '\',pictureDescriptionId:\'' + picture.pictureDescriptionId + '\',pictureDescription:\'' + picture.pictureDescription + '\',definedPictureDescriptionId:\'\'}';
      } else {
        pictureDescription = '{pictureId:\'' + picture.pictureId + '\',pictureDescriptionId:\'\',pictureDescription:\'' + picture.pictureDescription + '\',definedPictureDescriptionId:\'\'}';
      }
    }
    $http({
      url: '/MedicalPictures/webresources/MedicalPicturesCommon/savePictureDescription',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      data: pictureDescription
    }).success(function(data, status, header, config) {
      switch (data.errorCode) {
        case 0:
          $translate('SUCCESSFULLY_EDITED_PICTURE').then(function(translation) {
            showAlertMessageSuccess(translation, picture.pictureName);
          });
          break;
        case -6:
          $translate('ADD_TO_DB_FAILED').then(function(translation) {
            showAlertMessageError(translation, picture.pictureName);
          });
          break;
        case -1: //unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
        case -99: //internal server error
          $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
            showAlertMessageError(translation, "");
          });
          break;
        case -3: //json parse
          $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
            showAlertMessageError(translation, '');
          });
          break;
      }
    }).error(function(data, status, header, config) {
      $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
        showAlertMessageError(translation, "");
      });
      console.log(status);
    });
  };
  $scope.setDefinedPictureDescriptionForSelectedPicture = function(definedPictureDescription) {
    $scope.selectedPicture.definedPictureDescriptionId = definedPictureDescription.id;
    $scope.selectedPicture.pictureDescription = definedPictureDescription.pictureDescription;
  };

});
/* ****************************************** */
/* Patient Controller */
MedicalPictures.controller('PatientViewController', function($scope, $http, $translate, $window, MedicalPicturesGlobal) {
  $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
  $scope.patientPictures = [];
  $scope.selectedPicture;
  $scope.selectedDescription = "";
  $scope.pictureDescriptions = [];
  document.getElementById("alertMessageDiv").style.visibility = "hidden";
  $scope.logoutUser = function() {
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/Logout').
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          $window.location.href = "/MedicalPictures/LoginView";
        case -1: //user unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
      }
    }).
    error(function(data, status, headers, config) {
      console.log(status);
    });
  };
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getLoggedUser').
  success(function(data, status, headers, config) {
    switch (data.errorCode) {
      case 0:
        $scope.loggedUsername = data.username;
        $translate.use(data.applicationLanguage);
        $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getPatientPicturesNames/' + $scope.loggedUsername).
        success(function(data, status, headers, config) {
          switch (data.errorCode) {
            case 0:
              $scope.patientPictures = data.pictures;
              break;
            case -1: //unauthorized
              $window.location.href = "/MedicalPictures/UnauthorizedView";
              break;
            case -4: //not logged
              $window.location.href = "/MedicalPictures/LoginView";
              break;
            case -99: //internal server error
              $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
                showAlertMessageError(translation, "");
              });
              break;
          }
        }).
        error(function(data, status, headers, config) {
          $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
            showAlertMessageError(translation, "");
          });
          console.log(status);
        });
        break;
    }
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, "");
    });
    console.log(status);
  });
  $scope.getPatientPictureWithThumbnail = function(picture) {
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getPatientPictureWithThumbnail/' + picture.pictureId).
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          $scope.selectedPicture = data;
          $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getPictureDescriptions/' + picture.pictureId).
          success(function(data, status, headers, config) {
            switch (data.errorCode) {
              case 0:
                $scope.pictureDescriptions = data.pictureDescriptions;
                $scope.selectedDescription = $scope.pictureDescriptions[0];
                break;
              case -6:
                $translate('ADD_TO_DB_FAILED').then(function(translation) {
                  showAlertMessageError(translation, picture.pictureName);
                });
                break;
              case -1: //unauthorized
                $window.location.href = "/MedicalPictures/UnauthorizedView";
                break;
              case -4: //not logged
                $window.location.href = "/MedicalPictures/LoginView";
                break;
              case -99: //internal server error
                $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
                  showAlertMessageError(translation, "");
                });
                break;
              case -3: //json parse
                $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
                  showAlertMessageError(translation, '');
                });
                break;
              case -1000: //user cannot access not his content
                $translate('USER_NOT_HAVE_PERMISSIONS_TO_SEE_THIS').then(function(translation) {
                  showAlertMessageError(translation, $scope.loggedUsername);
                });
                break;
            }
          }).
          error(function(data, status, headers, config) {
            console.log(status);
          });
          break;
        case -6:
          $translate('ADD_TO_DB_FAILED').then(function(translation) {
            showAlertMessageError(translation, picture.pictureName);
          });
          break;
        case -1: //unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
        case -99: //internal server error
          $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
            showAlertMessageError(translation, "");
          });
          break;
        case -3: //json parse
          $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
            showAlertMessageError(translation, data.username);
          });
          break;
      }
    }).
    error(function(data, status, headers, config) {
      console.log(status);
    });
  };
  $scope.getFullPictureData = function(picture) {
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getFullPictureData/' + picture.pictureId).
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          window.open(data.pictureData);
          break;
        case -6:
          $translate('ADD_TO_DB_FAILED').then(function(translation) {
            showAlertMessageError(translation, picture.pictureName);
          });
          break;
        case -1: //unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
        case -99: //internal server error
          $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
            showAlertMessageError(translation, "");
          });
          break;
      }
    }).
    error(function(data, status, headers, config) {
      $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
        showAlertMessageError(translation, "");
      });
      console.log(status);
    });
  };
  $scope.setSelectedDescription = function(selectedDesription) {
    $scope.selectedDescription = selectedDesription;
  };

});
/* ******************************************** */
/* UserSettings Controller */
MedicalPictures.controller('UserSettingsController', function($scope, $http, $translate, $window, MedicalPicturesGlobal) {
  $scope.appName = MedicalPicturesGlobal.GLOBAL_APP_NAME;
  $scope.languages = ['pl', 'en'];
  $scope.maxPasswordLength = MedicalPicturesGlobal.MAX_PASSWORD_LENGTH;
  $scope.minPasswordLength = MedicalPicturesGlobal.MIN_PASSWORD_LENGTH;
  $scope.minAge = MedicalPicturesGlobal.MIN_AGE;
  $scope.maxAge = MedicalPicturesGlobal.MAX_AGE;
  $scope.loggedUser;
  document.getElementById("alertMessageDiv").style.visibility = "hidden";
  $scope.logoutUser = function() {
    $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/Logout').
    success(function(data, status, headers, config) {
      switch (data.errorCode) {
        case 0:
          $window.location.href = "/MedicalPictures/LoginView";
        case -1: //user unauthorized
          $window.location.href = "/MedicalPictures/UnauthorizedView";
          break;
        case -4: //not logged
          $window.location.href = "/MedicalPictures/LoginView";
          break;
      }
    }).
    error(function(data, status, headers, config) {
      console.log(status);
    });
  };
  $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getLoggedUser').
  success(function(data, status, headers, config) {
    switch (data.errorCode) {
      case 0:
        $scope.loggedUsername = data.username;
        $scope.chosenUserLanguage = data.applicationLanguage;
        switch (data.accountType) {
          case "ADMIN":
            $scope.mainViewSpecifiedByAccountType = "/MedicalPictures/AdminViewManageUsers";
            break;
          case "PATIENT":
            $scope.mainViewSpecifiedByAccountType = "/MedicalPictures/PatientViewManagePictures";
            break;
          case "TECHNICIAN":
            $scope.mainViewSpecifiedByAccountType = "/MedicalPictures/TechnicianViewManagePictures";
            break;
          case "DOCTOR":
            $scope.mainViewSpecifiedByAccountType = "/MedicalPictures/DoctorViewManageDescriptions";
            break;
        }
        $translate.use(data.applicationLanguage);
        $http.get('/MedicalPictures/webresources/MedicalPicturesCommon/getUserInfo/' + $scope.loggedUsername).
        success(function(data, status, headers, config) {
          switch (data.errorCode) {
            case 0:
              $scope.loggedUser = data;
              break;
            case -1: //unauthorized
              $window.location.href = "/MedicalPictures/UnauthorizedView";
              break;
            case -4: //not logged
              $window.location.href = "/MedicalPictures/LoginView";
              break;
            case -5: //user doesn't exist
              $translate('USER_DOESNT_EXISTS').then(function(translation) {
                showAlertMessageError(translation, $scope.loggedUsername);
              });
              break;
            case -3: //json parse
              $translate('INPUT_JSON_PARSE_PROBLEM').then(function(translation) {
                showAlertMessageError(translation, data.username);
              });
              break;
          }
        }).
        error(function(data, status, headers, config) {
          $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
            showAlertMessageError(translation, "");
          });
          console.log(status);
        });
        break;
    }
  }).
  error(function(data, status, headers, config) {
    $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
      showAlertMessageError(translation, "");
    });
    console.log(status);
  });
  $scope.changePasswordChanged = function() {
    if ($scope.loggedUser.changePassword === true) {
      $scope.changePasswordChangedFlag = true;
    } else {
      if ($scope.valuesChanged != true) {
        $scope.changePasswordChangedFlag = false;
      }
    }
  }
  $scope.saveSettingsClicked = function() {
    var userValuesToSave;
    if (!angular.isUndefined($scope.loggedUser.username) && !angular.isUndefined($scope.loggedUser.surname) && !angular.isUndefined($scope.loggedUser.name) && !angular.isUndefined($scope.loggedUser.age)) {
      if ($scope.loggedUser.changePassword === true && (($scope.loggedUser.password2 === $scope.loggedUser.password1) && $scope.loggedUser.password1 != undefined)) {
        userValuesToSave = {
          password: $scope.loggedUser.password1,
          username: $scope.loggedUser.username,
          age: $scope.loggedUser.age,
          name: $scope.loggedUser.name,
          surname: $scope.loggedUser.surname,
          chosenLanguage: $scope.chosenUserLanguage
        };
      } else { //if user didn't change password, it will be sent as empty value - must be checked on the server side
        userValuesToSave = {
          password: '',
          username: $scope.loggedUser.username,
          age: $scope.loggedUser.age,
          name: $scope.loggedUser.name,
          surname: $scope.loggedUser.surname,
          chosenLanguage: $scope.chosenUserLanguage
        };
      }
      $http({
        url: '/MedicalPictures/webresources/MedicalPicturesCommon/changeUserSettings',
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        data: userValuesToSave
      }).success(function(data, status, headers, config) {
        switch (data.errorCode) {
          case 0:
            $translate('USER_EDITED_SUCCESSFULLY').then(function(translation) {
              showAlertMessageSuccess(translation);
            });
            break;
          case -6:
            $translate('ADD_TO_DB_FAILED').then(function(translation) {
              showAlertMessageError(translation, picture.pictureName);
            });
            break;
          case -1: //unauthorized
            $window.location.href = "/MedicalPictures/UnauthorizedView";
            break;
          case -4: //not logged
            $window.location.href = "/MedicalPictures/LoginView";
            break;
          case -99: //internal server error
            $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
              showAlertMessageError(translation, "");
            });
            break;
        }
      }).
      error(function(data, status, headers, config) {
        $translate('INTERNAL_PROBLEM_OCCURRED').then(function(translation) {
          showAlertMessageError(translation, "");
        });
        console.log(status);
      });
    } else {
      $translate('SOME_OF_THE_VALUES_ARE_INCORRECT').then(function(translation) {
        showAlertMessageError(translation, "");
      });
    }
  }
});
MedicalPictures.controller('UnauthorizedController', function($scope, $translate, $window) {
  translate.use('en');
  $scope.goToLogin = function() {
    $window.location.href = "/MedicalPictures/LoginView";
  }

});
