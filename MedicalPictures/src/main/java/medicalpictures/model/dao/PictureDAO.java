package medicalpictures.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import medicalpictures.model.common.DBNameManager;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.exception.AddToDbFailed;
import medicalpictures.model.orm.entity.BodyPart;
import medicalpictures.model.orm.entity.DefinedPictureDescription;
import medicalpictures.model.orm.entity.Doctor;
import medicalpictures.model.orm.entity.Patient;
import medicalpictures.model.orm.entity.Picture;
import medicalpictures.model.orm.entity.PictureDescription;
import medicalpictures.model.orm.entity.PictureType;
import medicalpictures.model.orm.entity.Technician;
import org.apache.shiro.SecurityUtils;

/**
 *
 * @author PeerZet
 */
@Stateless
public class PictureDAO {

	@EJB
	private BodyPartDAO bodyPartDAO;

	@EJB
	private PictureTypeDAO pictureTypeDAO;

	@EJB
	private UserDAO userDAO;

	@EJB
	private ManagerDAO managerDAO;

	@EJB
	private JsonFactory jsonFactory;

	@EJB
	private PictureDescriptionDAO pictureDescriptionDAO;

	@EJB
	private DefinedPictureDescriptionDAO definedPictureDescriptionDAO;

	@EJB
	private MedicalLogger logger;

	/**
	 * Adds the new picture the database.
	 *
	 * @param pictureDetails
	 * @param pictureData
	 * @param thumbnailData
	 * @return
	 */
	public int addNewPicture(Map<String, String> pictureDetails, byte[] pictureData, byte[] thumbnailData) {
		String pictureName = pictureDetails.get("pictureName");
		String patientUnserame = pictureDetails.get("patientUsername");
		String bodyPartName = pictureDetails.get("bodyPart");
		String pictureTypeName = pictureDetails.get("pictureType");
		Patient patient = userDAO.findPatient(patientUnserame);
		BodyPart bodyPart = bodyPartDAO.getBodyPartByName(bodyPartName);
		PictureType pictureType = pictureTypeDAO.getPictureTypeByName(pictureTypeName);
		Technician technician = userDAO.findTechnician((String) SecurityUtils.getSubject().getSession().getAttribute("username"));
		if (patient != null && bodyPart != null && pictureType != null && technician != null) {
			Picture picture = new Picture(patient, technician, pictureType, bodyPart, pictureName, pictureData, thumbnailData);
			managerDAO.getEntityManager().persist(picture);
			logger.logInfo("Successfully added new picture: " + pictureName, PictureDAO.class);
			return ResultCodes.OPERATION_SUCCEED;
		} else {
			logger.logWarning("Couldn't add new picture, because some of the components was null", PictureDAO.class);
			return ResultCodes.OBJECT_DOESNT_EXIST;
		}
	}

	/**
	 * Returns list of all pictures.
	 *
	 * @return
	 */
	public List<Picture> getAllPictureList() {
		Query query = managerDAO.getEntityManager().createQuery("SELECT p FROM " + DBNameManager.getPictureTable() + " p", Picture.class);
		List<Picture> pictureList = query.getResultList();
		if (pictureList == null) {
			logger.logWarning("No pictures found.", Picture.class);
			return new ArrayList<>();
		} else {
			return pictureList;
		}

	}

	/**
	 * Removes pictures from the database.
	 *
	 * @param picturesList
	 * @return
	 */
	public int removePictures(List<String> picturesList) {
		for (String singlePictureId : picturesList) {
			Picture picture = managerDAO.getEntityManager().find(Picture.class, singlePictureId);
			if (picture != null) {
				managerDAO.getEntityManager().remove(picture);
				logger.logInfo("Successfully removed picture: " + singlePictureId, PictureDAO.class);
			} else {
				logger.logInfo("Couldn't remove picture. Picture ': " + singlePictureId + "' not found", PictureDAO.class);
			}
		}
		return ResultCodes.OPERATION_SUCCEED;
	}

	/**
	 * Updates the picture. Can update bodyPart and pictureType.
	 *
	 * @param pictureDetailsMap
	 * @return
	 */
	public int updatePicture(Map<String, String> pictureDetailsMap) {
		String pictureId = pictureDetailsMap.get("pictureId");
		String pictureType = pictureDetailsMap.get("pictureType");
		String bodyPart = pictureDetailsMap.get("bodyPart");
		BodyPart bodyPartEntity = bodyPartDAO.getBodyPartByName(bodyPart);
		PictureType pictureTypeEntity = pictureTypeDAO.getPictureTypeByName(pictureType);
		Picture picture = managerDAO.getEntityManager().find(Picture.class, pictureId);
		if (picture != null && bodyPartEntity != null && pictureTypeEntity != null) {
			picture.setBodyPart(bodyPartEntity);
			picture.setPictureType(pictureTypeEntity);
			return ResultCodes.OPERATION_SUCCEED;
		} else {
			logger.logWarning("Couldn't update picture, because some of the values (picture, bodyPart or pictureType) was not found.", PictureDAO.class);
			return ResultCodes.OBJECT_DOESNT_EXIST;
		}
	}

