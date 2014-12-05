/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.exception.AddBodyPartFailed;
import medicalpictures.model.exception.AddToDbFailed;
import medicalpictures.model.orm.entity.BodyPart;
import medicalpictures.model.orm.entity.PictureType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Responsible for operations on BodyPart table.
 *
 * @author PeerZet
 */
@Stateless
public class BodyPartDAO {

    @EJB
    private ManagerDAO ormManager;

    private Log logger = LogFactory.getLog(PictureTypeDAO.class);

    /**
     * Returns the list of body parts.
     *
     * @return BodyPart list.
     */
    /**
     * Returns all picture types taken from DB.
     *
     * @return All body parts as JSONObject
     */
    public JSONObject getAllBodyParts() {
        Query query = ormManager.getEntityManager().createQuery("SELECT c FROM " + DBNameManager.getBodyPartTable() + " c", BodyPart.class);
        Collection<BodyPart> bodyParts = query.getResultList();
        JSONObject bodyPartsJson = new JSONObject();
        JSONArray bodyPartsArray = new JSONArray();
        for (BodyPart bodyPart : bodyParts) {
            bodyPartsArray.put(bodyPart.getBodyPart());
        }
        bodyPartsJson.put("bodyParts", bodyPartsArray);
        return bodyPartsJson;
    }

    /**
     * Creates new body part in DB.
     *
     * @param bodyPartString
     * @throws medicalpictures.model.exception.AddBodyPartFailed when creation
     * fails
     */
    public void addBodyPart(String bodyPartString) throws AddBodyPartFailed {
        BodyPart bodyPart = new BodyPart();
        bodyPart.setBodyPart(bodyPartString);
        try {
            ormManager.persistObject(bodyPart);
            logger.info(bodyPartString + ": Body part successfully added!");
        } catch (AddToDbFailed ex) {
            logger.error(ex.getMessage());
            throw new AddBodyPartFailed(bodyPartString + ": Adding body part failed!");
        }
    }
}
