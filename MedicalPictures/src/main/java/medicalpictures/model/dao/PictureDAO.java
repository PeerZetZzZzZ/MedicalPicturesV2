/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.dao;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.exception.AddToDbFailed;
import medicalpictures.model.orm.entity.BodyPart;
import medicalpictures.model.orm.entity.DefinedPictureDescription;
import medicalpictures.model.orm.entity.Doctor;
import medicalpictures.model.orm.entity.Patient;
import medicalpictures.model.orm.entity.Picture;
import medicalpictures.model.orm.entity.PictureDescription;
import medicalpictures.model.orm.entity.PictureType;
import medicalpictures.model.orm.entity.Technician;
import org.apache.shiro.SecurityUtils;
import org.json.JSONObject;

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

    @EJB
    private JsonFactory jsonFactory;

    @EJB
    private PictureDescriptionDAO pictureDescriptionDAO;

    @EJB
    private DefinedPictureDescriptionDAO definedPictureDescriptionDAO;

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

    public void removePictures(List<String> picturesList) {
        for (String singlePictureId : picturesList) {
            Picture picture = managerDAO.getEntityManager().find(Picture.class, singlePictureId);
            if (picture != null) {
                managerDAO.getEntityTransaction().begin();
                managerDAO.getEntityManager().remove(picture);
                managerDAO.getEntityTransaction().commit();
                LOG.info("Successfully removed picture: " + singlePictureId);
            } else {
                LOG.info("Couldn't remove picture. Picture ': " + singlePictureId + "' not found");
            }
        }
    }

    /**
     * Updates the picture. Can update bodyPart and pictureType.
     *
     * @param pictureDetailsMap
     */
    public void updatePicture(Map<String, String> pictureDetailsMap) throws AddToDbFailed {
        String pictureId = pictureDetailsMap.get("pictureId");
        String pictureType = pictureDetailsMap.get("pictureType");
        String bodyPart = pictureDetailsMap.get("bodyPart");
        managerDAO.getEntityTransaction().begin();
        BodyPart bodyPartEntity = bodyPartDAO.getBodyPartByName(bodyPart);
        PictureType pictureTypeEntity = pictureTypeDAO.getPictureTypeByName(pictureType);
        Picture picture = managerDAO.getEntityManager().find(Picture.class, pictureId);
        if (picture != null && bodyPartEntity != null && pictureTypeEntity != null) {
            picture.setBodyPart(bodyPartEntity);
            picture.setPictureType(pictureTypeEntity);
        } else {
            throw new AddToDbFailed("Failed to edit picture with id: '" + pictureId + "'");
        }
        managerDAO.getEntityTransaction().commit();
    }

    /**
     * Returns the picture or null if wasn't found ( by id )
     *
     * @param id
     * @return
     */
    public Picture getPictureById(String id) {
        Picture picture = managerDAO.getEntityManager().find(Picture.class, id);
        if (picture == null) {
            LOG.warning("Couldn't find picture with id: " + id);
        }
        return picture;
    }

    public String savePictureOrUpdateDescription(JSONObject pictureDescriptionJson) {
        System.out.println("co dostalem: " + pictureDescriptionJson.toString());
        String pictureId = pictureDescriptionJson.getString("pictureId");
        String pictureDescriptionId = pictureDescriptionJson.getString("pictureDescriptionId");
        String pictureDescription = pictureDescriptionJson.getString("pictureDescription");
        String definedPictureDescriptionId = pictureDescriptionJson.getString("definedPictureDescriptionId");
        Picture picture = getPictureById(pictureId);
        if (picture != null) {
            if (!pictureDescriptionId.equals("")) {//in case when we not create but edit existing description
                PictureDescription existingPictureDescription = pictureDescriptionDAO.getPictureDesriptionById(pictureDescriptionId);
                if (!definedPictureDescriptionId.equals("")) {//don't add own pictureDescription but defined pictureDescription
                    DefinedPictureDescription dpd = definedPictureDescriptionDAO.getDefinedPictureDesriptionById(definedPictureDescriptionId);
                    if (existingPictureDescription != null && dpd != null) {
                        managerDAO.getEntityTransaction().begin();
                        managerDAO.getEntityManager().merge(existingPictureDescription);
                        existingPictureDescription.setDescription("");//we want to clear existing description
                        existingPictureDescription.setDefinedPictureDescription(dpd);//update with defined picture description
                        managerDAO.getEntityTransaction().commit();
                        LOG.info("Set defined picture description with id'" + dpd.getId() + "' for picture '" + existingPictureDescription.getId() + "'.");
                    } else {
                        LOG.warning("DefinedPictureDescription or PictureDescription not found'");
                        return jsonFactory.notObjectFound();
                    }
                } else {
                    if (existingPictureDescription != null) {
                        managerDAO.getEntityManager().merge(existingPictureDescription);
                        managerDAO.getEntityManager().persist(existingPictureDescription);
                        managerDAO.getEntityTransaction().begin();
                        existingPictureDescription.setDescription(pictureDescription);
                        existingPictureDescription.setDefinedPictureDescription(null);
                        managerDAO.getEntityTransaction().commit();
                        LOG.info("Set picture description '" + pictureDescription + "' for picture '" + existingPictureDescription.getId() + "'.");
                    } else {
                        LOG.warning("PictureDescription with id'" + pictureDescriptionId + "' not found.");
                        return jsonFactory.notObjectFound();
                    }
                }
            } else {
                DefinedPictureDescription dpd = definedPictureDescriptionDAO.getDefinedPictureDesriptionById(definedPictureDescriptionId);
                Picture pictureEntity = getPictureById(pictureId);
                String doctorUsername = (String) SecurityUtils.getSubject().getSession().getAttribute("username");
                Doctor doctorEntity = userDAO.findDoctor(doctorUsername);
                if (!definedPictureDescriptionId.equals("")) {//if doctor want to use defined picture description
                    if (dpd != null && pictureEntity != null && doctorEntity != null) {
                        try {
                            PictureDescription newPictureDescription = new PictureDescription("", doctorEntity, pictureEntity, dpd);
                            managerDAO.persistObject(newPictureDescription);
                            LOG.info("Create new picture description with defined picture description '" + definedPictureDescriptionId + "'.");
                        } catch (AddToDbFailed ex) {
                            Logger.getLogger(PictureDAO.class.getName()).log(Level.SEVERE, null, ex);
                            return jsonFactory.insertToDbFailed();
                        }
                    } else {
                        LOG.warning("Couldn't find picture or doctor or defined picture description!");
                        return jsonFactory.notObjectFound();
                    }
                } else {
                    if (pictureEntity != null && doctorEntity != null) {
                        try {
                            PictureDescription newPictureDescription = new PictureDescription(pictureDescription, doctorEntity, pictureEntity, null);
                            managerDAO.persistObject(newPictureDescription);
                            LOG.info("Create new picture description with picture description '" + pictureDescription + "'.");
                        } catch (AddToDbFailed ex) {
                            Logger.getLogger(PictureDAO.class.getName()).log(Level.SEVERE, null, ex);
                            return jsonFactory.insertToDbFailed();
                        }
                    } else {
                        LOG.warning("Couldn't find picture or doctor!");
                        return jsonFactory.notObjectFound();
                    }
                }
            }
        } else {
            LOG.info("Can't save picture description. Picture '" + pictureId + "' not found.");
            return jsonFactory.notObjectFound();
        }
        return "";
    }
}
