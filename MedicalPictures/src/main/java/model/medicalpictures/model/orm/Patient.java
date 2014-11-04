package model.medicalpictures.model.orm;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Przemysław Thomann
 */
@Entity
public class Patient implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(length = 100)
	private String username;

	@Column(length = 255)
	private String name;

	@Column(length = 255)
	private String surname;

	@Column(length = 3)
	private int age;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public int getAge() {
		return age;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Patient(String username, String name, String surname, int age) {
		this.username = username;
		this.name = name;
		this.surname = surname;
		this.age = age;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (username != null ? username.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Patient)) {
			return false;
		}
		Patient other = (Patient) object;
		if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.medicalpictures.model.orm.NewEntity[ id=" + username + " ]";
	}

}
