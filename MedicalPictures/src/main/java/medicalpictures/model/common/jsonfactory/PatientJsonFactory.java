package medicalpictures.model.common.jsonfactory;

import java.util.List;
import javax.ejb.Stateless;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.orm.entity.Patient;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Przemysław Thomann
 */
@Stateless
public class PatientJsonFactory {

    /**
     * Returns all patients
     *
     * @param patients
     * @return
     */
    public String getAllPatients(List<Patient> patients) {
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
        patientsJson.put("errorCode", ResultCodes.OPERATION_SUCCEED);
        return patientsJson.toString();
    }
}
