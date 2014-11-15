/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.controller.views.admin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jdk.nashorn.internal.parser.JSONParser;
import medicalpictures.controller.views.admin.AdminViewAddUser;
import medicalpictures.model.admin.AdminOperationResponse;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.AddBodyPartFailed;
import medicalpictures.model.exception.AddPictureTypeFailed;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.orm.DBPictureTypeManager;
import medicalpictures.model.security.UserSecurityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

/**
 *
 * @author PeerZet
 */
public class AdminViewManageBodyParts extends HttpServlet {

    @EJB
    private UserSecurityManager securityManager;

    @EJB
    private DBPictureTypeManager pictureTypeManager;

    @EJB
    private JsonFactory jsonFactory;

    @EJB
    private AdminOperationResponse adminResponse;

    private Log log = LogFactory.getLog(AdminViewManagePictureTypes.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
            request.getRequestDispatcher("/WEB-INF/admin/adminViewManageBodyParts.html").forward(request, response);
        } catch (UserNotPermitted ex) {
            log.error("GET " + AdminViewManageBodyParts.class.toString() + ": No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            log.error("GET " + AdminViewManageBodyParts.class.toString() + ": No logged user exists!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bodyPartString = "";
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
            JSONObject bodyPart = jsonFactory.decryptRequest(request);
            bodyPartString = jsonFactory.getBodyPart(bodyPart);
            pictureTypeManager.addBodyPart(bodyPartString);
            response.getWriter().write(adminResponse.bodyPartAddedSuccessfully(bodyPartString));
        } catch (UserNotPermitted ex) {
            log.error("POST " + AdminViewManageBodyParts.class.toString() + ": No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            log.error("POST " + AdminViewManageBodyParts.class.toString() + ": No logged user exists!");
        } catch (AddBodyPartFailed ex) {
            log.error("POST " + AdminViewManageBodyParts.class.toString() + "-"
                    + bodyPartString + ": Body part creation fail!");
            response.getWriter().write(adminResponse.bodyPartAddedFailed(bodyPartString));
        }
    }

}
