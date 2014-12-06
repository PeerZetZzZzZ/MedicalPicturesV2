package medicalpictures.model.orm.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Is the single picture description made by doctor for specified picture.
 *
 * @author Przemysław Thomann
 */
@Entity
public class PictureDescription implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID_GEN")
    @Column(length = 36)
    private String id;

    @Column(length = 1000, nullable = true)
    private String description;

    @ManyToOne
    @JoinColumn(name = "DOCTOR_ID")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "PICTURE_ID")
    private Picture picture;

    @ManyToOne
    @JoinColumn(name = "DEFINED_PICTURE_DESCRIPTION_ID", nullable = true)
    private DefinedPictureDescription definedPictureDescription;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public DefinedPictureDescription getDefinedPictureDescription() {
        return definedPictureDescription;
    }

    public void setDefinedPictureDescription(DefinedPictureDescription definedPictureDescription) {
        this.definedPictureDescription = definedPictureDescription;
    }

    public String getId() {
        return id;
    }

    public PictureDescription(String description, Doctor doctor, Picture picture, DefinedPictureDescription definedPictureDescription) {
        this.description = description;
        this.doctor = doctor;
        this.picture = picture;
        this.definedPictureDescription = definedPictureDescription;
    }

    public PictureDescription() {
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PictureDescription)) {
            return false;
        }
        PictureDescription other = (PictureDescription) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.medicalpictures.model.orm.PictureDescription[ id=" + id + " ]";
    }

}
