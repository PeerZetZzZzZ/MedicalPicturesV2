package medicalpictures.model.orm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.eclipse.persistence.annotations.UuidGenerator;

/**
 *
 * @author PeerZet
 */
@Entity
@UuidGenerator(name = "UUID_GEN")
public class User {

    @Id
    @GeneratedValue(generator = "UUID_GEN")
    private String id;

    @Column(length = 100, unique = true)
    private String username;

    @Column(length = 128)
    @NotNull
    private String password;

    @Column(length = 15)
    private String accountType;

    public User(String username, String password, String accountType) {
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }

    public User() {

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAccountType() {
        return accountType;
    }

}
