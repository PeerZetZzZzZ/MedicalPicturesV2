/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.controller.model.rest.common;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.xml.bind.DatatypeConverter;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.dao.BodyPartDAO;
import medicalpictures.model.dao.PatientDAO;
import medicalpictures.model.dao.PictureDAO;
import medicalpictures.model.dao.PictureTypeDAO;
import medicalpictures.model.dao.UserDAO;
import medicalpictures.model.orm.entity.Picture;
import medicalpictures.model.security.UserSecurityManager;
import medicalpictures.model.technician.TechnicianOperationResponse;
import org.apache.commons.io.IOUtils;
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
    private BodyPartDAO bodyPartManager;

    @EJB
    private PictureTypeDAO pictureTypeDAO;

    @EJB
    private PatientDAO patientDAO;

    @EJB
    private PictureDAO pictureDAO;

    @EJB
    private JsonFactory jsonFactory;

    @EJB
    private UserDAO userManager;

    @EJB
    private TechnicianOperationResponse technicianOperationResponse;

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
            securityManager.checkUserPermissionToThisContent(AccountType.ADMIN, AccountType.TECHNICIAN);
            String allBodyParts = bodyPartManager.getAllBodyParts().toString();
            logger.info("Returned all body parts: " + allBodyParts);
            return allBodyParts;

        } catch (NoLoggedUserExistsHere ex) {
            System.out.println("NoLoggedUserExistsHere - return " + jsonFactory.notUserLogged());
            return jsonFactory.notUserLogged();
        } catch (UserNotPermitted ex) {
            System.out.println("UserNotPermitted - return " + jsonFactory.userNotPermitted());
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
            System.out.println(allUsernames.toString());
            return allUsernames;
        } catch (NoLoggedUserExistsHere ex) {
            System.out.println("user not logged");
            return jsonFactory.notUserLogged();
        } catch (UserNotPermitted ex) {
            System.out.println("user not permitted");
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
            securityManager.checkUserPermissionToThisContent(AccountType.ADMIN, AccountType.TECHNICIAN);
            String pictureTypes = pictureTypeDAO.getAllPictureTypes().toString();
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
//        try {
//            securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN);
        String pictureTypes = patientDAO.getAllPatients().toString();
        logger.info("Returned all patients: " + pictureTypes);
        return pictureTypes;
//        } catch (NoLoggedUserExistsHere ex) {
//            return jsonFactory.notUserLogged();
//        } catch (UserNotPermitted ex) {
//            return jsonFactory.userNotPermitted();
//        }
    }

    @GET
    @Path("/getPictureData")
    @Produces("application/json")
    public String getPictureData() throws FileNotFoundException, IOException {
        JSONObject json = new JSONObject();
        File file = new File("zdjecie.jpg");
        BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        img.createGraphics().drawImage(ImageIO.read(file).getScaledInstance(200, 200, Image.SCALE_SMOOTH), 0, 0, null);
        ImageIO.write(img, "jpg", new File("zdjecieThumb.jpg"));
        File thumbnail = new File("zdjecieThumb.jpg");
        FileInputStream fileStream = new FileInputStream(file);
        String imageString = "data:image/png;base64,"
                + DatatypeConverter.printBase64Binary(IOUtils.toByteArray(fileStream));
        json.put("zdjecie", imageString);
        return json.toString();
    }

    @GET
    @Path("/getBigPictureData")
    @Produces("application/json")
    public String getBigPictureData() throws FileNotFoundException, IOException {
        JSONObject json = new JSONObject();
        System.out.println("zwracam duze");
        File file = new File("zdjecie.jpg");
        FileInputStream fileStream = new FileInputStream(file);
        String imageString = "data:image/png;base64,"
                + DatatypeConverter.printBase64Binary(IOUtils.toByteArray(fileStream));
        json.put("zdjecie", imageString);
        return json.toString();
    }

    @GET
    @Path("/getAllPictures")
    @Produces("application/json")
    public String getAllPictures() {
        List<Picture> picturesList = pictureDAO.getAllPictureList();
        if (picturesList == null) {
            picturesList = new ArrayList<Picture>();
        }
        return technicianOperationResponse.getAllPicturesResponse(picturesList);
    }
}
