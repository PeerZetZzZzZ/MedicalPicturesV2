package medicalpictures.model.exception;

/**
 * Throwed when adding new user to database failed.
 *
 * @author PeerZet
 */
public class AddNewUserFailed extends AddToDbFailed {

    public AddNewUserFailed(String message) {
        super(message);
    }

}
