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
 * Class is responsible for decrypting and reading the json object from Servlet
 * request. It returns String already in JSON notation, which client can read as
 * well as can read data from JSON and return in any desired format.
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

<<<<<<< HEAD

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
=======
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
>>>>>>> e3d32246ecb93d5cfcf0402a2411a5870135dc07

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

<<<<<<< HEAD
	/**
	 * Returns picture type taken from proper json.
	 *
	 * @param pictureType JSON input
	 * @return picture type
	 */
	public String getPictureType(JSONObject pictureType) {
		return pictureType.getString("pictureType");
	}
=======
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
>>>>>>> e3d32246ecb93d5cfcf0402a2411a5870135dc07

    /**
     * Gets the picture values ( but no data ) when technician edit picture
     *
     * @param pictureDetails JSONObject of details
     * @return map with picture details
     */
    public Map<String, String> getEditPictureValues(JSONObject pictureDetails) {
        Map<String, String> pictureValues = new HashMap<>();
        pictureValues.put("pictureId", pictureDetails.getString("pictureId"));
        pictureValues.put("bodyPart", pictureDetails.getString("bodyPart"));
        pictureValues.put("pictureType", pictureDetails.getString("pictureType"));
        return pictureValues;
    }

    public List<String> getPicturesToDeleteList(String pictures) {
        JSONObject picturesJson = new JSONObject(pictures);
        JSONArray picturesArray = picturesJson.getJSONArray("pictures");
        List<String> picturesIdList = new ArrayList<>();
        for (int i = 0; i < picturesArray.length(); i++) {
            JSONObject pic = picturesArray.getJSONObject(i);
            picturesIdList.add(pic.getString("pictureId"));
        }
        return picturesIdList;
    }

<<<<<<< HEAD
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
=======
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

    public String getPicturesNames(Set<Picture> patientPictures) {
        JSONObject pictures = new JSONObject();
        JSONArray picturesArray = new JSONArray();
        for (Picture picture : patientPictures) {
            JSONObject singlePicture = new JSONObject();
            singlePicture.put("pictureName", picture.getPictureName());
            singlePicture.put("pictureId", picture.getId());
            picturesArray.put(singlePicture);
        }
        pictures.put("pictures", picturesArray);
        return pictures.toString();
    }

    public String getPictureDetails(Picture picture) throws IOException {
        JSONObject pictureJson = new JSONObject();
        if (picture != null) {
            pictureJson.put("pictureId", picture.getId());
            pictureJson.put("bodyPart", picture.getBodyPart().getBodyPart());
            pictureJson.put("pictureType", picture.getPictureType().getPictureType());
            pictureJson.put("patientAge", picture.getPatient().getAge());
            pictureJson.put("patientName", picture.getPatient().getName());
            pictureJson.put("patientSurname", picture.getPatient().getSurname());
            pictureJson.put("patientUsername", picture.getPatient().getUser().getUsername());
            pictureJson.put("pictureName", picture.getPictureName());
            Technician technician = picture.getTechnician();
            if (technician != null) {
                pictureJson.put("technicianName", picture.getTechnician().getName());
                pictureJson.put("technicianSurname", picture.getTechnician().getSurname());
                pictureJson.put("technicianUsername", picture.getTechnician().getUser().getUsername());
            } else {//if technician was maybe deleted
                pictureJson.put("technicianName", "no data");
                pictureJson.put("technicianSurname", "no data");
                pictureJson.put("technicianUsername", "no data");
            }
            pictureJson.put("captureTimestamp", picture.getCaptureTimestamp());
            ByteArrayInputStream is = new ByteArrayInputStream(picture.getThumbnailData());
            String imageString = "data:image/png;base64,"
                    + DatatypeConverter.printBase64Binary(IOUtils.toByteArray(is));
            pictureJson.put("thumbnailData", imageString);
            /* Now we easily looking for doctor who already checks patient, and we will get just his descriptions */
            String loggedUsername = (String) SecurityUtils.getSubject().getSession().getAttribute("username");
            boolean pictureHasDescription = false;
            for (PictureDescription pictureDescription : picture.getPictureDescriptions()) {
                if (pictureDescription.getDoctor().getUser().getUsername().equals(loggedUsername)) {
                    String pictureDesc = pictureDescription.getDescription();
                    if (pictureDesc == null || pictureDesc.equals("")) {
                        pictureDesc = pictureDescription.getDefinedPictureDescription().getPictureDescription();
                        pictureJson.put("definedPictureDescriptionId", pictureDescription.getDefinedPictureDescription().getId());
                    }
                    pictureJson.put("pictureDescription", pictureDesc);
                    pictureJson.put("pictureDescriptionId", pictureDescription.getId());
                    pictureHasDescription = true;
                    break;
                }
            }
            if (!pictureHasDescription) {
                pictureJson.put("pictureDescription", "");//if picture doesn't have description yet made by this doctor
                pictureJson.put("pictureDescriptionId", "");//if picture doesn't have description yet made by this doctor
                //we want to send empty description
            }
            return pictureJson.toString();
        } else {
            return insertToDbFailed();
        }

    }

    /**
     * Returns the answer to the client that no object found in db.
     *
     * @return
     */
    public String notObjectFound() {
        JSONObject json = new JSONObject();
        json.put("errorCode", ResultCodes.OBJECT_DOESNT_EXIST);
        return json.toString();
    }

    public String getFullPictureData(Picture picture) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(picture.getPictureData());
        String imageString = "data:image/png;base64,"
                + DatatypeConverter.printBase64Binary(IOUtils.toByteArray(is));
        JSONObject json = new JSONObject();
        json.put("pictureData", imageString);
        LOG.info("Successfully retreived full picture data for picture: '" + picture.getId() + "'");
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
>>>>>>> e3d32246ecb93d5cfcf0402a2411a5870135dc07

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
