package medicalpictures.controller.views.doctor;

import java.io.IOException;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.controller.views.technician.TechnicianViewAddPictures;
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
public class DoctorViewManageDescriptions extends HttpServlet {

    @EJB
    private UserSecurityManager securityManager;

    private Log logger = LogFactory.getLog(DoctorViewManageDescriptions.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.DOCTOR);
            request.getRequestDispatcher("/WEB-INF/doctor/doctorViewManageDescriptions.html").forward(request, response);
        } catch (UserNotPermitted ex) {
            logger.error("GET " + DoctorViewManageDescriptions.class.toString() + " :No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            logger.error("GET " + DoctorViewManageDescriptions.class.toString() + " : No logged user exists!");

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("dupa");

    }
}
