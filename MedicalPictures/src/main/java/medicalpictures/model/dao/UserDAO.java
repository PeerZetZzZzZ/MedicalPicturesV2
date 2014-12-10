package medicalpictures.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Named;
import javax.persistence.Query;
import medicalpictures.model.common.DBNameManager;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.common.PasswordGenerator;
import medicalpictures.model.common.ResultCodes;
import medicalpictures.model.exception.AddNewUserFailed;
import medicalpictures.model.exception.AddToDbFailed;
import medicalpictures.model.exception.UserDoesntExistException;
import medicalpictures.model.orm.entity.Admin;
import medicalpictures.model.orm.entity.Doctor;
import medicalpictures.model.orm.entity.Patient;
import medicalpictures.model.orm.entity.Technician;
import medicalpictures.model.orm.entity.User;

/**
 *
 * @author PeerZet
 */
@Stateful
@Named(value = "DBUserManager")
public class UserDAO {

	@EJB
	private ManagerDAO managerDAO;
	@EJB
	private MedicalLogger logger;
	@EJB
	private PasswordGenerator passwordGenerator;

	/**
	 * Saves new user in database (UsersDB + specified table (by accountType)).
	 *
	 * @param userDetails Map with created user details.
	 * @return
	 */
	public int addNewUser(Map<String, String> userDetails) {
		try {
			addUser(userDetails, true);
			return ResultCodes.OPERATION_SUCCEED;
		} catch (AddToDbFailed ex) {
			logger.logError("Internal server problem while adding new user!", UserDAO.class, ex);
			return ResultCodes.INTERNAL_SERVER_ERROR;
		}
	}

	/**
	 * Saves user only in specified table ( by accountType)
	 *
	 * @param userDetails
	 * @throws AddToDbFailed
	 */
	private void addNewUserInSpecifiedAccountTable(Map<String, String> userDetails) throws AddNewUserFailed {
		try {
			addUser(userDetails, false);
		} catch (AddToDbFailed ex) {
			throw new AddNewUserFailed(userDetails.get("username") + ": adding user failed!");
		}
	}

	/**
	 * Saves user in UsersDB.
	 *
	 * @param username username
	 * @param password password
	 * @param accountType accountType
	 */
	private void addNewUserInUserDB(final String username, final String password, final String accountType) throws AddToDbFailed {
		User user = new User(username, password, accountType);
		managerDAO.persistObject(user);
	}

	/**
	 * Saves the user in database.
	 *
	 * @param userDetails User details
	 * @param inUsersDBToo If == true, creates user in UsersDB too, if false = creates user only in specified table (by accountType)
	 * @throws AddToDbFailed
	 */
	private void addUser(Map<String, String> userDetails, boolean inUsersDBToo) throws AddToDbFailed {
		final String username = userDetails.get("username");
		final String name = userDetails.get("name");
		final String surname = userDetails.get("surname");
		final String password = passwordGenerator.generatePassword(name, surname);
		final String accountType = userDetails.get("accountType");
		final String age = userDetails.get("age");
		final String specialization = userDetails.get("specialization");
		if (inUsersDBToo) { //if it's false it means that don't create
			addNewUserInUserDB(username, password, accountType);
		}
		User foundUser = findUser(username);
		if (foundUser != null) {
			Object user = null;
			switch (accountType) {
				case "ADMIN":
					user = new Admin(foundUser, name, surname, Integer.valueOf(age));
					break;
				case "PATIENT":
					user = new Patient(foundUser, name, surname, Integer.valueOf(age));
					break;
				case "TECHNICIAN":
					user = new Technician(foundUser, name, surname, Integer.valueOf(age));
					break;
				case "DOCTOR":
					user = new Doctor(foundUser, name, surname, Integer.valueOf(age), specialization);
					break;
			}
			managerDAO.persistObject(user);
			logger.logInfo("Added user: " + username + "," + password + "," + accountType + "," + name + "," + surname + "," + age + "," + specialization, UserDAO.class);
		} else {
			logger.logWarning("Couldn't add user in specified table, because of internal problem while adding user in Users! ", UserDAO.class);
		}
	}

	/**
	 * Returns the list with all users in User table.
	 *
	 * @return JSON with users - their usernames and accountTypes
	 */
	public List<User> getAllUsernames() {
		Query query = managerDAO.getEntityManager().createQuery("SELECT c FROM " + DBNameManager.getUsersDbTable() + " c", User.class);
		List<User> users = query.getResultList();
		if (users == null) {
			logger.logWarning("No users found.", UserDAO.class);
			return new ArrayList<>();
		} else {
			return users;
		}
	}

