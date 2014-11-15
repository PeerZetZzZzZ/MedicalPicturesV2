package medicalpictures.model.orm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Przemysław Thomann
 */
@Entity
@Table(name="Picture")
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Column(length = 100)
    private String username;

    @Column(length = 100, unique = true)
    private String pictureName;

    @Temporal(TemporalType.DATE)
    private java.util.Date captureDatetime;

    private byte[] picture_data;

    @Column(length = 100)
    private String technicianUsername;

    @Column(length = 100)
    private String doctorUsername;

    @Column(length = 100)
    private String pictureType;

    @Column(length = 200)
    private String pictureDescription;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPicture_name(String picture_name) {
        this.pictureName = picture_name;
    }

    public void setCapture_datetime(Date capture_datetime) {
        this.captureDatetime = capture_datetime;
    }

    public void setPicture_data(byte[] picture_data) {
        this.picture_data = picture_data;
    }

    public void setTechnician_username(String technician_username) {
        this.technicianUsername = technician_username;
    }

    public void setDoctor_username(String doctor_username) {
        this.doctorUsername = doctor_username;
    }

    public void setPicture_type(String picture_type) {
        this.pictureType = picture_type;
    }

    public void setPicture_description(String picture_description) {
        this.pictureDescription = picture_description;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUsername() {
        return username;
    }

    public String getPicture_name() {
        return pictureName;
    }

    public Date getCapture_datetime() {
        return captureDatetime;
    }

    public byte[] getPicture_data() {
        return picture_data;
    }

    public String getTechnician_username() {
        return technicianUsername;
    }

    public String getDoctor_username() {
        return doctorUsername;
    }

    public String getPicture_type() {
        return pictureType;
    }

    public String getPicture_description() {
        return pictureDescription;
    }

    public String getId() {
        return id;
    }

    public Picture(String username, String pictureName, Date captureDatetime, byte[] picture_data, String technicianUsername, String doctorUsername, String pictureType, String pictureDescription) {
        this.username = username;
        this.pictureName = pictureName;
        this.captureDatetime = captureDatetime;
        this.picture_data = picture_data;
        this.technicianUsername = technicianUsername;
        this.doctorUsername = doctorUsername;
        this.pictureType = pictureType;
        this.pictureDescription = pictureDescription;
        this.id = UUID.randomUUID().toString();
    }

    public Picture() {

    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Picture)) {
            return false;
        }
        Picture other = (Picture) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.medicalpictures.model.orm.Picture[ id=" + id + " ]";
    }

}
