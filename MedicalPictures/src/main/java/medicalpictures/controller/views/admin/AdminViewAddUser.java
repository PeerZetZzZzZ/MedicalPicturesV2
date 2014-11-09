/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.controller.views.admin;

import java.io.IOException;
import java.util.Map;
import javax.ejb.EJB;
import javax.persistence.EntityExistsException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.controller.views.common.LoginView;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.orm.DbManager;
import medicalpictures.model.security.UserSecurityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

/**
 *
 * @author PeerZet
 */
public class AdminViewAddUser extends HttpServlet {

    @EJB
    private UserSecurityManager manager;
    @EJB
    private JsonFactory jsonFactory;
    @EJB
    private DbManager dbManager;
    private Log log = LogFactory.getLog(AdminViewAddUser.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        manager.checkUserPermissionToThisContent(AccountType.ADMIN);
        request.getRequestDispatcher("/WEB-INF/admin/adminViewAddUser.html").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        manager.checkUserPermissionToThisContent(AccountType.ADMIN);
        JSONObject user = jsonFactory.decryptRequest(request);
        System.out.println("dostolem do dodania: " + user);
        Map<String, String> userDetails = jsonFactory.readUser(user);
        try {
            dbManager.addNewUser(userDetails);
            log.info("Added new user: " + userDetails.get("username") + ".AccountType: " + userDetails.get("accountType"));
        } catch (EntityExistsException ex) {
            log.info("Can't add user: " + userDetails.get("username") + ". Already exists!");

        }

    }

}
