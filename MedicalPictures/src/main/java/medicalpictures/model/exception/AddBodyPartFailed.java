package medicalpictures.model.exception;

/**
 * Throwed when adding new body part fails.
 *
 * @author PeerZet
 */
public class AddBodyPartFailed extends AddToDbFailed {

    public AddBodyPartFailed(String message) {
        super(message);
    }

}
