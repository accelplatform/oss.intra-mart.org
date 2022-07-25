/*
 * TextFileServicePropertyHandlerTest.java
 *
 * Created on 2002/07/08, 17:31
 */

package org.intra_mart.framework.base.service;

import java.util.Locale;
import java.util.Vector;

import org.intra_mart.framework.system.property.PropertyHandlerParam;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * @author intra-mart
 */
public class UTServicePropertyHandlerTest extends TestCase implements
        ServicePropertyHandlerTestIF {

    private UTServicePropertyHandler handler = null;

    private static final String PROPERTY_DIR = ".";

    private static final String PROPERTY_FILE_PREFIX = "TextFileServiceConfig";

    public static Test suite() {
        TestSuite suite = new TestSuite(UTServicePropertyHandlerTest.class);
        suite.setName("UTervicePropertyHandler test");

        return suite;
    }

    /** Creates a new instance of TextFileServicePropertyHandlerTest */
    public UTServicePropertyHandlerTest(String name) {
        super(name);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    private void setDefault() throws Exception {
        try {

            // common
            this.handler.setClientEncoding("DummyEncode");
            assertEquals("DummyEncode", this.handler.getClientEncoding());
            this.handler.setClientLocale(new Locale("du", "MM", "Y"));
            assertEquals(new Locale("du", "MM", "Y"), this.handler
                    .getClientLocale());
            this.handler.setServiceServletPath("DummyServletURL");
            assertEquals("DummyServletURL", this.handler
                    .getServiceServletPath());
            this.handler.setInputErrorPagePath("DummyInputErrorPageCommon");
            assertEquals("DummyInputErrorPageCommon", this.handler
                    .getInputErrorPagePath());
            this.handler.setServiceErrorPagePath("DummyServiceErrorPageCommon");
            assertEquals("DummyServiceErrorPageCommon", this.handler
                    .getServiceErrorPagePath());
            this.handler.setServiceErrorPagePath("dummyApp",
                    "DummyServiceErrorPage3");
            assertEquals("DummyServiceErrorPage3", this.handler
                    .getServiceErrorPagePath("dummyApp"));
            this.handler.setSystemErrorPagePath("DummySystemErrorPageCommon");
            assertEquals("DummySystemErrorPageCommon", this.handler
                    .getSystemErrorPagePath());
            this.handler.setContextPath("DummyContext");
            assertEquals("DummyContext", this.handler.getContextPath());
            this.handler.setEncodingAttributeName("DummyEncodeAttribute1");
            assertEquals("DummyEncodeAttribute1", this.handler
                    .getEncodingAttributeName());
            this.handler.setLocaleAttributeName("DummyLocaleAttribute1");
            assertEquals("DummyLocaleAttribute1", this.handler
                    .getLocaleAttributeName());

            // application (dummyApp)
            this.handler.setInputErrorPagePath("dummyApp", "dummyService",
                    "dummyKey", "DummyInputErrorPage");
            assertEquals("DummyInputErrorPage", this.handler
                    .getInputErrorPagePath("dummyApp", "dummyService",
                            "dummyKey"));
            this.handler.setTransitionName("dummyApp", "dummyService",
                    "DummyTransition");
            assertEquals("DummyTransition", this.handler.getTransitionName(
                    "dummyApp", "dummyService"));
            this.handler.setSystemErrorPagePath("dummyApp",
                    "DummySystemErrorPage3");
            assertEquals("DummySystemErrorPage3", this.handler
                    .getSystemErrorPagePath("dummyApp"));
            this.handler.setSystemErrorPagePath("dummyApp", "dummyService",
                    "DummySystemErrorPage2");
            assertEquals("DummySystemErrorPage2", this.handler
                    .getSystemErrorPagePath("dummyApp", "dummyService"));
            this.handler.setServiceErrorPagePath("dummyApp", "dummyService",
                    "DummyServiceErrorPage2");
            assertEquals("DummyServiceErrorPage2", this.handler
                    .getServiceErrorPagePath("dummyApp", "dummyService"));
            this.handler.setInputErrorPagePath("dummyApp",
                    "DummyInputErrorPage3");
            assertEquals("DummyInputErrorPage3", this.handler
                    .getInputErrorPagePath("dummyApp"));
            this.handler.setInputErrorPagePath("dummyApp", "dummyService",
                    "DummyInputErrorPage2");
            assertEquals("DummyInputErrorPage2", this.handler
                    .getInputErrorPagePath("dummyApp", "dummyService"));
            this.handler.setSystemErrorPagePath("dummyApp", "dummyService",
                    "dummyKey", "DummySystemErrorPage");
            assertEquals("DummySystemErrorPage", this.handler
                    .getSystemErrorPagePath("dummyApp", "dummyService",
                            "dummyKey"));
            this.handler.setServiceErrorPagePath("dummyApp", "dummyService",
                    "dummyKey", "DummyServiceErrorPage");
            assertEquals("DummyServiceErrorPage", this.handler
                    .getServiceErrorPagePath("dummyApp", "dummyService",
                            "dummyKey"));
            this.handler.setServiceControllerName("dummyApp", "dummyService",
                    "DummyController");
            assertEquals("DummyController", this.handler
                    .getServiceControllerName("dummyApp", "dummyService"));
            this.handler.setNextPagePath("dummyApp", "dummyService",
                    "dummyKey", "DummyNextPageWithKey");
            assertEquals("DummyNextPageWithKey", this.handler.getNextPagePath(
                    "dummyApp", "dummyService", "dummyKey"));
            this.handler.setNextPagePath("dummyApp", "dummyService",
                    "DummyNextPageWithoutKey");
            assertEquals("DummyNextPageWithoutKey", this.handler
                    .getNextPagePath("dummyApp", "dummyService"));

            // application (dummyApp2)
            this.handler.setTransitionName("dummyApp2", "dummyService",
                    "DummyTransition");
            this.handler.setServiceControllerName("dummyApp2", "dummyService",
                    "DummyController");
            this.handler.setNextPagePath("dummyApp2", "dummyService",
                    "dummyKey", "DummyNextPageWithKey");
            this.handler.setNextPagePath("dummyApp2", "dummyService",
                    "DummyNextPageWithoutKey");

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }

    private void replace() throws Exception {

        // common
        this.handler.setClientEncoding("ReplacedDummyEncode");
        this.handler.setClientLocale(new Locale("du", "MM", "YREPLACED"));
        this.handler.setServiceServletPath("ReplacedDummyServletURL");
        this.handler.setInputErrorPagePath("ReplacedDummyInputErrorPageCommon");
        this.handler
                .setServiceErrorPagePath("ReplacedDummyServiceErrorPageCommon");
        this.handler
                .setServiceErrorPagePath("ReplacedDummyServiceErrorPageCommon");
        this.handler
                .setSystemErrorPagePath("ReplacedDummySystemErrorPageCommon");
        this.handler.setContextPath("ReplacedDummyContext");
        Vector cacheRuleInfos = new Vector();
        this.handler.setEncodingAttributeName("DummyEncodeAttribute2");
        this.handler.setLocaleAttributeName("DummyLocaleAttribute2");
        this.handler.setApplicationParamName("DummyApplicationParam2");
        this.handler.setServiceParamName("DummyServiceParam2");

        // application (dummyApp)
        this.handler.setInputErrorPagePath("dummyApp", "dummyService",
                "dummyKey", "ReplacedDummyInputErrorPage");
        this.handler.setTransitionName("dummyApp", "dummyService",
                "ReplacedDummyTransition");
        this.handler.setServiceErrorPagePath("dummyApp",
                "ReplacedDummyServiceErrorPage3");
        this.handler.setSystemErrorPagePath("dummyApp",
                "ReplacedDummySystemErrorPage3");
        this.handler.setSystemErrorPagePath("dummyApp", "dummyService",
                "ReplacedDummySystemErrorPage2");
        this.handler.setServiceErrorPagePath("dummyApp", "dummyService",
                "ReplacedDummyServiceErrorPage2");
        this.handler.setInputErrorPagePath("dummyApp",
                "ReplacedDummyInputErrorPage3");
        this.handler.setInputErrorPagePath("dummyApp", "dummyService",
                "ReplacedDummyInputErrorPage2");
        this.handler.setSystemErrorPagePath("dummyApp", "dummyService",
                "dummyKey", "ReplacedDummySystemErrorPage");
        this.handler.setServiceErrorPagePath("dummyApp", "dummyService",
                "dummyKey", "ReplacedDummyServiceErrorPage");
        this.handler.setServiceControllerName("dummyApp", "dummyService",
                "ReplacedDummyController");
        this.handler.setNextPagePath("dummyApp", "dummyService", "dummyKey",
                "ReplacedDummyNextPageWithKey");
        this.handler.setNextPagePath("dummyApp", "dummyService",
                "ReplacedDummyNextPageWithoutKey");

        // application (dummyApp2)
        this.handler.setTransitionName("dummyApp2", "dummyService",
                "ReplacedDummyTransition");
        this.handler.setServiceControllerName("dummyApp2", "dummyService",
                "ReplacedDummyController");
        this.handler.setNextPagePath("dummyApp2", "dummyService", "dummyKey",
                "ReplacedDummyNextPageWithKey");
        this.handler.setNextPagePath("dummyApp2", "dummyService",
                "ReplacedDummyNextPageWithoutKey");
    }

    private void handlerInit(boolean dynamic) throws Exception {
        PropertyHandlerParam[] params = new PropertyHandlerParam[1];
        params[0] = new PropertyHandlerParam();
        params[0].setName(TextFileServicePropertyHandler.PARAM_DYNAMIC);
        if (dynamic) {
            params[0].setValue("true");
        } else {
            params[0].setValue("false");
        }
        this.handler.init(params);
    }

    protected void setUp() throws Exception {
        this.handler = new UTServicePropertyHandler();
        setDefault();
    }

    public void testClientEncoding() throws Exception {
        handlerInit(true);
        String encode = null;

        encode = this.handler.getClientEncoding();
        assertEquals("encode = " + encode, "DummyEncode", encode);

        replace();

        encode = this.handler.getClientEncoding();
        assertEquals("encode = " + encode, "ReplacedDummyEncode", encode);
    }

    public void testClientLocale() throws Exception {
        handlerInit(true);
        Locale locale = null;

        locale = this.handler.getClientLocale();
        assertEquals("locale = " + locale, new Locale("du", "MM", "Y"), locale);

        replace();

        locale = this.handler.getClientLocale();
        assertEquals("locale = " + locale, new Locale("du", "MM", "YREPLACED"),
                locale);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testSystemErrorPagePathAppServiceKey() throws Exception {
        handlerInit(true);
        String path = null;

        // ApplicationImpl, Service, Key が一致する
        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                "dummyKey");
        assertEquals("path = " + path, "DummySystemErrorPage", path);

        // ApplicationImpl, Service が一致する
        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                "dummyKey2");
        assertEquals("path = " + path, "DummySystemErrorPage2", path);

        // ApplicationImpl が一致する
        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService2",
                "dummyKey2");
        assertEquals("path = " + path, "DummySystemErrorPage3", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath("dummyApp2",
                "dummyService2", "dummyKey2");
        assertEquals("path = " + path, "DummySystemErrorPageCommon", path);

        replace();

        String url = null;

        // ApplicationImpl, Service, Key が一致する
        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                "dummyKey");
        assertEquals("path = " + path, "ReplacedDummySystemErrorPage", path);

        // ApplicationImpl, Service が一致する
        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                "dummyKey2");
        assertEquals("path = " + path, "ReplacedDummySystemErrorPage2", path);

        // ApplicationImpl が一致する
        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService2",
                "dummyKey2");
        assertEquals("path = " + path, "ReplacedDummySystemErrorPage3", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath("dummyApp2",
                "dummyService2", "dummyKey2");
        assertEquals("path = " + path, "ReplacedDummySystemErrorPageCommon",
                path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testSystemErrorPagePathAppService() throws Exception {
        handlerInit(true);
        String path = null;

        // ApplicationImpl, Service が一致する
        path = handler.getSystemErrorPagePath("dummyApp", "dummyService");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummySystemErrorPage2", path);

        // ApplicationImpl が一致する
        path = handler.getSystemErrorPagePath("dummyApp", "dummyService2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummySystemErrorPage3", path);

        // 一致しない
        path = handler.getSystemErrorPagePath("dummyApp2", "dummyService2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummySystemErrorPageCommon", path);

        replace();

        // ApplicationImpl, Service が一致する
        path = handler.getSystemErrorPagePath("dummyApp", "dummyService");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummySystemErrorPage2", path);

        // ApplicationImpl が一致する
        path = handler.getSystemErrorPagePath("dummyApp", "dummyService2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummySystemErrorPage3", path);

        // 一致しない
        path = handler.getSystemErrorPagePath("dummyApp2", "dummyService2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummySystemErrorPageCommon",
                path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testSystemErrorPagePathApp() throws Exception {
        handlerInit(true);
        String path = null;

        // ApplicationImpl が一致する
        path = handler.getSystemErrorPagePath("dummyApp");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummySystemErrorPage3", path);

        // 一致しない
        path = handler.getSystemErrorPagePath("dummyApp2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummySystemErrorPageCommon", path);

        replace();

        // ApplicationImpl が一致する
        path = handler.getSystemErrorPagePath("dummyApp");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummySystemErrorPage3", path);

        // 一致しない
        path = handler.getSystemErrorPagePath("dummyApp2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummySystemErrorPageCommon",
                path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testSystemErrorPagePath() throws Exception {
        handlerInit(true);
        String path = null;

        // 一致しない
        path = handler.getSystemErrorPagePath();
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummySystemErrorPageCommon", path);

        replace();

        // 一致しない
        path = handler.getSystemErrorPagePath();
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummySystemErrorPageCommon",
                path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testSystemErrorPagePathException() throws Exception {
        handlerInit(true);
        String path = null;

        try {
            path = handler.getSystemErrorPagePath("dummyApp3", "dummyService",
                    "dummyKey");
        } catch (ServicePropertyException e) {
            assertTrue(true);
        }

        replace();

        try {
            path = handler.getSystemErrorPagePath("dummyApp3", "dummyService",
                    "dummyKey");
        } catch (ServicePropertyException e) {
            assertTrue(true);
        }
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testNextPagePathWithKey() throws Exception {
        handlerInit(true);
        String path = null;

        path = handler.getNextPagePath("dummyApp", "dummyService", "dummyKey");
        assertEquals("path = " + path, "DummyNextPageWithKey", path);

        replace();

        path = handler.getNextPagePath("dummyApp", "dummyService", "dummyKey");
        assertEquals("path = " + path, "ReplacedDummyNextPageWithKey", path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testTransitionName() throws Exception {
        handlerInit(true);
        String transition = null;

        transition = handler.getTransitionName("dummyApp", "dummyService");
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition, "DummyTransition",
                transition);

        replace();

        transition = handler.getTransitionName("dummyApp", "dummyService");
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition, "ReplacedDummyTransition",
                transition);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testServiceControllerName() throws Exception {
        handlerInit(true);
        String controller = null;

        controller = handler.getServiceControllerName("dummyApp",
                "dummyService");
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller, "DummyController",
                controller);

        replace();

        controller = handler.getServiceControllerName("dummyApp",
                "dummyService");
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller, "ReplacedDummyController",
                controller);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testInputErrorPagePathAppServiceKey() throws Exception {
        handlerInit(true);
        String path = null;

        // ApplicationImpl, Service, Key が一致する
        path = handler.getInputErrorPagePath("dummyApp", "dummyService",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPage", path);

        // ApplicationImpl, Service が一致する
        path = handler.getInputErrorPagePath("dummyApp", "dummyService",
                "dummyKey2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPage2", path);

        // ApplicationImpl が一致する
        path = handler.getInputErrorPagePath("dummyApp", "dummyService2",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPage3", path);

        // 一致しない
        path = handler.getInputErrorPagePath("dummyApp2", "dummyService",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPageCommon", path);

        replace();

        // ApplicationImpl, Service, Key が一致する
        path = handler.getInputErrorPagePath("dummyApp", "dummyService",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyInputErrorPage", path);

        // ApplicationImpl, Service が一致する
        path = handler.getInputErrorPagePath("dummyApp", "dummyService",
                "dummyKey2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyInputErrorPage2", path);

        // ApplicationImpl が一致する
        path = handler.getInputErrorPagePath("dummyApp", "dummyService2",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyInputErrorPage3", path);

        // 一致しない
        path = handler.getInputErrorPagePath("dummyApp2", "dummyService",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyInputErrorPageCommon",
                path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testInputErrorPagePathAppService() throws Exception {
        handlerInit(true);
        String path = null;

        // ApplicationImpl, Service が一致する
        path = handler.getInputErrorPagePath("dummyApp", "dummyService");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPage2", path);

        // ApplicationImpl が一致する
        path = handler.getInputErrorPagePath("dummyApp", "dummyService2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPage3", path);

        // 一致しない
        path = handler.getInputErrorPagePath("dummyApp2", "dummyService");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPageCommon", path);

        replace();

        // ApplicationImpl, Service が一致する
        path = handler.getInputErrorPagePath("dummyApp", "dummyService");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyInputErrorPage2", path);

        // ApplicationImpl が一致する
        path = handler.getInputErrorPagePath("dummyApp", "dummyService2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyInputErrorPage3", path);

        // 一致しない
        path = handler.getInputErrorPagePath("dummyApp2", "dummyService");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyInputErrorPageCommon",
                path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testInputErrorPagePathApp() throws Exception {
        handlerInit(true);
        String path = null;

        // ApplicationImpl が一致する
        path = handler.getInputErrorPagePath("dummyApp");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPage3", path);

        // 一致しない
        path = handler.getInputErrorPagePath("dummyApp2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPageCommon", path);

        replace();

        // ApplicationImpl が一致する
        path = handler.getInputErrorPagePath("dummyApp");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyInputErrorPage3", path);

        // 一致しない
        path = handler.getInputErrorPagePath("dummyApp2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyInputErrorPageCommon",
                path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testInputErrorPagePath() throws Exception {
        handlerInit(true);
        String path = null;

        // 一致しない
        path = handler.getInputErrorPagePath();
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPageCommon", path);

        replace();

        // 一致しない
        path = handler.getInputErrorPagePath();
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyInputErrorPageCommon",
                path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testInputErrorPagePathException() throws Exception {
        handlerInit(true);
        String path = null;

        try {
            path = handler.getInputErrorPagePath("dummyApp3", "dummyService",
                    "dummyKey");
        } catch (ServicePropertyException e) {
            assertTrue(true);
        }

        replace();

        try {
            path = handler.getInputErrorPagePath("dummyApp3", "dummyService",
                    "dummyKey");
        } catch (ServicePropertyException e) {
            assertTrue(true);
        }
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testServiceErrorPagePathAppServiceKey() throws Exception {
        handlerInit(true);
        String path = null;

        // ApplicationImpl, Service, Key が一致する
        path = handler.getServiceErrorPagePath("dummyApp", "dummyService",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPage", path);

        // ApplicationImpl, Service が一致する
        path = handler.getServiceErrorPagePath("dummyApp", "dummyService",
                "dummyKey2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPage2", path);

        // ApplicationImpl が一致する
        path = handler.getServiceErrorPagePath("dummyApp", "dummyService2",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPage3", path);

        // 一致しない
        path = handler.getServiceErrorPagePath("dummyApp2", "dummyService",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPageCommon", path);

        replace();

        // ApplicationImpl, Service, Key が一致する
        path = handler.getServiceErrorPagePath("dummyApp", "dummyService",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyServiceErrorPage", path);

        // ApplicationImpl, Service が一致する
        path = handler.getServiceErrorPagePath("dummyApp", "dummyService",
                "dummyKey2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyServiceErrorPage2", path);

        // ApplicationImpl が一致する
        path = handler.getServiceErrorPagePath("dummyApp", "dummyService2",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyServiceErrorPage3", path);

        // 一致しない
        path = handler.getServiceErrorPagePath("dummyApp2", "dummyService",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyServiceErrorPageCommon",
                path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testServiceErrorPagePathAppService() throws Exception {
        handlerInit(true);
        String path = null;

        // ApplicationImpl, Service が一致する
        path = handler.getServiceErrorPagePath("dummyApp", "dummyService");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPage2", path);

        // ApplicationImpl が一致する
        path = handler.getServiceErrorPagePath("dummyApp", "dummyService2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPage3", path);

        // 一致しない
        path = handler.getServiceErrorPagePath("dummyApp2", "dummyService");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPageCommon", path);

        replace();

        // ApplicationImpl, Service が一致する
        path = handler.getServiceErrorPagePath("dummyApp", "dummyService");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyServiceErrorPage2", path);

        // ApplicationImpl が一致する
        path = handler.getServiceErrorPagePath("dummyApp", "dummyService2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyServiceErrorPage3", path);

        // 一致しない
        path = handler.getServiceErrorPagePath("dummyApp2", "dummyService");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyServiceErrorPageCommon",
                path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testServiceErrorPagePathApp() throws Exception {
        handlerInit(true);
        String path = null;

        // ApplicationImpl が一致する
        path = handler.getServiceErrorPagePath("dummyApp");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPage3", path);

        // 一致しない
        path = handler.getServiceErrorPagePath("dummyApp2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPageCommon", path);

        replace();

        // ApplicationImpl が一致する
        path = handler.getServiceErrorPagePath("dummyApp");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyServiceErrorPage3", path);

        // 一致しない
        path = handler.getServiceErrorPagePath("dummyApp2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyServiceErrorPageCommon",
                path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testServiceErrorPagePath() throws Exception {
        handlerInit(true);
        String path = null;

        // 一致しない
        path = handler.getServiceErrorPagePath();
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPageCommon", path);

        replace();

        // 一致しない
        path = handler.getServiceErrorPagePath();
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyServiceErrorPageCommon",
                path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testServiceErrorPagePathException() throws Exception {
        handlerInit(true);
        String path = null;

        try {
            path = handler.getServiceErrorPagePath("dummyApp3", "dummyService",
                    "dummyKey");
        } catch (ServicePropertyException e) {
            assertTrue(true);
        }

        replace();

        try {
            path = handler.getServiceErrorPagePath("dummyApp3", "dummyService",
                    "dummyKey");
        } catch (ServicePropertyException e) {
            assertTrue(true);
        }
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testNextPagePathWithoutKey() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getNextPagePath("dummyApp", "dummyService");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyNextPageWithoutKey", path);

        replace();

        path = this.handler.getNextPagePath("dummyApp2", "dummyService");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyNextPageWithoutKey", path);
    }

    public void testServiceServletPath() throws Exception {
        handlerInit(true);
        String path = null;

        path = handler.getServiceServletPath();
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServletURL", path);

        replace();

        path = handler.getServiceServletPath();
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyServletURL", path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testContextPath() throws Exception {
        handlerInit(true);
        String path = null;

        path = handler.getContextPath();
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyContext", path);

        replace();

        path = handler.getContextPath();
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "ReplacedDummyContext", path);
    }

    public void testEncodeAttributeName() throws Exception {
        handlerInit(true);
        String encode = null;

        encode = this.handler.getEncodingAttributeName();
        assertEquals("encode attribute name = " + encode,
                "DummyEncodeAttribute1", encode);

        replace();

        encode = this.handler.getEncodingAttributeName();
        assertEquals("encode attribute name = " + encode, encode,
                "DummyEncodeAttribute2");
    }

    public void testLocaleAttributeName() throws Exception {
        handlerInit(true);
        String locale = null;

        locale = this.handler.getLocaleAttributeName();
        assertEquals("locale attribute name = " + locale, locale,
                "DummyLocaleAttribute1");

        replace();

        locale = this.handler.getLocaleAttributeName();
        assertEquals("locale attribute name = " + locale, locale,
                "DummyLocaleAttribute2");
    }

    public void testApplicationParamName() throws Exception {
        handlerInit(true);
        String application = null;

        application = this.handler.getApplicationParamName();
        assertEquals("application param name = " + application,
                ServicePropertyHandler.DEFAULT_APPLICATION_PARAMETER,
                application);

        replace();

        application = this.handler.getApplicationParamName();
        assertEquals("application param name = " + application,
                "DummyApplicationParam2", application);
    }

    public void testServiceParamName() throws Exception {
        handlerInit(true);
        String service = null;

        service = this.handler.getServiceParamName();
        assertEquals("service param name = " + service, service,
                ServicePropertyHandler.DEFAULT_SERVICE_PARAMETER);

        replace();

        service = this.handler.getServiceParamName();
        assertEquals("service param name = " + service, service,
                "DummyServiceParam2");
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppForLocale() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getInputErrorPagePath("dummyApp", (Locale) null);
        assertEquals("input error page path = " + path, "DummyInputErrorPage3",
                path);

        replace();

        path = this.handler.getInputErrorPagePath("dummyApp", (Locale) null);
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPage3", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppForLocaleLanguage() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getInputErrorPagePath("dummyApp", new Locale("aa",
                ""));
        assertEquals("input error page path = " + path, "DummyInputErrorPage3",
                path);

        replace();

        path = this.handler.getInputErrorPagePath("dummyApp", new Locale("aa",
                ""));
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPage3", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppForLocaleLanguageCountry()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getInputErrorPagePath("dummyApp", new Locale("aa",
                "AA"));
        assertEquals("input error page path = " + path, "DummyInputErrorPage3",
                path);

        replace();

        path = this.handler.getInputErrorPagePath("dummyApp", new Locale("aa",
                "AA"));
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPage3", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppForLocaleLanguageCountryVariant()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getInputErrorPagePath("dummyApp", new Locale("aa",
                "AA", "AAA"));
        assertEquals("input error page path = " + path, "DummyInputErrorPage3",
                path);

        replace();

        path = this.handler.getInputErrorPagePath("dummyApp", new Locale("aa",
                "AA", "AAA"));
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPage3", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceForLocale() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                (Locale) null);
        assertEquals("input error page path = " + path, "DummyInputErrorPage2",
                path);

        replace();

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                (Locale) null);
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPage2", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceForLocaleLanguage()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", ""));
        assertEquals("input error page path = " + path, "DummyInputErrorPage2",
                path);

        replace();

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", ""));
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPage2", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceForLocaleLanguageCountry()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA"));
        assertEquals("input error page path = " + path, "DummyInputErrorPage2",
                path);

        replace();

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA"));
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPage2", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceForLocaleLanguageCountryVariant()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA", "AAA"));
        assertEquals("input error page path = " + path, "DummyInputErrorPage2",
                path);

        replace();

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA", "AAA"));
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPage2", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceKeyForLocale() throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                "dummyKey", (Locale) null);
        assertEquals("input error page path = " + path, "DummyInputErrorPage",
                path);

        replace();

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                "dummyKey", (Locale) null);
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPage", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceKeyForLocaleLanguage()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", ""));
        assertEquals("input error page path = " + path, "DummyInputErrorPage",
                path);

        replace();

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", ""));
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPage", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceKeyForLocaleLanguageCountry()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA"));
        assertEquals("input error page path = " + path, "DummyInputErrorPage",
                path);

        replace();

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA"));
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPage", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceKeyForLocaleLanguageCountryVariant()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA", "AAA"));
        assertEquals("input error page path = " + path, "DummyInputErrorPage",
                path);

        replace();

        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA", "AAA"));
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPage", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathExceptionForLocale() throws Exception {
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathExceptionForLocaleLanguage()
            throws Exception {
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathExceptionForLocaleLanguageCountry()
            throws Exception {
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathExceptionForLocaleLanguageCountryVariant()
            throws Exception {
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathForLocale() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getInputErrorPagePath((Locale) null);
        assertEquals("input error page path = " + path,
                "DummyInputErrorPageCommon", path);

        replace();

        path = this.handler.getInputErrorPagePath((Locale) null);
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathForLocaleLanguage() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getInputErrorPagePath(new Locale("aa", ""));
        assertEquals("input error page path = " + path,
                "DummyInputErrorPageCommon", path);

        replace();

        path = this.handler.getInputErrorPagePath(new Locale("aa", ""));
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathForLocaleLanguageCountry()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getInputErrorPagePath(new Locale("aa", "AA"));
        assertEquals("input error page path = " + path,
                "DummyInputErrorPageCommon", path);

        replace();

        path = this.handler.getInputErrorPagePath(new Locale("aa", "AA"));
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathForLocaleLanguageCountryVariant()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler
                .getInputErrorPagePath(new Locale("aa", "AA", "AAA"));
        assertEquals("input error page path = " + path,
                "DummyInputErrorPageCommon", path);

        replace();

        path = this.handler
                .getInputErrorPagePath(new Locale("aa", "AA", "AAA"));
        assertEquals("input error page path = " + path,
                "ReplacedDummyInputErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithKeyForLocale() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                "dummyKey", (Locale) null);
        assertEquals("next page path = " + path, "DummyNextPageWithKey", path);

        replace();

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                "dummyKey", (Locale) null);
        assertEquals("next page path = " + path,
                "ReplacedDummyNextPageWithKey", path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithKeyForLocaleLanguage() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", ""));
        assertEquals("next page path = " + path, "DummyNextPageWithKey", path);

        replace();

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", ""));
        assertEquals("next page path = " + path,
                "ReplacedDummyNextPageWithKey", path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithKeyForLocaleLanguageCountry()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA"));
        assertEquals("next page path = " + path, "DummyNextPageWithKey", path);

        replace();

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA"));
        assertEquals("next page path = " + path,
                "ReplacedDummyNextPageWithKey", path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithKeyForLocaleLanguageCountryVariant()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA", "AAA"));
        assertEquals("next page path = " + path, "DummyNextPageWithKey", path);

        replace();

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA", "AAA"));
        assertEquals("next page path = " + path,
                "ReplacedDummyNextPageWithKey", path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithoutKeyForLocale() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                (Locale) null);
        assertEquals("next page path = " + path, "DummyNextPageWithoutKey",
                path);

        replace();

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                (Locale) null);
        assertEquals("next page path = " + path,
                "ReplacedDummyNextPageWithoutKey", path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithoutKeyForLocaleLanguage() throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                new Locale("aa", ""));
        assertEquals("next page path = " + path, "DummyNextPageWithoutKey",
                path);

        replace();

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                new Locale("aa", ""));
        assertEquals("next page path = " + path,
                "ReplacedDummyNextPageWithoutKey", path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithoutKeyForLocaleLanguageCountry()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA"));
        assertEquals("next page path = " + path, "DummyNextPageWithoutKey",
                path);

        replace();

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA"));
        assertEquals("next page path = " + path,
                "ReplacedDummyNextPageWithoutKey", path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithoutKeyForLocaleLanguageCountryVariant()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA", "AAA"));
        assertEquals("next page path = " + path, "DummyNextPageWithoutKey",
                path);

        replace();

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA", "AAA"));
        assertEquals("next page path = " + path,
                "ReplacedDummyNextPageWithoutKey", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceControllerNameForLocale() throws Exception {
        handlerInit(true);
        String controller = null;

        controller = this.handler.getServiceControllerName("dummyApp",
                "dummyService", (Locale) null);
        assertEquals("controller = " + controller, "DummyController",
                controller);

        replace();

        controller = this.handler.getServiceControllerName("dummyApp",
                "dummyService", (Locale) null);
        assertEquals("controller = " + controller, "ReplacedDummyController",
                controller);
    }

    /**
     * @throws Exception
     */
    public void testServiceControllerNameForLocaleLanguage() throws Exception {
        handlerInit(true);
        String controller = null;

        controller = this.handler.getServiceControllerName("dummyApp",
                "dummyService", new Locale("aa", ""));
        assertEquals("controller = " + controller, "DummyController",
                controller);

        replace();

        controller = this.handler.getServiceControllerName("dummyApp",
                "dummyService", new Locale("aa", ""));
        assertEquals("controller = " + controller, "ReplacedDummyController",
                controller);
    }

    /**
     * @throws Exception
     */
    public void testServiceControllerNameForLocaleLanguageCountry()
            throws Exception {

        handlerInit(true);
        String controller = null;

        controller = this.handler.getServiceControllerName("dummyApp",
                "dummyService", new Locale("aa", "AA"));
        assertEquals("controller = " + controller, "DummyController",
                controller);

        replace();

        controller = this.handler.getServiceControllerName("dummyApp",
                "dummyService", new Locale("aa", "AA"));
        assertEquals("controller = " + controller, "ReplacedDummyController",
                controller);
    }

    /**
     * @throws Exception
     */
    public void testServiceControllerNameForLocaleLanguageCountryVariant()
            throws Exception {

        handlerInit(true);
        String controller = null;

        controller = this.handler.getServiceControllerName("dummyApp",
                "dummyService", new Locale("aa", "AA", "AAA"));
        assertEquals("controller = " + controller, "DummyController",
                controller);

        replace();

        controller = this.handler.getServiceControllerName("dummyApp",
                "dummyService", new Locale("aa", "AA", "AAA"));
        assertEquals("controller = " + controller, "ReplacedDummyController",
                controller);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppForLocale() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath("dummyApp", (Locale) null);
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPage3", path);

        replace();

        path = this.handler.getServiceErrorPagePath("dummyApp", (Locale) null);
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPage3", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppForLocaleLanguage() throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath("dummyApp", new Locale(
                "aa", ""));
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPage3", path);

        replace();

        path = this.handler.getServiceErrorPagePath("dummyApp", new Locale(
                "aa", ""));
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPage3", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppForLocaleLanguageCountry()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath("dummyApp", new Locale(
                "aa", "AA"));
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPage3", path);

        replace();

        path = this.handler.getServiceErrorPagePath("dummyApp", new Locale(
                "aa", "AA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPage3", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppForLocaleLanguageCountryVariant()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath("dummyApp", new Locale(
                "aa", "AA", "AAA"));
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPage3", path);

        replace();

        path = this.handler.getServiceErrorPagePath("dummyApp", new Locale(
                "aa", "AA", "AAA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPage3", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceForLocale() throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                (Locale) null);
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPage2", path);

        replace();

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                (Locale) null);
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPage2", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceForLocaleLanguage()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", ""));
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPage2", path);

        replace();

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", ""));
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPage2", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceForLocaleLanguageCountry()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA"));
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPage2", path);

        replace();

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPage2", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceForLocaleLanguageCountryVariant()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA", "AAA"));
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPage2", path);

        replace();

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA", "AAA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPage2", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceKeyForLocale()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                "dummyKey", (Locale) null);
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPage", path);

        replace();

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                "dummyKey", (Locale) null);
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPage", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceKeyForLocaleLanguage()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", ""));
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPage", path);

        replace();

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", ""));
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPage", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceKeyForLocaleLanguageCountry()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA"));
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPage", path);

        replace();

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPage", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceKeyForLocaleLanguageCountryVariant()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA", "AAA"));
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPage", path);

        replace();

        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA", "AAA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPage", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathExceptionForLocale() throws Exception {
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathExceptionForLocaleLanguage()
            throws Exception {
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathExceptionForLocaleLanguageCountry()
            throws Exception {
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathExceptionForLocaleLanguageCountryVariant()
            throws Exception {
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathForLocale() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath((Locale) null);
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPageCommon", path);

        replace();

        path = this.handler.getServiceErrorPagePath((Locale) null);
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathForLocaleLanguage() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath(new Locale("aa", ""));
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPageCommon", path);

        replace();

        path = this.handler.getServiceErrorPagePath(new Locale("aa", ""));
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathForLocaleLanguageCountry()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath(new Locale("aa", "AA"));
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPageCommon", path);

        replace();

        path = this.handler.getServiceErrorPagePath(new Locale("aa", "AA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathForLocaleLanguageCountryVariant()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getServiceErrorPagePath(new Locale("aa", "AA",
                "AAA"));
        assertEquals("service error page path = " + path,
                "DummyServiceErrorPageCommon", path);

        replace();

        path = this.handler.getServiceErrorPagePath(new Locale("aa", "AA",
                "AAA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummyServiceErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppForLocale() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getSystemErrorPagePath("dummyApp", (Locale) null);
        assertEquals("system error page path = " + path,
                "DummySystemErrorPage3", path);

        replace();

        path = this.handler.getSystemErrorPagePath("dummyApp", (Locale) null);
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPage3", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppForLocaleLanguage() throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getSystemErrorPagePath("dummyApp", new Locale("aa",
                ""));
        assertEquals("system error page path = " + path,
                "DummySystemErrorPage3", path);

        replace();

        path = this.handler.getSystemErrorPagePath("dummyApp", new Locale("aa",
                ""));
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPage3", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppForLocaleLanguageCountry()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getSystemErrorPagePath("dummyApp", new Locale("aa",
                "AA"));
        assertEquals("system error page path = " + path,
                "DummySystemErrorPage3", path);

        replace();

        path = this.handler.getSystemErrorPagePath("dummyApp", new Locale("aa",
                "AA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPage3", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppForLocaleLanguageCountryVariant()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getSystemErrorPagePath("dummyApp", new Locale("aa",
                "AA", "AAA"));
        assertEquals("system error page path = " + path,
                "DummySystemErrorPage3", path);

        replace();

        path = this.handler.getSystemErrorPagePath("dummyApp", new Locale("aa",
                "AA", "AAA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPage3", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceForLocale() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                (Locale) null);
        assertEquals("system error page path = " + path,
                "DummySystemErrorPage2", path);

        replace();

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                (Locale) null);
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPage2", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceForLocaleLanguage()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", ""));
        assertEquals("system error page path = " + path,
                "DummySystemErrorPage2", path);

        replace();

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", ""));
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPage2", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceForLocaleLanguageCountry()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA"));
        assertEquals("system error page path = " + path,
                "DummySystemErrorPage2", path);

        replace();

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPage2", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceForLocaleLanguageCountryVariant()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA", "AAA"));
        assertEquals("system error page path = " + path,
                "DummySystemErrorPage2", path);

        replace();

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                new Locale("aa", "AA", "AAA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPage2", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceKeyForLocale()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                "dummyKey", (Locale) null);
        assertEquals("system error page path = " + path,
                "DummySystemErrorPage", path);

        replace();

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                "dummyKey", (Locale) null);
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPage", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceKeyForLocaleLanguage()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", ""));
        assertEquals("system error page path = " + path,
                "DummySystemErrorPage", path);

        replace();

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", ""));
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPage", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceKeyForLocaleLanguageCountry()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA"));
        assertEquals("system error page path = " + path,
                "DummySystemErrorPage", path);

        replace();

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPage", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceKeyForLocaleLanguageCountryVariant()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA", "AAA"));
        assertEquals("system error page path = " + path,
                "DummySystemErrorPage", path);

        replace();

        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                "dummyKey", new Locale("aa", "AA", "AAA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPage", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathExceptionForLocale() throws Exception {
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathExceptionForLocaleLanguage()
            throws Exception {
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathExceptionForLocaleLanguageCountry()
            throws Exception {
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathExceptionForLocaleLanguageCountryVariant()
            throws Exception {
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathForLocale() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getSystemErrorPagePath((Locale) null);
        assertEquals("system error page path = " + path,
                "DummySystemErrorPageCommon", path);

        replace();

        path = this.handler.getSystemErrorPagePath((Locale) null);
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathForLocaleLanguage() throws Exception {
        handlerInit(true);
        String path = null;

        path = this.handler.getSystemErrorPagePath(new Locale("aa", ""));
        assertEquals("system error page path = " + path,
                "DummySystemErrorPageCommon", path);

        replace();

        path = this.handler.getSystemErrorPagePath(new Locale("aa", ""));
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathForLocaleLanguageCountry()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler.getSystemErrorPagePath(new Locale("aa", "AA"));
        assertEquals("system error page path = " + path,
                "DummySystemErrorPageCommon", path);

        replace();

        path = this.handler.getSystemErrorPagePath(new Locale("aa", "AA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathForLocaleLanguageCountryVariant()
            throws Exception {

        handlerInit(true);
        String path = null;

        path = this.handler
                .getSystemErrorPagePath(new Locale("aa", "AA", "AAA"));
        assertEquals("system error page path = " + path,
                "DummySystemErrorPageCommon", path);

        replace();

        path = this.handler
                .getSystemErrorPagePath(new Locale("aa", "AA", "AAA"));
        assertEquals("service error page path = " + path,
                "ReplacedDummySystemErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testTransitionNameForLocale() throws Exception {
        handlerInit(true);
        String transition = null;

        transition = this.handler.getTransitionName("dummyApp", "dummyService",
                (Locale) null);
        assertEquals("transition = " + transition, "DummyTransition",
                transition);

        replace();

        transition = this.handler.getTransitionName("dummyApp", "dummyService",
                (Locale) null);
        assertEquals("transition = " + transition, "ReplacedDummyTransition",
                transition);
    }

    /**
     * @throws Exception
     */
    public void testTransitionNameForLocaleLanguage() throws Exception {
        handlerInit(true);
        String transition = null;

        transition = this.handler.getTransitionName("dummyApp", "dummyService",
                new Locale("aa", ""));
        assertEquals("transition = " + transition, "DummyTransition",
                transition);

        replace();

        transition = this.handler.getTransitionName("dummyApp", "dummyService",
                new Locale("aa", ""));
        assertEquals("transition = " + transition, "ReplacedDummyTransition",
                transition);
    }

    /**
     * @throws Exception
     */
    public void testTransitionNameForLocaleLanguageCountry() throws Exception {
        handlerInit(true);
        String transition = null;

        transition = this.handler.getTransitionName("dummyApp", "dummyService",
                new Locale("aa", "AA"));
        assertEquals("transition = " + transition, "DummyTransition",
                transition);

        replace();

        transition = this.handler.getTransitionName("dummyApp", "dummyService",
                new Locale("aa", "AA"));
        assertEquals("transition = " + transition, "ReplacedDummyTransition",
                transition);
    }

    /**
     * @throws Exception
     */
    public void testTransitionNameForLocaleLanguageCountryVariant()
            throws Exception {
        handlerInit(true);
        String transition = null;

        transition = this.handler.getTransitionName("dummyApp", "dummyService",
                new Locale("aa", "AA", "AAA"));
        assertEquals("transition = " + transition, "DummyTransition",
                transition);

        replace();

        transition = this.handler.getTransitionName("dummyApp", "dummyService",
                new Locale("aa", "AA", "AAA"));
        assertEquals("transition = " + transition, "ReplacedDummyTransition",
                transition);
    }
}