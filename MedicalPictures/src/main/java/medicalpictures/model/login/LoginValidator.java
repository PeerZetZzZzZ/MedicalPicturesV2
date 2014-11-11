/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.login;

import javax.ejb.Stateless;
import medicalpictures.model.enums.AccountType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.json.JSONObject;

/**
 * Validates the user if exists in UsersDB.
 *
 * @author PeerZet
 */
@Stateless
public class LoginValidator {

    /**
     * Returns the json which informs that login is successful and gives the type of account;
     *
     * @param username The username which logged.
     * @param userAccountType The account type of the user.
     * @return JSON which is sent as response from LoginView POST.
     */
    public String loginSucceed(final String username,final String userAccountType) {
        JSONObject user = new JSONObject();
        user.put("username", username);
        user.put("status", "true");
        user.put("accountType", userAccountType);
        return user.toString();
    }

    /**
     * Returns the json which informs that login is not successful because user
     * is already logged somewhere outside!
     *
     * @param username
     * @return JSON which is sent as response from LoginView POST.
     */
    public String loginFailedUserAlreadyLoggedOutside(final String username) {
        JSONObject user = new JSONObject();
        user.put("username", username);
        user.put("status", "false");
        user.put("reason", "alreadyLoggedOutside");
        return user.toString();
    }
    /**
     * Returns the json which informs that login is not successful because user
     * is already logged locally here!
     *
     * @param username
     * @return JSON which is sent as response from LoginView POST.
     */
    public String loginFailedUserAlreadyLoggedLocally(final String username, final String accountType) {
        JSONObject user = new JSONObject();
        user.put("username", username);
        user.put("status", "false");
        user.put("reason", "alreadyLoggedLocally");
        user.put("accountType", accountType);
        return user.toString();
    }

    /**
     * Returns the json which informs that login is not successful because given
     * user doesn't exist.
     *
     * @param username
     * @return JSON which is sent as response from LoginView POST.
     */
    public String loginFailedAuthenticationFailed(final String username) {
        JSONObject user = new JSONObject();
        user.put("username", username);
        user.put("status", "false");
        user.put("reason", "authenticationFailed");
        return user.toString();
    }
    
    /**
     * Checks the role of the user based on all account_types ( ENUM )
     * @param username
     * @return The account_type as String
     */
    public String getUserAccountType(final String username) {
        Subject currentUser = SecurityUtils.getSubject();
        for (AccountType type : AccountType.values()) {
            if (currentUser.hasRole(type.toString())) {
                currentUser.getSession().setAttribute("accountType", type.toString());//we set this value in Session
                return type.toString();
            }
        }
        return "";
    }

}
