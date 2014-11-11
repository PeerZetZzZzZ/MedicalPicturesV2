/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.controller.views.common;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.security.UserSecurityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author PeerZet
 */
public class Logout extends HttpServlet {

    @EJB
    UserSecurityManager manager;

    private final Log log = LogFactory.getLog(Logout.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            manager.logoutUser();
            response.sendRedirect("/MedicalPictures/LoginView");
        } catch (NoLoggedUserExistsHere ex) {
            log.error("GET " + Logout.class.toString() + ": No logged user exists!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

}
