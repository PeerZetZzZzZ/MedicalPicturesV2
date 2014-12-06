/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.technician;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import medicalpictures.model.orm.entity.Picture;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class is responsible for returning Technician operations responses
 *
 * @author PeerZet
 */
@Stateless
public class TechnicianOperationResponse {

    private static final Logger LOG = Logger.getLogger(TechnicianOperationResponse.class.getName());

    public String getAllPicturesResponse(List<Picture> pictures) {
        JSONObject picturesList = new JSONObject();
        JSONArray picturesArray = new JSONArray();
        for (Picture picture : pictures) {
            JSONObject singlePicture = new JSONObject();
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
        LOG.info("Return pictures list: " + picturesList.toString());
        return picturesList.toString();
    }
}
