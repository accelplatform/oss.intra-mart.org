package org.intra_mart.framework.base.web.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.intra_mart.framework.base.web.util");
		//$JUnit-BEGIN$
		suite.addTest(HTMLEncoderTest.suite());
		//$JUnit-END$
		return suite;
	}

}
