package medicalpictures.model.exception;

/**
 * Thrown where there is a problem with parsing for example request from client.
 *
 * @author PeerZet
 */
public class JsonParsingException extends MedicalPicturesException {

    public JsonParsingException(String message) {
        super(message);
    }

}
