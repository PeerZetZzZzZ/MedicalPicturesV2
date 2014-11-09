package medicalpictures.model.orm.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Przemysław Thomann
 */
@Entity
public class AccountType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    public AccountType(String accountType) {
        this.accountType = accountType;
        this.id = UUID.randomUUID().toString();
    }

    @Column(length = 15, unique = true)
    private String accountType;

    public String getAccount_type() {
        return accountType;
    }

    public void setAccount_type(String account_type) {
        this.accountType = account_type;
    }

    public String getId() {
        return id;
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
        if (!(object instanceof AccountType)) {
            return false;
        }
        AccountType other = (AccountType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.medicalpictures.model.orm.AccountType[ id=" + id + " ]";
    }

}
