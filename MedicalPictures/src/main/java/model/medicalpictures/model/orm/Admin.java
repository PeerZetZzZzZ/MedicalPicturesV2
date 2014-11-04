/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.medicalpictures.model.orm;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author PeerZet
 */
@Entity
@Table(name="Admin")
public class Admin implements Serializable {

    @Id
    private String username;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private int age;

    public Admin() {

    }

    public Admin(String username, String name, String surname, int age) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.age = age;

    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
