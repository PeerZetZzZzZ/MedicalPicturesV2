package medicalpictures.model.dao;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.orm.entity.PictureDescription;

/**
 *
 * @author PeerZet
 */
@Stateless
public class PictureDescriptionDAO {

	@EJB
	private ManagerDAO managerDAO;

	@EJB
	private MedicalLogger logger;

	/**
	 * Gets the picture by it's UUID id.
	 *
	 * @param id
	 * @return
	 */
	public PictureDescription getPictureDesriptionById(String id) {
		PictureDescription pictureDescription = managerDAO.getEntityManager().find(PictureDescription.class, id);
		managerDAO.getEntityManager().refresh(pictureDescription);
		if (pictureDescription == null) {
			logger.logWarning("Picture description with id '" + id + "' not found.", PictureDescriptionDAO.class);
		}
		return pictureDescription;
	}
}
