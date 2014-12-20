package medicalpictures.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import medicalpictures.model.common.DBNameManager;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.orm.entity.DefinedPictureDescription;
import medicalpictures.model.orm.entity.PictureDescription;
import medicalpictures.model.orm.entity.PictureType;

/**
 *
 * @author PeerZet
 */
@Stateless
public class DefinedPictureDescriptionDAO {

    @EJB
    private ManagerDAO managerDAO;
    @EJB
    private MedicalLogger logger;

    /**
     * Gets defined picture description by id.
     *
     * @param id
     * @return
     */
    public DefinedPictureDescription getDefinedPictureDesriptionById(String id) {
        DefinedPictureDescription definedPictureDescription = managerDAO.getEntityManager().find(DefinedPictureDescription.class, id);
        if (definedPictureDescription == null) {
            logger.logWarning("Defined picture description with id '" + id + "' not found.", DefinedPictureDescriptionDAO.class);
        } else {
            managerDAO.getEntityManager().refresh(definedPictureDescription);
        }
        return definedPictureDescription;
    }

    /**
     * Gets defined picture description by id.
     *
     * @param name
     * @param id
     * @return
     */
    public DefinedPictureDescription getDefinedPictureDesriptionByName(String name) {
        try {
            DefinedPictureDescription dpd = (DefinedPictureDescription) managerDAO.getEntityManager().createQuery("SELECT u FROM " + DBNameManager.getDefinedPictureDescriptionTableName() + " u WHERE u.descriptionName LIKE :name").
                    setParameter("name", name).getSingleResult();
            managerDAO.getEntityManager().refresh(dpd);
            return dpd;
        } catch (Exception ex) {
            logger.logWarning("Couldn't find defined picture description entity: " + name, DefinedPictureDescriptionDAO.class);
            return null;//in any case of failure
        }
    }

    /**
     * Returns the list of defined picture descriptions.
     *
     * @return
     */
    public List<DefinedPictureDescription> getDefinedPictureDescriptions() {
        List<DefinedPictureDescription> definedPictureDescriptionList = managerDAO.getEntityManager().
                createQuery("SELECT d FROM " + DBNameManager.getDefinedPictureDescriptionTableName() + " d").getResultList();
        if (definedPictureDescriptionList == null) {
            logger.logWarning("No defined picture descriptions found.", DefinedPictureDescriptionDAO.class);
            return new ArrayList<>();
        } else {
            return definedPictureDescriptionList;
        }
    }

    /**
     * Adds the defined picture description.
     *
     * @param dpdValues
     * @return
     */
    public int addDefinedPictureDescription(Map<String, String> dpdValues) {
        DefinedPictureDescription dpd = getDefinedPictureDesriptionByName(dpdValues.get("name"));
        if (dpd == null) {
            DefinedPictureDescription newDpd = new DefinedPictureDescription(dpdValues.get("name"), dpdValues.get("description"));
            managerDAO.getEntityManager().persist(newDpd);
            logger.logInfo("Successfully added new defined picture description with name '" + dpdValues.get("name") + "'!", DefinedPictureDescriptionDAO.class);
            return ResultCodes.OPERATION_SUCCEED;
        } else {
            logger.logWarning("Couldn't add defined picture description. Defined picture description with name '" + dpdValues.get("name") + "' already exists!", DefinedPictureDescriptionDAO.class);
            return ResultCodes.OBJECT_ALREADY_EXISTS;
        }
    }

    /**
     * Removes the defined picture description if it's not used to describe any
     * existing.
     *
     * @param definedPictureDescriptionToRemove
     * @return
     */
    public int removeDefinedPictureDescription(String definedPictureDescriptionToRemove) {
        try {
            PictureDescription dpd = (PictureDescription) managerDAO.getEntityManager().createQuery("SELECT u FROM " + DBNameManager.getPictureDescriptionTableName() + " u WHERE u.definedPictureDescription.descriptionName LIKE :definedPictureDescriptionName").
                    setParameter("definedPictureDescriptionName", definedPictureDescriptionToRemove).getSingleResult();
            return ResultCodes.OBJECT_ALREADY_EXISTS;//object is already used by some pictures - can't delete
        } catch (Exception ex) {
            DefinedPictureDescription dpd = getDefinedPictureDesriptionByName(definedPictureDescriptionToRemove);
            managerDAO.getEntityManager().remove(dpd);
            logger.logError("Successfully removed defined picture description'" + definedPictureDescriptionToRemove + "' !", DefinedPictureDescriptionDAO.class, ex);
            return ResultCodes.OPERATION_SUCCEED;
        }
    }

    /**
     * Updates the existing defined picture description.
     *
     * @param editingValues
     * @return
     */
    public int updateDefinedPictureDescription(Map<String, String> editingValues) {
        String oldDpd = editingValues.get("oldDefinedPictureDescription");
        String newDpd = editingValues.get("newDefinedPictureDescription");
        String oldDpdName = editingValues.get("oldDefinedPictureDescriptionName");
        String newDpdName = editingValues.get("newDefinedPictureDescriptionName");
        DefinedPictureDescription existingDpd = getDefinedPictureDesriptionByName(oldDpdName);
        if (existingDpd != null) {
            if (!oldDpd.equals(newDpd)) {//if description changed
                existingDpd.setPictureDescription(newDpd);
            }
            if (!oldDpdName.equals(newDpdName)) {
                existingDpd.setDescriptionName(newDpdName);
            }
            logger.logInfo("Successfully updated defined picture description'" + oldDpdName + "' to '" + newDpdName + "'. Description '" + oldDpd + "' to '" + newDpd + "' !", DefinedPictureDescriptionDAO.class);
            return ResultCodes.OPERATION_SUCCEED;
        } else {
            logger.logInfo("Can't edit picture type '" + oldDpdName + ", because it doesn't exist!", DefinedPictureDescriptionDAO.class);
            return ResultCodes.OBJECT_DOESNT_EXIST;
        }
    }
}
