package medicalpictures.model.exception;

/**
 * Thrown when user which is already logged, want to log again.
 *
 * @author PeerZet
 */
public class UserAlreadyLoggedException extends MedicalPicturesException {

    public UserAlreadyLoggedException(String message) {
        super(message);
    }

}
