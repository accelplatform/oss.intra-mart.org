package org.intra_mart.framework.base.service;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.intra_mart.framework.base.service");
		//$JUnit-BEGIN$
        suite.addTest(UTServicePropertyHandlerTest.suite());
        suite.addTest(DefaultServicePropertyHandlerTest.suite());
//        suite.addTest(TextFileServicePropertyHandlerTest.suite());
        suite.addTest(DummyTransitionTest.suite());

		//$JUnit-END$
		return suite;
	}

}
