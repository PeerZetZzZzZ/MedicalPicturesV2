package medicalpictures.model.orm.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Przemysław Thomann
 */
@Entity
public class Technician implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID_GEN")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(length = 255)
    private String name;

    @Column(length = 255)
    private String surname;

    @Column(length = 3)
    private int age;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Technician(User user, String name, String surname, int age) {
        this.user = user;
        this.surname = surname;
        this.age = age;
    }

    public Technician() {

    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Technician)) {
            return false;
        }
        Technician other = (Technician) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
