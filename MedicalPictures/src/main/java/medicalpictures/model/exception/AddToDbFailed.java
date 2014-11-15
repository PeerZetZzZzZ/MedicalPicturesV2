/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.exception;

/**
 * Throwed when it's error while adding sth to database.
 * @author PeerZet
 */
public class AddToDbFailed extends MedicalPicturesException {

    public AddToDbFailed(String message) {
        super(message);
    }

}
