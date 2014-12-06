/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.dao;

import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.orm.entity.BodyPart;
import medicalpictures.model.orm.entity.Patient;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author PeerZet
 */
@Stateless
public class PatientDAO {

    @EJB
    private ManagerDAO ormManager;

    /**
     * Returns all patients in application.
     *
     * @return
     */
    public JSONObject getAllPatients() {
        Query query = ormManager.getEntityManager().createQuery("SELECT c FROM " + DBNameManager.getPatientTable() + " c", Patient.class);
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
}
