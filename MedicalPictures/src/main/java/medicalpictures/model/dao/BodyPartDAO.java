/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.dao;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import medicalpictures.controller.views.common.DBNameManager;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.exception.AddBodyPartFailed;
import medicalpictures.model.exception.AddToDbFailed;
import medicalpictures.model.orm.entity.BodyPart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Responsible for operations on BodyPart table.
 *
 * @author PeerZet
 */
@Stateless
public class BodyPartDAO {

	@EJB
	private ManagerDAO managerDAO;

	private Log LOG = LogFactory.getLog(PictureTypeDAO.class);

	@EJB
	private MedicalLogger logger;

	/**
	 * Returns the list of body parts.
	 *
	 * @return BodyPart list.
	 */
	/**
	 * Returns all picture types taken from DB.
	 *
	 * @return All body parts as JSONObject
	 */
	public List<BodyPart> getAllBodyParts() {
		Query query = managerDAO.getEntityManager().createQuery("SELECT c FROM " + DBNameManager.getBodyPartTable() + " c", BodyPart.class);
		List<BodyPart> bodyPartsList = query.getResultList();
		if (bodyPartsList == null) {
			logger.logWarning("No body parts found.", BodyPartDAO.class);
			return new ArrayList<>();
		} else {
			return bodyPartsList;
		}
	}

	/**
	 * Creates new body part in DB.
	 *
	 * @param bodyPartString
	 * @return
	 */
	public int addBodyPart(String bodyPartString) {
		BodyPart bodyPart = new BodyPart();
		bodyPart.setBodyPart(bodyPartString);
		try {
			managerDAO.persistObject(bodyPart);
			logger.logInfo("Successfully added body part: " + bodyPartString, BodyPartDAO.class);
			return ResultCodes.OPERATION_SUCCEED;
		} catch (AddToDbFailed ex) {
			logger.logError("Couldn't add body part '" + bodyPartString + "'. Internal server problem!", BodyPartDAO.class, ex);
			return ResultCodes.INTERNAL_SERVER_ERROR;
		}
	}

	public BodyPart getBodyPartByName(String name) {
		try {
			BodyPart bodyPart = (BodyPart) managerDAO.getEntityManager().createQuery("SELECT u FROM " + DBNameManager.getBodyPartTable() + " u WHERE u.bodyPart LIKE :bodyPart").
					setParameter("bodyPart", name).getSingleResult();
			managerDAO.getEntityManager().refresh(bodyPart);
			return bodyPart;
		} catch (Exception ex) {
			System.out.println("Couldn't find body part entity: " + name);
			return null;//in any case of failure
		}
	}
}
