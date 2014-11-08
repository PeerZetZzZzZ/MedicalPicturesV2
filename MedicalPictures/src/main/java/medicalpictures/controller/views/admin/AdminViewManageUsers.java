package medicalpictures.controller.views.admin;

import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.orm.OrmManager;
import medicalpictures.model.security.UserSecurityManager;

/**
 *
 * @author PeerZet
 */
public class AdminViewManageUsers extends HttpServlet {

    @Inject
    public OrmManager ormManager;
    @EJB
    private UserSecurityManager manager;

  
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        manager.checkUserPermissionToThisContent(AccountType.ADMIN);
        request.getRequestDispatcher("/WEB-INF/admin/adminViewManageUsers.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

}
