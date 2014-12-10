package medicalpictures.model.exception;

/**
 * Throwed when someone want to access content which is not permitted for him.
 *
 * @author PeerZet
 */
public class UserNotPermitted extends MedicalPicturesException {

	public UserNotPermitted(String message) {
		super(message);
	}

}
