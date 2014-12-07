package medicalpictures.controller.views.patient;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class PatientViewManagePictures extends HttpServlet {

    @EJB
    private UserSecurityManager securityManager;

    private Log logger = LogFactory.getLog(PatientViewManagePictures.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            System.out.println("dupa");
            securityManager.checkUserPermissionToThisContent(AccountType.PATIENT);
            request.getRequestDispatcher("/WEB-INF/patient/patientViewManagePictures.html").forward(request, response);
        } catch (UserNotPermitted ex) {
            logger.error("GET " + PatientViewManagePictures.class.toString() + " :No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            logger.error("GET " + PatientViewManagePictures.class.toString() + " : No logged user exists!");

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
