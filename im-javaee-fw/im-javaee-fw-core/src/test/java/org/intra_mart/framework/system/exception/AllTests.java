package org.intra_mart.framework.system.exception;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.intra_mart.framework.system.exception");
		//$JUnit-BEGIN$
		suite.addTest(FrameworkExceptionTest.suite());
		//$JUnit-END$
		return suite;
	}

}
