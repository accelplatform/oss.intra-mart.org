package org.intra_mart.framework.base;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.intra_mart.framework");
		//$JUnit-BEGIN$
        suite.addTest(org.intra_mart.framework.base.event.AllTests.suite());
        suite.addTest(org.intra_mart.framework.base.service.AllTests.suite());
        suite.addTest(org.intra_mart.framework.base.web.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
