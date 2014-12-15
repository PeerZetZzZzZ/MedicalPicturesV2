package medicalpictures.model.common.jsonfactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.orm.entity.Picture;
import medicalpictures.model.orm.entity.PictureDescription;
import org.apache.shiro.SecurityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Przemysław Thomann
 */
@Stateless
public class PictureDescriptionJsonFactory {

    @EJB
    private MedicalLogger logger;

    @EJB
    private JsonFactory jsonFactory;

    /**
     * Returns the descriptions of the patient picture as response to the
     * client.
     *
     * @param picture
     * @return
     */
    public String getPatientPictureDescriptions(Picture picture) {
        String patientUsername = (String) SecurityUtils.getSubject().getSession().getAttribute("username");
        if (picture.getPatient().getUser().getUsername().equals(patientUsername)) {
            JSONObject pictureDescriptions = new JSONObject();
            JSONArray descriptionsArray = new JSONArray();
            for (PictureDescription description : picture.getPictureDescriptions()) {
                JSONObject desc = new JSONObject();
                desc.put("doctor", description.getDoctor().getName() + " " + description.getDoctor().getSurname());
                desc.put("doctorSpecialization", description.getDoctor().getSpecialization());
                if (description.getDefinedPictureDescription() != null) {
                    desc.put("pictureDescription", description.getDefinedPictureDescription().getPictureDescription());
                } else {
                    desc.put("pictureDescription", description.getDescription());
                }
                descriptionsArray.put(desc);
            }
            pictureDescriptions.put("pictureDescriptions", descriptionsArray);
            pictureDescriptions.put("errorCode", ResultCodes.OPERATION_SUCCEED);
            return pictureDescriptions.toString();
        } else {
            logger.logWarning("User with username '" + patientUsername + "' is not owner of the picture with id '" + picture.getId() + "'",
                    PictureDescriptionJsonFactory.class);
            return jsonFactory.getOperationResponseByCode(ResultCodes.USER_AUTHENTICATION_FAILED);
        }
    }
}
