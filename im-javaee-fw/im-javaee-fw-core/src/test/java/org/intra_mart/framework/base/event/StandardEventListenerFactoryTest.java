/*
 * StandardEventListenerFactoryTest.java
 *
 * Created on 2001/12/06, 18:27
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
public class StandardEventListenerFactoryTest extends TestCase implements EventListenerFactoryTestIF {

    private StandardEventListenerFactory factory;

    public static Test suite() {
        TestSuite suite = new TestSuite(StandardEventListenerFactoryTest.class);
        suite.setName("StandardEventListenerFactory test");

        return suite;
    }

    /** Creates new StandardEventListenerFactoryTest */
    public StandardEventListenerFactoryTest(String name) {
        super(name);
    }

    public void setUp() {
        this.factory = new StandardEventListenerFactory();
    }

    public void tearDown() {
        this.factory = null;
    }

    public void testCreate() {
        try {
            this.factory.initParam("listener", "org.intra_mart.framework.base.event.DummyStandardEventListener");
            this.factory.create(null);
        } catch (Exception e) {
            System.err.println("[StandardEventListenerFactoryTest:testCreate]");
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }

    public void testCreateException() {
        try {
            this.factory.initParam("listener", "org.intra_mart.framework.base.event.Nothing");
            this.factory.create(null);
        } catch (EventListenerException e) {
        } catch (Exception e) {
            System.err.println("[StandardEventListenerFactoryTest:testCreateException]");
            e.printStackTrace();
            assertTrue(e.getClass().getName() + " : " + e.getMessage(), false);
        }
    }
}
