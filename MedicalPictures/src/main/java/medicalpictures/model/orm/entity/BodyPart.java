package medicalpictures.model.orm.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Przemysław Thomann
 */
@Entity
@Table(name = "BodyPart")
public class BodyPart implements Serializable {

    @Id
    private String id;

    @Column(length = 255, unique = true)
    private String bodyPart;

    public void setBodypart(String bodypart) {
        this.bodyPart = bodypart;
    }

    public String getBodypart() {
        return bodyPart;
    }

    public BodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
        this.id = UUID.randomUUID().toString();
    }

    public BodyPart() {

    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
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
