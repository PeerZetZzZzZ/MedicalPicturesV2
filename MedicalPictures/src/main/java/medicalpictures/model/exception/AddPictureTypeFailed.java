package medicalpictures.model.exception;

/**
 * Throwed when adding new picture type failed.
 *
 * @author PeerZet
 */
public class AddPictureTypeFailed extends AddToDbFailed {

    public AddPictureTypeFailed(String message) {
        super(message);
    }

}
