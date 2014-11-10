package medicalpictures.model.orm.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Przemysław Thomann
 */
@Entity
public class PictureType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Column(length = 100, unique = true)
    private String pictureType;

    public void setPicturetype(String picturetype) {
        this.pictureType = picturetype;
    }

    public String getPicturetype() {
        return pictureType;
    }

    public String getId() {
        return id;
    }

    public PictureType(String pictureType) {
        this.pictureType = pictureType;
        this.id = UUID.randomUUID().toString();
    }

    public PictureType() {

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
        if (!(object instanceof PictureType)) {
            return false;
        }
        PictureType other = (PictureType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.medicalpictures.model.orm.PictureType[ id=" + id + " ]";
    }

}
