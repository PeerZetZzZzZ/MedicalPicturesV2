/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.controller.model.rest.common;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.orm.DBBodyPartManager;
import medicalpictures.model.orm.DBPatientManager;
import medicalpictures.model.orm.DBPictureTypeManager;
import medicalpictures.model.orm.DBUserManager;
import medicalpictures.model.security.UserSecurityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private DBPictureTypeManager pictureTypeManager;
    @EJB
    private DBPatientManager patientManager;

    @EJB
    private JsonFactory jsonFactory;

    @EJB
    private DBUserManager userManager;

    private Log logger = LogFactory.getLog(MedicalPicturesCommonResource.class);

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
                String username = securityManager.getLoggedUsername().toString();
                logger.info("Returned logged user: " + username);
                return username;

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
    @Path("/getAllBodyParts")
    @Produces("application/json")
    public String getBodyParts() {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
            String allBodyParts = bodyPartManager.getAllBodyParts().toString();
            logger.info("Returned all body parts: " + allBodyParts);
            return allBodyParts;

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
    @Path("/getAllUsernames")
    @Produces("application/json")
    public String getAllUsernames() {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
            String allUsernames = userManager.getAllUsernames().toString();
            logger.info("Return all usernames: " + allUsernames);
            return allUsernames;
        } catch (NoLoggedUserExistsHere ex) {
            return jsonFactory.notUserLogged();
        } catch (UserNotPermitted ex) {
            return jsonFactory.userNotPermitted();
        }
    }

    /**
     * Returns the details of given username
     *
     * @param username
     * @return
     */
    @GET
    @Path("/getUserInfo/{username}")
    @Produces("application/json")
    public String getUserInfo(@PathParam("username") String username) {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
            Map<String, String> userDetailsMap = userManager.getUserDetails(username);
            String userDetailsJson = jsonFactory.getUserDetailsAsJson(userDetailsMap);
            logger.info("Return user info: " + userDetailsJson);
            return userDetailsJson;
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
    @Path("/getAllPictureTypes")
    @Produces("application/json")
    public String getAllPictureTypes() {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
            String pictureTypes = pictureTypeManager.getAllPictureTypes().toString();
            logger.info("Returned picture types: " + pictureTypes);
            return pictureTypes;
        } catch (NoLoggedUserExistsHere ex) {
            return jsonFactory.notUserLogged();
        } catch (UserNotPermitted ex) {
            return jsonFactory.userNotPermitted();
        }
    }

    @GET
    @Path("/getAllPatients")
    @Produces("application/json")
    public String getAllPatients() {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN);
            String pictureTypes = patientManager.getAllPatients().toString();
            logger.info("Returned all patients: " + pictureTypes);
            return pictureTypes;
        } catch (NoLoggedUserExistsHere ex) {
            return jsonFactory.notUserLogged();
        } catch (UserNotPermitted ex) {
            return jsonFactory.userNotPermitted();
        }
    }
}
