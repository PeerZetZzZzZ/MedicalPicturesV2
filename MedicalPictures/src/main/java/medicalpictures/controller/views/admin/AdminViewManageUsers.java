package medicalpictures.controller.views.admin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.dao.ManagerDAO;
import medicalpictures.model.security.UserSecurityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author PeerZet
 */
public class AdminViewManageUsers extends HttpServlet {

    @Inject
    public ManagerDAO ormManager;
    @EJB
    private UserSecurityManager manager;

    private final Log log = LogFactory.getLog(AdminViewManageUsers.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            manager.checkUserPermissionToThisContent(AccountType.ADMIN);
            request.getRequestDispatcher("/WEB-INF/admin/adminViewManageUsers.html").forward(request, response);
        } catch (UserNotPermitted ex) {
            log.error("GET " + AdminViewManageUsers.class.toString() + " :No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            log.error("GET " + AdminViewManageUsers.class.toString() + " : No logged user exists!");

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            manager.checkUserPermissionToThisContent(AccountType.ADMIN);
            
        } catch (UserNotPermitted ex) {
            log.error("POST " + AdminViewManageUsers.class.toString() + " :No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            log.error("POST " + AdminViewManageUsers.class.toString() + " : No logged user exists!");
        }
    }

}
