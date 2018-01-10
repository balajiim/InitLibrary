/**
 * Copyright Venuetize 2017
 * <UserRegistrationInfo>
 * created by Venuetize
 */
package com.venue.loyaltymanager.skidata.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class UserRegistrationInfo implements Serializable {

	private String Username;
	
	private String Password;
	
	private String Email;
	
	private String DisplayName;
	
	private String CreateSeatEntities;

	private String SendAdminRegistrationEmail;

	private String SendAdminVerificationEmail;

	public String getCreateSeatEntities() {
		return CreateSeatEntities;
	}

	public void setCreateSeatEntities(String createSeatEntities) {
		CreateSeatEntities = createSeatEntities;
	}

	public String getSendAdminRegistrationEmail() {
		return SendAdminRegistrationEmail;
	}

	public void setSendAdminRegistrationEmail(String sendAdminRegistrationEmail) {
		SendAdminRegistrationEmail = sendAdminRegistrationEmail;
	}

	public String getSendAdminVerificationEmail() {
		return SendAdminVerificationEmail;
	}

	public void setSendAdminVerificationEmail(String sendAdminVerificationEmail) {
		SendAdminVerificationEmail = sendAdminVerificationEmail;
	}

	private List<ProfilePropertiesItem> ProfileProperties;

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getDisplayName() {
		return DisplayName;
	}

	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}

	public List<ProfilePropertiesItem> getProfileProperties() {
		return ProfileProperties;
	}

	public void setProfileProperties(List<ProfilePropertiesItem> profileProperties) {
		ProfileProperties = profileProperties;
	}
}
