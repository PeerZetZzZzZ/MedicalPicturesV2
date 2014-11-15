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
import javax.inject.Named;
import javax.persistence.Query;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.exception.AddUserFailed;
import medicalpictures.model.orm.entity.Admin;
import medicalpictures.model.orm.entity.Doctor;
import medicalpictures.model.orm.entity.Patient;
import medicalpictures.model.orm.entity.Technician;
import medicalpictures.model.orm.entity.UsersDB;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

/**
 *
 * @author PeerZet
 */
@Stateful
@Named(value = "DBUserManager")
public class DBUserManager {

    @EJB
    private OrmManager ormManager;
    private Log logger = LogFactory.getLog(DBUserManager.class);

    /**
     * Saves new user in database (UsersDB + specified table (by accountType)).
     *
     * @param userDetails Map with created user details.
     * @throws medicalpictures.model.exception.AddUserFailed
     */
    public void addNewUser(Map<String, String> userDetails) throws AddUserFailed {
        addUser(userDetails, true);
    }

    /**
     * Saves user only in specified table ( by accountType)
     *
     * @param userDetails
     * @throws AddUserFailed
     */
    private void addNewUserInSpecifiedAccountTable(Map<String, String> userDetails) throws AddUserFailed {
        addUser(userDetails, false);//dont create in UsersDB too
    }

    /**
     * Saves user in UsersDB.
     *
     * @param username username
     * @param password password
     * @param accountType accountType
     */
    private void addNewUserInUserDB(final String username, final String password, final String accountType) throws AddUserFailed {
        UsersDB user = new UsersDB(username, password, accountType);
        ormManager.persistObject(user);
    }

