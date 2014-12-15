package medicalpictures.controller.views.admin;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.security.UserSecurityManager;

/**
 *
 * @author PeerZet
 */
public class AdminViewManagePictureTypes extends HttpServlet {

    @EJB
    private UserSecurityManager securityManager;

    @EJB
    private MedicalLogger logger;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
            request.getRequestDispatcher("/WEB-INF/admin/adminViewManagePictureTypes.html").forward(request, response);
        } catch (UserNotPermitted ex) {
            logger.logError("User not permitted to access /AdminViewManagePictureTypes !", AdminViewManagePictureTypes.class, ex);
            request.getRequestDispatcher("/WEB-INF/common/NotAuthorizedView.html").forward(request, response);
        } catch (NoLoggedUserExistsHere ex) {
            logger.logError("User is not logged - can't access /AdminViewManagePictureTypes !", AdminViewManagePictureTypes.class, ex);
            request.getRequestDispatcher("/WEB-INF/common/NotAuthorizedView.html").forward(request, response);
        }
    }

	//addPictureType
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

}
