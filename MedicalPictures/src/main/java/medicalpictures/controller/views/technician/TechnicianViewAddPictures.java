/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.controller.views.technician;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.dao.PictureDAO;
import medicalpictures.model.enums.AccountType;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

/**
 *
 * @author PeerZet
 */
public class TechnicianViewAddPictures extends HttpServlet {

    @EJB
    private UserSecurityManager securityManager;
    @EJB
    private JsonFactory jsonFactory;
    @EJB
    private PictureDAO pictureDAO;
    @EJB
    private ThumbnailProducer thumbnailProducer;

    private Log logger = LogFactory.getLog(TechnicianViewAddPictures.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN);
            request.getRequestDispatcher("/WEB-INF/technician/technicianViewAddPictures.html").forward(request, response);
        } catch (UserNotPermitted ex) {
            logger.error("GET " + TechnicianViewAddPictures.class.toString() + " :No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            logger.error("GET " + TechnicianViewAddPictures.class.toString() + " : No logged user exists!");

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
//            securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN);
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    JSONObject pictureDetailsJson = jsonFactory.decryptRequest(FilenameUtils.getName(item.getName()));
                    Map<String, String> pictureDetailsMap = jsonFactory.getAddPictureValues(pictureDetailsJson);
                    byte[] pictureData = IOUtils.toByteArray(item.getInputStream());
                    byte[] thumbnailData = thumbnailProducer.getThumbnail(pictureData, pictureDetailsMap.get("pictureName"));
                    pictureDAO.addNewPicture(pictureDetailsMap, pictureData, thumbnailData);
                }
            }
        } catch (FileUploadException e) {
            throw new ServletException("Cannot parse multipart request.", e);
//        } catch (UserNotPermitted ex) {
//            logger.error("POST " + TechnicianViewAddPictures.class.toString() + " :No permission to see the content!");
//        } catch (NoLoggedUserExistsHere ex) {
//            logger.error("POST " + TechnicianViewAddPictures.class.toString() + " : No logged user exists!");
        } catch (Exception ex) {
            Logger.getLogger(TechnicianViewAddPictures.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
