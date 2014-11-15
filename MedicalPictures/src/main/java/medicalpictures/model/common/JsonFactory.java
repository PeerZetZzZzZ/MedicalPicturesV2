/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.common;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import javax.json.JsonException;
import javax.servlet.http.HttpServletRequest;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.JsonParsingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class is responsible for decrypting and reading the json object from Servlet
 * request.
 *
 * @author PeerZet
 */
@Stateless
public class JsonFactory {

    /**
     * Return the JSON read from request.
     *
     * @param request
     * @return JSON
     */
    public JSONObject decryptRequest(HttpServletRequest request) {
        StringBuilder jb = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
        } catch (Exception e) { /*report an error*/ }

        JSONObject jsonObject = new JSONObject(jb.toString());
        return jsonObject;
    }

    /**
     * Reads the user value which will be added to database from json.
     *
     * @param jsonUser JSON document with user details
     * @return Map with user values
     * @throws medicalpictures.model.exception.JsonParsingException
     * @throws JsonException When json is invalid.
     */
    public Map<String, String> readUserFromJson(JSONObject jsonUser) throws JsonParsingException {
        Map<String, String> userMap = new HashMap<>();
        try {
            userMap.put("username", jsonUser.getString("username"));
            String accountType = jsonUser.getString("accountType");
            if (accountType.equals("DOCTOR")) {
                userMap.put("specialization", jsonUser.getString("specialization"));
            }
            userMap.put("accountType", accountType);
            userMap.put("name", jsonUser.getString("name"));
            userMap.put("surname", jsonUser.getString("surname"));
            userMap.put("age", jsonUser.getString("age"));
            String resetPassword = jsonUser.getString("resetPassword");
            if (resetPassword != null) {//it's when we want to edit user values
                userMap.put("resetPassword", resetPassword);
            }
            return userMap;
        } catch (JSONException ex) {
            throw new JsonParsingException(ex.getMessage());
        }
    }

    /**
     * Returns the json which informs that there is no permission to see
     * content.
     *
     * @return message about not being permitted to see content
     */
    public String userNotPermitted() {
        return "{error:\"notPermitted\"}";
    }

    /**
     * Returns the json which informs that there is no permission to see
     * content.
     *
     * @return message about not being permitted to see content
     */
    public String notUserLogged() {
        return "{error:\"notUserLogged\"}";
    }

    /**
     * Returns the list of users to delete in such form
     * Map<username,accountType> for example there can be object such as:
     * <user@gmail.com,ADMIN>
     *
     * @param users
     * @return
     */
    public Map<String, String> readUsersToDelete(JSONObject users) {
        JSONArray usersToDelete = users.getJSONArray("usernames");
        Map<String, String> usersToDeleteList = new HashMap<>();
        for (int i = 0; i < usersToDelete.length(); i++) {
            JSONObject singleUserToDelete = (JSONObject) usersToDelete.get(i);
            usersToDeleteList.put(singleUserToDelete.getString("username"), singleUserToDelete.getString("accountType"));
        }
        return usersToDeleteList;
    }

    /**
     * Returns the user read from database with its all values as JSON
     * representation. It can be later sent to the client.
     *
     * @param username Username whos data will be taken
     * @return
     */
    public String getUserDetailsAsJson(Map<String, String> userDetials) {
        JSONObject user = new JSONObject();
        user.put("username", userDetials.get("username"));
        user.put("accountType", userDetials.get("accountType"));
        user.put("name", userDetials.get("name"));
        user.put("surname", userDetials.get("surname"));
        user.put("age", Integer.valueOf(userDetials.get("age")));
        if (userDetials.get("accountType").equals(AccountType.DOCTOR.toString())) {
            user.put("specialization", userDetials.get("specialization"));
        }
        return user.toString();
    }

    /**
     * Returns picture type taken from proper json.
     * @param pictureType JSON input
     * @return  picture type
     */
    public String getPictureType(JSONObject pictureType) {
        return pictureType.getString("pictureType");
    }

}
