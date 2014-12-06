/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Named;
import javax.persistence.Query;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.exception.AddNewUserFailed;
import medicalpictures.model.exception.AddToDbFailed;
import medicalpictures.model.orm.entity.Admin;
import medicalpictures.model.orm.entity.Doctor;
import medicalpictures.model.orm.entity.Patient;
import medicalpictures.model.orm.entity.Technician;
import medicalpictures.model.orm.entity.User;
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
public class UserDAO {

    @EJB
    private ManagerDAO managerDAO;
    private Log logger = LogFactory.getLog(UserDAO.class);

    /**
     * Saves new user in database (UsersDB + specified table (by accountType)).
     *
     * @param userDetails Map with created user details.
     * @throws medicalpictures.model.exception.AddNewUserFailed
     */
    public void addNewUser(Map<String, String> userDetails) throws AddNewUserFailed {
        try {
            addUser(userDetails, true);
        } catch (AddToDbFailed ex) {
            throw new AddNewUserFailed(userDetails.get("username") + ": adding user failed!");
        }
    }

    /**
     * Saves user only in specified table ( by accountType)
     *
     * @param userDetails
     * @throws AddToDbFailed
     */
    private void addNewUserInSpecifiedAccountTable(Map<String, String> userDetails) throws AddNewUserFailed {
        try {
            addUser(userDetails, false);
        } catch (AddToDbFailed ex) {
            throw new AddNewUserFailed(userDetails.get("username") + ": adding user failed!");
        }
    }

    /**
     * Saves user in UsersDB.
     *
     * @param username username
     * @param password password
     * @param accountType accountType
     */
    private void addNewUserInUserDB(final String username, final String password, final String accountType) throws AddToDbFailed {
        User user = new User(username, password, accountType);
        managerDAO.persistObject(user);
    }

    /**
     * Saves the user in database.
     *
     * @param userDetails User details
     * @param inUsersDBToo If == true, creates user in UsersDB too, if false =
     * creates user only in specified table (by accountType)
     * @throws AddToDbFailed
     */
    private void addUser(Map<String, String> userDetails, boolean inUsersDBToo) throws AddToDbFailed {
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
        User foundUser = findUser(username);
        if (foundUser != null) {
            Object user = null;
            switch (accountType) {
                case "ADMIN":
                    user = new Admin(foundUser, name, surname, Integer.valueOf(age));
                    break;
                case "PATIENT":
                    user = new Patient(foundUser, name, surname, Integer.valueOf(age));
                    break;
                case "TECHNICIAN":
                    user = new Technician(foundUser, name, surname, Integer.valueOf(age));
                    break;
                case "DOCTOR":
                    user = new Doctor(foundUser, name, surname, Integer.valueOf(age), specialization);
                    break;
            }
            managerDAO.persistObject(user);
            logger.info("Added user: " + username + "," + password + "," + accountType + "," + name + "," + surname + "," + age + "," + specialization);
        } else {
            logger.info("Couldn't add user, null ");

        }
    }

