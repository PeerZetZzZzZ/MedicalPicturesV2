package medicalpictures.model.common;

import javax.ejb.Stateless;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Przemysław Thomann
 */
@Stateless
public class PasswordGenerator {

	/**
	 * Generates the default password, which is "name - surname" as sha512hex
	 *
	 * @param name Name of the user
	 * @param surname Surname of the user
	 * @return generated password
	 */
	public String generatePassword(String name, String surname) {
		return DigestUtils.sha512Hex(name + "-" + surname);
	}

	/**
	 * Generates the password hash for given password.
	 *
	 * @param password
	 * @return
	 */
	public String getPasswordHash(String password) {
		return DigestUtils.sha512Hex(password);
	}

}
