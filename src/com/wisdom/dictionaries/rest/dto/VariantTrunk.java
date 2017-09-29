package com.wisdom.dictionaries.rest.dto;

import java.util.List;

import org.json.JSONObject;

public class VariantTrunk {
	
	private String varString;
	private String dictionary;
	private List<JSONObject> props;
	
	
	public String getVarString() {
		return varString;
	}
	public void setVarString(String varString) {
		this.varString = varString;
	}
	public String getDictionary() {
		return dictionary;
	}
	public void setDictionary(String dictionary) {
		this.dictionary = dictionary;
	}
	public List<JSONObject> getProps() {
		return props;
	}
	public void setProps(List<JSONObject> props) {
		this.props = props;
	}
	
	
	

}
