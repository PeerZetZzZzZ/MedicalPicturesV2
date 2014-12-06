package medicalpictures.model.dao;

import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import medicalpictures.model.orm.entity.PictureDescription;

/**
 *
 * @author PeerZet
 */
@Stateless
public class PictureDescriptionDAO {

    @EJB
    private ManagerDAO managerDAO;
    private static final Logger LOG = Logger.getLogger(PictureDescriptionDAO.class.getName());

    public PictureDescription getPictureDesriptionById(String id) {
        PictureDescription pictureDescription = managerDAO.getEntityManager().find(PictureDescription.class, id);
        if (pictureDescription == null) {
            LOG.warning("Picture description with id '" + id + "' not found.");
        }
        return pictureDescription;
    }
}
