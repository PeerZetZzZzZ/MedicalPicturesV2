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
 * Is the body part which is on the picture.
 *
 * @author Przemysław Thomann
 */
@Entity
public class BodyPart implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID_GEN")
    private String id;

    @Column(length = 255, unique = true)
    private String bodyPart;

    @OneToMany(mappedBy = "bodyPart", fetch = FetchType.LAZY)
    private Set<Picture> pictures = new HashSet<>();

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public Set<Picture> getPictures() {
        return pictures;
    }

    public BodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public BodyPart() {

    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BodyPart)) {
            return false;
        }
        BodyPart other = (BodyPart) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
