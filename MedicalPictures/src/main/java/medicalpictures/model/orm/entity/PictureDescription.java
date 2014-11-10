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
public class PictureDescription implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Column(length = 500, unique = true)
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public PictureDescription(String description) {
        this.description = description;
        this.id = UUID.randomUUID().toString();
    }

    public PictureDescription() {

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
