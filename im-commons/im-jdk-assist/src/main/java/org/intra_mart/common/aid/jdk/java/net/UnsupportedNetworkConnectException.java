package org.intra_mart.common.aid.jdk.java.net;

import java.io.IOException;

/**
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 * 
 */
public class UnsupportedNetworkConnectException extends IOException {

	/**
	 * Constructor for UnsupportedNetworkConnectException.
	 */
	public UnsupportedNetworkConnectException() {
		super("Network connection is not supported.");
	}

	/**
	 * Constructor for UnsupportedNetworkConnectException.
	 * @param arg0
	 */
	public UnsupportedNetworkConnectException(String arg0) {
		super(arg0);
	}

}
