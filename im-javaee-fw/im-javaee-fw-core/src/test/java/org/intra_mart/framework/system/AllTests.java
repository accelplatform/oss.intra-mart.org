package org.intra_mart.framework.system;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.intra_mart.framework.system");
		//$JUnit-BEGIN$
        suite.addTest(org.intra_mart.framework.system.exception.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
