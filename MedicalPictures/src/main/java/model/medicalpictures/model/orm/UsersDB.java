/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.medicalpictures.model.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author PeerZet
 */
@Entity
@Table(name = "UsersDB")
public class UsersDB {

    @Id
    @Column(length = 100)
    private String username;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 15)
    private String account_type;

    public UsersDB(String username, String password, String account_type) {
        this.username = username;
        this.password = password;
        this.account_type = account_type;
    }

    public UsersDB() {

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAccount_type() {
        return account_type;
    }

}
