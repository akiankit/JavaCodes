package com.bluestone.app.shipping.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.core.util.Constants;

@Audited
@Entity
@Table(name = "address")
public class Address extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;
    
    @Column(name = "city_name", columnDefinition = "varchar(32)", nullable = false)
    private String            cityName;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id", nullable = false)
    private State             state;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false, updatable = false)
    private Customer          customerId;
    
    @Column(name = "alias", columnDefinition = "varchar(32)", nullable = false)
    private String            alias;
    
    @Pattern(regexp = Constants.VALID_PHONE_NO_REGEXP, message = Constants.INVALID_PHONE_NO_ERROR_MESSAGE)
    @Column(name = "contact_number", columnDefinition = "varchar(32)", nullable = false)
    private String            contactNumber;
    
    @Pattern(regexp = Constants.VALID_USER_NAME_REGEXP, message = Constants.INVALID_NAME_ERROR_MESSAGE)
    @Size(min = 3, max = 32, message = Constants.INVALID_NAME_ERROR_MESSAGE)
    @Column(name = "fullname", columnDefinition = "varchar(32)", nullable = false)
    private String            fullname;
    
    @Column(name = "address", columnDefinition = "varchar(255)", nullable = false)
    private String            address;
    
    @Size(max = 6, min = 6, message = Constants.INVALID_PIN_CODE_ERROR_MESSAGE)
    @Column(name = "postcode", columnDefinition = "varchar(12)")
    private String            postCode;
    
    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public String getCityName() {
        return cityName;
    }
    
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
    public Customer getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }
    
    public String getAlias() {
        return alias;
    }
    
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    public String getFullname() {
        return fullname;
    }
    
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPostCode() {
        return postCode;
    }
    
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
    
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
    public State getState() {
        return state;
    }
    
    public void setState(State state) {
        this.state = state;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Address)) {
            return false;
        }
        Address other = (Address) obj;
        if (address == null) {
            if (other.address != null) {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }
        if (alias == null) {
            if (other.alias != null) {
                return false;
            }
        } else if (!alias.equals(other.alias)) {
            return false;
        }
        if (cityName == null) {
            if (other.cityName != null) {
                return false;
            }
        } else if (!cityName.equals(other.cityName)) {
            return false;
        }
        if (contactNumber == null) {
            if (other.contactNumber != null) {
                return false;
            }
        } else if (!contactNumber.equals(other.contactNumber)) {
            return false;
        }
        if (fullname == null) {
            if (other.fullname != null) {
                return false;
            }
        } else if (!fullname.equals(other.fullname)) {
            return false;
        }
        if (postCode == null) {
            if (other.postCode != null) {
                return false;
            }
        } else if (!postCode.equals(other.postCode)) {
            return false;
        }
        if (state == null) {
            if (other.state != null) {
                return false;
            }
        } else if (!state.equals(other.state)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Address [ Id=").append(getId());
        sb.append(", CityName='").append(cityName).append('\'');
        sb.append(", StateId=").append(state.getId());
        sb.append(", Alias='").append(alias).append('\'');
        sb.append(", ContactNumber='").append(contactNumber).append('\'');
        sb.append(", Fullname='").append(fullname).append('\'');
        sb.append(", Address='").append(address).append('\'');
        sb.append(", PostCode='").append(postCode).append('\'');
        sb.append('}');
        sb.append("\n");
        return sb.toString();
    }
    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
    
}
