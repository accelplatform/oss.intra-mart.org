/*
 * HTMLEncoderTest.java
 *
 * Created on 2002/03/13, 13:08
 */

package org.intra_mart.framework.base.web.util;

import junit.framework.*;

/**
 *
 * @author  intra-mart
 * @version 
 */
public class HTMLEncoderTest extends TestCase {

    /** Creates new HTMLEncoderTest */
    public HTMLEncoderTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(HTMLEncoderTest.class);
        suite.setName("HTMLEncoder test");

        return suite;
    }

    public void setUp() {
    }

    public void tearDown() {
    }

    public void testCaption() {
        assertEquals("<BR>", HTMLEncoder.encodeCaption("\n"));
        assertEquals("<BR>", HTMLEncoder.encodeCaption("\r"));
        assertEquals("<BR>", HTMLEncoder.encodeCaption("\r\n"));
        assertEquals("<BR><BR>", HTMLEncoder.encodeCaption("\n\r"));
        assertEquals("&lt;&gt;&amp;&quot;&nbsp;<BR><BR><BR>", HTMLEncoder.encodeCaption("<>&\" \n\r\n\r"));
    }

    public void testValue() {
        assertEquals("&lt;&gt;&amp;&quot; \n\r\n\r", HTMLEncoder.encodeValue("<>&\" \n\r\n\r"));
    }
}
