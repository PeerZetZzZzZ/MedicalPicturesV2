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
public class Diagnosis implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private String id;

	@Column(length = 100)
	private String username;

	@Column(length = 100)
	private String doctorUsername;

	@Column(length = 500)
	private String description;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getUsername() {
		return username;
	}

	public String getDoctor_username() {
		return doctorUsername;
	}

	public String getDescription() {
		return description;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setDoctor_username(String doctor_username) {
		this.doctorUsername = doctor_username;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public Diagnosis(String username, String doctorUsername, String description) {
		this.username = username;
		this.doctorUsername = doctorUsername;
		this.description = description;
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
		if (!(object instanceof Diagnosis)) {
			return false;
		}
		Diagnosis other = (Diagnosis) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.medicalpictures.model.orm.Diagnosis[ id=" + id + " ]";
	}

}
