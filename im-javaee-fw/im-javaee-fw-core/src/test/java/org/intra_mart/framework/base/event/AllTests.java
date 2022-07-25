package org.intra_mart.framework.base.event;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.intra_mart.framework.base.event");
		//$JUnit-BEGIN$
        suite.addTest(DefaultEventPropertyHandlerTest.suite());
//        suite.addTest(TextFileEventPropertyHandlerTest.suite());
        suite.addTest(DummyEventTest.suite());
        suite.addTest(DummyEventTriggerTest.suite());
//        suite.addTest(DummyStandardEventListenerTest.suite());
        suite.addTest(EventTriggerListTest.suite());
//        suite.addTest(GenericEventListenerTest.suite());
        suite.addTest(StandardEventListenerFactoryTest.suite());
        suite.addTest(GenericEventListenerFactoryTest.suite());
//        suite.addTest(EventManagerTest.suite());
//        suite.addTest(TestMessageStandardEventListenerTest.suite());
		//$JUnit-END$
		return suite;
	}

}
