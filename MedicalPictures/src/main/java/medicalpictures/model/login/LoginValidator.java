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
     * @param username
     * @return JSON which is sent as response from LoginView POST.
     */
    public String loginSucceed(String username) {
        JSONObject user = new JSONObject();
        user.put("username", "user");
        user.put("status", "true");
        return user.toString();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
