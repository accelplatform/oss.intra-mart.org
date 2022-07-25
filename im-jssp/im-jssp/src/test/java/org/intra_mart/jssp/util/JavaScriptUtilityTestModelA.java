package org.intra_mart.jssp.util;

import java.util.Date;

public class JavaScriptUtilityTestModelA {
	
	private JavaScriptUtilityTestModelA modelA;
	private JavaScriptUtilityTestModelA[] modelArrayA;

	private JavaScriptUtilityTestModelB modelB;
	private JavaScriptUtilityTestModelB[] modelArrayB;

	private String valueWrapperStringModelA = "モデルA";
	private Number valueWrapperNumberModelA = new Double(394.859);
	private Boolean valueWrapperBooleanModelA = Boolean.FALSE;
	private Date valueWrapperDateModelA = new Date(1209612896000L); // Mon May 1 2008 12:34:56 GMT+0900 (JST)
	
	
	public JavaScriptUtilityTestModelA getModelA() {
		return modelA;
	}
	public void setModelA(JavaScriptUtilityTestModelA modelA) {
		this.modelA = modelA;
	}
	public JavaScriptUtilityTestModelA[] getModelArrayA() {
		return modelArrayA;
	}
	public void setModelArrayA(JavaScriptUtilityTestModelA[] modelArrayA) {
		this.modelArrayA = modelArrayA;
	}
	public JavaScriptUtilityTestModelB getModelB() {
		return modelB;
	}
	public void setModelB(JavaScriptUtilityTestModelB modelB) {
		this.modelB = modelB;
	}
	public JavaScriptUtilityTestModelB[] getModelArrayB() {
		return modelArrayB;
	}
	public void setModelArrayB(JavaScriptUtilityTestModelB[] modelArrayB) {
		this.modelArrayB = modelArrayB;
	}
	public String getValueWrapperStringModelA() {
		return valueWrapperStringModelA;
	}
	public void setValueWrapperStringModelA(String valueWrapperStringModelA) {
		this.valueWrapperStringModelA = valueWrapperStringModelA;
	}
	public Number getValueWrapperNumberModelA() {
		return valueWrapperNumberModelA;
	}
	public void setValueWrapperNumberModelA(Number valueWrapperNumberModelA) {
		this.valueWrapperNumberModelA = valueWrapperNumberModelA;
	}
	public Boolean getValueWrapperBooleanModelA() {
		return valueWrapperBooleanModelA;
	}
	public void setValueWrapperBooleanModelA(Boolean valueWrapperBooleanModelA) {
		this.valueWrapperBooleanModelA = valueWrapperBooleanModelA;
	}
	public Date getValueWrapperDateModelA() {
		return valueWrapperDateModelA;
	}
	public void setValueWrapperDateModelA(Date valueWrapperDateModelA) {
		this.valueWrapperDateModelA = valueWrapperDateModelA;
	}

	
	
	private Object plainObject = new Object();

	public Object getPlainObject() {
		return plainObject;
	}
	public void setPlainObject(Object plainObject) {
		this.plainObject = plainObject;
	}
}
