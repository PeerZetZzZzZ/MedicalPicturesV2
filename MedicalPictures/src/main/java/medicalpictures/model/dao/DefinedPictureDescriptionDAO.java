package medicalpictures.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import medicalpictures.model.common.DBNameManager;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.exception.AddToDbFailed;
import medicalpictures.model.orm.entity.BodyPart;
import medicalpictures.model.orm.entity.DefinedPictureDescription;

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
}