	/**
	 * Deletes the user from UsersDB and table specified for given accountType.
	 *
	 * @param usernamesMap Map with users in such form <username,accountType>
	 * @return
	 */
	public int deleteUsers(Map<String, String> usernamesMap) {
		Set<String> usernames = usernamesMap.keySet();
		for (String username : usernames) {
			String accountType = usernamesMap.get(username);//get accountType
			switch (accountType) {
				case "ADMIN": {
					Admin admin = findAdmin(username);
					managerDAO.getEntityManager().remove(admin);
					break;
				}
				case "DOCTOR": {
					Doctor doctor = findDoctor(username);
					managerDAO.getEntityManager().remove(doctor);
					break;
				}
				case "TECHNICIAN": {
					Technician technician = findTechnician(username);
					managerDAO.getEntityManager().remove(technician);
					break;
				}
				case "PATIENT": {
					Patient patient = findPatient(username);
					managerDAO.getEntityManager().remove(patient);
					break;
				}
			}
			User user = findUser(username);
			if (user != null) {//if user found
				managerDAO.getEntityManager().remove(user);
				logger.logInfo("Deleted user: " + username + ", accountType: " + accountType, UserDAO.class);
			} else {
				logger.logWarning("Cant remove user '" + username + "' because user doesn't ! " + accountType, UserDAO.class);
			}
		}
		return ResultCodes.OPERATION_SUCCEED;
	}

	/**
	 * Deletes users only in table specified by accountType
	 *
	 * @param usernamesMap Map with users in such form <username,accountType>
	 */
	private void deleteUsersOnlyInSpecifiedTable(Map<String, String> usernamesMap) {
		Set<String> usernames = usernamesMap.keySet();
		for (String username : usernames) {
			String accountType = usernamesMap.get(username);//get accountType
			switch (accountType) {
				case "ADMIN": {
					Admin admin = findAdmin(username);
					if (admin != null) {
						managerDAO.getEntityManager().remove(admin);
						logger.logInfo("Successfully removed user '" + username + "'", UserDAO.class);
					} else {
						logger.logWarning("Couldn't removed user " + username + "'.User doesn't exist!", UserDAO.class);
					}
					break;
				}
				case "DOCTOR": {
					Doctor doctor = findDoctor(username);
					if (doctor != null) {
						managerDAO.getEntityManager().remove(doctor);
						logger.logInfo("Successfully removed user '" + username + "'", UserDAO.class);
					} else {
						logger.logWarning("Couldn't removed user " + username + "'.User doesn't exist!", UserDAO.class);
					}
					break;
				}
				case "TECHNICIAN": {
					Technician technician = findTechnician(username);
					if (technician != null) {
						managerDAO.getEntityManager().remove(technician);
						logger.logInfo("Successfully removed user '" + username + "'", UserDAO.class);
					} else {
						logger.logWarning("Couldn't removed user " + username + "'.User doesn't exist!", UserDAO.class);
					}
					break;
				}
				case "PATIENT": {
					Patient patient = findPatient(username);
					if (patient != null) {
						managerDAO.getEntityManager().remove(patient);
						logger.logInfo("Successfully removed user '" + username + "'", UserDAO.class);
					} else {
						logger.logWarning("Couldn't removed user " + username + "'.User doesn't exist!", UserDAO.class);
					}
					break;
				}
			}
		}
	}

