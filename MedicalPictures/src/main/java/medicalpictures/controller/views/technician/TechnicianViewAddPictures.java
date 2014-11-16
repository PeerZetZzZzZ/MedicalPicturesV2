/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.controller.views.technician;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.controller.views.admin.AdminViewManageUsers;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.security.UserSecurityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author PeerZet
 */
public class TechnicianViewAddPictures extends HttpServlet {

    @EJB
    private UserSecurityManager securityManager;

    private Log logger = LogFactory.getLog(TechnicianViewAddPictures.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN);
            request.getRequestDispatcher("/WEB-INF/technician/technicianViewAddPictures.html").forward(request, response);
        } catch (UserNotPermitted ex) {
            logger.error("GET " + TechnicianViewAddPictures.class.toString() + " :No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            logger.error("GET " + TechnicianViewAddPictures.class.toString() + " : No logged user exists!");

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN);
        } catch (UserNotPermitted ex) {
            logger.error("POST " + TechnicianViewAddPictures.class.toString() + " :No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            logger.error("POST " + TechnicianViewAddPictures.class.toString() + " : No logged user exists!");
        }
    }

}
