package org.intra_mart.common.web.log;

/**
 * TODO javadoc
 */
public class HttpServletResponseHeader4ServletFilterBasedLog {
	private String name;
	private Object value;
	
	/**
	 * @param name
	 * @param value
	 */
	public HttpServletResponseHeader4ServletFilterBasedLog(String name, Object value){
		if(name == null){
			throw new IllegalArgumentException(new NullPointerException("argument 'name' is null."));
		}
		
		this.name = name;
		this.value = value;
	}

	/**
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return
	 */
	public Object getValue() {
		return this.value;
	}
}
