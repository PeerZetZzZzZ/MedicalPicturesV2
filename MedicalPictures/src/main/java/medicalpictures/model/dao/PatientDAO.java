package medicalpictures.model.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import medicalpictures.model.common.DBNameManager;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.orm.entity.Patient;
import medicalpictures.model.orm.entity.Picture;

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
