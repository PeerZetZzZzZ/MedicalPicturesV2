package medicalpictures.controller.views.common;

import java.io.IOException;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.security.UserSecurityManager;

/**
 *
 * @author PeerZet
 */
public class UserSettings extends HttpServlet {

    @EJB
    private UserSecurityManager securityManager;
    private static final Logger LOG = Logger.getLogger(UserSettings.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            System.out.println("wlaza");
            securityManager.checkUserPermissionToAnyContent();
            request.getRequestDispatcher("/WEB-INF/common/UserSettings.html").forward(request, response);
        } catch (UserNotPermitted ex) {
            LOG.warning("GET " + UserSettings.class.toString() + " :No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            LOG.warning("GET " + UserSettings.class.toString() + " : No logged user exists!");

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
