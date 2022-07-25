/*
 * GenericEventListenerFactoryTest.java
 *
 * Created on 2001/12/10, 18:47
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
public class GenericEventListenerFactoryTest
    extends TestCase
    implements EventListenerFactoryTestIF {

    private GenericEventListenerFactory factory;

    /** Creates new GenericEventListenerFactoryTest */
    public GenericEventListenerFactoryTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(GenericEventListenerFactoryTest.class);
        suite.setName("GeneralEventListenerFactory test");

        return suite;
    }

    public void setUp() {
        this.factory = new GenericEventListenerFactory();
    }

    public void tearDown() {
        this.factory = null;
    }

    public void testCreate() throws Exception {
        this.factory.initParam(
            "listener",
            "org.intra_mart.framework.base.event.DummyStandardEventListener");
        this.factory.create(null);
    }

    public void testCreateException() throws Exception {
        try {
            this.factory.initParam(
                "listener",
                "org.intra_mart.framework.base.event.Nothing");
            this.factory.create(null);
            fail();
        } catch (EventListenerException e) {
        }
    }
}
