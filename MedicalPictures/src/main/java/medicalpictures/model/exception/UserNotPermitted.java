/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.exception;

/**
 * Throwed when someone want to access content which is not permitted for him.
 *
 * @author PeerZet
 */
public class UserNotPermitted extends MedicalPicturesException {

    public UserNotPermitted(String message) {
        super(message);
    }

}