	/**
	 * Returns users details readed from UsersDB table and specified for given accountType.
	 *
	 * @param username Username for which details will be found
	 * @return Map with user details
	 * @throws medicalpictures.model.exception.UserDoesntExistException
	 */
	public Map<String, String> getUserDetails(String username) throws UserDoesntExistException {
		Map<String, String> userDetails = new HashMap<>();
		User user = findUser(username);
		String userAccountType = user.getAccountType();
		String name = "";
		String surname = "";
		int age = 0;
		switch (userAccountType) {
			case "ADMIN": {
				Admin admin = findAdmin(username);
				if (admin != null) {
					name = admin.getName();
					surname = admin.getSurname();
					age = admin.getAge();
				} else {
					logger.logWarning("Coudln't get user details '" + username + "' because user doesn't exist!", UserDAO.class);
					throw new UserDoesntExistException("Coudln't get user details '" + username + "' because user doesn't exist!");
				}
				break;
			}
			case "DOCTOR": {
				Doctor doctor = findDoctor(username);
				if (doctor != null) {
					name = doctor.getName();
					surname = doctor.getSurname();
					age = doctor.getAge();
					String specialization = doctor.getSpecialization();
					userDetails.put("specialization", String.valueOf(specialization));//it's special case when we do it
				} else {
					logger.logWarning("Coudln't get user details '" + username + "' because user doesn't exist!", UserDAO.class);
					throw new UserDoesntExistException("Coudln't get user details '" + username + "' because user doesn't exist!");
				}
				break;
			}
			case "PATIENT": {
				Patient patient = findPatient(username);
				if (patient != null) {
					name = patient.getName();
					surname = patient.getSurname();
					age = patient.getAge();
				} else {
					logger.logWarning("Coudln't get user details '" + username + "' because user doesn't exist!", UserDAO.class);
					throw new UserDoesntExistException("Coudln't get user details '" + username + "' because user doesn't exist!");
				}
				break;
			}
			case "TECHNICIAN": {
				Technician technician = findTechnician(username);
				if (technician != null) {
					name = technician.getName();
					surname = technician.getSurname();
					age = technician.getAge();
				} else {
					logger.logWarning("Coudln't get user details '" + username + "' because user doesn't exist!", UserDAO.class);
					throw new UserDoesntExistException("Coudln't get user details '" + username + "' because user doesn't exist!");
				}
				break;
			}
		}
		userDetails.put("username", username);
		userDetails.put("name", name);
		userDetails.put("surname", surname);
		userDetails.put("age", String.valueOf(age));
		userDetails.put("accountType", userAccountType);
		logger.logInfo("Successfuly found user details for user: " + username, UserDAO.class);
		return userDetails;
	}

	/**
	 * Changes the values of specified user
	 *
	 * @param userDetails User which will be changed
	 * @return
	 */
	public int editUser(Map<String, String> userDetails) {
		String username = userDetails.get("username");
		String name = userDetails.get("name");
		String surname = userDetails.get("surname");
		String accountType = userDetails.get("accountType");
		Integer age = Integer.valueOf(userDetails.get("age"));
		String resetPassword = userDetails.get("resetPassword");
		/* If we change user accountType, it means that we must move him to another table, so we must
		 delete him in which he is already */
		Map<String, String> usersToDelete = new HashMap<>();
		User userToEdit = findUser(username);
		if (userToEdit != null) {
			String currentAccountType = userToEdit.getAccountType();
			boolean accountTypeChanged = false;
			if (resetPassword.equals("true")) {
				userToEdit.setPassword(passwordGenerator.generatePassword(name, surname));//default password is generated
			}
			if (!userToEdit.getAccountType().equals(accountType)) {//if accountType changed
				userToEdit.setAccountType(accountType);
				accountTypeChanged = true;
			}
			managerDAO.getEntityManager().persist(userToEdit);
			if (!accountTypeChanged) {
				switch (accountType) {
					case "ADMIN": {
						Admin admin = findAdmin(username);
						admin.setName(name);
						admin.setSurname(surname);
						admin.setAge(age);
						try {
							managerDAO.getEntityManager().persist(admin);
						} catch (Exception ex) {
							logger.logError("Persisting failed. Internal server error !", UserDAO.class, ex);
							return ResultCodes.INTERNAL_SERVER_ERROR;
						}
						logger.logInfo("User '" + username + "' successfully edited. User account type: !" + accountType + "'.", UserDAO.class);
						break;
					}
					case "DOCTOR": {
						Doctor doctor = findDoctor(username);
						doctor.setName(name);
						doctor.setSurname(surname);
						doctor.setAge(age);
						doctor.setSpecialization(userDetails.get("specialization"));
						try {
							managerDAO.getEntityManager().persist(doctor);
						} catch (Exception ex) {
							logger.logError("Persisting failed. Internal server error !", UserDAO.class, ex);
							return ResultCodes.INTERNAL_SERVER_ERROR;
						}
						logger.logInfo("User '" + username + "' successfully edited. User account type: !" + accountType + "'.", UserDAO.class);
						break;
					}
					case "PATIENT": {
						Patient patient = findPatient(username);
						patient.setName(name);
						patient.setSurname(surname);
						patient.setAge(age);
						try {
							managerDAO.getEntityManager().persist(patient);
						} catch (Exception ex) {
							logger.logError("Persisting failed. Internal server error !", UserDAO.class, ex);
							return ResultCodes.INTERNAL_SERVER_ERROR;
						}
						logger.logInfo("User '" + username + "' successfully edited. User account type: !" + accountType + "'.", UserDAO.class);
						break;
					}
					case "TECHNICIAN": {
						Technician technician = findTechnician(username);
						technician.setName(name);
						technician.setSurname(surname);
						technician.setAge(age);
						try {
							managerDAO.getEntityManager().persist(technician);
						} catch (Exception ex) {
							logger.logError("Persisting failed. Internal server error !", UserDAO.class, ex);
							return ResultCodes.INTERNAL_SERVER_ERROR;
						}
						logger.logInfo("User '" + username + "' successfully edited. User account type: !" + accountType + "'.", UserDAO.class);
						break;
					}
				}
			} else {
				usersToDelete.put(username, currentAccountType);//we add user to delete
				deleteUsersOnlyInSpecifiedTable(usersToDelete);//delete existing user but not in UsersDB table
				Map<String, String> newUserDetails = new HashMap<>();
				newUserDetails.put("username", username);
				newUserDetails.put("name", name);
				newUserDetails.put("accountType", accountType);
				newUserDetails.put("surname", surname);
				newUserDetails.put("age", String.valueOf(age));
				try {
					addNewUserInSpecifiedAccountTable(userDetails);
					logger.logInfo("User '" + newUserDetails.get("username") + "' successfully edited. User account type changed for: !" + newUserDetails.get("accountType") + "'.", UserDAO.class);
					return ResultCodes.OPERATION_SUCCEED;
				} catch (AddNewUserFailed ex) {
					logger.logError("Editing user failed! Internal server problem!", UserDAO.class, ex);
					return ResultCodes.INTERNAL_SERVER_ERROR;
				}
			}
			return ResultCodes.OPERATION_SUCCEED;
		} else {
			logger.logWarning("Couldn't edit user '" + username + "' because user doesn't exist!", UserDAO.class);
			return ResultCodes.USER_DOESNT_EXIST;
		}
	}

