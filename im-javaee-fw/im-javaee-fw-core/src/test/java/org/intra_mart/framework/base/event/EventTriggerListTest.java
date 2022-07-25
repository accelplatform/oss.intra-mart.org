/*
 * EventTriggerListTest.java
 *
 * Created on 2001/12/06, 16:19
 */

package org.intra_mart.framework.base.event;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author  intra-mart
 * @version 
 */
public class EventTriggerListTest extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite(EventTriggerListTest.class);
        suite.setName("EventTriggerList test");

        return suite;
    }

    /** Creates new EventTriggerListTest */
    public EventTriggerListTest(String name) {
        super(name);
    }

    public void testCreateException() throws Exception {
        try {
            EventTriggerList list = new EventTriggerList("app1", "key1");
            fail();
        } catch (EventPropertyException e) {
        }
    }

    public void testFireAll() throws Exception {
        EventTriggerList list = new EventTriggerList("triggerListTest", "key");
        TestMessageEvent event = new TestMessageEvent();

        list.fireAll(event, null);
        assertEquals(
            "trigger message 00/trigger message 01/trigger message 02/",
            event.getMessage());
    }

    public void testFireAllPre() throws Exception {
        EventTriggerList list =
            new EventTriggerList("triggerListTest", "key", true);
        TestMessageEvent event = new TestMessageEvent();

        list.fireAll(event, null);
        assertEquals(
            "trigger message 00/trigger message 01/trigger message 02/",
            event.getMessage());
    }

    public void testFireAllPost() throws Exception {
        EventTriggerList list =
            new EventTriggerList("triggerListTest", "key", false);
        TestMessageEvent event = new TestMessageEvent();

        list.fireAll(event, null);
        assertEquals(
            "trigger message 10/trigger message 11/trigger message 12/",
            event.getMessage());
    }
}
