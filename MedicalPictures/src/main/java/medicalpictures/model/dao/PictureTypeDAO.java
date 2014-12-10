package medicalpictures.model.dao;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.exception.AddPictureTypeFailed;
import medicalpictures.model.exception.AddToDbFailed;
import medicalpictures.model.orm.entity.PictureType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author PeerZet
 */
@Stateless
public class PictureTypeDAO {

	@EJB
	private ManagerDAO managerDAO;
	private Log LOG = LogFactory.getLog(PictureTypeDAO.class);

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
	}

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
}
