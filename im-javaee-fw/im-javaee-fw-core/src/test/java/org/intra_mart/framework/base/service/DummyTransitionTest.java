/*
 * DummyTransitionTest.java
 *
 * Created on 2002/01/29, 16:19
 */

package org.intra_mart.framework.base.service;

import junit.framework.*;

/**
 *
 * @author  intra-mart
 * @version 
 */
public class DummyTransitionTest extends TransitionTest {

    /** Creates new DummyTransitionTest */
    public DummyTransitionTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DummyTransitionTest.class);
        suite.setName("DummyTransition test");

        return suite;
    }

    protected Transition createTransition() {
        return new DummyTransition();
    }

    public void testInformation() {
        try {
            getTransition().setInformation();
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }

    public void testInputErrorPage() {
        try {
            String page;
            page = getTransition().getInputErrorPage(new RequestException("dummy"));
            assertTrue(page, page.equals("DummyErrorPage: exception = org.intra_mart.framework.base.service.RequestException, dummy"));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }

    public void testNextpage() {
        try {
            String page;
            page = getTransition().getNextPage();
            assertTrue(page, page.equals("NextPage: request is empty, response is empty, result is empty"));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }

    public void testServiceErrorPage() {
        try {
            String page;
            page = getTransition().getServiceErrorPage(new Exception("dummy"));
            assertTrue(page, page.equals("ServiceErrorPage: exception = java.lang.Exception, dummy"));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }

    public void testSystemErrorPage() {
        try {
            String page;
            page = getTransition().getSystemErrorPage(new Exception("dummy"));
            assertTrue(page, page.equals("SystemErrorPage: exception = java.lang.Exception, dummy"));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }

    public void testTransfer() {
        try {
            getTransition().transfer();
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }
}
