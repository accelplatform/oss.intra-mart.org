package org.intra_mart.jssp.exception;

public class TagRuntimeException extends JSSPSystemException{

	private String tagName = null;

	public TagRuntimeException(String name, Throwable t){
		super("<IMART> tag runtime error: " + name);
		this.tagName = name;
		this.initCause(t);
	}

	public String getTagName(){
		return this.tagName;
	}
}