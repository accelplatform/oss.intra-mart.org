/*
 * DefaultEventPropertyHandlerTest.java
 *
 * Created on 2001/12/05, 19:40
 */

package org.intra_mart.framework.base.event;

import java.util.Collection;
import java.util.Iterator;

import org.intra_mart.framework.system.property.PropertyHandlerParam;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author  intra-mart
 * @version 
 */
public class DefaultEventPropertyHandlerTest
    extends TestCase
    implements EventPropertyHandlerTestIF {

    /** Creates new DefaultEventPropertyHandlerTest */
    public DefaultEventPropertyHandlerTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DefaultEventPropertyHandlerTest.class);
        suite.setName("DefaultEventPropertyHandler test");

        return suite;
    }

    protected EventPropertyHandler handler;

    protected void setUp() throws Exception {
        this.handler = new DefaultEventPropertyHandler();
        this.handler.init(null);
    }

    protected void tearDown() throws Exception {
        this.handler = null;
    }

    public void testEventName() throws Exception {
        String name = this.handler.getEventName("dummyApp1", "dummyKey");
        assertEquals("test.dummyClass", name);
    }

    public void testEventNameNothing() throws Exception {
        String name = this.handler.getEventName("dummyApp1", "nothingKey");
        assertNull(name);
    }

    public void testEventListenerFactoryName() throws Exception {
        String name =
            this.handler.getEventListenerFactoryName("dummyApp1", "dummyKey");
        assertEquals("test.dummyFactory", name);
    }

    public void testEventListenerFactoryNameException() throws Exception {
        String name = null;
        try {
            name =
                this.handler.getEventListenerFactoryName(
                    "dummyIllegalApp",
                    "dummyIllegalKey");
            fail(name);
        } catch (EventPropertyException e) {
        }
    }

    public void testEventListenerFactoryOption() throws Exception {
        EventListenerFactoryParam[] params =
            this.handler.getEventListenerFactoryParams("dummyApp1", "dummyKey");
        String param0 = params[0].getName() + ":" + params[0].getValue();
        String param1 = params[1].getName() + ":" + params[1].getValue();
        assertTrue(
            param0 + "," + param1,
            param0.equals("dummyParam0:dummyParamValue0")
                && param1.equals("dummyParam1:dummyParamValue1")
                || param0.equals("dummyParam1:dummyParamValue1")
                && param1.equals("dummyParam0:dummyParamValue0"));
    }

    public void testEventListenerFactoryOptionNothing() throws Exception {
        EventListenerFactoryParam[] params = null;

        params =
            this.handler.getEventListenerFactoryParams(
                "dummyApp1",
                "dummyIllegalKey");
        assertNotNull(params);
        assertEquals(0, params.length);
    }

    public void testEventListenerFactoryOptionException() throws Exception {
        EventListenerFactoryParam[] params = null;

        try {
            params =
                this.handler.getEventListenerFactoryParams(
                    "dummyIllegalApp",
                    "dummyIllegalKey");
            fail(params.toString());
        } catch (EventPropertyException e) {
        }
    }

    public void testEventTriggerInfos() throws Exception {
        EventTriggerInfo trigger = null;
        Iterator triggers = null;
        String name = null;
        int number = 0;

        //
        // アプリケーション：dummyApp1
        // キー　　　　　　：dummyKey
        triggers =
            this
                .handler
                .getEventTriggerInfos("dummyApp1", "dummyKey")
                .iterator();

        // trigger 0
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(0, number);
        assertEquals("trigger0", name);

        // trigger 1
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(1, number);
        assertEquals("trigger1", name);

        // trigger 2
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(2, number);
        assertEquals("trigger2", name);

        // 終了
        assertFalse(triggers.hasNext());

        //
        // アプリケーション：dummyApp2
        // キー　　　　　　：dummyKey
        triggers =
            this
                .handler
                .getEventTriggerInfos("dummyApp2", "dummyKey")
                .iterator();
        name = null;
        number = 0;

        // trigger 0
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(0, number);
        assertEquals("trigger0App2", name);

        // trigger 1
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(1, number);
        assertEquals("trigger1App2", name);

        // trigger 2
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(2, number);
        assertEquals("trigger2App2", name);

        // 終了
        assertFalse(triggers.hasNext());

        //
        // 4.3 対応
        //

        // 前トリガ
        triggers =
            this.handler.getEventTriggerInfos("dummyApp1", "key3").iterator();

        // 前trigger 0
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(0, number);
        assertEquals("trigger3_0_pre", name);

        // 前trigger 1
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(1, number);
        assertEquals("trigger3_1_pre", name);

        // 前trigger 2
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(2, number);
        assertEquals("trigger3_2_pre", name);

        // 終了
        assertFalse(triggers.hasNext());

        // 前トリガ
        triggers =
            this.handler.getEventTriggerInfos("dummyApp1", "key4").iterator();

        // 前trigger 0
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(0, number);
        assertEquals("trigger4_0_pre", name);

        // 前trigger 1
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(1, number);
        assertEquals("trigger4_1_pre", name);

        // 前trigger 2
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(2, number);
        assertEquals("trigger4_2_pre", name);

        // 終了
        assertFalse(triggers.hasNext());
    }

    public void testEventTriggerInfosException() throws Exception {
        Collection infos = null;
        String application1 = "dummyIllegalApp1";
        String key1 = "dummyIllegalKey1";
        String application2 = "dummyIllegalApp2";
        String key2 = "dummyIllegalKey2";

        PropertyHandlerParam param = new PropertyHandlerParam();
        param.setName("bundle");
        param.setValue("EventExceptionConfig");
        PropertyHandlerParam[] params = { param };
        this.handler.init(params);
        try {
            infos = this.handler.getEventTriggerInfos(application1, key1);
            fail(infos.toString());
        } catch (EventPropertyException e) {
        }

        try {
            infos = this.handler.getEventTriggerInfos(application2, key2);
            fail(infos.toString());
        } catch (EventPropertyException e) {
        }
    }

    public void testPostEventTriggerInfos() throws Exception {
        Iterator triggers = null;
        EventTriggerInfo trigger = null;
        int number = 0;
        String name = null;

        // 後トリガ
        triggers =
            this
                .handler
                .getPostEventTriggerInfos("dummyApp1", "key3")
                .iterator();

        // 後trigger 0
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(0, number);
        assertEquals("trigger3_0_post", name);

        // 後trigger 1
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(1, number);
        assertEquals("trigger3_1_post", name);

        // 後trigger 2
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(2, number);
        assertEquals("trigger3_2_post", name);

        // 終了
        assertFalse(triggers.hasNext());

        // 後トリガ
        triggers =
            this
                .handler
                .getPostEventTriggerInfos("dummyApp1", "key4")
                .iterator();

        // 後trigger 0
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(0, number);
        assertEquals("trigger4_0_post", name);

        // 後trigger 1
        assertTrue(triggers.hasNext());
        trigger = (EventTriggerInfo)triggers.next();
        number = trigger.getNumber();
        name = trigger.getName();
        assertEquals(1, number);
        assertEquals("trigger4_1_post", name);

        // 終了
        assertFalse(triggers.hasNext());
    }

    public void testPostEventTriggerInfosException() throws Exception {
        Collection infos = null;
        String application1 = "dummyIllegalApp1";
        String key1 = "dummyIllegalKey1";
        String application2 = "dummyIllegalApp2";
        String key2 = "dummyIllegalKey2";

        PropertyHandlerParam param = new PropertyHandlerParam();
        param.setName("bundle");
        param.setValue("EventExceptionConfig");
        PropertyHandlerParam[] params = { param };
        this.handler.init(params);

        try {
            infos = this.handler.getPostEventTriggerInfos(application1, key1);
            fail(infos.toString());
            ;
        } catch (EventPropertyException e) {
        }

        try {
            infos = this.handler.getPostEventTriggerInfos(application2, key2);
            fail(infos.toString());
        } catch (EventPropertyException e) {
        }
    }
}