	/**
	 * Returns the picture or null if wasn't found ( by id )
	 *
	 * @param id
	 * @return
	 */
	public Picture getPictureById(String id) {
		Picture picture = managerDAO.getEntityManager().find(Picture.class, id);
		managerDAO.getEntityManager().refresh(picture);
		if (picture == null) {
			logger.logWarning("Couldn't find picture with id: " + id, PictureDAO.class);
		}
		return picture;
	}

	/**
	 * Saves the description if it doesn't exist or updates the existing one.
	 *
	 * @param pictureDescriptionJson
	 * @return
	 */
	public String savePictureOrUpdateDescription(Map<String, String> pictureDescriptionJson) {
		String pictureId = pictureDescriptionJson.get("pictureId");
		String pictureDescriptionId = pictureDescriptionJson.get("pictureDescriptionId");
		String pictureDescription = pictureDescriptionJson.get("pictureDescription");
		String definedPictureDescriptionId = pictureDescriptionJson.get("definedPictureDescriptionId");
		Picture picture = getPictureById(pictureId);
		if (picture != null) {
			if (!pictureDescriptionId.equals("")) {//in case when we not create but edit existing description
				PictureDescription existingPictureDescription = pictureDescriptionDAO.getPictureDesriptionById(pictureDescriptionId);
				if (!definedPictureDescriptionId.equals("")) {//don't add own pictureDescription but defined pictureDescription
					DefinedPictureDescription dpd = definedPictureDescriptionDAO.getDefinedPictureDesriptionById(definedPictureDescriptionId);
					if (existingPictureDescription != null && dpd != null) {
						managerDAO.getEntityManager().merge(existingPictureDescription);
						existingPictureDescription.setDescription("");//we want to clear existing description
						existingPictureDescription.setDefinedPictureDescription(dpd);//update with defined picture description
						logger.logInfo("Set defined picture description with id'" + dpd.getId() + "' for picture '" + existingPictureDescription.getId() + "'.",
								PictureDAO.class);
					} else {
						logger.logWarning("DefinedPictureDescription or PictureDescription not found'", PictureDAO.class);
						return jsonFactory.getOperationResponseByCode(ResultCodes.OBJECT_DOESNT_EXIST);
					}
				} else {
					if (existingPictureDescription != null) {
						managerDAO.getEntityManager().merge(existingPictureDescription);
						existingPictureDescription.setDescription(pictureDescription);
						existingPictureDescription.setDefinedPictureDescription(null);
						logger.logWarning("Set picture description '" + pictureDescription + "' for picture '" + existingPictureDescription.getId() + "'.",
								PictureDAO.class);
					} else {
						logger.logWarning("PictureDescription with id'" + pictureDescriptionId + "' not found.", PictureDAO.class);
						return jsonFactory.getOperationResponseByCode(ResultCodes.OBJECT_DOESNT_EXIST);
					}
				}
			} else {
				DefinedPictureDescription dpd = definedPictureDescriptionDAO.getDefinedPictureDesriptionById(definedPictureDescriptionId);
				Picture pictureEntity = getPictureById(pictureId);
				String doctorUsername = (String) SecurityUtils.getSubject().getSession().getAttribute("username");
				Doctor doctorEntity = userDAO.findDoctor(doctorUsername);
				if (!definedPictureDescriptionId.equals("")) {//if doctor want to use defined picture description
					if (dpd != null && pictureEntity != null && doctorEntity != null) {
						try {
							PictureDescription newPictureDescription = new PictureDescription("", doctorEntity, pictureEntity, dpd);
							managerDAO.persistObject(newPictureDescription);
							logger.logInfo("Create new picture description with defined picture description '" + definedPictureDescriptionId + "'.",
									PictureDAO.class);
						} catch (AddToDbFailed ex) {
							logger.logError("Inserting picture description failed. Internal server problem! '" + definedPictureDescriptionId + "'.",
									PictureDAO.class, ex);
							return jsonFactory.getOperationResponseByCode(ResultCodes.INTERNAL_SERVER_ERROR);
						}
					} else {
						logger.logWarning("Couldn't find picture or doctor or defined picture description!", PictureDAO.class);
						return jsonFactory.getOperationResponseByCode(ResultCodes.OBJECT_DOESNT_EXIST);
					}
				} else {
					if (pictureEntity != null && doctorEntity != null) {
						try {
							PictureDescription newPictureDescription = new PictureDescription(pictureDescription, doctorEntity, pictureEntity, null);
							managerDAO.persistObject(newPictureDescription);
							logger.logInfo("Create new picture description with picture description '" + pictureDescription + "'.", PictureDAO.class);
						} catch (AddToDbFailed ex) {
							logger.logError("Inserting picture description failed. Internal server problem! '" + definedPictureDescriptionId + "'.",
									PictureDAO.class, ex);
							return jsonFactory.getOperationResponseByCode(ResultCodes.INTERNAL_SERVER_ERROR);
						}
					} else {
						logger.logWarning("Couldn't find picture or doctor!", PictureDAO.class);
						return jsonFactory.getOperationResponseByCode(ResultCodes.OBJECT_DOESNT_EXIST);
					}
				}
			}
		} else {
			logger.logWarning("Can't save picture description. Picture '" + pictureId + "' not found.", PictureDAO.class);
			return jsonFactory.getOperationResponseByCode(ResultCodes.OBJECT_DOESNT_EXIST);
		}
		return jsonFactory.getOperationResponseByCode(ResultCodes.OPERATION_SUCCEED);
	}
}
