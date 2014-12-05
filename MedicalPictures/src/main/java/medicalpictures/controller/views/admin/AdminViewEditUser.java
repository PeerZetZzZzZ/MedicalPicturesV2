/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.controller.views.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.exception.AddToDbFailed;
import medicalpictures.model.exception.JsonParsingException;
import medicalpictures.model.dao.UserDAO;
import medicalpictures.model.dao.ManagerDAO;
import medicalpictures.model.security.UserSecurityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

/**
 * Responsible for redirecting to editing user window
 *
 * @author PeerZet
 */
public class AdminViewEditUser extends HttpServlet {

    @EJB
    private UserSecurityManager securityManager;
    @EJB
    private JsonFactory jsonFactory;

    @EJB
    private UserDAO userManager;

    private Log logger = LogFactory.getLog(AdminViewEditUser.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            JSONObject userToEdit = jsonFactory.decryptRequest(request);
            Map<String, String> userDetails = jsonFactory.readUserFromJson(userToEdit);
            userManager.editUser(userDetails);
            logger.info("User: " + userDetails.get("username") + " successfully edited!");
        } catch (JsonParsingException ex) {
            logger.error(ex);
        } catch (AddToDbFailed ex) {
            Logger.getLogger(AdminViewEditUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
