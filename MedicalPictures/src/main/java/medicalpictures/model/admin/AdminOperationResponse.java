/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.admin;

import javax.ejb.Stateless;
import org.json.JSONObject;

/**
 * Responsible for responsing to the client for every operation which Admin
 * does.
 *
 * @author PeerZet
 */
@Stateless
public class AdminOperationResponse {

    /**
     * Prepares the response to the client, that user has been added
     * successfully.
     *
     * @param username Username which has been added.
     */
    public String userAddedSuccessfully(String username) {
        JSONObject response = new JSONObject();
        response.put("username", username);
        response.put("result", "success");
        return response.toString();
    }

    /**
     * Prepares the response to the client, that user has not been added.
     *
     * @param username Username which has been added.
     */
    public String userAddedFailed(String username) {
        JSONObject response = new JSONObject();
        response.put("username", username);
        response.put("result", "failed");
        System.out.println("response: " + response.toString());
        return response.toString();
    }

}