    /**
     * Returns the list with all users in UsersDB table.
     *
     * @return JSON with users - their usernames and accountTypes
     */
    public JSONObject getAllUsernames() {
        Query query = managerDAO.getEntityManager().createQuery("SELECT c FROM " + DBNameManager.getUsersDbTable() + " c", User.class);
        Collection<User> usersDb = query.getResultList();
        JSONObject users = new JSONObject();
        List<JSONObject> usersList = new ArrayList<>();
        for (User userDb : usersDb) {
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
        Set<String> usernames = usernamesMap.keySet();
        for (String username : usernames) {
            String accountType = usernamesMap.get(username);//get accountType
            switch (accountType) {
                case "ADMIN": {
                    Admin admin = findAdmin(username);
                    managerDAO.getEntityManager().remove(admin);
                    break;
                }
                case "DOCTOR": {
                    Doctor doctor = findDoctor(username);
                    managerDAO.getEntityManager().remove(doctor);
                    break;
                }
                case "TECHNICIAN": {
                    Technician technician = findTechnician(username);
                    managerDAO.getEntityManager().remove(technician);
                    break;
                }
                case "PATIENT": {
                    Patient patient = findPatient(username);
                    managerDAO.getEntityManager().remove(patient);
                    break;
                }
            }
            User user = findUser(username);
            managerDAO.getEntityManager().remove(user);
            logger.info("Deleted user: " + username + ", accountType: " + accountType);
        }
    }

    /**
     * Deletes users only in table specified by accountType
     *
     * @param usernamesMap Map with users in such form <username,accountType>
     */
    private void deleteUsersOnlyInSpecifiedTable(Map<String, String> usernamesMap) {
        Set<String> usernames = usernamesMap.keySet();
        for (String username : usernames) {
            String accountType = usernamesMap.get(username);//get accountType
            switch (accountType) {
                case "ADMIN": {
                    Admin admin = findAdmin(username);
                    managerDAO.getEntityManager().remove(admin);
                    break;
                }
                case "DOCTOR": {
                    Doctor doctor = findDoctor(username);
                    managerDAO.getEntityManager().remove(doctor);
                    break;
                }
                case "TECHNICIAN": {
                    Technician technician = findTechnician(username);
                    managerDAO.getEntityManager().remove(technician);
                    break;
                }
                case "PATIENT": {
                    Patient patient = findPatient(username);
                    managerDAO.getEntityManager().remove(patient);
                    break;
                }
            }
            logger.info("Deleted user: " + username + ", accountType: " + accountType);
        }
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
        User user = findUser(username);
        String userAccountType = user.getAccountType();
        String name = "";
        String surname = "";
        int age = 0;
        switch (userAccountType) {
            case "ADMIN": {
                Admin admin = findAdmin(username);
                name = admin.getName();
                surname = admin.getSurname();
                age = admin.getAge();
                break;
            }
            case "DOCTOR": {
                Doctor doctor = findDoctor(username);
                name = doctor.getName();
                surname = doctor.getSurname();
                age = doctor.getAge();
                String specialization = doctor.getSpecialization();
                userDetails.put("specialization", String.valueOf(specialization));//it's special case when we do it
                break;
            }
            case "PATIENT": {
                Patient patient = findPatient(username);
                name = patient.getName();
                surname = patient.getSurname();
                age = patient.getAge();
                break;
            }
            case "TECHNICIAN": {
                Technician technician = findTechnician(username);
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
    public void editUser(Map<String, String> userDetails) throws AddToDbFailed {
        String username = userDetails.get("username");
        String name = userDetails.get("name");
        String surname = userDetails.get("surname");
        String accountType = userDetails.get("accountType");
        Integer age = Integer.valueOf(userDetails.get("age"));
        String resetPassword = userDetails.get("resetPassword");
        /* If we change user accountType, it means that we must move him to another table, so we must
         delete him in which he is already */
        Map<String, String> usersToDelete = new HashMap<>();
        User userToEdit = findUser(username);
        String currentAccountType = userToEdit.getAccountType();
        boolean accountTypeChanged = false;
        if (resetPassword.equals("true")) {
            userToEdit.setPassword(generatePassword(name, surname));//default password is generated
        }
        if (!userToEdit.getAccountType().equals(accountType)) {//if accountType changed
            userToEdit.setAccountType(accountType);
            accountTypeChanged = true;
        }
        managerDAO.getEntityManager().persist(userToEdit);
        if (!accountTypeChanged) {
            switch (accountType) {
                case "ADMIN": {
                    Admin admin = findAdmin(username);
                    admin.setName(name);
                    admin.setSurname(surname);
                    admin.setAge(age);
                    managerDAO.getEntityManager().persist(admin);
                    break;
                }
                case "DOCTOR": {
                    Doctor doctor = findDoctor(username);
                    doctor.setName(name);
                    doctor.setSurname(surname);
                    doctor.setAge(age);
                    doctor.setSpecialization("");
                    managerDAO.getEntityManager().persist(doctor);
                    break;
                }
                case "PATIENT": {
                    Patient patient = findPatient(username);
                    patient.setName(name);
                    patient.setSurname(surname);
                    patient.setAge(age);
                    managerDAO.getEntityManager().persist(patient);
                    break;
                }
                case "TECHNICIAN": {
                    Technician technician = findTechnician(username);
                    technician.setName(name);
                    technician.setSurname(surname);
                    technician.setAge(age);
                    managerDAO.getEntityManager().persist(technician);
                    break;
                }
            }
        } else {
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

    public Patient findPatient(String username) {
        try {
            Query query = managerDAO.getEntityManager().createQuery("SELECT a FROM " + DBNameManager.getPatientTable() + " a WHERE a.user.username LIKE :userName", Patient.class);
            Patient patient = (Patient) query.setParameter("userName", username).getSingleResult();
            managerDAO.getEntityManager().refresh(patient);
            return patient;
        } catch (Exception ex) {
            System.out.println("No patient with username: " + username);
            return null;
        }
    }

    public Admin findAdmin(String username) {
        try {
            Query query = managerDAO.getEntityManager().createQuery("SELECT a FROM " + DBNameManager.getAdminTable() + " a WHERE a.user.username LIKE :userName", Admin.class);
            Admin admin = (Admin) query.setParameter("userName", username).getSingleResult();
            managerDAO.getEntityManager().refresh(admin);
            return admin;
        } catch (Exception ex) {
            System.out.println("No admin with username: " + username);
            return null;
        }
    }

    public Technician findTechnician(String username) {
        try {
            Query query = managerDAO.getEntityManager().createQuery("SELECT a FROM " + DBNameManager.getTechnicianTable() + " a WHERE a.user.username LIKE :userName", Technician.class);
            Technician technician = (Technician) query.setParameter("userName", username).getSingleResult();
            managerDAO.getEntityManager().refresh(technician);
            return technician;
        } catch (Exception ex) {
            System.out.println("No technician with username: " + username);
            return null;
        }
    }

    public Doctor findDoctor(String username) {
        try {
            Query query = managerDAO.getEntityManager().createQuery("SELECT a FROM " + DBNameManager.getDoctorTable() + " a WHERE a.user.username LIKE :userName", Doctor.class);
            Doctor doctor = (Doctor) query.setParameter("userName", username).getSingleResult();
            managerDAO.getEntityManager().refresh(doctor);
            return doctor;
        } catch (Exception ex) {
            System.out.println("No doctor with username: " + username);
            return null;
        }
    }

    public User findUser(String username) {
        try {
            Query query = managerDAO.getEntityManager().createQuery("SELECT u FROM " + DBNameManager.getUsersDbTable() + " u WHERE u.username LIKE :userName", User.class);
            User user = (User) query.setParameter("userName", username).getSingleResult();
            managerDAO.getEntityManager().refresh(user);
            return user;
        } catch (Exception ex) {
            System.out.println("No user with username: " + username);
            return null;
        }
    }
}
