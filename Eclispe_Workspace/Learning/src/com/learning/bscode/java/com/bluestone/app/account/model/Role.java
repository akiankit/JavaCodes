package com.bluestone.app.account.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "role")
public class Role extends BaseEntity implements Comparable<Role> {
    
    private static final long serialVersionUID = 1L;
    
    @Column(name = "name", columnDefinition = "VARCHAR(32)", nullable = false, unique = true)
    private String            name;
    
    @ManyToOne
    private Role              parentRole;
    
    @OneToMany(mappedBy = "parentRole")
    @BatchSize(size=10)
    private Set<Role>         childRoles;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = @JoinColumn(name = "permission_id"))
    @BatchSize(size=10)
    private Set<Permission>   permissions;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = @JoinColumn(name = "user_id"))
    @BatchSize(size=100)
    private Set<User>         Users;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Role getParentRole() {
        return parentRole;
    }
    
    public void setParentRole(Role parentRole) {
        this.parentRole = parentRole;
    }
    
    public Set<Role> getChildRoles() {
        return childRoles;
    }
    
    public void setChildRoles(Set<Role> childRoles) {
        this.childRoles = childRoles;
    }
    
    public Set<Permission> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
    
    public Set<User> getUsers() {
        return Users;
    }
    
    public void setUsers(Set<User> users) {
        Users = users;
    }
    
    @Override
    public int compareTo(Role o) {
        return this.name.compareTo(o.name);
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Role: ");
        //sb.append("{childRoles=").append(childRoles);
        sb.append("name='").append(name).append('\'');
        //sb.append(", parentRole=").append(parentRole);
        sb.append(", permissions=").append(permissions);
        sb.append('}');
        return sb.toString();
    }
}
