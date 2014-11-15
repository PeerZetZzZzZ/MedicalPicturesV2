/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.exception;

/**
 * Throwed when adding new body part fails.
 *
 * @author PeerZet
 */
public class AddBodyPartFailed extends AddToDbFailed {

    public AddBodyPartFailed(String message) {
        super(message);
    }

}
