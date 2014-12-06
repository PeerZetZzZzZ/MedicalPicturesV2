/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.dao;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.orm.entity.BodyPart;
import medicalpictures.model.orm.entity.Patient;
import medicalpictures.model.orm.entity.Picture;
import medicalpictures.model.orm.entity.PictureType;
import medicalpictures.model.orm.entity.Technician;
import org.apache.shiro.SecurityUtils;

/**
 *
 * @author PeerZet
 */
@Stateless
public class PictureDAO {

    @EJB
    private PatientDAO patientDAO;

    @EJB
    private BodyPartDAO bodyPartDAO;

    @EJB
    private PictureTypeDAO pictureTypeDAO;

    @EJB
    private UserDAO userDAO;

    @EJB
    private ManagerDAO managerDAO;

    private static final Logger LOG = Logger.getLogger(PictureDAO.class.getName());

    public void addNewPicture(Map<String, String> pictureDetails, byte[] pictureData, byte[] thumbnailData) {
        String pictureName = pictureDetails.get("pictureName");
        String patientUnserame = pictureDetails.get("patientUsername");
        String bodyPartName = pictureDetails.get("bodyPart");
        String pictureTypeName = pictureDetails.get("pictureType");
        Patient patient = userDAO.findPatient(patientUnserame);
        BodyPart bodyPart = bodyPartDAO.getBodyPartByName(bodyPartName);
        PictureType pictureType = pictureTypeDAO.getPictureTypeByName(pictureTypeName);
        Technician technician = userDAO.findTechnician((String) SecurityUtils.getSubject().getSession().getAttribute("username"));
        if (patient != null && bodyPart != null && pictureType != null && technician != null) {
            Picture picture = new Picture(patient, technician, pictureType, bodyPart, pictureName, pictureData, thumbnailData);
            managerDAO.getEntityTransaction().begin();
            managerDAO.getEntityManager().persist(picture);
            managerDAO.getEntityTransaction().commit();
            LOG.info("Successfully added new picture: " + pictureName);
        } else {
            LOG.info("Couldn't add new picture, because some of the components was null");
        }
    }

    public List<Picture> getAllPictureList() {
        try {
            return managerDAO.getEntityManager().createQuery("SELECT p FROM " + DBNameManager.getPictureTable() + " p", Picture.class).getResultList();
        } catch (Exception ex) {
            LOG.info("Couldn't get pictures list, because it's empty");
            return null;
        }
    }

}
