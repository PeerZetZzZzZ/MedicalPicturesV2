package medicalpictures.model.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.orm.entity.User;
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
		return users.toString();
	}

	/**
	 * Returns the user read from database with its all values as JSON representation. It can be later sent to the client.
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
		return user.toString();
	}
}
