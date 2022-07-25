/*
 * TransitionTestIF.java
 *
 * Created on 2002/01/29, 15:40
 */

package org.intra_mart.framework.base.service;

import junit.framework.*;

/**
 *
 * @author  intra-mart
 * @version 
 */
public abstract class TransitionTest extends TestCase {

    /** Creates new TransitionTest */
    public TransitionTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TransitionTest.class);
        suite.setName("Transition test");

        return suite;
    }

    private ServicePropertyHandler handler;

    private Transition testTransition;

    protected void setUp() throws Exception {
        testTransition = createTransition();
    }

    protected Transition getTransition() {
        return this.testTransition;
    }

    abstract protected Transition createTransition();

    protected void tearDown() throws Exception {
        this.handler = null;
        testTransition = null;
    }

    public void testServiceManager() {
        try {
            ServiceManager manager = ServiceManager.getServiceManager();
            testTransition.setServiceManager(manager);
            assertTrue(testTransition.getServiceManager() == manager);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }

    public void testApplication() {
        String application = "dummyApp";

        testTransition.setApplication(application);
        String app = testTransition.getApplication();
        assertTrue(app, app.equals(application));
    }

    public void testService() {
        String service = "dummyService";

        testTransition.setService(service);
        String srv = testTransition.getService();
        assertTrue(srv, srv.equals(service));
    }

    public abstract void testInputErrorPage();

    public abstract void testSystemErrorPage();

    public abstract void testServiceErrorPage();

    public abstract void testInformation();

    public abstract void testNextpage();
}
