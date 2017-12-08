package com.bluestone.app.account.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;
import org.hibernate.search.annotations.Indexed;

import com.bluestone.app.core.util.Constants;
import com.bluestone.app.shipping.model.Address;

@Audited
@Entity
@Table(name = "customer")
@PrimaryKeyJoinColumn(name = "customer_id", referencedColumnName = "id")
@Indexed
public class Customer extends User {
    
    private static final long serialVersionUID = 1L;
    
    @Column(name = "gender", columnDefinition = "tinyint(1) default '0'", nullable = false)
    private short             gender;
    
    @Column(name = "last_passwd_gen", columnDefinition = "timestamp", nullable = false)
    private Timestamp         lastPasswdGen;
    
    @Column(name = "birthday")
    private Date              birthDay;
    
    @Column(name = "newsletter", columnDefinition = "tinyint(1) default'0'", nullable = false)
    private boolean           newsletter;
    
    @Column(name = "ip_registration_newsletter", columnDefinition = "varchar(15)")
    private String            ipRegistrationNewsLetter;
    
    @Column(name = "newsletter_date_add", columnDefinition = "datetime")
    private Date              newsLetterDateAdd;
    
    @Pattern(regexp = Constants.VALID_PHONE_NO_REGEXP, message = Constants.INVALID_PHONE_NO_ERROR_MESSAGE)
    @Column(name = "customer_phone", columnDefinition = "varchar(16)")
    private String            customerPhone;
    
    @OneToMany(mappedBy = "customerId")
    @BatchSize(size=20)
    private List<Address>     addressList;          
    
    public short getGender() {
        return gender;
    }
    
    public void setGender(short gender) {
        this.gender = gender;
    }
    
    public Timestamp getLastPasswdGen() {
        return lastPasswdGen;
    }
    
    public void setLastPasswdGen(Timestamp lastPasswdGen) {
        this.lastPasswdGen = lastPasswdGen;
    }
    
    public String getIpRegistrationNewsLetter() {
        return ipRegistrationNewsLetter;
    }
    
    public Date getBirthDay() {
        return birthDay;
    }
    
    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }
    
    public boolean getNewsletter() {
        return newsletter;
    }
    
    public void setNewsletter(boolean newsletter) {
        this.newsletter = newsletter;
    }
    
    public void setIpRegistrationNewsLetter(String ipRegistrationNewsLetter) {
        this.ipRegistrationNewsLetter = ipRegistrationNewsLetter;
    }
    
    public Date getNewsLetterDateAdd() {
        return newsLetterDateAdd;
    }
    
    public void setNewsLetterDateAdd(Date newsLetterDateAdd) {
        this.newsLetterDateAdd = newsLetterDateAdd;
    }
    
    public String getCustomerPhone() {
        return customerPhone;
    }
    
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    
    public List<Address> getAddressList() {
        if (addressList != null) {
            Set<Address> addressList = new HashSet<Address>();
            addressList.addAll(this.addressList);
            List<Address> addressList1 = new ArrayList<Address>();
            addressList1.addAll(addressList);
            return addressList1;
        } else {
            return null;
        }
    }
    
    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public void setGender(String gender) {
        try {
            if (gender != null) {
                Gender value = Gender.valueOf(gender.toUpperCase());
                switch (value) {
                    case FEMALE:
                        setGender((short) 2);
                        break;

                    case MALE:
                        setGender((short) 1);
                        break;
                    default:
                        setGender((short) 0);
                        break;
                }
            }
        } catch (IllegalArgumentException e) {
            setGender((short) 0);
        }
    }

    private enum Gender {MALE, FEMALE}

    ;

	@Override
    public String toString() {
        return "Customer=[UserName=" + getUserName() + " Email=" + getEmail() + "Id=" + getId() + "]";
    }
}
