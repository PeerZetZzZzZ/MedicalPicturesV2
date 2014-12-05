/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.controller.views.admin;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.model.admin.AdminOperationResponse;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.AddNewUserFailed;
import medicalpictures.model.exception.AddToDbFailed;
import medicalpictures.model.exception.JsonParsingException;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.dao.UserDAO;
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
    private UserDAO dbManager;
    @EJB
    private AdminOperationResponse adminResponse;

    private final Log log = LogFactory.getLog(AdminViewAddUser.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            manager.checkUserPermissionToThisContent(AccountType.ADMIN);
            request.getRequestDispatcher("/WEB-INF/admin/adminViewAddUser.html").forward(request, response);
        } catch (UserNotPermitted ex) {
            log.error("GET " + AdminViewAddUser.class.toString() + ": No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            log.error("GET " + AdminViewAddUser.class.toString() + ": No logged user exists!");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            manager.checkUserPermissionToThisContent(AccountType.ADMIN);
            JSONObject user = jsonFactory.decryptRequest(request);
            String username = "";
            try {
                Map<String, String> userDetails = jsonFactory.readUserFromJson(user);
                username = userDetails.get("username");
                dbManager.addNewUser(userDetails);
                response.getWriter().write(adminResponse.userAddedSuccessfully(username));
                log.info("Added new user: " + username + ".AccountType: " + userDetails.get("accountType"));
            } catch (AddNewUserFailed ex) {
                response.getWriter().write(adminResponse.userAddedFailed(username));
                log.error(username + "-" + ex);
            } catch (JsonParsingException ex) {
                response.getWriter().write(adminResponse.userAddedFailed(username));
                log.error("Json Parsing problem" + "-" + ex);
            }

        } catch (UserNotPermitted ex) {
            log.error("POST" + AdminViewAddUser.class.toString() + " : No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            log.error("POST" + AdminViewAddUser.class.toString() + ": No logged user exists!");
        }

    }

}
