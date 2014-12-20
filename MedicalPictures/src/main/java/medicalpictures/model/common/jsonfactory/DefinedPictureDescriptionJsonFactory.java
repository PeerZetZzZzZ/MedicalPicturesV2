package medicalpictures.model.common.jsonfactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.exception.JsonParsingException;
import medicalpictures.model.orm.entity.DefinedPictureDescription;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Przemysław Thomann
 */
@Stateless
public class DefinedPictureDescriptionJsonFactory {

    /**
     * Gets defined picture description response to the client.
     *
     * @param definedPictureDescriptionList
     * @return
     */
    public String getDefinedPictureDescription(List<DefinedPictureDescription> definedPictureDescriptionList) {
        JSONObject dpdList = new JSONObject();
        JSONArray dpdArray = new JSONArray();
        for (DefinedPictureDescription dpd : definedPictureDescriptionList) {
            JSONObject singleDpd = new JSONObject();
            singleDpd.put("name", dpd.getDescriptionName());
            singleDpd.put("pictureDescription", dpd.getPictureDescription());
            singleDpd.put("id", dpd.getId());
            dpdArray.put(singleDpd);
        }
        dpdList.put("definedPictureDescriptions", dpdArray);
        dpdList.put("errorCode", ResultCodes.OPERATION_SUCCEED);
        return dpdList.toString();
    }

    /**
     * Decrypts the request from user and allows to add new
     * DefinedPictureDescription
     *
     * @param dpdToAdd
     * @return
     * @throws JsonParsingException
     */
    public Map<String, String> getDefinedPictureDescriptionToAdd(String dpdToAdd) throws JsonParsingException {
        try {
            JSONObject dpdJson = new JSONObject(dpdToAdd);
            Map<String, String> definedPictureDescriptionValues = new HashMap<>();
            definedPictureDescriptionValues.put("name", dpdJson.getString("name"));
            definedPictureDescriptionValues.put("description", dpdJson.getString("description"));
            return definedPictureDescriptionValues;
        } catch (JSONException ex) {
            throw new JsonParsingException(ex.getMessage());
        }
    }

    /**
     * Decrypts the request from the client and gets the values to edit defined
     * picture description.
     *
     * @param editingDpdValues
     * @return
     * @throws JsonParsingException
     */
    public Map<String, String> getEditingDefinedPictureDescriptionValues(String editingDpdValues) throws JsonParsingException {
        try {
            JSONObject editingPictureType = new JSONObject(editingDpdValues);
            String oldDefinedPictureDescription = editingPictureType.getString("oldDefinedPictureDescription");
            String oldDefinedPictureDescriptionName = editingPictureType.getString("oldDefinedPictureDescriptionName");
            String newDefinedPictureDescription = editingPictureType.getString("newDefinedPictureDescription");
            String newDefinedPictureDescriptionName = editingPictureType.getString("newDefinedPictureDescriptionName");
            Map<String, String> valuesMap = new HashMap<>();
            valuesMap.put("oldDefinedPictureDescription", oldDefinedPictureDescription);
            valuesMap.put("newDefinedPictureDescription", newDefinedPictureDescription);
            valuesMap.put("oldDefinedPictureDescriptionName", oldDefinedPictureDescriptionName);
            valuesMap.put("newDefinedPictureDescriptionName", newDefinedPictureDescriptionName);
            return valuesMap;
        } catch (JSONException ex) {
            throw new JsonParsingException(ex.getMessage());
        }
    }
}
