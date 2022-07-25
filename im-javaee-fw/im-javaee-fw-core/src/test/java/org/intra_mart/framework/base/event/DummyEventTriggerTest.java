/*
 * DummyEventTriggerTest.java
 *
 * Created on 2001/12/06, 15:55
 */

package org.intra_mart.framework.base.event;

import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author  intra-mart
 * @version 
 */
public class DummyEventTriggerTest
    extends TestCase
    implements EventTriggerTestIF {

    private DummyEvent event;
    private DummyEventTrigger trigger;

    public static Test suite() {
        TestSuite suite = new TestSuite(DummyEventTriggerTest.class);
        suite.setName("DummyEventTrigger test");

        return suite;
    }

    /** Creates new DummyEventTriggerTest */
    public DummyEventTriggerTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        this.event = new DummyEvent();
        this.trigger = new DummyEventTrigger();
    }

    public void tearDown() throws Exception {
        this.event = null;
        this.trigger = null;
    }

    public void testFire() throws Exception {
        this.trigger.fire(this.event, null);
    }

    public void testFireException() throws Exception {
        try {
            this.trigger.setApplicationException(true);
            this.trigger.setSystemException(false);
            this.trigger.fire(this.event, null);
            fail();
        } catch (ApplicationException e) {
        }
        try {
            this.trigger.setApplicationException(false);
            this.trigger.setSystemException(true);
            this.trigger.fire(this.event, null);
            fail();
        } catch (SystemException e) {
        }
        assertTrue(true);
    }
}
