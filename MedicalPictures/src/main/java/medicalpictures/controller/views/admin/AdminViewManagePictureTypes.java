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
import medicalpictures.model.exception.AddPictureTypeFailed;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.dao.PictureTypeDAO;
import medicalpictures.model.security.UserSecurityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

/**
 *
 * @author PeerZet
 */
public class AdminViewManagePictureTypes extends HttpServlet {

    @EJB
    private UserSecurityManager securityManager;

    @EJB
    private PictureTypeDAO pictureTypeManager;

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
            request.getRequestDispatcher("/WEB-INF/admin/adminViewManagePictureTypes.html").forward(request, response);
        } catch (UserNotPermitted ex) {
            log.error("GET " + AdminViewManagePictureTypes.class.toString() + ": No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            log.error("GET " + AdminViewManagePictureTypes.class.toString() + ": No logged user exists!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pictureTypeString = "";
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
            JSONObject pictureType = jsonFactory.decryptRequest(request);
            pictureTypeString = jsonFactory.getPictureType(pictureType);
            pictureTypeManager.addPictureType(pictureTypeString);
            response.getWriter().write(adminResponse.pictureTypeAddedSuccessfully(pictureTypeString));
        } catch (UserNotPermitted ex) {
            log.error("POST " + AdminViewManagePictureTypes.class.toString() + ": No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            log.error("POST " + AdminViewManagePictureTypes.class.toString() + ": No logged user exists!");
        } catch (AddPictureTypeFailed ex) {
            log.error("POST " + AdminViewManagePictureTypes.class.toString() + "-"
                    + pictureTypeString + ": Picture type creation fail!");
            response.getWriter().write(adminResponse.pictureTypeAddedFailed(pictureTypeString));
        }
    }

}
