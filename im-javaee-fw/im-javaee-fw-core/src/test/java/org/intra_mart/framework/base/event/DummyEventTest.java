/*
 * DummyEventTest.java
 *
 * Created on 2001/11/29, 13:40
 */

package org.intra_mart.framework.base.event;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author  intra-mart
 * @version 
 */
public class DummyEventTest extends EventTest {

    public static Test suite() {
        TestSuite suite = new TestSuite(DummyEventTest.class);
        suite.setName("DummyEvent test");

        return suite;
    }

    public DummyEventTest(String name) {
        super(name);
    }

    public Event create() {
        return new DummyEvent();
    }
}
