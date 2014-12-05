/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.dao;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.exception.AddBodyPartFailed;
import medicalpictures.model.exception.AddNewUserFailed;
import medicalpictures.model.exception.AddPictureTypeFailed;
import medicalpictures.model.exception.AddToDbFailed;
import medicalpictures.model.orm.entity.BodyPart;
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
public class PictureTypeDAO {

    @EJB
    private ManagerDAO ormManager;
    private Log logger = LogFactory.getLog(PictureTypeDAO.class);

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
            pictureTypesArray.put(pictureType.getPictureType());
        }
        pictureTypesJson.put("pictureTypes", pictureTypesArray);
        return pictureTypesJson;
    }

    /**
     * Creates new picture type in DB.
     *
     * @param type type which will be created
     * @throws AddPictureTypeFailed When creation fails.
     */
    public void addPictureType(String type) throws AddPictureTypeFailed {
        PictureType pictureType = new PictureType();
        pictureType.setPictureType(type);
        try {
            ormManager.persistObject(pictureType);
            logger.info(type + ": Picture type successfully added!");
        } catch (AddToDbFailed ex) {
            logger.error(ex.getMessage());
            throw new AddPictureTypeFailed(type + ": Adding picture type failed!");
        }
    }
}
