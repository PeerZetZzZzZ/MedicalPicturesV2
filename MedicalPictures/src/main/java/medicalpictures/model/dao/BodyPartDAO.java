package medicalpictures.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import medicalpictures.model.common.DBNameManager;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.exception.AddToDbFailed;
import medicalpictures.model.orm.entity.BodyPart;
import medicalpictures.model.orm.entity.Picture;

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
        BodyPart exisingBodyPart = getBodyPartByName(bodyPartString);
        if (exisingBodyPart == null) {
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
        } else {
            logger.logWarning("Couldn't add body part '" + bodyPartString + "' because body part already exists!", BodyPartDAO.class);
            return ResultCodes.OBJECT_ALREADY_EXISTS;
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

    /**
     * Removes the body part only when it's not already used by some pictures.
     *
     * @param bodyPartToRemove
     * @return
     */
    public int removeBodyPart(String bodyPartToRemove) {
        try {
            Picture picture = (Picture) managerDAO.getEntityManager().createQuery("SELECT u FROM " + DBNameManager.getPictureTable() + " u WHERE u.bodyPart.bodyPart LIKE :bodyPart").
                    setParameter("bodyPart", bodyPartToRemove).getSingleResult();
            return ResultCodes.OBJECT_ALREADY_EXISTS;//object is already used by some pictures - can't delete
        } catch (Exception ex) {
            BodyPart bodyPart = getBodyPartByName(bodyPartToRemove);
            managerDAO.getEntityManager().remove(bodyPart);
            logger.logError("Successfully removed body part '" + bodyPartToRemove + "' !", BodyPartDAO.class, ex);
            return ResultCodes.OPERATION_SUCCEED;
        }
    }

    /**
     * Updates the body part.
     *
     * @param editingValues
     * @return
     */
    public int updateBodyPart(Map<String, String> editingValues) {
        String oldBodyPart = editingValues.get("oldBodyPart");
        String newBodyPart = editingValues.get("newBodyPart");
        BodyPart existingBodyPart = getBodyPartByName(oldBodyPart);
        if (existingBodyPart != null) {
            existingBodyPart.setBodyPart(newBodyPart);
            logger.logInfo("Successfully updated body part '" + oldBodyPart + "' to '" + newBodyPart + "' !", BodyPartDAO.class);
            return ResultCodes.OPERATION_SUCCEED;
        } else {
            logger.logInfo("Can't edit body part '" + oldBodyPart + ", because it doesn't exist!", BodyPartDAO.class);
            return ResultCodes.OBJECT_DOESNT_EXIST;
        }
    }
}
