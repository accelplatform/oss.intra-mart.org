package org.intra_mart.framework;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.intra_mart.framework");
		//$JUnit-BEGIN$
        suite.addTest(org.intra_mart.framework.base.AllTests.suite());
        suite.addTest(org.intra_mart.framework.system.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
