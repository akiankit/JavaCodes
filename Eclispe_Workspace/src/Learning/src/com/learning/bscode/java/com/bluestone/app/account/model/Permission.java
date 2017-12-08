package com.bluestone.app.account.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import javax.persistence.JoinColumn;

import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "permission",uniqueConstraints=@UniqueConstraint(columnNames={"entity","type"}))
public class Permission extends BaseEntity {

	@Column(name = "type", columnDefinition = "VARCHAR(32)", nullable = false)
	private String type;
	
	@Column(name = "entity", columnDefinition = "VARCHAR(32)", nullable = false)
	private String entity;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "role_permission", joinColumns = { @JoinColumn(name = "permission_id") }, inverseJoinColumns = @JoinColumn(name = "role_id"))
	@BatchSize(size=10)
	private Set<Role> roles;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Permission");
        sb.append(entity).append('\'');
        sb.append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
