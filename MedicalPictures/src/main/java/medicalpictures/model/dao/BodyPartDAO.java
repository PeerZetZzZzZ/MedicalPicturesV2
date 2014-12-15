package medicalpictures.model.dao;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import medicalpictures.model.common.DBNameManager;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.exception.AddToDbFailed;
import medicalpictures.model.orm.entity.BodyPart;

/**
 * Responsible for operations on BodyPart table.
 *
 * @author PeerZet
 */
@Stateless
public class BodyPartDAO {

    @EJB
    private ManagerDAO managerDAO;

    @EJB
    private MedicalLogger logger;

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
    public List<BodyPart> getAllBodyParts() {
        Query query = managerDAO.getEntityManager().createQuery("SELECT c FROM " + DBNameManager.getBodyPartTable() + " c", BodyPart.class);
        List<BodyPart> bodyPartsList = query.getResultList();
        if (bodyPartsList == null) {
            logger.logWarning("No body parts found.", BodyPartDAO.class);
            return new ArrayList<>();
        } else {
            return bodyPartsList;
        }
    }

    /**
     * Creates new body part in DB.
     *
     * @param bodyPartString
     * @return
     */
    public int addBodyPart(String bodyPartString) {
        BodyPart bodyPart = new BodyPart();
        bodyPart.setBodyPart(bodyPartString);
        try {
            managerDAO.persistObject(bodyPart);
            logger.logInfo("Successfully added body part: " + bodyPartString, BodyPartDAO.class);
            return ResultCodes.OPERATION_SUCCEED;
        } catch (AddToDbFailed ex) {
            logger.logError("Couldn't add body part '" + bodyPartString + "'. Internal server problem!", BodyPartDAO.class, ex);
            return ResultCodes.INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * Gets the body part by unique name
     *
     * @param name
     * @return
     */
    public BodyPart getBodyPartByName(String name) {
        try {
            BodyPart bodyPart = (BodyPart) managerDAO.getEntityManager().createQuery("SELECT u FROM " + DBNameManager.getBodyPartTable() + " u WHERE u.bodyPart LIKE :bodyPart").
                    setParameter("bodyPart", name).getSingleResult();
            managerDAO.getEntityManager().refresh(bodyPart);
            return bodyPart;
        } catch (Exception ex) {
            logger.logWarning("Couldn't find body part entity: " + name, BodyPartDAO.class);
            return null;//in any case of failure
        }
    }
}
