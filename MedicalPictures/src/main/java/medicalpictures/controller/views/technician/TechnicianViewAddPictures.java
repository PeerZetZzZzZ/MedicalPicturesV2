package medicalpictures.controller.views.technician;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.model.common.jsonfactory.JsonFactory;
import medicalpictures.model.common.MedicalLogger;
import medicalpictures.model.common.jsonfactory.PictureJsonFactory;
import medicalpictures.model.dao.PictureDAO;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.JsonParsingException;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.security.UserSecurityManager;
import medicalpictures.model.technician.ThumbnailProducer;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

/**
 *
 * @author PeerZet
 */
public class TechnicianViewAddPictures extends HttpServlet {

    @EJB
    private UserSecurityManager securityManager;
    @EJB
    private PictureJsonFactory pictureJsonFactory;
    @EJB
    private JsonFactory jsonFactory;
    @EJB
    private PictureDAO pictureDAO;
    @EJB
    private ThumbnailProducer thumbnailProducer;

    @EJB
    private MedicalLogger logger;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN);
            request.getRequestDispatcher("/WEB-INF/technician/technicianViewAddPictures.html").forward(request, response);
        } catch (UserNotPermitted ex) {
            logger.logError("User not permitted to access /TechnicianViewAddPictures !", TechnicianViewAddPictures.class, ex);
            request.getRequestDispatcher("/WEB-INF/common/NotAuthorizedView.html").forward(request, response);
        } catch (NoLoggedUserExistsHere ex) {
            logger.logError("User is not logged - can't access /TechnicianViewAddPictures!", TechnicianViewAddPictures.class, ex);
            request.getRequestDispatcher("/WEB-INF/common/NotAuthorizedView.html").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN);
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    JSONObject pictureDetailsJson = jsonFactory.decryptRequest(FilenameUtils.getName(item.getName()));
                    Map<String, String> pictureDetailsMap = pictureJsonFactory.getAddPictureValues(pictureDetailsJson);
                    byte[] pictureData = IOUtils.toByteArray(item.getInputStream());
                    byte[] thumbnailData = thumbnailProducer.getThumbnail(pictureData, pictureDetailsMap.get("pictureName"));
                    int result = pictureDAO.addNewPicture(pictureDetailsMap, pictureData, thumbnailData);
                    String responseMessage = jsonFactory.getOperationResponseByCode(result);
                    logger.logInfo("Adding picture response: " + responseMessage, TechnicianViewAddPictures.class);
                    response.getWriter().write(responseMessage);
                    response.getWriter().flush();
                    response.getWriter().close();
                }
            }
        } catch (FileUploadException e) {
            throw new ServletException("Cannot parse multipart request.", e);
        } catch (UserNotPermitted ex) {
            logger.logError("User not permitted to access /TechnicianViewAddPictures !", TechnicianViewAddPictures.class, ex);
            request.getRequestDispatcher("/WEB-INF/common/NotAuthorizedView.html").forward(request, response);
        } catch (NoLoggedUserExistsHere ex) {
            logger.logError("User is not logged - can't access /TechnicianViewAddPictures!", TechnicianViewAddPictures.class, ex);
            request.getRequestDispatcher("/WEB-INF/common/NotAuthorizedView.html").forward(request, response);
        } catch (JsonParsingException ex) {
            logger.logError("/TechnicianViewAddPictures: input json parse exception!Can't add picture! ", TechnicianViewAddPictures.class, ex);
        }

    }
}
