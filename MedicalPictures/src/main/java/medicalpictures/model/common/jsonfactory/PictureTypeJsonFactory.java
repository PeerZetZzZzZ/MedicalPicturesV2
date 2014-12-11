package medicalpictures.model.common.jsonfactory;

import java.util.List;
import javax.ejb.Stateless;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.exception.JsonParsingException;
import medicalpictures.model.orm.entity.PictureType;
import org.json.JSONArray;
import org.json.JSONException;
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
		pictureTypesJson.put("errorCode", ResultCodes.OPERATION_SUCCEED);
		return pictureTypesJson.toString();
	}

	/**
	 * Decryptes request from client and returns picture type taken from proper json.
	 *
	 * @param pictureType JSON input
	 * @return picture type
	 * @throws medicalpictures.model.exception.JsonParsingException
	 */
	public String getPictureType(String pictureType) throws JsonParsingException {
		try {
			JSONObject pictureTypeJson = new JSONObject(pictureType);
			return pictureTypeJson.getString("pictureType");
		} catch (JSONException ex) {
			throw new JsonParsingException(ex.getMessage());
		}
	}
}
