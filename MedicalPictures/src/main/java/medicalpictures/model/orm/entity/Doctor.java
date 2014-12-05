package medicalpictures.model.orm.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * Is the doctor, who gives picture descriptions.
 *
 * @author Przemysław Thomann
 */
@Entity
public class Doctor implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID_GEN")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @NotNull
    @Column(length = 255)
    private String name;

    @NotNull
    @Column(length = 255)
    private String surname;

    @NotNull
    private int age;

    @Column(length = 100)
    private String specialization;

    @OneToMany(mappedBy = "doctor")
    private Set<PictureDescription> pictureDescriptions = new HashSet<>();

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public String getSpecialization() {
        return specialization;
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

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<PictureDescription> getPictureDescriptions() {
        return pictureDescriptions;
    }

    public Doctor(User user, String name, String surname, int age, String specialization) {
        this.user = user;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.specialization = specialization;
    }

    public Doctor() {

    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Doctor)) {
            return false;
        }
        Doctor other = (Doctor) object;
        if ((this.user == null && other.user != null) || (this.user != null && !this.user.equals(other.user))) {
            return false;
        }
        return true;
    }

}
