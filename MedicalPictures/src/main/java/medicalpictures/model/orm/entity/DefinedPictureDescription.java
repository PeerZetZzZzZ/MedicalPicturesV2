package medicalpictures.model.orm.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Is the defined picture description, if doctor doesn't want to write his own
 * picture description.
 *
 * @author PeerZet
 */
@Entity
public class DefinedPictureDescription implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID_GEN")
    private String id;

    @Column(length = 100, unique = true)
    private String descriptionName;

    @Column(length = 1000)
    private String pictureDescription;

    @OneToMany(mappedBy = "definedPictureDescription")
    private Set<PictureDescription> pictureDescriptions = new HashSet<>();

    public String getPictureDescription() {
        return pictureDescription;
    }

    public void setPictureDescription(String pictureDescription) {
        this.pictureDescription = pictureDescription;
    }

    public Set<PictureDescription> getPictures() {
        return pictureDescriptions;
    }

    public String getDescriptionName() {
        return descriptionName;
    }

    public void setDescriptionName(String descriptionName) {
        this.descriptionName = descriptionName;
    }

    public DefinedPictureDescription(String descriptionName, String pictureDescription) {
        this.pictureDescription = pictureDescription;
        this.descriptionName = descriptionName;
    }

    public DefinedPictureDescription() {

    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DefinedPictureDescription)) {
            return false;
        }
        DefinedPictureDescription other = (DefinedPictureDescription) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "medicalpictures.model.orm.entity.DefinedPictureDescription[ id=" + id + " ]";
    }

}
