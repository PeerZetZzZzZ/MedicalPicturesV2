/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.orm;

import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityExistsException;
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
    public void addNewUser(Map<String, String> userDetails) throws EntityExistsException {
        final String username = userDetails.get("username");
        final String password = userDetails.get("password");
        final String accountType = userDetails.get("accountType");
        final String name = userDetails.get("name");
        final String surname = userDetails.get("surname");
        final String age = userDetails.get("age");
        final String specialization = userDetails.get("specialization");
        addNewUsersDbUser(username, password, accountType.toString());
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

    }

    /**
     * Saves user in UsersDB.
     *
     * @param username username
     * @param password password
     * @param accountType accountType
     */
    private void addNewUsersDbUser(final String username, final String password, final String accountType) {
        UsersDB user = new UsersDB(username, password, accountType);
        ormManager.persistObject(user);
    }
}
