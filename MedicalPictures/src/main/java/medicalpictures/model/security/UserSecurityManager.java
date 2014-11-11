/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.security;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserAlreadyLoggedException;
import medicalpictures.model.exception.UserDoesntExistException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.mgt.SecurityManager;

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
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            try {
                Session session = currentUser.getSession();
                session.setAttribute("username", username);
                currentUser.login(token);
            } catch (AuthenticationException ex) {
                throw new UserDoesntExistException(ex.getMessage());
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
     */
    public void checkUserPermissionToThisContent(final AccountType requriedRole) {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.checkRole(requriedRole.toString());
    }

    /**
     * Checks if user has content to any content.
     *
     * @return True - if has, false - if not.
     */
    public Boolean checkUserPermissionToAnyContent() {
        Subject currentUser = SecurityUtils.getSubject();
        for (AccountType accountType : AccountType.values()) {
            if (currentUser.hasRole(accountType.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Logouts the logged user.
     */
    public void logoutUser() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
    }

    /**
     * Gets the logged user username
     *
     * @return username
     */
    public String getLoggedUsername() throws NoLoggedUserExistsHere {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            return currentUser.getSession().getAttribute("username").toString();
        } catch (IllegalStateException ex) {
            throw new NoLoggedUserExistsHere("No logged user here!");
        }
    }
}
