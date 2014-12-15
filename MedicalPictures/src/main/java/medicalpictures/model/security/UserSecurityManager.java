package medicalpictures.model.security;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.common.PasswordGenerator;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.dao.UserDAO;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.enums.ContentPermissions;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.orm.entity.User;
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

/**
 * The class is responsible for validating user and logging him to application.
 * It uses Apache Shiro as a SecurityProvider and validate user username and
 * password with data in UsersDB table.
 *
 * @author Przemysław Thomann
 */
@Stateful
public class UserSecurityManager {

    @EJB
    private MedicalLogger logger;

    @EJB
    private PasswordGenerator passwordGeneartor;

    @EJB
    private UserDAO userDAO;

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
     * @param userDetails
     * @return
     */
    public int loginUser(Map<String, String> userDetails) {
        String username = userDetails.get("username");
        String password = userDetails.get("password");
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.getSession().getId();
        if (currentUser.isAuthenticated()) {
            currentUser.logout();
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username, passwordGeneartor.getPasswordHash(password));
        try {
            Session session = currentUser.getSession();
            session.setAttribute("username", username);
            currentUser.login(token);
            User loggedUser = userDAO.findUser(username);
            if (loggedUser != null) {
                session.setAttribute("applicationLanguage", loggedUser.getChosenLanguage());
                session.setAttribute("accountType", loggedUser.getAccountType());
            }
            logger.logInfo("Successful login for username '" + username + "'!!", UserSecurityManager.class);
            return ResultCodes.OPERATION_SUCCEED;
        } catch (AuthenticationException ex) {
            logger.logError("Login failed for username '" + username + "'.Authentication failed!", UserSecurityManager.class, ex);
            return ResultCodes.USER_UNAOTHRIZED;
        }
    }

    /**
     * Checks if user has role requried role for this content. If not,
     * page/content won't be showed for this user.
     *
     * @param requriedRole Requried role to show the content.
     * @param requriedRoles
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
     * @return username Logged user
     * @throws medicalpictures.model.exception.NoLoggedUserExistsHere
     */
    public Map<String, String> getLoggedUsername() throws NoLoggedUserExistsHere {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            String username = currentUser.getSession().getAttribute("username").toString();
            String applicationLanguage = currentUser.getSession().getAttribute("applicationLanguage").toString();
            String accountType = currentUser.getSession().getAttribute("accountType").toString();
            Map<String, String> userDetails = new HashMap<>();
            userDetails.put("username", username);
            userDetails.put("applicationLanguage", applicationLanguage);
            userDetails.put("accountType", accountType);
            if (username == null) {
                throw new NoLoggedUserExistsHere("No logged user here");
            } else {
                return userDetails;
            }
        } catch (IllegalStateException ex) {
            throw new NoLoggedUserExistsHere("No logged user here!");
        }
    }
}