    /**
     * Saves the user in database.
     *
     * @param userDetails User details
     * @param inUsersDBToo If == true, creates user in UsersDB too, if false =
     * creates user only in specified table (by accountType)
     * @throws AddUserFailed
     */
    private void addUser(Map<String, String> userDetails, boolean inUsersDBToo) throws AddUserFailed {
        final String username = userDetails.get("username");
        final String name = userDetails.get("name");
        final String surname = userDetails.get("surname");
        final String password = generatePassword(name, surname);
        final String accountType = userDetails.get("accountType");
        final String age = userDetails.get("age");
        final String specialization = userDetails.get("specialization");
        if (inUsersDBToo) { //if it's false it means that don't create
            addNewUserInUserDB(username, password, accountType);
        }
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

    /**
     * Deletes the user from UsersDB and table specified for given accountType.
     *
     * @param usernamesMap Map with users in such form <username,accountType>
     */
    public void deleteUsers(Map<String, String> usernamesMap) {
        ormManager.getEntityTransaction().begin();
        Set<String> usernames = usernamesMap.keySet();
        for (String username : usernames) {
            String accountType = usernamesMap.get(username);//get accountType
            switch (accountType) {
                case "ADMIN": {
                    Admin admin = ormManager.getEntityManager().find(Admin.class, username);
                    ormManager.getEntityManager().remove(admin);
                    break;
                }
                case "DOCTOR": {
                    Doctor doctor = ormManager.getEntityManager().find(Doctor.class, username);
                    ormManager.getEntityManager().remove(doctor);
                    break;
                }
                case "TECHNICIAN": {
                    Technician technician = ormManager.getEntityManager().find(Technician.class, username);
                    ormManager.getEntityManager().remove(technician);
                    break;
                }
                case "PATIENT": {
                    Patient patient = ormManager.getEntityManager().find(Patient.class, username);
                    ormManager.getEntityManager().remove(patient);
                    break;
                }
            }
            UsersDB user = ormManager.getEntityManager().find(UsersDB.class, username);
            ormManager.getEntityManager().remove(user);
            logger.info("Deleted user: " + username + ", accountType: " + accountType);
        }
        ormManager.getEntityTransaction().commit();
    }

    /**
     * Deletes users only in table specified by accountType
     *
     * @param usernamesMap Map with users in such form <username,accountType>
     */
    private void deleteUsersOnlyInSpecifiedTable(Map<String, String> usernamesMap) {
        ormManager.getEntityTransaction().begin();
        Set<String> usernames = usernamesMap.keySet();
        for (String username : usernames) {
            String accountType = usernamesMap.get(username);//get accountType
            switch (accountType) {
                case "ADMIN": {
                    Admin admin = ormManager.getEntityManager().find(Admin.class, username);
                    ormManager.getEntityManager().remove(admin);
                    break;
                }
                case "DOCTOR": {
                    Doctor doctor = ormManager.getEntityManager().find(Doctor.class, username);
                    ormManager.getEntityManager().remove(doctor);
                    break;
                }
                case "TECHNICIAN": {
                    Technician technician = ormManager.getEntityManager().find(Technician.class, username);
                    ormManager.getEntityManager().remove(technician);
                    break;
                }
                case "PATIENT": {
                    Patient patient = ormManager.getEntityManager().find(Patient.class, username);
                    ormManager.getEntityManager().remove(patient);
                    break;
                }
            }
            logger.info("Deleted user: " + username + ", accountType: " + accountType);
        }
        ormManager.getEntityTransaction().commit();
    }

    /**
     * Generates the default password, which is "name - surname" as sha512hex
     *
     * @param name Name of the user
     * @param surname Surname of the user
     * @return generated password
     */
    private String generatePassword(String name, String surname) {
        return DigestUtils.sha512Hex(name + "-" + surname);
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
        userDetails.put("name", name);
        userDetails.put("surname", surname);
        userDetails.put("age", String.valueOf(age));
        userDetails.put("accountType", userAccountType);
        logger.info("Returned user details: " + username);
        return userDetails;
    }

    /**
     * Changes the values of specified user
     *
     * @param userDetails User which will be changed
     */
    public void editUser(Map<String, String> userDetails) throws AddUserFailed {
        String username = userDetails.get("username");
        String name = userDetails.get("name");
        String surname = userDetails.get("surname");
        String accountType = userDetails.get("accountType");
        Integer age = Integer.valueOf(userDetails.get("age"));
        String resetPassword = userDetails.get("resetPassword");
        /* If we change user accountType, it means that we must move him to another table, so we must
         delete him in which he is already */
        Map<String, String> usersToDelete = new HashMap<>();
        ormManager.getEntityTransaction().begin();
        UsersDB userToEdit = ormManager.getEntityManager().find(UsersDB.class, username);
        String currentAccountType = userToEdit.getAccountType();
        boolean accountTypeChanged = false;
        if (resetPassword.equals("true")) {
            userToEdit.setPassword(generatePassword(name, surname));//default password is generated
        }
        if (!userToEdit.getAccountType().equals(accountType)) {//if accountType changed
            userToEdit.setAccountType(accountType);
            accountTypeChanged = true;
        }
        ormManager.getEntityManager().persist(userToEdit);
        if (!accountTypeChanged) {
            switch (accountType) {
                case "ADMIN": {
                    Admin admin = ormManager.getEntityManager().find(Admin.class, username);
                    admin.setName(name);
                    admin.setSurname(surname);
                    admin.setAge(age);
                    ormManager.getEntityManager().persist(admin);
                    ormManager.getEntityTransaction().commit();
                    break;
                }
                case "DOCTOR": {
                    Doctor doctor = ormManager.getEntityManager().find(Doctor.class, username);
                    doctor.setName(name);
                    doctor.setSurname(surname);
                    doctor.setAge(age);
                    doctor.setSpecialization("");
                    ormManager.getEntityManager().persist(doctor);
                    ormManager.getEntityTransaction().commit();
                    break;
                }
                case "PATIENT": {
                    Patient patient = ormManager.getEntityManager().find(Patient.class, username);
                    patient.setName(name);
                    patient.setSurname(surname);
                    patient.setAge(age);
                    ormManager.getEntityManager().persist(patient);
                    ormManager.getEntityTransaction().commit();
                    break;
                }
                case "TECHNICIAN": {
                    Technician technician = ormManager.getEntityManager().find(Technician.class, username);
                    technician.setName(name);
                    technician.setSurname(surname);
                    technician.setAge(age);
                    ormManager.getEntityManager().persist(technician);
                    ormManager.getEntityTransaction().commit();
                    break;
                }
            }
        } else {
            ormManager.getEntityTransaction().commit();
            usersToDelete.put(username, currentAccountType);//we add user to delete
            deleteUsersOnlyInSpecifiedTable(usersToDelete);//delete existing user but not in UsersDB table
            Map<String, String> newUserDetails = new HashMap<>();
            newUserDetails.put("username", username);
            newUserDetails.put("name", name);
            newUserDetails.put("accountType", accountType);
            newUserDetails.put("surname", surname);
            newUserDetails.put("age", String.valueOf(age));
            addNewUserInSpecifiedAccountTable(userDetails);
        }

    }

}
