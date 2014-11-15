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
        return response.toString();
    }

    /**
     * Creates the response to the client that picture type has been added.
     *
     * @param pictureType picture type which was added or tried to be added
     * @return response as String
     */
    public String pictureTypeAddedSuccessfully(String pictureType) {
        JSONObject response = new JSONObject();
        response.put("pictureType", pictureType);
        response.put("result", "success");
        return response.toString();
    }

    /**
     * Creates the response that picture type hasn't been created successfully.
     *
     * @param pictureType picture type which was added or tried to be added
     * @return response as String
     */
    public String pictureTypeAddedFailed(String pictureType) {
        JSONObject response = new JSONObject();
        response.put("pictureType", pictureType);
        response.put("result", "failed");
        return response.toString();
    }

    /**
     * Creates the response that body part has been created successfully.
     *
     * @param bodyPartString body part which should be added
     * @return response to the client as String
     */
    public String bodyPartAddedSuccessfully(String bodyPartString) {
        JSONObject response = new JSONObject();
        response.put("bodyPart", bodyPartString);
        response.put("result", "success");
        return response.toString();
    }

    /**
     * Creates the response that body part hasn't been created successfully.
     *
     * @param bodyPartString body part which should be added
     * @return response to the client as String
     */
    public String bodyPartAddedFailed(String bodyPartString) {
        JSONObject response = new JSONObject();
        response.put("bodyPart", bodyPartString);
        response.put("result", "failed");
        return response.toString();
    }
}
