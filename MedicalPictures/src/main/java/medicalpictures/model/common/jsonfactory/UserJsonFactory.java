package medicalpictures.model.common.jsonfactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.JsonParsingException;
import medicalpictures.model.orm.entity.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Przemysław Thomann
 */
@Stateless
public class UserJsonFactory {

    /**
     * Gets usernames of all users.
     *
     * @param userList
     * @return
     */
    public String getAllUsernames(List<User> userList) {
        JSONObject users = new JSONObject();
        List<JSONObject> usersList = new ArrayList<>();
        for (User user : userList) {
            JSONObject singleUser = new JSONObject();
            singleUser.put("username", user.getUsername());
            singleUser.put("accountType", user.getAccountType());
            usersList.add(singleUser);
        }
        users.put("usernames", usersList);
        users.put("errorCode", ResultCodes.OPERATION_SUCCEED);
        return users.toString();
    }

    /**
     * Returns the user read from database with its all values as JSON
     * representation. It can be later sent to the client.
     *
     * @param userDetials
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
        user.put("errorCode", ResultCodes.OPERATION_SUCCEED);
        return user.toString();
    }

    /**
     * Decryptes request from the client and reads the user value which will be
     * added to database from json.
     *
     * @param userData
     * @return Map with user values
     * @throws medicalpictures.model.exception.JsonParsingException
     */
    public Map<String, String> readUserFromJson(String userData) throws JsonParsingException {
        JSONObject jsonUser = new JSONObject(userData);
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
            userMap.put("chosenLanguage", jsonUser.getString("chosenLanguage"));
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
     * Decryptes request from the client and returns the list of users to delete
     * in such form Map<username,accountType> for example there can be object
     * such as:
     * <user@gmail.com,ADMIN>
     *
     * @param usersToRemove
     * @return
     * @throws medicalpictures.model.exception.JsonParsingException
     */
    public Map<String, String> readUsersToDelete(String usersToRemove) throws JsonParsingException {
        try {
            JSONObject users = new JSONObject(usersToRemove);
            JSONArray usersToDelete = users.getJSONArray("usernames");
            Map<String, String> usersToDeleteList = new HashMap<>();
            for (int i = 0; i < usersToDelete.length(); i++) {
                JSONObject singleUserToDelete = (JSONObject) usersToDelete.get(i);
                usersToDeleteList.put(singleUserToDelete.getString("username"), singleUserToDelete.getString("accountType"));
            }
            return usersToDeleteList;
        } catch (JSONException ex) {
            throw new JsonParsingException(ex.getMessage());
        }
    }

    /**
     * Decrypts request and getting all info needed for user login.
     *
     * @param userToLoginDetails
     * @return
     * @throws medicalpictures.model.exception.JsonParsingException
     */
    public Map<String, String> getUserLoginDetails(String userToLoginDetails) throws JsonParsingException {
        try {
            Map<String, String> detailsMap = new HashMap<>();
            JSONObject loginDetails = new JSONObject(userToLoginDetails);
            detailsMap.put("username", loginDetails.getString("username"));
            detailsMap.put("password", loginDetails.getString("password"));
            return detailsMap;
        } catch (JSONException ex) {
            throw new JsonParsingException(ex.getMessage());
        }
    }

    /**
     * Returns the response to the client about currently logged user.
     *
     * @param loggedUserDetails
     * @return
     */
    public String getLoggedUser(Map<String, String> loggedUserDetails) {
        JSONObject user = new JSONObject();
        user.put("username", loggedUserDetails.get("username"));
        user.put("applicationLanguage", loggedUserDetails.get("applicationLanguage"));
        user.put("errorCode", ResultCodes.OPERATION_SUCCEED);
        return user.toString();
    }

    /**
     * Gives response to the client about successful login with values he needs.
     *
     * @param loggedUserDetails
     * @param result
     * @return
     */
    public String getUserLoginResponse(Map<String, String> loggedUserDetails, int result) {
        JSONObject resp = new JSONObject();
        resp.put("username", loggedUserDetails.get("username"));
        resp.put("accountType", loggedUserDetails.get("accountType"));
        resp.put("errorCode", result);
        return resp.toString();
    }
}
