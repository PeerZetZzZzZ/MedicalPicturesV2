/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.enums.ContentPermissions;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserAlreadyLoggedException;
import medicalpictures.model.exception.UserDoesntExistException;
import medicalpictures.model.exception.UserNotPermitted;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.InvalidSessionException;
import org.json.JSONObject;

/**
 * The class is responsible for validating user and logging him to application.
 * It uses Apache Shiro as a SecurityProvider and validate user username and
 * password with data in UsersDB table.
 *
 * @author Przemysław Thomann
 */
@Stateful
public class UserSecurityManager {

    /**
     * Initializes the SecurityManager to use shiro:ini.
     */
    @PostConstruct
    public void initSecurityManager() {
        Factory factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = (SecurityManager) factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
    }

    /**
     * Method registers the user = log the user to Application and handles it's
     * connection.
     *
     * @param username Username defined in UsersDB
     * @param password Password for username defined in UsersDB
     * @throws medicalpictures.model.exception.UserAlreadyLoggedException When
     * user is already logged in the system.
     * @throws medicalpictures.model.exception.UserDoesntExistException When
     * given credentials don't match any user.
     */
    public void registerUser(String username, String password) throws UserAlreadyLoggedException, UserDoesntExistException {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.getSession().getId();
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            try {
                PrintWriter sessionFile = new PrintWriter(username, "UTF-8");
                sessionFile.close();
                Session session = currentUser.getSession();
                session.setAttribute("username", username);
                currentUser.login(token);
            } catch (AuthenticationException ex) {
                throw new UserDoesntExistException(ex.getMessage());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UserSecurityManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(UserSecurityManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            String loggedUsername = currentUser.getSession().getAttribute("username").toString();
            throw new UserAlreadyLoggedException("User: " + loggedUsername + "is already logged!", loggedUsername);
        }
    }

    /**
     * Checks if user has role requried role for this content. If not,
     * page/content won't be showed for this user.
     *
     * @param requriedRole Requried role to show the content.
     * @throws medicalpictures.model.exception.NoLoggedUserExistsHere When there
     * is no logged user.
     * @throws medicalpictures.model.exception.UserNotPermitted When user is not
     * permitted to see this content.
     */
    public void checkUserPermissionToThisContent(final AccountType requriedRole, AccountType... requriedRoles) throws UserNotPermitted, NoLoggedUserExistsHere {
        String username = "";
        ContentPermissions userPermissionToTheContent = ContentPermissions.USER_NOT_ALLOWED;
        userPermissionToTheContent = checkUserPermissionToTheSingleContent(requriedRole);
        if (userPermissionToTheContent == ContentPermissions.USER_NOT_ALLOWED) {
            for (AccountType role : requriedRoles) {
                if (checkUserPermissionToTheSingleContent(role) == ContentPermissions.USER_ALLOWED) {//if user has any of other roles
                    userPermissionToTheContent = ContentPermissions.USER_ALLOWED;
                    break;
                }
            }
        }
        if (userPermissionToTheContent == ContentPermissions.USER_ISNT_LOGGED) {
            throw new NoLoggedUserExistsHere("There is no logged user! Can't check specified user permission.");
        } else if (userPermissionToTheContent == ContentPermissions.USER_NOT_ALLOWED) {
            throw new UserNotPermitted(username + ": is not permitted to see this content. Requried accountType: " + requriedRole);
        }
    }

    private ContentPermissions checkUserPermissionToTheSingleContent(final AccountType requriedRole) {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.checkRole(requriedRole.toString());
            return ContentPermissions.USER_ALLOWED;
        } catch (IllegalStateException | InvalidSessionException ex) {
            System.out.println(ex.getMessage());
            return ContentPermissions.USER_ISNT_LOGGED;
        } catch (AuthorizationException | NullPointerException ex) {
            System.out.println(ex.getMessage());
            return ContentPermissions.USER_NOT_ALLOWED;
        }
    }

    /**
     * Checks if user has content to any content.
     *
     * @return True - if has, false - if not.
     * @throws medicalpictures.model.exception.NoLoggedUserExistsHere When there
     * is no logged user.
     * @throws medicalpictures.model.exception.UserNotPermitted When user is not
     * permitted to see this content.
     */
    public Boolean checkUserPermissionToAnyContent() throws NoLoggedUserExistsHere, UserNotPermitted {
        String username = "";
        try {
            Subject currentUser = SecurityUtils.getSubject();
            username = currentUser.getSession().getAttribute("username").toString();
            for (AccountType accountType : AccountType.values()) {
                if (currentUser.hasRole(accountType.toString())) {
                    return true;
                }
            }
        } catch (IllegalStateException | InvalidSessionException ex) {
            System.out.println(ex.getMessage());
            throw new UserNotPermitted(username + ": is not permitted to see any content.");
        } catch (AuthorizationException | NullPointerException ex) {
            throw new NoLoggedUserExistsHere("There is no logged user! Can't check specified user permission.");
        }
        return false;
    }

    /**
     * Logouts the logged user.
     *
     * @throws medicalpictures.model.exception.NoLoggedUserExistsHere
     */
    public void logoutUser() throws NoLoggedUserExistsHere {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.logout();
        } catch (IllegalStateException ex) {
            throw new NoLoggedUserExistsHere("There is no logged user! Can't logout");
        }
    }

    /**
     * Gets the logged user username
     *
     * @return username
     * @throws medicalpictures.model.exception.NoLoggedUserExistsHere
     */
    public JSONObject getLoggedUsername() throws NoLoggedUserExistsHere {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            String username = currentUser.getSession().getAttribute("username").toString();
            JSONObject user = new JSONObject();
            user.put("username", username);
            return user;

        } catch (IllegalStateException ex) {
            throw new NoLoggedUserExistsHere("No logged user here!");
        }
    }
}
