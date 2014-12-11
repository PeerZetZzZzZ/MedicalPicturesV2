package medicalpictures.model.common.jsonfactory;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import medicalpictures.model.common.ResultCodes;
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
		bodyPartsJson.put("errorCode", ResultCodes.OPERATION_SUCCEED);
		return bodyPartsJson.toString();
	}

	/**
	 * Gets the body part to add from client request
	 *
	 * @param bodyPart
	 * @return
	 */
	public String getBodyPart(String bodyPart) {
		JSONObject bodyPartJson = new JSONObject(bodyPart);
		return bodyPartJson.getString("bodyPart");
	}
}
