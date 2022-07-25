package org.intra_mart.jssp.exception;

public class NoSuchTagException extends JSSPSystemException{

	private String tagName = null;

	/**
	 * @param name
	 */
	public NoSuchTagException(String name){
		super("<IMART> tag type is missing: " + name);
		this.tagName = name;
	}

	/**
	 * @return
	 */
	public String getTagName(){
		return this.tagName;
	}
}