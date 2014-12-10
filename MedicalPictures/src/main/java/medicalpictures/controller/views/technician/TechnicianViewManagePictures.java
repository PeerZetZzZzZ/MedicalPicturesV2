package medicalpictures.controller.views.technician;

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
public class TechnicianViewManagePictures extends HttpServlet {

	@EJB
	private UserSecurityManager securityManager;

	@EJB
	private MedicalLogger logger;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN);
			request.getRequestDispatcher("/WEB-INF/technician/technicianViewManagePictures.html").forward(request, response);
		} catch (UserNotPermitted ex) {
			logger.logError("User not permitted to access /TechnicianViewManagePictures !", TechnicianViewManagePictures.class, ex);
			request.getRequestDispatcher("/WEB-INF/common/NotAuthorizedView.html").forward(request, response);
		} catch (NoLoggedUserExistsHere ex) {
			logger.logError("User is not logged - can't access /TechnicianViewManagePictures!", TechnicianViewManagePictures.class, ex);
			request.getRequestDispatcher("/WEB-INF/common/NotAuthorizedView.html").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
