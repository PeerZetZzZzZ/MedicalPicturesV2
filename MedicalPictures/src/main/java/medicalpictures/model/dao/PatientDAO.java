/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.orm.entity.Patient;
import medicalpictures.model.orm.entity.Picture;
import medicalpictures.model.orm.entity.PictureDescription;
import org.apache.shiro.SecurityUtils;
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

	@EJB
	private MedicalLogger logger;

	/**
	 * Returns all patients in application.
	 *
	 * @return
	 */
	public List<Patient> getAllPatients() {
		Query query = managerDAO.getEntityManager().createQuery("SELECT c FROM " + DBNameManager.getPatientTable() + " c", Patient.class);
		List<Patient> patients = query.getResultList();
		if (patients == null) {
			logger.logWarning("No patients found", PatientDAO.class);
			return new ArrayList<>();
		} else {
			return patients;
		}

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
			logger.logWarning("No patient found with username '" + patientUsername + "'.", PatientDAO.class);
			return new HashSet<>();
		}
	}

}
