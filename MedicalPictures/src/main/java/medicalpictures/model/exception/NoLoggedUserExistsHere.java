/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.exception;

/**
 * Throwed when we check which user is logged locally and there is none.
 * @author PeerZet
 */
public class NoLoggedUserExistsHere  extends MedicalPicturesException{

    public NoLoggedUserExistsHere(String message) {
        super(message);
    }
    
}
