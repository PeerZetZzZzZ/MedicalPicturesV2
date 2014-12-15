package medicalpictures.model.common.jsonfactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.DatatypeConverter;
import medicalpictures.model.common.ResultCodes;
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
 *
 * @author Przemysław Thomann
 */
@Stateless
public class PictureJsonFactory {

    /**
     * Returns all picture response.
     *
     * @param pictures
     * @return
     */
    public String getAllPicturesResponse(List<Picture> pictures) {
        JSONObject picturesList = new JSONObject();
        JSONArray picturesArray = new JSONArray();
        for (Picture picture : pictures) {
            JSONObject singlePicture = new JSONObject();
            singlePicture.put("pictureId", picture.getId());
            singlePicture.put("pictureName", picture.getPictureName());
            singlePicture.put("bodyPart", picture.getBodyPart().getBodyPart());
            singlePicture.put("pictureType", picture.getPictureType().getPictureType());
            singlePicture.put("captureTimestamp", picture.getCaptureTimestamp());
            singlePicture.put("patientName", picture.getPatient().getName());
            singlePicture.put("patientSurname", picture.getPatient().getSurname());
            singlePicture.put("patientUsername", picture.getPatient().getUser().getUsername());
            singlePicture.put("technicianName", picture.getTechnician().getName());
            singlePicture.put("technicianSurname", picture.getTechnician().getSurname());
            singlePicture.put("technicianUsername", picture.getTechnician().getUser().getUsername());
            picturesArray.put(singlePicture);
        }
        picturesList.put("pictures", picturesArray);
        picturesList.put("errorCode", ResultCodes.OPERATION_SUCCEED);
        return picturesList.toString();
    }

    /**
     * Decrypts the request from client and returns the list of pictures to
     * delete.
     *
     * @param pictures
     * @return
     * @throws medicalpictures.model.exception.JsonParsingException
     */
    public List<String> getPicturesToDeleteList(String pictures) throws JsonParsingException {
        try {
            JSONObject picturesJson = new JSONObject(pictures);
            JSONArray picturesArray = picturesJson.getJSONArray("pictures");
            List<String> picturesIdList = new ArrayList<>();
            for (int i = 0; i < picturesArray.length(); i++) {
                JSONObject pic = picturesArray.getJSONObject(i);
                picturesIdList.add(pic.getString("pictureId"));
            }
            return picturesIdList;
        } catch (JSONException ex) {
            throw new JsonParsingException(ex.getMessage());
        }
    }

    /**
     * Returns all pictures of the patient to the client.
     *
     * @param patientPictures
     * @return
     */
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
        pictures.put("errorCode", ResultCodes.OPERATION_SUCCEED);
        return pictures.toString();
    }

    /**
     * Returns the response to the client with picture details also with
     * thumbnail data.
     *
     * @param picture
     * @return
     * @throws IOException
     */
    public String getPictureDetailsWithThumbnail(Picture picture) throws IOException {
        JSONObject pictureJson = new JSONObject();
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
        pictureJson.put("errorCode", ResultCodes.OPERATION_SUCCEED);
        return pictureJson.toString();
    }

    /**
     * Getting the response to the client with full picture data.
     *
     * @param picture
     * @return
     * @throws IOException
     */
    public String getFullPictureData(Picture picture) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(picture.getPictureData());
        String imageString = "data:image/png;base64,"
                + DatatypeConverter.printBase64Binary(IOUtils.toByteArray(is));
        JSONObject json = new JSONObject();
        json.put("pictureData", imageString);
        json.put("errorCode", ResultCodes.OPERATION_SUCCEED);
        return json.toString();
    }

    /**
     * Decrypts the request from client with all details needed to adding the
     * picture description.
     *
     * @param pictureDescriptionDetails
     * @return
     * @throws medicalpictures.model.exception.JsonParsingException
     */
    public Map<String, String> getSavePictureDescription(String pictureDescriptionDetails) throws JsonParsingException {
        try {
            Map<String, String> descMap = new HashMap<>();
            JSONObject pictureDescriptionJson = new JSONObject(pictureDescriptionDetails);
            descMap.put("pictureId", pictureDescriptionJson.getString("pictureId"));
            descMap.put("pictureDescriptionId", pictureDescriptionJson.getString("pictureDescriptionId"));
            descMap.put("pictureDescription", pictureDescriptionJson.getString("pictureDescription"));
            descMap.put("definedPictureDescriptionId", pictureDescriptionJson.getString("definedPictureDescriptionId"));
            return descMap;
        } catch (JSONException ex) {
            throw new JsonParsingException(ex.getMessage());
        }
    }

    /**
     * Decryptes request from the client and gets the picture values ( but no
     * data ) which will be added to db
     *
     * @param pictureDetails JSONObject of details
     * @return map with picture details
     * @throws medicalpictures.model.exception.JsonParsingException
     */
    public Map<String, String> getAddPictureValues(JSONObject pictureDetails) throws JsonParsingException {
        try {
            Map<String, String> pictureValues = new HashMap<>();
            pictureValues.put("patient", pictureDetails.getString("patient"));
            String patient = pictureDetails.getString("patient");
            String patientUsername = patient.substring(0, patient.indexOf(":") - 1);//we have email:name username so we want only email
            pictureValues.put("patientUsername", patientUsername);
            pictureValues.put("pictureName", pictureDetails.getString("pictureName"));
            pictureValues.put("bodyPart", pictureDetails.getString("bodyPart"));
            pictureValues.put("pictureType", pictureDetails.getString("pictureType"));
            return pictureValues;
        } catch (JSONException ex) {
            throw new JsonParsingException(ex.getMessage());
        }
    }

    /**
     * Decryptes request from the client and gets the picture values ( but no
     * data ) when technician edit picture
     *
     * @param details JSONObject of details
     * @return map with picture details
     * @throws medicalpictures.model.exception.JsonParsingException
     */
    public Map<String, String> getEditPictureValues(String details) throws JsonParsingException {
        try {
            JSONObject pictureDetails = new JSONObject(details);
            Map<String, String> pictureValues = new HashMap<>();
            pictureValues.put("pictureId", pictureDetails.getString("pictureId"));
            pictureValues.put("bodyPart", pictureDetails.getString("bodyPart"));
            pictureValues.put("pictureType", pictureDetails.getString("pictureType"));
            return pictureValues;
        } catch (JSONException ex) {
            throw new JsonParsingException(ex.getMessage());
        }
    }
}
