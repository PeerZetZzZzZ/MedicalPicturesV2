/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.dao;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.orm.entity.Patient;
import medicalpictures.model.orm.entity.Picture;
import medicalpictures.model.orm.entity.PictureDescription;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author PeerZet
 */
@Stateless
public class PatientDAO {

    @EJB
    private ManagerDAO managerDAO;

    @EJB
    private UserDAO userDAO;

    @EJB
    private PictureDAO pictureDAO;

    @EJB
    private JsonFactory jsonFactory;
    private static final Logger LOG = Logger.getLogger(PatientDAO.class.getName());

    /**
     * Returns all patients in application.
     *
     * @return
     */
    public JSONObject getAllPatients() {
        Query query = managerDAO.getEntityManager().createQuery("SELECT c FROM " + DBNameManager.getPatientTable() + " c", Patient.class);
        Collection<Patient> patients = query.getResultList();
        JSONObject patientsJson = new JSONObject();
        JSONArray patientsArray = new JSONArray();
        for (Patient patient : patients) {
            JSONObject singlePatient = new JSONObject();
            singlePatient.put("username", patient.getUser().getUsername());
            singlePatient.put("surname", patient.getSurname());
            singlePatient.put("name", patient.getName());
            patientsArray.put(singlePatient);
        }
        patientsJson.put("patients", patientsArray);
        return patientsJson;
    }

    /**
     * Returns patient pictures list ( empty if there are no pictures )
     *
     * @param patientUsername
     * @return
     */
    public Set<Picture> getPatientPictures(String patientUsername) {
        Patient patient = userDAO.findPatient(patientUsername);
        if (patient != null) {
            return patient.getPictureList();
        } else {
            return new HashSet<>();
        }
    }

    public String getPatientPictureDescriptions(String patientUsername, String pictureId) {
        Picture picture = pictureDAO.getPictureById(pictureId);
        if (picture != null) {
            if (picture.getPatient().getUser().getUsername().equals(patientUsername)) {
                JSONObject pictureDescriptions = new JSONObject();
                JSONArray descriptionsArray = new JSONArray();
                for (PictureDescription description : picture.getPictureDescriptions()) {
                    JSONObject desc = new JSONObject();
                    desc.put("doctor", description.getDoctor().getName() + " " + description.getDoctor().getSurname());
                    desc.put("doctorSpecialization", description.getDoctor().getSpecialization());
                    if (description.getDefinedPictureDescription() != null) {
                        desc.put("pictureDescription", description.getDefinedPictureDescription().getPictureDescription());
                    } else {
                        desc.put("pictureDescription", description.getDescription());
                    }
                    descriptionsArray.put(desc);
                }
                pictureDescriptions.put("pictureDescriptions", descriptionsArray);
                LOG.info("Return patient picture descriptions: " + pictureDescriptions.toString());
                return pictureDescriptions.toString();
            } else {
                LOG.info("User with username '" + patientUsername + "' is not owner of the picture with id '" + pictureId + "'");
                return jsonFactory.insertToDbFailed();
            }
        } else {
            LOG.info("Picture with id '" + pictureId + "' not found.");
            return jsonFactory.notObjectFound();
        }
    }
}
