package model.medicalpictures.model.orm;

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
public class BodyPart implements Serializable {

	private static final long serialVersionUID = 1L;
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

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof BodyPart)) {
			return false;
		}
		BodyPart other = (BodyPart) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.medicalpictures.model.orm.BodyPart[ id=" + id + " ]";
	}

}
