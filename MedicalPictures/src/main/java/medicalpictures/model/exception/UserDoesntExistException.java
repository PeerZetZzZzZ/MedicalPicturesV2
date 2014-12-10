package medicalpictures.model.exception;

/**
 *
 * @author PeerZet
 */
public class UserDoesntExistException extends MedicalPicturesException {

    public UserDoesntExistException(String message) {
        super(message);
    }
}
