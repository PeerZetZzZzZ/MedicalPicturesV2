package medicalpictures.model.exception;

/**
 * Throwed when it's error while adding sth to database.
 *
 * @author PeerZet
 */
public class AddToDbFailed extends MedicalPicturesException {

    public AddToDbFailed(String message) {
        super(message);
    }

}
