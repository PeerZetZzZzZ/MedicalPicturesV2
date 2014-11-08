/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.security;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
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
public class UserSessionManager {

    private Subject currentUser;

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
     */
    public void registerUser(String username, String password) throws UserAlreadyLoggedException, UserDoesntExistException {
        currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            token.setRememberMe(true);
            try {
                currentUser.login(token);
            } catch (AuthenticationException ex) {
                throw new UserDoesntExistException(ex.getMessage());
            }
            Session session = currentUser.getSession();
            session.setAttribute("username", username);
        } else {
            String loggedUsername = currentUser.getSession().getAttribute("username").toString();
            throw new UserAlreadyLoggedException("User: " + loggedUsername + "is already logged!",loggedUsername);
        }

    }

}
