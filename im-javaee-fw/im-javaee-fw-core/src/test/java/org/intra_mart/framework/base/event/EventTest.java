/*
 * EventTest.java
 *
 * Created on 2001/11/29, 13:32
 */

package org.intra_mart.framework.base.event;

import junit.framework.TestCase;

/**
 *
 * @author  intra-mart
 * @version 
 */
public abstract class EventTest extends TestCase {

    public EventTest(String name) {
        super(name);
    }

    public abstract Event create();

    public void testApplicationName() {
        String app = "def";
        Event event = create();
        String result;

        event.setApplication(app);
        result = event.getApplication();
        assertTrue("\"" + app + "\"を設定しましたが\"" + result + "\"が取得されました。", result.equals(app));
    }

    public void testKey() {
        String key = "abc";
        Event event = create();
        String result;

        event.setKey(key);
        result = event.getKey();
        assertTrue("\"" + key + "\"を設定しましたが\"" + result + "\"が取得されました。", result.equals(key));
    }
}
