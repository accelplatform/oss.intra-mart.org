package org.intra_mart.framework.base.web;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.intra_mart.framework.base.web");
		//$JUnit-BEGIN$
        suite.addTest(org.intra_mart.framework.base.web.util.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
