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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.xml.bind.DatatypeConverter;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.dao.BodyPartDAO;
import medicalpictures.model.dao.DefinedPictureDescriptionDAO;
import medicalpictures.model.dao.PatientDAO;
import medicalpictures.model.dao.PictureDAO;
import medicalpictures.model.dao.PictureTypeDAO;
import medicalpictures.model.dao.UserDAO;
import medicalpictures.model.exception.AddToDbFailed;
import medicalpictures.model.orm.entity.Picture;
import medicalpictures.model.orm.entity.PictureDescription;
import medicalpictures.model.security.UserSecurityManager;
import medicalpictures.model.technician.TechnicianOperationResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
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
    private UserDAO userDAO;

    @EJB
    private TechnicianOperationResponse technicianOperationResponse;

    @EJB
    private DefinedPictureDescriptionDAO definedPictureDescriptionDAO;

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
            String allUsernames = userDAO.getAllUsernames().toString();
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
            Map<String, String> userDetailsMap = userDAO.getUserDetails(username);
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
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN, AccountType.ADMIN);
            List<Picture> picturesList = pictureDAO.getAllPictureList();
            if (picturesList == null) {
                picturesList = new ArrayList<Picture>();
            }
            return technicianOperationResponse.getAllPicturesResponse(picturesList);
        } catch (UserNotPermitted ex) {
            Logger.getLogger(MedicalPicturesCommonResource.class.getName()).log(Level.SEVERE, null, ex);
            return jsonFactory.notUserLogged();
        } catch (NoLoggedUserExistsHere ex) {
            Logger.getLogger(MedicalPicturesCommonResource.class.getName()).log(Level.SEVERE, null, ex);
            return jsonFactory.userNotPermitted();
        }
    }

    @POST
    @Path("/deletePictures")
    @Produces("application/json")
    @Consumes("application/json")
    public String deletePictures(String picturesList) {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN, AccountType.ADMIN);
            List<String> picturesIdList = jsonFactory.getPicturesToDeleteList(picturesList);
            pictureDAO.removePictures(picturesIdList);
            return "";
        } catch (UserNotPermitted ex) {
            Logger.getLogger(MedicalPicturesCommonResource.class.getName()).log(Level.SEVERE, null, ex);
            return jsonFactory.notUserLogged();
        } catch (NoLoggedUserExistsHere ex) {
            Logger.getLogger(MedicalPicturesCommonResource.class.getName()).log(Level.SEVERE, null, ex);
            return jsonFactory.userNotPermitted();
        }

    }

    @POST
    @Path("/updatePicture")
    @Produces("application/json")
    public String updatePicture(String pictureValues) {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN, AccountType.ADMIN);
            JSONObject pictureDetailsJson = jsonFactory.decryptRequest(pictureValues);
            Map<String, String> pictureDetailsMap = jsonFactory.getEditPictureValues(pictureDetailsJson);
            pictureDAO.updatePicture(pictureDetailsMap);
            return "";
        } catch (UserNotPermitted ex) {
            Logger.getLogger(MedicalPicturesCommonResource.class.getName()).log(Level.SEVERE, null, ex);
            return jsonFactory.notUserLogged();
        } catch (NoLoggedUserExistsHere ex) {
            Logger.getLogger(MedicalPicturesCommonResource.class.getName()).log(Level.SEVERE, null, ex);
            return jsonFactory.userNotPermitted();
        } catch (AddToDbFailed ex) {
            Logger.getLogger(MedicalPicturesCommonResource.class.getName()).log(Level.SEVERE, null, ex);
            return jsonFactory.insertToDbFailed();
        }

    }

    /**
     * Returns the patient pictures, but only description ( no picture or
     * thumbnail data )
     *
     * @param patientUsername
     * @return
     */
    @GET
    @Path("getPatientPicturesNames/{patientUsername}")
    @Produces("application/json")
    public String getPatientPicturesNames(@PathParam("patientUsername") String patientUsername) {
        Set<Picture> patientPictures = patientDAO.getPatientPictures(patientUsername);
        String picturesNames = jsonFactory.getPicturesNames(patientPictures);
        logger.info("Returning patient '" + patientUsername + "' pictures names: " + picturesNames);
        return picturesNames;

    }

    @GET
    @Path("getPatientPictureWithThumbnail/{pictureId}")
    @Produces("application/json")
    public String getPatientPictureWithThumbnail(@PathParam("pictureId") String pictureId) {
        try {
            Picture picture = pictureDAO.getPictureById(pictureId);
            String pictureDetails = jsonFactory.getPictureDetails(picture);
            logger.info("Retriving picture and thumbnail for '" + pictureId + "' result. Returned: " + pictureDetails);
            return pictureDetails;
        } catch (IOException ex) {
            Logger.getLogger(MedicalPicturesCommonResource.class.getName()).log(Level.SEVERE, null, ex);
            return jsonFactory.insertToDbFailed();
        }

    }

    @GET
    @Path("getFullPictureData/{pictureId}")
    @Produces("application/json")
    public String getFullPictureData(@PathParam("pictureId") String pictureId) {
        try {
            Picture picture = pictureDAO.getPictureById(pictureId);
            if (picture != null) {
                return jsonFactory.getFullPictureData(picture);
            } else {
                logger.error("Retrieving full picture data failed for picture '" + pictureId + "'. Picture not found.");
                return jsonFactory.notObjectFound();
            }
        } catch (IOException ex) {
            Logger.getLogger(MedicalPicturesCommonResource.class.getName()).log(Level.SEVERE, null, ex);
            return jsonFactory.internalServerProblem();
        }
    }

    @POST
    @Path("savePictureDescription")
    @Consumes("application/json")
    @Produces("application/json")
    public String savePictureDescription(String pictureDescriptionDetails) {
        JSONObject pictureDescriptionJson = jsonFactory.decryptRequest(pictureDescriptionDetails);
        return pictureDAO.savePictureOrUpdateDescription(pictureDescriptionJson);
    }

    @GET
    @Path("getDefinedPictureDescriptions")
    @Produces("application/json")
    public String getDefinedPictureDescriptions() {
        return definedPictureDescriptionDAO.getDefinedPictureDescription();
    }

    @GET
    @Path("getPictureDescriptions/{pictureId}")
    @Produces("application/json")
    public String getPictureDescriptions(@PathParam("pictureId") String pictureId) {
        String patientUsername = (String) SecurityUtils.getSubject().getSession().getAttribute("username");
        return patientDAO.getPatientPictureDescriptions(patientUsername, pictureId);
    }
}
