package medicalpictures.model.security;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/**
 *
 * @author PeerZet
 */
public class MedicalPicturesSessionListener implements SessionListener {

    @Override
    public void onStart(Session sn) {
        System.out.println("Session listener onStart!");
    }

    @Override
    public void onStop(Session sn) {
        System.out.println("Session listener onStop!");
    }

    @Override
    public void onExpiration(Session sn) {
        System.out.println("Session listener onExpiration!");
    }
    
}
