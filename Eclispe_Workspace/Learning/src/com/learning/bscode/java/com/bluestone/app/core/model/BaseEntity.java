package com.bluestone.app.core.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Resolution;

@MappedSuperclass
// Below filter definitions are on hibernate session
@FilterDef(name = "isActive", parameters = @ParamDef(name = "isActive", type = "short"))
@Filters({ @Filter(name = "isActive", condition = ":isActive = is_active") })
public class BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long              id;
    
    @Field
    @DateBridge(resolution = Resolution.MILLISECOND)
    @Column(nullable = false)
    private Date              createdAt;
    
    @Column(nullable = false)
    private Date              updatedAt;
    
    @Field
    @Column(name = "is_active", columnDefinition = "tinyint(1)", nullable = false)
    private short             isActive         = 1;
    
	@Version
	@Column(name = "version")
	public int version;
    
    public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public short getIsActive() {
        return isActive;
    }
    
    public void setIsActive(short isActive) {
        this.isActive = isActive;
    }
    
    public void setIsActive(boolean isActive) {
        if (isActive) {
            setIsActive((short) 1);
        } else {
            setIsActive((short) 0);
        }
    }
    
    @PrePersist
    public void setDefaultFields() {
        Date date = getCurrentDate();
        createdAt = date;
        updatedAt = date;
    }

    @PreUpdate
    public void setUpdatedDate() {
        Date date = getCurrentDate();
        updatedAt = date;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((isActive == 1) ? 1231 : 1237);
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof BaseEntity)) return false;
        BaseEntity other = (BaseEntity) obj;
        if (id != other.id) return false;
        if (isActive != other.isActive) return false;
        return true;
    }
    
    public boolean isActive() {
        return isActive == 0 ? false : true;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BaseEntity");
        sb.append("{ id=").append(id);
        sb.append(", isActive=").append(isActive);
        sb.append(" }\n");
        return sb.toString();
    }

    private Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }
}
