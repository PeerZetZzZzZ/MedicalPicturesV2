/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.exception;

/**
 * Thrown where there is a problem with parsing for example request from client.
 * @author PeerZet
 */
public class JsonParsingException extends MedicalPicturesException {

    public JsonParsingException(String message) {
        super(message);
    }

}
