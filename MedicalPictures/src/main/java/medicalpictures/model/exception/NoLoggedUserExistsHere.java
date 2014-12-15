package medicalpictures.model.exception;

/**
 * Throwed when we check which user is logged locally and there is none.
 *
 * @author PeerZet
 */
public class NoLoggedUserExistsHere extends MedicalPicturesException {

    public NoLoggedUserExistsHere(String message) {
        super(message);
    }

}
