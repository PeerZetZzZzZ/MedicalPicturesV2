/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.controller.model.rest.common;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.orm.DBBodyPartManager;
import medicalpictures.model.security.UserSecurityManager;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author PeerZet
 */
@Path("MedicalPicturesCommon")
//Zeby sie dostac localhost:8080:/MedicalPictures/webresources/MedicalPicturesCommon/getUsername
public class MedicalPicturesCommonResource {

    @Context
    private UriInfo context;

    @EJB
    private UserSecurityManager securityManager;

    @EJB
    private DBBodyPartManager bodyPartManager;

    @EJB
    private JsonFactory jsonFactory;

    /**
     * Creates a new instance of MedicalPicturesCommonResource
     */
    public MedicalPicturesCommonResource() {
    }

    /**
     * Retrieves representation of an instance of
     * medicalpictures.controller.model.rest.common.MedicalPicturesCommonResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/getLoggedUser")
    @Produces("application/json")
    public String getLoggedUser() {
        try {
            if (securityManager.checkUserPermissionToAnyContent()) {
                JSONObject user = new JSONObject();
                user.put("username", securityManager.getLoggedUsername());
                return user.toString();
            } else {
                return jsonFactory.userNotPermitted();
            }
        } catch (NoLoggedUserExistsHere ex) {
            return jsonFactory.notUserLogged();
        } catch (UserNotPermitted ex) {
            return jsonFactory.userNotPermitted();
        }
    }

    /**
     * Retrieves representation of an instance of
     * medicalpictures.controller.model.rest.common.MedicalPicturesCommonResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/getLoggedBodyParts")
    @Produces("application/json")
    public String getBodyParts() {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
            JSONObject bodyParts = new JSONObject();
            bodyParts.put("bodyParts", bodyPartManager.getBodyParts());
            return bodyParts.toString();
        } catch (NoLoggedUserExistsHere ex) {
            return jsonFactory.notUserLogged();
        } catch (UserNotPermitted ex) {
            return jsonFactory.userNotPermitted();
        }
    }
}
