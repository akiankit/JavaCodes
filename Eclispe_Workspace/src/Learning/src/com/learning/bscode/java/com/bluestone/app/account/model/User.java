package com.bluestone.app.account.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.core.util.Constants;

@Audited
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity {
    
    @Size(min = 3, max = 32, message = Constants.INVALID_NAME_ERROR_MESSAGE)
    @Pattern(regexp = Constants.VALID_USER_NAME_REGEXP, message = Constants.INVALID_NAME_ERROR_MESSAGE)
    @Column(name = "user_name", columnDefinition = "VARCHAR(32)", nullable = false)
    // todo ---regexp for customer name is pending
    private String    userName;
    
    @NotNull(message = Constants.INVALID_EMAIL_ADDRESS_ERROR_MESSAGE)
    @Size(min = 1, max = 254, message = Constants.INVALID_EMAIL_ADDRESS_ERROR_MESSAGE)
    @Pattern(regexp = Constants.VALID_EMAIL_REGEXP, message = Constants.INVALID_EMAIL_ADDRESS_ERROR_MESSAGE)
    @Column(name = "email", columnDefinition = "varchar(254)", nullable = false, unique = true)
    private String    email;
    
    @Size(min = 5, max = 32, message = Constants.INVALID_PASSWORD_ERROR_MESSAGE)
    @Column(name = "password", columnDefinition = "varchar(32)")
    private String    password;
    
    @Column(name = "is_accountActivated", columnDefinition = "tinyint(1) default'0'", nullable = false)
    private int       isAccountActivated = 0;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = @JoinColumn(name = "role_id"))
    @BatchSize(size=10)
    private Set<Role> roles;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @BatchSize(size=5)
    private List<UserSocialData> userSocialData;
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
    public int getIsAccountActivated() {
        return isAccountActivated;
    }
    
    public void setIsAccountActivated(int isAccountActivated) {
        this.isAccountActivated = isAccountActivated;
    }

    public boolean isAccountActivated() {
        return (isAccountActivated == 1 ? true : false);
    }

    public void activateAccount(){
        setIsAccountActivated(1);
    }

    public List<UserSocialData> getUsersocialData() {
		return userSocialData;
	}

	public void setUsersocialData(List<UserSocialData> usersocialData) {
		this.userSocialData = usersocialData;
	}

	/*@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        //sb.append(super.toString());
        sb.append("User");
        sb.append("{\t email=").append(email).append("\n");
        sb.append("\t userName=").append(userName).append("\n");
        sb.append("\t isAccountActivated=").append(isAccountActivated).append("\n");
        sb.append("\n}");
        return sb.toString();
    }*/


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("User");
        sb.append("{email='").append(email).append('\'');
        sb.append(", isAccountActivated=").append(isAccountActivated);
        sb.append(", userName='").append(userName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
