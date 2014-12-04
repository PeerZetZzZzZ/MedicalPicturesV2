/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.controller.views.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.orm.UserDAO;
import medicalpictures.model.security.UserSecurityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

/**
 *
 * @author PeerZet
 */
public class AdminViewDeleteUsers extends HttpServlet {

    @EJB
    private UserSecurityManager securityManager;
    @EJB
    private UserDAO userManager;
    @EJB
    private JsonFactory jsonFactory;

    private final Log log = LogFactory.getLog(AdminViewDeleteUsers.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
            JSONObject usersToDelete = jsonFactory.decryptRequest(request);
            userManager.deleteUsers(jsonFactory.readUsersToDelete(usersToDelete));
        } catch (UserNotPermitted ex) {
            log.error("POST" + AdminViewDeleteUsers.class.toString() + " : No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            log.error("POST" + AdminViewDeleteUsers.class.toString() + ": No logged user exists!");
        }
    }

}
