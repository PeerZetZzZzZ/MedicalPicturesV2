/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.orm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.orm.entity.BodyPart;

/**
 * Responsible for operations on BodyPart table.
 *
 * @author PeerZet
 */
@Stateless
public class DBBodyPartManager {

    @EJB
    private OrmManager ormManager;

    /**
     * Returns the list of body parts.
     *
     * @return BodyPart list.
     */
    public List<String> getBodyParts() {
        Query query = ormManager.getEntityManager().createQuery("SELECT c FROM " + DBNameManager.getBodyPartTable() + " c", BodyPart.class);
        Collection<BodyPart> bodyParts = query.getResultList();
        List<String> bodyPartsList = new ArrayList<String>();
        for (BodyPart bodyPart : bodyParts) {
            bodyPartsList.add(bodyPart.getBodypart());
        }
        return bodyPartsList;
    }
}
