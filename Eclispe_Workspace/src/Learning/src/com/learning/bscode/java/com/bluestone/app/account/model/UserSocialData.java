package com.bluestone.app.account.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "user_social_data", uniqueConstraints = @UniqueConstraint(columnNames = { "social_id", "social_type" }))
public class UserSocialData extends BaseEntity{

	private static final long serialVersionUID = 5365436880065157066L;
	
	public enum SocialType{
		FACEBOOK
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "social_type")
	private SocialType  socialType;
	
	@Column(name = "social_id", columnDefinition = "varchar(32)")
	private String socialId;
	
	@Column(name = "social_metadata", columnDefinition = "varchar(256)")
	private String socialMetadata;
	
	@ManyToOne
    @JoinColumn(name="user_id",nullable=false)
    private User user;
	
	public UserSocialData() {
		
	}
	
	public UserSocialData(SocialType socialType, String socialId,User user) {
		this.socialType = socialType;
		this.socialId = socialId;
		this.user = user;
	}
	
	public UserSocialData(SocialType socialType, String socialId,User user,String socialMetadata) {
		this(socialType,socialId,user);
		this.socialMetadata = socialMetadata;
	}

	public SocialType getSocialType() {
		return socialType;
	}

	public void setSocialType(SocialType socialType) {
		this.socialType = socialType;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public String getSocialMetadata() {
		return socialMetadata;
	}

	public void setSocialMetadata(String socialMetadata) {
		this.socialMetadata = socialMetadata;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "UserSocialData [socialType=" + socialType + ", socialId="
				+ socialId + ", socialMetadata=" + socialMetadata + ", user="
				+ user + "]";
	}

}
