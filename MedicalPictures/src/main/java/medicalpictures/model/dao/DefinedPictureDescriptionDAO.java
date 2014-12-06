package medicalpictures.model.dao;

import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import medicalpictures.model.orm.entity.DefinedPictureDescription;

/**
 *
 * @author PeerZet
 */
@Stateless
public class DefinedPictureDescriptionDAO {

    @EJB
    private ManagerDAO managerDAO;
    private static final Logger LOG = Logger.getLogger(DefinedPictureDescriptionDAO.class.getName());

    public DefinedPictureDescription getDefinedPictureDesriptionById(String id) {
        DefinedPictureDescription definedPictureDescription = managerDAO.getEntityManager().find(DefinedPictureDescription.class, id);
        if (definedPictureDescription == null) {
            LOG.warning("Defined picture description with id '" + id + "' not found.");
        }
        return definedPictureDescription;
    }
}
