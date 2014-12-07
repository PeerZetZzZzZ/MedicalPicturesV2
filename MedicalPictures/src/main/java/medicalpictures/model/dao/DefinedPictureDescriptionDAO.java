package medicalpictures.model.dao;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.orm.entity.DefinedPictureDescription;
import org.json.JSONArray;
import org.json.JSONObject;

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
        managerDAO.getEntityManager().refresh(definedPictureDescription);
        return definedPictureDescription;
    }
    
    public String getDefinedPictureDescription() {
        List<DefinedPictureDescription> definedPictureDescriptionList = managerDAO.getEntityManager().
                createQuery("SELECT d FROM " + DBNameManager.getDefinedPictureDescriptionTableName() + " d").getResultList();
        JSONObject dpdList = new JSONObject();
        JSONArray dpdArray = new JSONArray();
        for (DefinedPictureDescription dpd : definedPictureDescriptionList) {
            JSONObject singleDpd = new JSONObject();
            singleDpd.put("name", dpd.getDescriptionName());
            singleDpd.put("pictureDescription", dpd.getPictureDescription());
            singleDpd.put("id", dpd.getId());
            dpdArray.put(singleDpd);
        }
        dpdList.put("definedPictureDescriptions", dpdArray);
        LOG.info("Returned all defined picture descriptions :" + dpdList.toString());
        return dpdList.toString();
    }
}
