/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.controller.views.technician;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medicalpictures.model.common.JsonFactory;
import medicalpictures.model.enums.AccountType;
import medicalpictures.model.exception.NoLoggedUserExistsHere;
import medicalpictures.model.exception.UserNotPermitted;
import medicalpictures.model.security.UserSecurityManager;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author PeerZet
 */
public class TechnicianViewAddPictures extends HttpServlet {

    @EJB
    private UserSecurityManager securityManager;
    @EJB
    private JsonFactory jsonFactory;

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
            securityManager.checkUserPermissionToThisContent(AccountType.TECHNICIAN);
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            for (FileItem item : items) {
                if (!item.isFormField()) {
                     // Process form file field (input type="file").
                    String filename = FilenameUtils.getName(item.getName());
                    InputStream filecontent = item.getInputStream();
                    System.out.println(item.getSize());
                    System.out.println("Nazwa pliu to chuuje: " + filename);
//                     File file = new File("/home/peer/zdjecie.jpg");
//                    item.write(file);
//                    file.getPath();
                } 
            }
        } catch (FileUploadException e) {
            throw new ServletException("Cannot parse multipart request.", e);
        } catch (UserNotPermitted ex) {
            logger.error("POST " + TechnicianViewAddPictures.class.toString() + " :No permission to see the content!");
        } catch (NoLoggedUserExistsHere ex) {
            logger.error("POST " + TechnicianViewAddPictures.class.toString() + " : No logged user exists!");
        } catch (Exception ex) {
            Logger.getLogger(TechnicianViewAddPictures.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
