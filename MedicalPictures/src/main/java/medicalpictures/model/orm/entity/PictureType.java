package medicalpictures.model.orm.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Is the type of the picture.
 *
 * @author Przemysław Thomann
 */
@Entity
public class PictureType implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID_GEN")
    private String id;

    @Column(length = 100, unique = true)
    private String pictureType;

    @OneToMany(mappedBy = "pictureType", fetch = FetchType.LAZY)
    private Set<Picture> pictures = new HashSet<>();

    public Set<Picture> getPictures() {
        return pictures;
    }

    public String getPictureType() {
        return pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }

    public PictureType(String pictureType) {
        this.pictureType = pictureType;
    }

    public PictureType() {

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
