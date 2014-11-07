/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.exception;

/**
 * Main MedialPicturesException
 *
 * @author PeerZet
 */
public class MedicalPicturesException extends Exception {

    private String message = "";

    public MedicalPicturesException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
