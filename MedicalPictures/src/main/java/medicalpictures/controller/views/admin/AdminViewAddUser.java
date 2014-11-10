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
import javax.persistence.EntityExistsException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.controller.views.common.LoginView;
import medicalpictures.model.admin.AdminOperationResponse;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.AddUserFailed;
import medicalpictures.model.exception.JsonParsingException;
import medicalpictures.model.orm.DbManager;
import medicalpictures.model.security.UserSecurityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
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
    @EJB
    private AdminOperationResponse adminResponse;

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
        String username = "";
        try {
            Map<String, String> userDetails = jsonFactory.readUser(user);
            username = userDetails.get("username");
            System.out.println("Dlugosc: " + userDetails.get("password").length());
            dbManager.addNewUser(userDetails);
            response.getWriter().write(adminResponse.userAddedSuccessfully(username));
            log.info("Added new user: " + username + ".AccountType: " + userDetails.get("accountType"));
        } catch (AddUserFailed ex) {
            response.getWriter().write(adminResponse.userAddedFailed(username));
            log.error(username + "-" + ex);
        } catch (JsonParsingException ex) {
            response.getWriter().write(adminResponse.userAddedFailed(username));
            log.error("Json Parsing problem" + "-" + ex);
        }

    }

}
