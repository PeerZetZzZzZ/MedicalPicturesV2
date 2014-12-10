package medicalpictures.model.dao;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import medicalpictures.model.common.DBNameManager;
import medicalpictures.model.common.MedicalLogger;
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
	 * Returns the list of defined picture descriptions.
	 *
	 * @return
	 */
	public List<DefinedPictureDescription> getDefinedPictureDescription() {
		List<DefinedPictureDescription> definedPictureDescriptionList = managerDAO.getEntityManager().
				createQuery("SELECT d FROM " + DBNameManager.getDefinedPictureDescriptionTableName() + " d").getResultList();
		if (definedPictureDescriptionList == null) {
			logger.logWarning("No defined picture descriptions found.", DefinedPictureDescriptionDAO.class);
			return new ArrayList<>();
		} else {
			return definedPictureDescriptionList;
		}
	}
}
