package medicalpictures.model.common.jsonfactory;

import java.io.BufferedReader;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.common.ResultCodes;
import org.json.JSONObject;

/**
 * Class is responsible for decrypting and reading the json object from Servlet
 * request. It returns String already in JSON notation, which client can read as
 * well as can read data from JSON and return in any desired format.
 *
 * @author PeerZet
 */
@Stateless
public class JsonFactory {

    @EJB
    private MedicalLogger logger;
    /**
     * Return the JSON read from request.
     *
     * @param request
     * @return JSON
     */
    private static final Logger LOG = Logger.getLogger(JsonFactory.class.getName());

    public JSONObject decryptRequest(HttpServletRequest request) {
        StringBuilder jb = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
        } catch (Exception e) { /*report an error*/ }
        System.out.println(jb.toString());
        JSONObject jsonObject = new JSONObject(jb.toString());
        return jsonObject;
    }

    /**
     * Decrypts request from input string
     *
     * @param request json as string
     * @return json as JSONObject
     */
    public JSONObject decryptRequest(String request) {
        return new JSONObject(request);
    }

    /**
     * Returns the json which informs that there is no permission to see
     * content.
     *
     * @return message about not being permitted to see content
     */
    public String userNotPermitted() {
        JSONObject response = new JSONObject();
        response.put("errorCode", ResultCodes.USER_UNAOTHRIZED);
        return response.toString();
    }

    /**
     * Returns the json which informs that there is no permission to see
     * content.
     *
     * @return message about not being permitted to see content
     */
    public String notUserLogged() {
        JSONObject response = new JSONObject();
        response.put("errorCode", ResultCodes.USER_IS_NOT_LOGGED);
        return response.toString();
    }

    /**
     * Returns the response to the client by ResultCode
     *
     * @param result
     * @return
     */
    public String getOperationResponseByCode(int result) {
        JSONObject response = new JSONObject();
        response.put("errorCode", result);
        logger.logInfo("Sending response to the client: " + response.toString(), JsonFactory.class);
        return response.toString();

    }
}
