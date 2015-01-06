package medicalpictures.model.common.jsonfactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.exception.JsonParsingException;
import medicalpictures.model.orm.entity.BodyPart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON encrypter/decrypter for BodyParts operations.
 *
 * @author Przemysław Thomann
 */
@Stateless
public class BodyPartJsonFactory {

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
     * @throws medicalpictures.model.exception.JsonParsingException
     */
    public String getBodyPart(String bodyPart) throws JsonParsingException {
        try {
            JSONObject bodyPartJson = new JSONObject(bodyPart);
            return bodyPartJson.getString("bodyPart");
        } catch (JSONException ex) {
            throw new JsonParsingException(ex.getMessage());
        }
    }

    /**
     * Decrypts the request from client and reads the body part to edit.
     *
     * @param editingBodyPartValues
     * @return
     * @throws JsonParsingException
     */
    public Map<String, String> getEditingBodyPartValues(String editingBodyPartValues) throws JsonParsingException {
        try {
            JSONObject editingBodyPart = new JSONObject(editingBodyPartValues);
            String oldBodyPart = editingBodyPart.getString("oldBodyPart");
            String newBodyPart = editingBodyPart.getString("newBodyPart");
            Map<String, String> valuesMap = new HashMap<>();
            valuesMap.put("oldBodyPart", oldBodyPart);
            valuesMap.put("newBodyPart", newBodyPart);
            return valuesMap;
        } catch (JSONException ex) {
            throw new JsonParsingException(ex.getMessage());
        }
    }
}
