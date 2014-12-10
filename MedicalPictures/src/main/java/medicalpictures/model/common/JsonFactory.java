package medicalpictures.model.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.JsonException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.JsonParsingException;
import medicalpictures.model.orm.entity.Picture;
import medicalpictures.model.orm.entity.PictureDescription;
import medicalpictures.model.orm.entity.Technician;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class is responsible for decrypting and reading the json object from Servlet request. It returns String already in JSON notation, which client can read as well as can read data from JSON and return
 * in any desired format.
 *
 * @author PeerZet
 */
@Stateless
public class JsonFactory {

	@EJB
	private MedicalLogger logger;
	/**
	 * Return the JSON read from request.
	 *
	 * @param request
	 * @return JSON
	 */
	private static final Logger LOG = Logger.getLogger(JsonFactory.class.getName());

	public JSONObject decryptRequest(HttpServletRequest request) {
		StringBuilder jb = new StringBuilder();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}
		} catch (Exception e) { /*report an error*/ }
		System.out.println(jb.toString());
		JSONObject jsonObject = new JSONObject(jb.toString());
		return jsonObject;
	}

	/**
	 * Decrypts request from input string
	 *
	 * @param request json as string
	 * @return json as JSONObject
	 */
	public JSONObject decryptRequest(String request) {
		return new JSONObject(request);
	}


	/**
	 * Returns the json which informs that there is no permission to see content.
	 *
	 * @return message about not being permitted to see content
	 */
	public String userNotPermitted() {
		JSONObject response = new JSONObject();
		response.put("errorCode", ResultCodes.USER_UNAOTHRIZED);
		return response.toString();
	}

	/**
	 * Returns the json which informs that there is no permission to see content.
	 *
	 * @return message about not being permitted to see content
	 */
	public String notUserLogged() {
		JSONObject response = new JSONObject();
		response.put("errorCode", ResultCodes.USER_IS_NOT_LOGGED);
		return response.toString();
	}

	/**
	 * Returns the list of users to delete in such form Map<username,accountType> for example there can be object such as:
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
	 * Returns picture type taken from proper json.
	 *
	 * @param pictureType JSON input
	 * @return picture type
	 */
	public String getPictureType(JSONObject pictureType) {
		return pictureType.getString("pictureType");
	}

	public String getBodyPart(JSONObject bodyPart) {
		return bodyPart.getString("bodyPart");
	}

	/**
	 * Gets the picture values ( but no data ) which will be added to db
	 *
	 * @param pictureDetails JSONObject of details
	 * @return map with picture details
	 */
	public Map<String, String> getAddPictureValues(JSONObject pictureDetails) {
		Map<String, String> pictureValues = new HashMap<>();
		pictureValues.put("patient", pictureDetails.getString("patient"));
		String patient = pictureDetails.getString("patient");
		String patientUsername = patient.substring(0, patient.indexOf(":") - 1);//we have email:name username so we want only email
		pictureValues.put("patientUsername", patientUsername);
		pictureValues.put("pictureName", pictureDetails.getString("pictureName"));
		pictureValues.put("bodyPart", pictureDetails.getString("bodyPart"));
		pictureValues.put("pictureType", pictureDetails.getString("pictureType"));
		return pictureValues;
	}

	/**
	 * Gets the picture values ( but no data ) when technician edit picture
	 *
	 * @param details JSONObject of details
	 * @return map with picture details
	 */
	public Map<String, String> getEditPictureValues(String details) {
		JSONObject pictureDetails = new JSONObject();
		Map<String, String> pictureValues = new HashMap<>();
		pictureValues.put("pictureId", pictureDetails.getString("pictureId"));
		pictureValues.put("bodyPart", pictureDetails.getString("bodyPart"));
		pictureValues.put("pictureType", pictureDetails.getString("pictureType"));
		return pictureValues;
	}

	/**
	 * Returns the information that the inserting/updating item in db failed
	 *
	 * @return
	 */
	public String insertToDbFailed() {
		JSONObject json = new JSONObject();
		json.put("error", "insertToDbFailed");
		return json.toString();
	}

	public String internalServerProblemResponse() {
		JSONObject json = new JSONObject();
		json.put("errorCode", ResultCodes.INTERNAL_SERVER_ERROR);
		return json.toString();
	}

	public String getOperationResponseByCode(int result) {
		JSONObject response = new JSONObject();
		response.put("errorCode", result);
		logger.logInfo("Sending response to the client: " + response.toString(), JsonFactory.class);
		return response.toString();

	}

	/**
	 * Returns the response to the client about currently logged user.
	 *
	 * @param username
	 * @return
	 */
	public String getLoggedUser(String username) {
		JSONObject user = new JSONObject();
		user.put("username", username);
		return user.toString();
	}
}
