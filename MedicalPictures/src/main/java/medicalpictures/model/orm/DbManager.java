/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.orm;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityExistsException;
import medicalpictures.model.exception.AddUserFailed;
import medicalpictures.model.orm.entity.Admin;
import medicalpictures.model.orm.entity.Doctor;
import medicalpictures.model.orm.entity.Patient;
import medicalpictures.model.orm.entity.Technician;
import medicalpictures.model.orm.entity.UsersDB;

/**
 *
 * @author PeerZet
 */
@Stateful
public class DbManager {

    @EJB
    private OrmManager ormManager;

    /**
     * Saves new user in database.
     *
     * @param userDetails Map with created user details.
     */
    public void addNewUser(Map<String, String> userDetails) throws AddUserFailed {
        final String username = userDetails.get("username");
        System.out.println(username);
        final String password = userDetails.get("password");
        System.out.println(password);
        final String accountType = userDetails.get("accountType");
        System.out.println(accountType);
        final String name = userDetails.get("name");
        System.out.println(name);
        final String surname = userDetails.get("surname");
        System.out.println(surname);
        final String age = userDetails.get("age");
        System.out.println(age);
        final String specialization = userDetails.get("specialization");
        try {
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
        } catch (Exception ex) {
            throw new AddUserFailed("Adding user hihi: " + username + " failed!");

        }

    }

    /**
     * Saves user in UsersDB.
     *
     * @param username username
     * @param password password
     * @param accountType accountType
     */
    private void addNewUsersDbUser(final String username, final String password, final String accountType) throws Exception {
        UsersDB user = new UsersDB(username, password, accountType);
        ormManager.persistObject(user);
        System.out.println("Dodaelem go do user");
    }
}
