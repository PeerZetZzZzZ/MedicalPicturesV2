/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.orm;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityExistsException;
import javax.persistence.Query;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.exception.AddUserFailed;
import medicalpictures.model.orm.entity.Admin;
import medicalpictures.model.orm.entity.BodyPart;
import medicalpictures.model.orm.entity.Doctor;
import medicalpictures.model.orm.entity.Patient;
import medicalpictures.model.orm.entity.Technician;
import medicalpictures.model.orm.entity.UsersDB;
import org.json.JSONObject;

/**
 *
 * @author PeerZet
 */
@Stateful
public class DBUserManager {

    @EJB
    private OrmManager ormManager;

    /**
     * Saves new user in database.
     *
     * @param userDetails Map with created user details.
     * @throws medicalpictures.model.exception.AddUserFailed
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
        System.out.println("Dodaelem go do user");
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
}
