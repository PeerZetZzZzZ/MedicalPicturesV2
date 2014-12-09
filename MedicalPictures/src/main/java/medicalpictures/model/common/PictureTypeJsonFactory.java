package medicalpictures.model.common;

import java.util.List;
import javax.ejb.Stateless;
import medicalpictures.model.orm.entity.PictureType;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * JSON encrypter/decrypter for PictureType operations.
 *
 * @author Przemysław Thomann
 */
@Stateless
public class PictureTypeJsonFactory {

    /**
     * Gets all picture types.
     *
     * @param pictureTypesList
     * @return
     */
    public String getAllPictureTypes(List<PictureType> pictureTypesList) {
        JSONObject pictureTypesJson = new JSONObject();
        JSONArray pictureTypesArray = new JSONArray();
        for (PictureType pictureType : pictureTypesList) {
            pictureTypesArray.put(pictureType.getPictureType());
        }
        pictureTypesJson.put("pictureTypes", pictureTypesArray);
        return pictureTypesJson.toString();
    }
}
