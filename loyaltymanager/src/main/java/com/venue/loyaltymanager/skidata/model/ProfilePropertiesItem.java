/**
 * Copyright Venuetize 2017
 * <ProfilePropertiesItem>
 * created by Venuetize
 */
package com.venue.loyaltymanager.skidata.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProfilePropertiesItem implements Serializable {

	private String DataType;
	
	private String DefaultValue;
	
	private String ListItems;
	
	private String Length;
	
	private String PropertyCategory;
	
	private String PropertyDefinitionID;
	
	private String PropertyName;
	
	private String FriendlyPropertyName;
	
	private String PropertyHelp;
	
	private String PropertyValue;
	
	private String ViewOrder;
	
	private String ValidationExpression;
	
	private String IsValid;
	
	private String ValidationMessage;
	
	private String Required;

	public String getDataType() {
		return DataType;
	}

	public void setDataType(String dataType) {
		DataType = dataType;
	}

	public String getDefaultValue() {
		return DefaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		DefaultValue = defaultValue;
	}

	public String getListItems() {
		return ListItems;
	}

	public void setListItems(String listItems) {
		ListItems = listItems;
	}

	public String getLength() {
		return Length;
	}

	public void setLength(String length) {
		Length = length;
	}

	public String getPropertyCategory() {
		return PropertyCategory;
	}

	public void setPropertyCategory(String propertyCategory) {
		PropertyCategory = propertyCategory;
	}

	public String getPropertyDefinitionID() {
		return PropertyDefinitionID;
	}

	public void setPropertyDefinitionID(String propertyDefinitionID) {
		PropertyDefinitionID = propertyDefinitionID;
	}

	public String getPropertyName() {
		return PropertyName;
	}

	public void setPropertyName(String propertyName) {
		PropertyName = propertyName;
	}

	public String getFriendlyPropertyName() {
		return FriendlyPropertyName;
	}

	public void setFriendlyPropertyName(String friendlyPropertyName) {
		FriendlyPropertyName = friendlyPropertyName;
	}

	public String getPropertyHelp() {
		return PropertyHelp;
	}

	public void setPropertyHelp(String propertyHelp) {
		PropertyHelp = propertyHelp;
	}

	public String getPropertyValue() {
		return PropertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		PropertyValue = propertyValue;
	}

	public String getViewOrder() {
		return ViewOrder;
	}

	public void setViewOrder(String viewOrder) {
		ViewOrder = viewOrder;
	}

	public String getValidationExpression() {
		return ValidationExpression;
	}

	public void setValidationExpression(String validationExpression) {
		ValidationExpression = validationExpression;
	}

	public String getIsValid() {
		return IsValid;
	}

	public void setIsValid(String isValid) {
		IsValid = isValid;
	}

	public String getValidationMessage() {
		return ValidationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		ValidationMessage = validationMessage;
	}

	public String getRequired() {
		return Required;
	}

	public void setRequired(String required) {
		Required = required;
	}

}
