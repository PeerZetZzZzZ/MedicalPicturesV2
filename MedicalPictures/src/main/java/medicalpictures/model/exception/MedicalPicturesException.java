package medicalpictures.model.exception;

/**
 * Main MedialPicturesException
 *
 * @author PeerZet
 */
public class MedicalPicturesException extends Exception {

    private String message = "";

    public MedicalPicturesException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
