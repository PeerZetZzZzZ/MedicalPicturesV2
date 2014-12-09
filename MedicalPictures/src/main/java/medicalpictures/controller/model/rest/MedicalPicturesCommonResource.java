package medicalpictures.controller.model.rest;

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
import medicalpictures.model.admin.AdminOperationResponse;
import medicalpictures.model.common.BodyPartJsonFactory;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.common.PatientJsonFactory;
import medicalpictures.model.common.PictureTypeJsonFactory;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.common.UserJsonFactory;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.dao.BodyPartDAO;
import medicalpictures.model.dao.DefinedPictureDescriptionDAO;
import medicalpictures.model.dao.ManagerDAO;
import medicalpictures.model.dao.PatientDAO;
import medicalpictures.model.dao.PictureDAO;
import medicalpictures.model.dao.PictureTypeDAO;
import medicalpictures.model.dao.UserDAO;
import medicalpictures.model.exception.AddNewUserFailed;
import medicalpictures.model.exception.AddToDbFailed;
import medicalpictures.model.exception.JsonParsingException;
import medicalpictures.model.orm.entity.BodyPart;
import medicalpictures.model.orm.entity.Patient;
import medicalpictures.model.orm.entity.Picture;
import medicalpictures.model.orm.entity.PictureType;
import medicalpictures.model.orm.entity.User;
import medicalpictures.model.security.UserSecurityManager;
import medicalpictures.model.technician.TechnicianOperationResponse;
import org.apache.commons.io.IOUtils;
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
	private BodyPartDAO bodyPartDAO;

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
	private ManagerDAO managerDAO;

	@EJB
	private TechnicianOperationResponse technicianOperationResponse;

	@EJB
	private AdminOperationResponse adminOperationResponse;

	@EJB
	private DefinedPictureDescriptionDAO definedPictureDescriptionDAO;

	@EJB
	private BodyPartJsonFactory bodyPartJsonFactory;

	@EJB
	private PictureTypeJsonFactory pictureTypeJsonFactory;

	@EJB
	private UserJsonFactory userJsonFactory;

	@EJB
	private PatientJsonFactory patientJsonFactory;

	@EJB
	private MedicalLogger medicalLogger;

	private static final Logger logger = Logger.getLogger(MedicalPicturesCommonResource.class.getName());

	public MedicalPicturesCommonResource() {
	}

	/**
	 * Gets the username of currently logged user in application.
	 *
	 * @return an instance of java.lang.String
	 */
	@GET
	@Path("/getLoggedUser")
	@Produces("application/json")
	public String getLoggedUser() {
		try {
			securityManager.checkUserPermissionToAnyContent();
			String username = securityManager.getLoggedUsername();
			medicalLogger.logInfo("Returned logged user: " + username, MedicalPicturesCommonResource.class);
			return jsonFactory.getLoggedUser(username);
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /getLoggedUser !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /getLoggedUser !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		}
	}

	/**
	 * Gets all body parts used in application.
	 *
	 * @return
	 */
	@GET
	@Path("/getAllBodyParts")
	@Produces("application/json")
	public String getBodyParts() {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.ADMIN, AccountType.TECHNICIAN);
			List<BodyPart> bodyPartList = bodyPartDAO.getAllBodyParts();
			String bodyParts = bodyPartJsonFactory.getBodyParts(bodyPartList);
			medicalLogger.logInfo("Returned all body parts: " + bodyParts, MedicalPicturesCommonResource.class);
			return bodyParts;
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /getAllBodyParts !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /getAllBodyParts !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
		}
	}

	/**
	 * Gets all the usernames of users in application.
	 *
	 * @return an instance of java.lang.String
	 */
	@GET
	@Path("/getAllUsernames")
	@Produces("application/json")
	public String getAllUsernames() {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
			List<User> usersList = userDAO.getAllUsernames();
			String usernames = userJsonFactory.getAllUsernames(usersList);
			medicalLogger.logInfo("Return all usernames: " + usernames, MedicalPicturesCommonResource.class);
			return usernames;
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /getAllUsernames !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /getAllUsernames !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
		}
	}

	/**
	 * Returns all picture types used in application.
	 *
	 * @return an instance of java.lang.String
	 */
	@GET
	@Path("/getAllPictureTypes")
	@Produces("application/json")
	public String getAllPictureTypes() {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.ADMIN, AccountType.TECHNICIAN);
			List<PictureType> pictureTypeList = pictureTypeDAO.getAllPictureTypes();
			String pictureTypes = pictureTypeJsonFactory.getAllPictureTypes(pictureTypeList);
			medicalLogger.logInfo("Return all picture types: " + pictureTypes, MedicalPicturesCommonResource.class);
			return pictureTypes;
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /getAllPictureTypes !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /getAllPictureTypes !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
		}
	}

	@GET
	@Path("/getUserInfo/{username}")
	@Produces("application/json")
	public String getUserInfo(@PathParam("username") String username) {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
			Map<String, String> userDetailsMap = userDAO.getUserDetails(username);
			String userInfo = userJsonFactory.getUserDetailsAsJson(userDetailsMap);
			medicalLogger.logInfo("Return user info of user '" + username + "' :" + userInfo, MedicalPicturesCommonResource.class);
			return userInfo;
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /getUserInfo/{username} !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /getUserInfo/{username} !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
		}
	}

	@GET
	@Path("/getAllPatients")
	@Produces("application/json")
	public String getAllPatients() {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN, AccountType.DOCTOR, AccountType.ADMIN);
			List<Patient> patientList = patientDAO.getAllPatients();
			String patients = patientJsonFactory.getAllPatients(patientList);
			medicalLogger.logInfo("Returned all patients: " + patients, MedicalPicturesCommonResource.class);
			return patients;
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /getAllPatients !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /getAllPatients !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
		}
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
	 * Returns the patient pictures, but only description ( no picture or thumbnail data )
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
				logger.warning("Retrieving full picture data failed for picture '" + pictureId + "'. Picture not found.");
				return jsonFactory.notObjectFound();
			}
		} catch (IOException ex) {
			Logger.getLogger(MedicalPicturesCommonResource.class.getName()).log(Level.SEVERE, null, ex);
			return jsonFactory.internalServerProblemResponse();
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

	@POST
	@Path("addNewUser")
	@Produces("application/json")
	public String addNewUser(String newUserData) {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
			JSONObject user = jsonFactory.decryptRequest(newUserData);
			String username = "";
			try {
				Map<String, String> userDetails = jsonFactory.readUserFromJson(user);
				username = userDetails.get("username");
				userDAO.addNewUser(userDetails);
				medicalLogger.logInfo("Added new user: " + username + ".AccountType: " + userDetails.get("accountType"), MedicalPicturesCommonResource.class);
				return adminOperationResponse.userAddedSuccessfully(username);
			} catch (AddNewUserFailed ex) {
				medicalLogger.logError("Adding user '" + username + "' failed. User already exists", MedicalPicturesCommonResource.class, ex);
				return adminOperationResponse.userAddedFailed(username);
			} catch (JsonParsingException ex) {
				medicalLogger.logError("addNewUser: Parsing input json problem: " + newUserData, MedicalPicturesCommonResource.class, ex);
				return jsonFactory.getOperationResponseByCode(ResultCodes.INPUT_JSON_PARSE_ERROR);
			}

		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /addNewUser !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.USER_UNAOTHRIZED);
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /addNewUser!", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.USER_IS_NOT_LOGGED);
		}
	}

//    @GET
//    @Path("getLoggedUserInfo")
//    @Produces("application/json")
//    public String getLoggedUserInfo() {
//        String patientUsername = (String) SecurityUtils.getSubject().getSession().getAttribute("username");
////        return userDAO.getUserInfo(patientUsername, pictureId);
//    }
}