	/**
	 * Finds patient by username.
	 *
	 * @param username
	 * @return
	 */
	public Patient findPatient(String username) {
		try {
			Query query = managerDAO.getEntityManager().createQuery("SELECT a FROM " + DBNameManager.getPatientTable() + " a WHERE a.user.username LIKE :userName", Patient.class);
			Patient patient = (Patient) query.setParameter("userName", username).getSingleResult();
			managerDAO.getEntityManager().refresh(patient);
			return patient;
		} catch (Exception ex) {
			logger.logWarning("Patient with username '" + username + "' not found.", UserDAO.class);
			return null;
		}
	}

	/**
	 * Finds admin by username.
	 *
	 * @param username
	 * @return
	 */
	public Admin findAdmin(String username) {
		try {
			Query query = managerDAO.getEntityManager().createQuery("SELECT a FROM " + DBNameManager.getAdminTable() + " a WHERE a.user.username LIKE :userName", Admin.class);
			Admin admin = (Admin) query.setParameter("userName", username).getSingleResult();
			managerDAO.getEntityManager().refresh(admin);
			return admin;
		} catch (Exception ex) {
			logger.logWarning("Admin with username '" + username + "' not found.", UserDAO.class);
			return null;
		}
	}

	/**
	 * Finds technician by username.
	 *
	 * @param username
	 * @return
	 */
	public Technician findTechnician(String username) {
		try {
			Query query = managerDAO.getEntityManager().createQuery("SELECT a FROM " + DBNameManager.getTechnicianTable() + " a WHERE a.user.username LIKE :userName", Technician.class);
			Technician technician = (Technician) query.setParameter("userName", username).getSingleResult();
			managerDAO.getEntityManager().refresh(technician);
			return technician;
		} catch (Exception ex) {
			logger.logWarning("Technician with username '" + username + "' not found.", UserDAO.class);
			return null;
		}
	}

	/**
	 * Finds doctor by username.
	 *
	 * @param username
	 * @return
	 */
	public Doctor findDoctor(String username) {
		try {
			Query query = managerDAO.getEntityManager().createQuery("SELECT a FROM " + DBNameManager.getDoctorTable() + " a WHERE a.user.username LIKE :userName", Doctor.class);
			Doctor doctor = (Doctor) query.setParameter("userName", username).getSingleResult();
			managerDAO.getEntityManager().refresh(doctor);
			return doctor;
		} catch (Exception ex) {
			logger.logWarning("Doctor with username '" + username + "' not found.", UserDAO.class);
			return null;
		}
	}

	/**
	 * Finds user by username.
	 *
	 * @param username
	 * @return
	 */
	public User findUser(String username) {
		try {
			Query query = managerDAO.getEntityManager().createQuery("SELECT u FROM " + DBNameManager.getUsersDbTable() + " u WHERE u.username LIKE :userName", User.class);
			User user = (User) query.setParameter("userName", username).getSingleResult();
			managerDAO.getEntityManager().refresh(user);
			return user;
		} catch (Exception ex) {
			logger.logWarning("User with username '" + username + "' not found.", UserDAO.class);
			return null;
		}
	}
}
