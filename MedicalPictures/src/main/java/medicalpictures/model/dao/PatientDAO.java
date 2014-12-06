/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.dao;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.orm.entity.Patient;
import medicalpictures.model.orm.entity.Picture;
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
     * @param patientUsername
     * @return 
     */
    public Set<Picture> getPatientPictures(String patientUsername) {
        Patient patient = userDAO.findPatient(patientUsername);
        if(patient!=null){
            return patient.getPictureList();
        } else{
            return new HashSet<>();
        }
    }
}
