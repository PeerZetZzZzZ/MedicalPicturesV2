/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.controller.model.rest.common;

import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import medicalpictures.model.security.UserSecurityManager;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author PeerZet
 */
@Path("MedicalPicturesCommon")
//Zeby sie dostac localhost:8080:/MedicalPictures/webresources/MedicalPicturesCommon/getUsername
public class MedicalPicturesCommonResource {

    @Context
    private UriInfo context;

    @EJB
    private UserSecurityManager manager;

    /**
     * Creates a new instance of MedicalPicturesCommonResource
     */
    public MedicalPicturesCommonResource() {
    }

    /**
     * Retrieves representation of an instance of
     * medicalpictures.controller.model.rest.common.MedicalPicturesCommonResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/getLoggedUser")
    @Produces("application/json")
    public String getJson() {
        if (manager.checkUserPermissionToAnyContent()) {
            JSONObject user = new JSONObject();
            user.put("username", manager.getLoggedUsername());
            System.out.println("Zwracom: " + user.toString());
            return user.toString();
        } else {
            return "";
        }
    }
}
