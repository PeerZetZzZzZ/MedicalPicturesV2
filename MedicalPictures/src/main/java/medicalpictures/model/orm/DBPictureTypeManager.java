/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.orm;

import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.orm.entity.PictureType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author PeerZet
 */
@Stateless
public class DBPictureTypeManager {

    @EJB
    private OrmManager ormManager;
    private Log logger = LogFactory.getLog(DBPictureTypeManager.class);

    /**
     * Returns all picture types taken from DB.
     *
     * @return
     */
    public JSONObject getAllPictureTypes() {
        Query query = ormManager.getEntityManager().createQuery("SELECT c FROM " + DBNameManager.getPictureTypeTable() + " c", PictureType.class);
        Collection<PictureType> pictureTypes = query.getResultList();
        JSONObject pictureTypesJson = new JSONObject();
        JSONArray pictureTypesArray = new JSONArray();
        for (PictureType pictureType : pictureTypes) {
            pictureTypesArray.put(pictureType.getPicturetype());
        }
        pictureTypesJson.put("pictureTypes", pictureTypesArray);
        return pictureTypesJson;
    }
}
