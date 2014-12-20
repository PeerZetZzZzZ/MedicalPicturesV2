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
import medicalpictures.model.orm.entity.Picture;
import medicalpictures.model.orm.entity.PictureType;

/**
 *
 * @author PeerZet
 */
@Stateless
public class PictureTypeDAO {

    @EJB
    private ManagerDAO managerDAO;

    @EJB
    private MedicalLogger logger;

    /**
     * Returns all picture types taken from DB.
     *
     * @return
     */
    public List<PictureType> getAllPictureTypes() {
        Query query = managerDAO.getEntityManager().createQuery("SELECT c FROM " + DBNameManager.getPictureTypeTable() + " c", PictureType.class);
        List<PictureType> pictureTypesList = query.getResultList();
        if (pictureTypesList == null) {
            logger.logWarning("No picture types found.", PictureTypeDAO.class);
            return new ArrayList<>();
        } else {
            return pictureTypesList;
        }
    }

    /**
     * Creates new picture type in DB.
     *
     * @param type type which will be created
     */
    public int addPictureType(String type) {
        PictureType existingPicture = getPictureTypeByName(type);
        if (existingPicture == null) {//if doesn't already exist
            PictureType pictureType = new PictureType();
            pictureType.setPictureType(type);
            try {
                managerDAO.persistObject(pictureType);
                logger.logInfo("Successfully added picture type: " + type, PictureTypeDAO.class);
                return ResultCodes.OPERATION_SUCCEED;
            } catch (AddToDbFailed ex) {
                logger.logError("Couldn't add picture type '" + type + "'. Internal server problem!", PictureTypeDAO.class, ex);
                return ResultCodes.INTERNAL_SERVER_ERROR;
            }
        } else {
            logger.logWarning("Couldn't add picture type '" + type + "' because this picture type already exists!", PictureTypeDAO.class);
            return ResultCodes.OBJECT_ALREADY_EXISTS;
        }
    }

    /**
     * Gets the picture type by its unique name
     *
     * @param pictureTypeName
     * @return
     */
    public PictureType getPictureTypeByName(String pictureTypeName) {
        try {
            PictureType pictureType = (PictureType) managerDAO.getEntityManager().createQuery("SELECT u FROM " + DBNameManager.getPictureTypeTable() + " u WHERE u.pictureType LIKE :pictureType").
                    setParameter("pictureType", pictureTypeName).getSingleResult();
            managerDAO.getEntityManager().refresh(pictureType);
            return pictureType;
        } catch (Exception ex) {
            logger.logError("Couldn't find PictureType entity: " + pictureTypeName, PictureTypeDAO.class, ex);
            return null;//in any case of failure

        }
    }

    /**
     * Removes the picture type if it's not currently used to describe any
     * existing picture.
     *
     * @param pictureTypeToRemove
     * @return
     */
    public int removePictureType(String pictureTypeToRemove) {
        try {
            Picture picture = (Picture) managerDAO.getEntityManager().createQuery("SELECT u FROM " + DBNameManager.getPictureTable() + " u WHERE u.pictureType.pictureType LIKE :pictureType").
                    setParameter("pictureType", pictureTypeToRemove).getSingleResult();
            return ResultCodes.OBJECT_ALREADY_EXISTS;//object is already used by some pictures - can't delete
        } catch (Exception ex) {
            PictureType pictureType = getPictureTypeByName(pictureTypeToRemove);
            managerDAO.getEntityManager().remove(pictureType);
            logger.logError("Successfully removed picture type '" + pictureTypeToRemove + "' !", PictureDAO.class, ex);
            return ResultCodes.OPERATION_SUCCEED;
        }
    }

    /**
     * Updates the existing picture type.
     *
     * @param editingValues
     * @return
     */
    public int updatePictureType(Map<String, String> editingValues) {
        String oldPictureType = editingValues.get("oldPictureType");
        String newPictureType = editingValues.get("newPictureType");
        PictureType duplicatePictureType = getPictureTypeByName(newPictureType);
        if (duplicatePictureType == null) {
            PictureType existingPictureType = getPictureTypeByName(oldPictureType);
            if (existingPictureType != null) {
                existingPictureType.setPictureType(newPictureType);
                logger.logInfo("Successfully updated picture type'" + oldPictureType + "' to '" + newPictureType + "' !", PictureTypeDAO.class);
                return ResultCodes.OPERATION_SUCCEED;
            } else {
                logger.logInfo("Can't edit picture type '" + oldPictureType + ", because it doesn't exist!", PictureTypeDAO.class);
                return ResultCodes.OBJECT_DOESNT_EXIST;
            }
        } else {
            logger.logInfo("Can't edit picture type '" + oldPictureType + ", because new value is already used!", PictureTypeDAO.class);
            return ResultCodes.OBJECT_ALREADY_EXISTS;
        }
    }
}
