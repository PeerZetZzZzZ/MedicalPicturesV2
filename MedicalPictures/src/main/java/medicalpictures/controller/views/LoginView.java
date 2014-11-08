package medicalpictures.controller.views;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.exception.UserAlreadyLoggedException;
import medicalpictures.model.exception.UserDoesntExistException;
import medicalpictures.model.login.LoginValidator;
import medicalpictures.model.security.UserSessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

/**
 * LoginView servlet responsible for logging to the application.
 *
 * @author PeerZet
 */
public class LoginView extends HttpServlet {

    @EJB
    private UserSessionManager manager;
    @EJB
    private JsonFactory jsonReader;
    @EJB
    private LoginValidator loginValidator;

    private Log log = LogFactory.getLog(UserSessionManager.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/common/LoginView.html").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request`
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSONObject jsonObject = jsonReader.decryptRequest(request);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        try {
            manager.registerUser(username, password);
            log.info("User: " + username + " has logged successfully");
            String message = loginValidator.loginSucceed(username);
            System.out.println("Send response: "+message);
            response.getWriter().write(message);
        } catch (UserAlreadyLoggedException ex) {
            String message = loginValidator.loginFailedUserAlreadyLogged(username);
            System.out.println("Send response: "+message);
            response.getWriter().write(message);
            log.trace(ex);
        } catch (UserDoesntExistException ex) {
            String message = loginValidator.loginFailedAuthenticationFailed(username);
            System.out.println("Send response: "+message);
            response.getWriter().write(message);
            log.trace(ex);
        }
    }
}
