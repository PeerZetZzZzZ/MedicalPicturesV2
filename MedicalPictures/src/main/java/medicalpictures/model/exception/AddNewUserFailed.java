/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.model.exception;

/**
 * Throwed when adding new user to database failed.
 *
 * @author PeerZet
 */
public class AddNewUserFailed extends AddToDbFailed {

    public AddNewUserFailed(String message) {
        super(message);
    }

}
