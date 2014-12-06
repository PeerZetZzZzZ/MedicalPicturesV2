package medicalpictures.model.orm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 * Is the single picture for the patient.
 *
 * @author Przemysław Thomann
 */
@Entity
public class Picture implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID_GEN")
    private String id;

    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "TECHNICIAN_ID")
    private Technician technician;

    @ManyToOne
    @JoinColumn(name = "PICTURE_TYPE_ID")
    private PictureType pictureType;

    @ManyToOne
    @JoinColumn(name = "BODY_PART_ID")
    private BodyPart bodyPart;

    @OneToMany(mappedBy = "picture")
    private Set<PictureDescription> pictureDescriptions = new HashSet<>();

    @Column(length = 100)
    private String pictureName;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date captureTimestamp = new Date();

    private byte[] pictureData;

    private byte[] thumbnailData;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }

    public PictureType getPictureType() {
        return pictureType;
    }

    public void setPictureType(PictureType pictureType) {
        this.pictureType = pictureType;
    }

    public BodyPart getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(BodyPart bodyPart) {
        this.bodyPart = bodyPart;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public byte[] getPictureData() {
        return pictureData;
    }

    public void setPictureData(byte[] pictureData) {
        this.pictureData = pictureData;
    }

    public byte[] getThumbnailData() {
        return thumbnailData;
    }

    public void setThumbnailData(byte[] thumbnailData) {
        this.thumbnailData = thumbnailData;
    }

    public Set<PictureDescription> getPictureDescriptions() {
        return pictureDescriptions;
    }

    public Date getCaptureTimestamp() {
        return captureTimestamp;
    }

    public Picture(Patient patient, Technician technician, PictureType pictureType, BodyPart bodyPart, String pictureName, byte[] pictureData, byte[] thumbnailData) {
        this.patient = patient;
        this.technician = technician;
        this.pictureType = pictureType;
        this.bodyPart = bodyPart;
        this.pictureName = pictureName;
        this.pictureData = pictureData;
        this.thumbnailData = thumbnailData;
    }

    public Picture() {

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
