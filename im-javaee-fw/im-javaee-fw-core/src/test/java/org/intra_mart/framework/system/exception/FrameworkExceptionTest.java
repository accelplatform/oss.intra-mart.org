/*
 * FrameworkExceptionTest.java
 *
 * Created on 2001/10/25, 17:53
 */

package org.intra_mart.framework.system.exception;

import junit.framework.*;

/**
 * {@link FrameworkException FrameworkException}に関連するテストクラスです。
 *
 * @author intra-mart
 * @version 1.0
 */
public class FrameworkExceptionTest extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite(FrameworkExceptionTest.class);
        suite.setName("FrameworkException test");

        return suite;
    }

    public FrameworkExceptionTest(String name) {
        super(name);
    }

    private FrameworkException frameworkException;

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Exception の設定・取得のテストです。
     */
    public void testException() {
        Exception e = new Exception();
        FrameworkException frameworkException1 = new FrameworkException("abc", e);
        assertTrue(frameworkException1.getException() == e);
        assertTrue(frameworkException1.getMessage().equals("abc"));

        FrameworkException frameworkException2 = new FrameworkException(e);
        assertTrue(frameworkException2.getException() == e);
        
        FrameworkException frameworkException3 = new FrameworkException("def");
        assertTrue(frameworkException3.getMessage().equals("def"));
    }

    /**
     * stackTraceのテストです。
     */
//    public void testStackTrace() {
//    }
}
