package medicalpictures.model.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/**
 *
 * @author PeerZet
 */
public class MedicalPicturesSessionListener implements SessionListener {

    private Log log = LogFactory.getLog(MedicalPicturesSessionListener.class);

    @Override
    public void onStart(Session sn) {
    }

    @Override
    public void onStop(Session sn) {
        System.out.println("User: " + sn.getAttribute("username") + " has logout!");
    }

    @Override
    public void onExpiration(Session sn) {
        System.out.println("User: " + sn.getAttribute("username") + " - session expired! Logout.");
    }

}
