package medicalpictures.model.common;

import java.util.List;
import javax.ejb.Stateless;
import medicalpictures.model.orm.entity.DefinedPictureDescription;
import org.json.JSONArray;
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
		return dpdList.toString();
	}
}
