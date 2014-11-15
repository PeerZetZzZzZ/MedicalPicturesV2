/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.exception;

/**
 * Throwed when adding new picture type failed.
 *
 * @author PeerZet
 */
public class AddPictureTypeFailed extends AddToDbFailed {

    public AddPictureTypeFailed(String message) {
        super(message);
    }

}
