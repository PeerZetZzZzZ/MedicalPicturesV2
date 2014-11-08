/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.login;

import javax.ejb.Stateless;
import org.json.JSONObject;

/**
 * Validates the user if exists in UsersDB.
 *
 * @author PeerZet
 */
@Stateless
public class LoginValidator {

    /**
     * Returns the json which informs that login is successful.
     *
     * @param username
     * @return JSON which is sent as response from LoginView POST.
     */
    public String loginSucceed(String username) {
        JSONObject user = new JSONObject();
        user.put("username", username);
        user.put("status", "true");
        return user.toString();
    }

    /**
     * Returns the json which informs that login is not successful.
     *
     * @param username
     * @return JSON which is sent as response from LoginView POST.
     */
    public String loginFailedUserAlreadyLogged(final String username) {
        JSONObject user = new JSONObject();
        user.put("username", username);
        user.put("status", "false");
        user.put("reason", "alreadyLogged");
        return user.toString();
    }

    public String loginFailedAuthenticationFailed(final String username) {
        JSONObject user = new JSONObject();
        user.put("username", username);
        user.put("status", "false");
        user.put("reason", "authenticationFailed");
        return user.toString();
    }

}
