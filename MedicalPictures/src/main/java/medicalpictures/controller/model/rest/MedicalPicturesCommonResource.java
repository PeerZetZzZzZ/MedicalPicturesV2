package medicalpictures.controller.model.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import medicalpictures.model.common.jsonfactory.BodyPartJsonFactory;
import medicalpictures.model.common.jsonfactory.DefinedPictureDescriptionJsonFactory;
import medicalpictures.model.common.jsonfactory.JsonFactory;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.common.jsonfactory.PatientJsonFactory;
import medicalpictures.model.common.jsonfactory.PictureDescriptionJsonFactory;
import medicalpictures.model.common.jsonfactory.PictureJsonFactory;
import medicalpictures.model.common.jsonfactory.PictureTypeJsonFactory;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.common.jsonfactory.UserJsonFactory;
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
import medicalpictures.model.exception.JsonParsingException;
import medicalpictures.model.exception.UserDoesntExistException;
import medicalpictures.model.orm.entity.BodyPart;
import medicalpictures.model.orm.entity.DefinedPictureDescription;
import medicalpictures.model.orm.entity.Patient;
import medicalpictures.model.orm.entity.Picture;
import medicalpictures.model.orm.entity.PictureType;
import medicalpictures.model.orm.entity.User;
import medicalpictures.model.security.UserSecurityManager;

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
	private PictureJsonFactory pictureJsonFactory;

	@EJB
	private PictureDescriptionJsonFactory pictureDescriptionJsonFactory;

	@EJB
	private DefinedPictureDescriptionJsonFactory definedPictureDescriptionJsonFactory;

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
			return userJsonFactory.getLoggedUser(username);
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

	/**
	 * Returns the info about user.
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
			String userInfo = userJsonFactory.getUserDetailsAsJson(userDetailsMap);
			medicalLogger.logInfo("Return user info of user '" + username + "' :" + userInfo, MedicalPicturesCommonResource.class);
			return userInfo;
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /getUserInfo/{username} !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /getUserInfo/{username} !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
		} catch (UserDoesntExistException ex) {
			medicalLogger.logError("Can't get user info. User '" + username + "' doesn't exist!!", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.USER_DOESNT_EXIST);
		}
	}

	/**
	 * Returns the all patients list.
	 *
	 * @return
	 */
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

	/**
	 * Returns all pictures list.
	 *
	 * @return
	 */
	@GET
	@Path("/getAllPictures")
	@Produces("application/json")
	public String getAllPictures() {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN, AccountType.ADMIN);
			List<Picture> picturesList = pictureDAO.getAllPictureList();
			String pictures = pictureJsonFactory.getAllPicturesResponse(picturesList);
			medicalLogger.logInfo("Returned all pictures: " + pictures, MedicalPicturesCommonResource.class);
			return pictures;
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /getAllPictures !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /getAllPictures !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
		}
	}

	/**
	 * Deletes the pictures.
	 *
	 * @param picturesList
	 * @return
	 */
	@POST
	@Path("/deletePictures")
	@Produces("application/json")
	@Consumes("application/json")
	public String deletePictures(String picturesList) {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN, AccountType.ADMIN);
			List<String> picturesIdList = pictureJsonFactory.getPicturesToDeleteList(picturesList);
			int result = pictureDAO.removePictures(picturesIdList);
			medicalLogger.logInfo("Removed pictures, return result: " + result, MedicalPicturesCommonResource.class);
			return jsonFactory.getOperationResponseByCode(result);
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /deletePictures !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /deletePictures !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
		}

	}

	/**
	 * Updates the picture.
	 *
	 * @param pictureValues
	 * @return
	 */
	@POST
	@Path("/updatePicture")
	@Produces("application/json")
	public String updatePicture(String pictureValues) {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN, AccountType.ADMIN);
			Map<String, String> pictureDetailsMap = pictureJsonFactory.getEditPictureValues(pictureValues);
			int result = pictureDAO.updatePicture(pictureDetailsMap);
			medicalLogger.logInfo("Updating picture, return result: " + result, MedicalPicturesCommonResource.class);
			return jsonFactory.getOperationResponseByCode(result);
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /updatePicture !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /update Picture !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
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
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.DOCTOR);
			Set<Picture> patientPictures = patientDAO.getPatientPictures(patientUsername);
			String picturesNames = pictureJsonFactory.getPicturesNames(patientPictures);
			medicalLogger.logInfo("Returned patient pictures names: " + picturesNames, MedicalPicturesCommonResource.class);
			return picturesNames;
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /getPatientPicturesNames/{patientUsername} !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /getPatientPicturesNames/{patientUsername} !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
		}
	}

	/**
	 * Return patient picture data and thumbnail of this picture.
	 *
	 * @param pictureId
	 * @return
	 */
	@GET
	@Path("getPatientPictureWithThumbnail/{pictureId}")
	@Produces("application/json")
	public String getPatientPictureWithThumbnail(@PathParam("pictureId") String pictureId) {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.DOCTOR, AccountType.PATIENT);
			Picture picture = pictureDAO.getPictureById(pictureId);
			if (picture != null) {
				String pictureDetails = pictureJsonFactory.getPictureDetailsWithThumbnail(picture);
				medicalLogger.logInfo("Retriving picture and thumbnail for '" + pictureId + "' successful!", MedicalPicturesCommonResource.class);
				return pictureDetails;
			} else {
				String result = jsonFactory.getOperationResponseByCode(ResultCodes.OBJECT_DOESNT_EXIST);
				medicalLogger.logInfo("Getting patient picture with thumbnail for picture '" + pictureId + "', return result: " + result, MedicalPicturesCommonResource.class);
				return result;
			}
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /getPatientPictureWithThumbnail/{pictureId} !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /getPatientPictureWithThumbnail/{pictureId}!", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
		} catch (IOException ex) {
			medicalLogger.logError("Internal server problem on /getPatientPictureWithThumbnail/{pictureId} !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.INTERNAL_SERVER_ERROR);
		}

	}

	@GET
	@Path("getFullPictureData/{pictureId}")
	@Produces("application/json")
	public String getFullPictureData(@PathParam("pictureId") String pictureId) {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.DOCTOR, AccountType.PATIENT);
			Picture picture = pictureDAO.getPictureById(pictureId);
			if (picture != null) {
				medicalLogger.logInfo("Retriving full picture data for '" + pictureId + "' successful!", MedicalPicturesCommonResource.class);
				return pictureJsonFactory.getFullPictureData(picture);
			} else {
				String result = jsonFactory.getOperationResponseByCode(ResultCodes.OBJECT_DOESNT_EXIST);
				medicalLogger.logWarning("Getting full picture data, return result: " + result, MedicalPicturesCommonResource.class);
				return result;
			}
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /getPatientPictureWithThumbnail/{pictureId} !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /getPatientPictureWithThumbnail/{pictureId}!", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
		} catch (IOException ex) {
			medicalLogger.logError("Internal server problem on /getPatientPictureWithThumbnail/{pictureId} !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Saves the description of the picture.
	 *
	 * @param pictureDescriptionDetails
	 * @return
	 */
	@POST
	@Path("savePictureDescription")
	@Consumes("application/json")
	@Produces("application/json")
	public String savePictureDescription(String pictureDescriptionDetails) {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.DOCTOR);
			Map<String, String> pictureDescriptionDetailsMap = pictureJsonFactory.getSavePictureDescription(pictureDescriptionDetails);
			String response = pictureDAO.savePictureOrUpdateDescription(pictureDescriptionDetailsMap);
			medicalLogger.logInfo("Save picture description response: " + response, MedicalPicturesCommonResource.class);
			return response;
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /savePictureDescription!", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /savePictureDescription !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
		}
	}

	/**
	 * Returns the list of the defined picture descriptions.
	 *
	 * @return
	 */
	@GET
	@Path("getDefinedPictureDescriptions")
	@Produces("application/json")
	public String getDefinedPictureDescriptions() {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.DOCTOR);
			List<DefinedPictureDescription> dpdList = definedPictureDescriptionDAO.getDefinedPictureDescription();
			String response = definedPictureDescriptionJsonFactory.getDefinedPictureDescription(dpdList);
			medicalLogger.logInfo("Get defined picture descriptions response: " + response, MedicalPicturesCommonResource.class);
			return response;
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /getDefinedPictureDescriptions!", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /getDefinedPictureDescriptions !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
		}
	}

	/**
	 * Returns the picture description for specified picture.
	 *
	 * @param pictureId
	 * @return
	 */
	@GET
	@Path("getPictureDescriptions/{pictureId}")
	@Produces("application/json")
	public String getPictureDescriptions(@PathParam("pictureId") String pictureId) {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.DOCTOR, AccountType.PATIENT);
			Picture picture = pictureDAO.getPictureById(pictureId);
			if (picture != null) {
				String description = pictureDescriptionJsonFactory.getPatientPictureDescriptions(picture);
				medicalLogger.logInfo("Getting picture description for id '" + pictureId + "' successful. Return result: !" + description, MedicalPicturesCommonResource.class);
				return description;
			} else {
				String result = jsonFactory.getOperationResponseByCode(ResultCodes.OBJECT_DOESNT_EXIST);
				medicalLogger.logWarning("Getting picture description for picture id '" + pictureId + "' return result: " + result, MedicalPicturesCommonResource.class);
				return result;
			}
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /getPictureDescriptions/{pictureId} !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.userNotPermitted();
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /getPictureDescriptions/{pictureId} !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.notUserLogged();
		}
	}

	/**
	 * Adds new user in application.
	 *
	 * @param newUserData
	 * @return
	 */
	@POST
	@Path("addNewUser")
	@Produces("application/json")
	public String addNewUser(String newUserData) {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
			Map<String, String> userDetails = userJsonFactory.readUserFromJson(newUserData);
			int result = userDAO.addNewUser(userDetails);
			String response = jsonFactory.getOperationResponseByCode(result);
			medicalLogger.logInfo("Adding new user operation result: " + result, MedicalPicturesCommonResource.class);
			return response;
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /addNewUser !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.USER_UNAOTHRIZED);
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /addNewUser!", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.USER_IS_NOT_LOGGED);
		} catch (JsonParsingException ex) {
			medicalLogger.logError("addNewUser: Parsing input json problem: " + newUserData, MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.INPUT_JSON_PARSE_ERROR);
		}
	}

	//adminViewDeleteUsers
	/**
	 * Removes users from application.
	 *
	 * @param usersToDelete
	 * @return
	 */
	@POST
	@Path("deleteUsers")
	@Produces("application/json")
	public String deleteUsers(String usersToDelete) {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
			int result = userDAO.deleteUsers(userJsonFactory.readUsersToDelete(usersToDelete));
			medicalLogger.logInfo("Delete useres response: " + result, MedicalPicturesCommonResource.class);
			return jsonFactory.getOperationResponseByCode(result);
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /deleteUsers !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.USER_UNAOTHRIZED);
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /deleteUsers !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.USER_IS_NOT_LOGGED);
		}
	}

	//adminViewEditUser
	/**
	 * Edits the user.
	 *
	 * @param userToEdit
	 * @return
	 */
	@POST
	@Path("editUser")
	@Produces("application/json")
	public String editUser(String userToEdit) {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
			Map<String, String> userDetails = userJsonFactory.readUserFromJson(userToEdit);
			int result = userDAO.editUser(userDetails);
			medicalLogger.logInfo("Editing user response: " + result, MedicalPicturesCommonResource.class);
			return jsonFactory.getOperationResponseByCode(result);
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /editUser !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.USER_UNAOTHRIZED);
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /editUser !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.USER_IS_NOT_LOGGED);
		} catch (JsonParsingException ex) {
			medicalLogger.logError("addNewUser: Parsing input json problem: " + userToEdit, MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.INPUT_JSON_PARSE_ERROR);
		}
	}

	@POST
	@Path("addBodyPart")
	@Produces("application/json")
	public String addBodyPart(String bodyPartToAdd) {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
			String bodyPartString = bodyPartJsonFactory.getBodyPart(bodyPartToAdd);
			int result = bodyPartDAO.addBodyPart(bodyPartString);
			medicalLogger.logInfo("Adding body part response: " + result, MedicalPicturesCommonResource.class);
			return jsonFactory.getOperationResponseByCode(result);
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /addBodyPart!", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.USER_UNAOTHRIZED);
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /addBodyPart!", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.USER_IS_NOT_LOGGED);
		}
	}

	@POST
	@Path("addPictureType")
	@Produces("application/json")
	public String addPictureType(String pictureTypeToAdd) {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
			String bodyPartString = pictureTypeJsonFactory.getPictureType(pictureTypeToAdd);
			int result = pictureTypeDAO.addPictureType(bodyPartString);
			medicalLogger.logInfo("Adding picture type response: " + result, MedicalPicturesCommonResource.class);
			return jsonFactory.getOperationResponseByCode(result);
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /addPictureType !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.USER_UNAOTHRIZED);
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /addPictureType !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.USER_IS_NOT_LOGGED);
		}
	}

	@POST
	@Path("loginUser")
	@Produces("application/json")
	public String loginUser(String pictureTypeToAdd) {
		try {
			securityManager.checkUserPermissionToThisContent(AccountType.ADMIN);
			Map<String, String> userDetails = userJsonFactory.getUserLoginDetails(pictureTypeToAdd);
			int result = securityManager.loginUser(userDetails);
			medicalLogger.logInfo("Login user response: " + result, MedicalPicturesCommonResource.class);
			return jsonFactory.getOperationResponseByCode(result);
		} catch (UserNotPermitted ex) {
			medicalLogger.logError("User not permitted to access /loginUser !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.USER_UNAOTHRIZED);
		} catch (NoLoggedUserExistsHere ex) {
			medicalLogger.logError("User is not logged - can't access /loginUser !", MedicalPicturesCommonResource.class, ex);
			return jsonFactory.getOperationResponseByCode(ResultCodes.USER_IS_NOT_LOGGED);
		}
	}

}
