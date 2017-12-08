package com.bluestone.app.account.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.core.util.Constants;

@Audited
@Entity
@Table(name = "newslettersubscriber")
public class NewsletterSubscriber extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = Constants.INVALID_EMAIL_ADDRESS_ERROR_MESSAGE)
	@Size(min = 1, max = 254, message = Constants.INVALID_EMAIL_ADDRESS_ERROR_MESSAGE)
	@Pattern(regexp = Constants.VALID_EMAIL_REGEXP, message = Constants.INVALID_EMAIL_ADDRESS_ERROR_MESSAGE)
	@Column(name = "email", columnDefinition = "varchar(254)", nullable = false, unique = true)
	private String email;

	@Column(name = "ipAddress", nullable = true , columnDefinition = "varchar(30)")
    private String ipAddress;

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }


    @Override
    public String toString() {
        return "NewsletterSubscriber{" +
               "email='" + email + '\'' +
               ", ipAddress='" + ipAddress + '\'' +
               '}';
    }
}
