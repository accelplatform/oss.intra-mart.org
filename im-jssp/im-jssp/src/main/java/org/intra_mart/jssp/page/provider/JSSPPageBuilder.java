package org.intra_mart.jssp.page.provider;

public interface JSSPPageBuilder {

	/**
	 * @return
	 */
	public boolean verify();

	/**
	 * @throws Exception
	 */
	public void invoke() throws Exception;

}
