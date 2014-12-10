package medicalpictures.model.common;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import medicalpictures.model.dao.ManagerDAO;
import medicalpictures.model.orm.entity.BodyPart;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * JSON encrypter/decrypter for BodyParts operations.
 *
 * @author Przemysław Thomann
 */
@Stateless
public class BodyPartJsonFactory {

    @EJB
    private ManagerDAO managerDAO;

    /**
     * Returns the list of body parts.
     *
     * @param bodyParts
     * @return
     */
    public String getBodyParts(List<BodyPart> bodyParts) {
        JSONObject bodyPartsJson = new JSONObject();
        JSONArray bodyPartsArray = new JSONArray();
        for (BodyPart bodyPart : bodyParts) {
            bodyPartsArray.put(bodyPart.getBodyPart());
        }
        bodyPartsJson.put("bodyParts", bodyPartsArray);
        return bodyPartsJson.toString();
    }
}
