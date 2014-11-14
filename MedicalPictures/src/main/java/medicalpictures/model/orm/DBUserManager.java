/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.orm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.Query;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.AddUserFailed;
import medicalpictures.model.orm.entity.Admin;
import medicalpictures.model.orm.entity.Doctor;
import medicalpictures.model.orm.entity.Patient;
import medicalpictures.model.orm.entity.Technician;
import medicalpictures.model.orm.entity.UsersDB;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

/**
 *
 * @author PeerZet
 */
@Stateful
public class DBUserManager {

    @EJB
    private OrmManager ormManager;
    private Log logger = LogFactory.getLog(DBUserManager.class);

    /**
     * Saves new user in database.
     *
     * @param userDetails Map with created user details.
     * @throws medicalpictures.model.exception.AddUserFailed
     */
    public void addNewUser(Map<String, String> userDetails) throws AddUserFailed {
        final String username = userDetails.get("username");
        final String password = userDetails.get("password");
        final String accountType = userDetails.get("accountType");
        final String name = userDetails.get("name");
        final String surname = userDetails.get("surname");
        final String age = userDetails.get("age");
        final String specialization = userDetails.get("specialization");
        addNewUsersDbUser(username, password, accountType);
        Object user = null;
        switch (accountType) {
            case "ADMIN":
                user = new Admin(username, name, surname, Integer.valueOf(age));
                break;
            case "PATIENT":
                user = new Patient(username, name, surname, Integer.valueOf(age));
                break;
            case "TECHNICIAN":
                user = new Technician(username, name, surname, Integer.valueOf(age));
                break;
            case "DOCTOR":
                user = new Doctor(username, name, surname, Integer.valueOf(age), specialization);
                break;
        }
        ormManager.persistObject(user);
        logger.info("Added user: " + username + "," + password + "," + accountType + "," + name + "," + surname + "," + age + "," + specialization);
    }

    /**
     * Saves user in UsersDB.
     *
     * @param username username
     * @param password password
     * @param accountType accountType
     */
    private void addNewUsersDbUser(final String username, final String password, final String accountType) throws AddUserFailed {
        UsersDB user = new UsersDB(username, password, accountType);
        ormManager.persistObject(user);
    }

    /**
     * Returns the list with all users in UsersDB table.
     *
     * @return JSON with users - their usernames and accountTypes
     */
    public JSONObject getAllUsernames() {
        Query query = ormManager.getEntityManager().createQuery("SELECT c FROM " + DBNameManager.getUsersDbTable() + " c", UsersDB.class);
        Collection<UsersDB> usersDb = query.getResultList();
        JSONObject users = new JSONObject();
        List<JSONObject> usersList = new ArrayList<>();
        for (UsersDB userDb : usersDb) {
            JSONObject singleUser = new JSONObject();
            singleUser.put("username", userDb.getUsername());
            singleUser.put("accountType", userDb.getAccountType());
            usersList.add(singleUser);
        }
        users.put("usernames", usersList);
        return users;
    }

    public void deleteUsers(List<String> usernames) {
        ormManager.getEntityTransaction().begin();
        for (String username : usernames) {
            Admin admin = ormManager.getEntityManager().find(Admin.class, username);
            ormManager.getEntityManager().remove(admin);
            UsersDB user = ormManager.getEntityManager().find(UsersDB.class, username);
            ormManager.getEntityManager().remove(user);
            logger.info("Deleted user: " + username);
        }
        ormManager.getEntityTransaction().commit();
    }

    /**
     * Returns users details readed from UsersDB table and specified for given
     * accountType.
     *
     * @param username Username for which details will be found
     * @return Map with user details
     */
    public Map<String, String> getUserDetails(String username) {
        Map<String, String> userDetails = new HashMap<>();
        ormManager.getEntityManager().getTransaction().begin();
        UsersDB user = ormManager.getEntityManager().find(UsersDB.class, username);
        String userAccountType = user.getAccountType();
        String name = "";
        String surname = "";
        int age = 0;
        switch (userAccountType) {
            case "ADMIN": {
                Admin admin = ormManager.getEntityManager().find(Admin.class, username);
                name = admin.getName();
                surname = admin.getSurname();
                age = admin.getAge();
                break;
            }
            case "DOCTOR": {
                Doctor doctor = ormManager.getEntityManager().find(Doctor.class, username);
                name = doctor.getName();
                surname = doctor.getSurname();
                age = doctor.getAge();
                String specialization = doctor.getSpecialization();
                userDetails.put("specialization", String.valueOf(specialization));//it's special case when we do it
                break;
            }
            case "PATIENT": {
                Patient patient = ormManager.getEntityManager().find(Patient.class, username);
                name = patient.getName();
                surname = patient.getSurname();
                age = patient.getAge();
                break;
            }
            case "TECHNICIAN": {
                Technician technician = ormManager.getEntityManager().find(Technician.class, username);
                name = technician.getName();
                surname = technician.getSurname();
                age = technician.getAge();
                break;
            }
        }
        userDetails.put("username", username);
        userDetails.put("password", user.getPassword());
        userDetails.put("name", name);
        userDetails.put("surname", surname);
        userDetails.put("age", String.valueOf(age));
        userDetails.put("accountType", userAccountType);
        return userDetails;
    }

}
