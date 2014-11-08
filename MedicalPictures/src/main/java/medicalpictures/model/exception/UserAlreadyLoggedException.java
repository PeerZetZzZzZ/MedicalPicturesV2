package medicalpictures.model.exception;

/**
 * Thrown when user which is already logged, want to log again.
 *
 * @author PeerZet
 */
public class UserAlreadyLoggedException extends MedicalPicturesException {

    private String loggedUsername = "";

    public UserAlreadyLoggedException(String message, String loggedUsername) {
        super(message);
        this.loggedUsername = loggedUsername;
    }

    public String getLoggedUsername() {
        return loggedUsername;
    }

    public void setLoggedUsername(String loggedUsername) {
        this.loggedUsername = loggedUsername;
    }

}
