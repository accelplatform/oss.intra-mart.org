/*
 * DefaultServicePropertyHandlerTest.java
 *
 * Created on 2002/01/29, 14:54
 */

package org.intra_mart.framework.base.service;

import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * @author intra-mart
 * @version
 */
public class DefaultServicePropertyHandlerTest extends TestCase implements
        ServicePropertyHandlerTestIF {

    /** Creates new DefaultServicePropertyHandlerTest */
    public DefaultServicePropertyHandlerTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DefaultServicePropertyHandlerTest.class);
        suite.setName("DefaultServicePropertyHandler test");

        return suite;
    }

    protected ServicePropertyHandler handler;

    protected void setUp() throws Exception {
        this.handler = new DefaultServicePropertyHandler();
        this.handler.init(null);
    }

    protected void tearDown() throws Exception {
        this.handler = null;
    }

    public void testClientEncoding() throws Exception {
        String encode = this.handler.getClientEncoding();
        assertNotNull("encode is null", encode);
        assertEquals("encode = " + encode, "DummyEncode", encode);
    }

    public void testClientLocale() throws Exception {
        Locale locale = this.handler.getClientLocale();
        assertEquals("locale = " + locale, new Locale("du", "MM", "Y"), locale);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testSystemErrorPagePathAppServiceKey() throws Exception {

        String path = null;

        // パッケージなし

        // ApplicationImpl, Service, Key が一致する
        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummySystemErrorPage", path);

        // ApplicationImpl, Service が一致する
        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService",
                "dummyKey2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummySystemErrorPage2", path);

        // ApplicationImpl が一致する
        path = this.handler.getSystemErrorPagePath("dummyApp", "dummyService2",
                "dummyKey2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummySystemErrorPage3", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath("dummyApp2",
                "dummyService2", "dummyKey2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummySystemErrorPageCommon", path);

        // パッケージあり

        // ApplicationImpl, Service, Key が一致する
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.sample", "dummyService", "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.DummySystemErrorPage", path);

        // ApplicationImpl, Service が一致する
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.sample", "dummyService", "dummyKey2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.DummySystemErrorPage2", path);

        // ApplicationImpl が一致する
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.sample", "dummyService2", "dummyKey2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.DummySystemErrorPage3", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.sample2", "dummyService2", "dummyKey2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummySystemErrorPageCommon", path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testSystemErrorPagePathAppService() throws Exception {

        String url = null;

        // パッケージなし

        // ApplicationImpl, Service が一致する
        url = this.handler.getSystemErrorPagePath("dummyApp", "dummyService");
        assertNotNull("url is null", url);
        assertEquals("url = " + url, "DummySystemErrorPage2", url);

        // ApplicationImpl が一致する
        url = this.handler.getSystemErrorPagePath("dummyApp", "dummyService2");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummySystemErrorPage3"));

        // 一致しない
        url = this.handler.getSystemErrorPagePath("dummyApp2", "dummyService2");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummySystemErrorPageCommon"));

        // パッケージあり

        // ApplicationImpl, Service が一致する
        url = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.sample", "dummyService");
        assertNotNull("url is null", url);
        assertEquals("url = " + url, "sample.DummySystemErrorPage2", url);

        // ApplicationImpl が一致する
        url = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.sample", "dummyService2");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("sample.DummySystemErrorPage3"));

        // 一致しない
        url = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.sample2", "dummyService2");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummySystemErrorPageCommon"));
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testSystemErrorPagePathApp() throws Exception {

        String url = null;

        // パッケージなし

        // ApplicationImpl が一致する
        url = this.handler.getSystemErrorPagePath("dummyApp");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummySystemErrorPage3"));

        // 一致しない
        url = this.handler.getSystemErrorPagePath("dummyApp2");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummySystemErrorPageCommon"));

        // パッケージあり

        // ApplicationImpl が一致する
        url = this.handler
                .getSystemErrorPagePath("org.intra_mart.test.sample");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("sample.DummySystemErrorPage3"));

        // 一致しない
        url = this.handler
                .getSystemErrorPagePath("org.intra_mart.test.sample2");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummySystemErrorPageCommon"));
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testSystemErrorPagePath() throws Exception {

        String url = null;

        // 一致しない
        url = this.handler.getSystemErrorPagePath();
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummySystemErrorPageCommon"));
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testSystemErrorPagePathException() throws Exception {

        // パッケージなし
        try {
            String url = this.handler.getSystemErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey");
            fail("url = " + url);
        } catch (ServicePropertyException e) {
        }

        // パッケージあり
        try {
            String url = this.handler
                    .getSystemErrorPagePath("org.intra_mart.test.sample3",
                            "dummyService", "dummyKey");
            fail("url = " + url);
        } catch (ServicePropertyException e) {
        }
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testNextPagePathWithKey() throws Exception {

        String path = null;

        // パッケージなし

        path = this.handler.getNextPagePath("dummyApp", "dummyService",
                "dummyKey");
        assertNotNull("path = " + path, path);
        assertEquals("path = " + path, "DummyNextPageWithKey", path);

        // パッケージあり

        path = this.handler.getNextPagePath("org.intra_mart.test.sample",
                "dummyService", "dummyKey");
        assertNotNull("path = " + path, path);
        assertEquals("path = " + path, "sample.DummyNextPageWithKey", path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testTransitionName() throws Exception {

        String transition = null;

        // パッケージなし

        transition = this.handler.getTransitionName("dummyApp", "dummyService");
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition, "DummyTransition",
                transition);

        // パッケージあり

        transition = this.handler.getTransitionName(
                "org.intra_mart.test.sample", "dummyService");
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition, "sample.DummyTransition",
                transition);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testServiceControllerName() throws Exception {

        String controller = null;

        // パッケージなし
        controller = this.handler.getServiceControllerName("dummyApp",
                "dummyService");
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller, "DummyController",
                controller);

        // パッケージあり
        controller = this.handler.getServiceControllerName(
                "org.intra_mart.test.sample", "dummyService");
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller, "sample.DummyController",
                controller);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testInputErrorPagePathAppServiceKey() throws Exception {

        String path = null;

        // パッケージなし

        // ApplicationImpl, Service, Key が一致する
        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPage", path);

        // ApplicationImpl, Service が一致する
        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService",
                "dummyKey2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPage2", path);

        // ApplicationImpl が一致する
        path = this.handler.getInputErrorPagePath("dummyApp", "dummyService2",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPage3", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("dummyApp2", "dummyService",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPageCommon", path);

        // パッケージあり

        // ApplicationImpl, Service, Key が一致する
        path = this.handler.getInputErrorPagePath(
                "org.intra_mart.test.sample", "dummyService", "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.DummyInputErrorPage", path);

        // ApplicationImpl, Service が一致する
        path = this.handler.getInputErrorPagePath(
                "org.intra_mart.test.sample", "dummyService", "dummyKey2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.DummyInputErrorPage2", path);

        // ApplicationImpl が一致する
        path = this.handler.getInputErrorPagePath(
                "org.intra_mart.test.sample", "dummyService2", "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.DummyInputErrorPage3", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath(
                "org.intra_mart.test.sample2", "dummyService", "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPageCommon", path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testInputErrorPagePathAppService() throws Exception {

        String url = null;

        // パッケージなし

        // ApplicationImpl, Service が一致する
        url = this.handler.getInputErrorPagePath("dummyApp", "dummyService");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyInputErrorPage2"));

        // ApplicationImpl が一致する
        url = this.handler.getInputErrorPagePath("dummyApp", "dummyService2");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyInputErrorPage3"));

        // 一致しない
        url = this.handler.getInputErrorPagePath("dummyApp2", "dummyService");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyInputErrorPageCommon"));

        // パッケージあり

        // ApplicationImpl, Service が一致する
        url = this.handler.getInputErrorPagePath(
                "org.intra_mart.test.sample", "dummyService");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("sample.DummyInputErrorPage2"));

        // ApplicationImpl が一致する
        url = this.handler.getInputErrorPagePath(
                "org.intra_mart.test.sample", "dummyService2");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("sample.DummyInputErrorPage3"));

        // 一致しない
        url = this.handler.getInputErrorPagePath(
                "org.intra_mart.test.sample2", "dummyService");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyInputErrorPageCommon"));
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testInputErrorPagePathApp() throws Exception {

        String url = null;

        // パッケージなし

        // ApplicationImpl が一致する
        url = this.handler.getInputErrorPagePath("dummyApp");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyInputErrorPage3"));

        // 一致しない
        url = this.handler.getInputErrorPagePath("dummyApp2");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyInputErrorPageCommon"));

        // パッケージあり

        // ApplicationImpl が一致する
        url = this.handler
                .getInputErrorPagePath("org.intra_mart.test.sample");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("sample.DummyInputErrorPage3"));

        // 一致しない
        url = this.handler
                .getInputErrorPagePath("org.intra_mart.test.sample2");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyInputErrorPageCommon"));
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testInputErrorPagePath() throws Exception {

        String path = null;

        // 一致しない
        path = this.handler.getInputErrorPagePath();
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPageCommon", path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testInputErrorPagePathException() throws Exception {

        String url = null;

        // パッケージなし
        try {
            url = this.handler.getInputErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey");
            fail("url = " + url);
        } catch (ServicePropertyException e) {
        }

        // パッケージあり
        try {
            url = this.handler
                    .getInputErrorPagePath("org.intra_mart.test.sample3",
                            "dummyService", "dummyKey");
            fail("url = " + url);
        } catch (ServicePropertyException e) {
        }
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testServiceErrorPagePathAppServiceKey() throws Exception {

        String path = null;

        // パッケージなし

        // ApplicationImpl, Service, Key が一致する
        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPage", path);

        // ApplicationImpl, Service が一致する
        path = this.handler.getServiceErrorPagePath("dummyApp", "dummyService",
                "dummyKey2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPage2", path);

        // ApplicationImpl が一致する
        path = this.handler.getServiceErrorPagePath("dummyApp",
                "dummyService2", "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPage3", path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath("dummyApp2",
                "dummyService", "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPageCommon", path);

        // パッケージあり

        // ApplicationImpl, Service, Key が一致する
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.sample", "dummyService", "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.DummyServiceErrorPage", path);

        // ApplicationImpl, Service が一致する
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.sample", "dummyService", "dummyKey2");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.DummyServiceErrorPage2", path);

        // ApplicationImpl が一致する
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.sample", "dummyService2", "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.DummyServiceErrorPage3", path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.sample2", "dummyService", "dummyKey");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPageCommon", path);
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testServiceErrorPagePathAppService() throws Exception {

        String url = null;

        // パッケージなし

        // ApplicationImpl, Service が一致する
        url = this.handler.getServiceErrorPagePath("dummyApp", "dummyService");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyServiceErrorPage2"));

        // ApplicationImpl が一致する
        url = this.handler.getServiceErrorPagePath("dummyApp", "dummyService2");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyServiceErrorPage3"));

        // 一致しない
        url = this.handler.getServiceErrorPagePath("dummyApp2", "dummyService");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyServiceErrorPageCommon"));

        // パッケージあり

        // ApplicationImpl, Service が一致する
        url = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.sample", "dummyService");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("sample.DummyServiceErrorPage2"));

        // ApplicationImpl が一致する
        url = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.sample", "dummyService2");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("sample.DummyServiceErrorPage3"));

        // 一致しない
        url = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.sample2", "dummyService");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyServiceErrorPageCommon"));
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testServiceErrorPagePathApp() throws Exception {

        String url = null;

        // パッケージなし

        // ApplicationImpl が一致する
        url = this.handler.getServiceErrorPagePath("dummyApp");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyServiceErrorPage3"));

        // 一致しない
        url = this.handler.getServiceErrorPagePath("dummyApp2");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyServiceErrorPageCommon"));

        // パッケージあり

        // ApplicationImpl が一致する
        url = this.handler
                .getServiceErrorPagePath("org.intra_mart.test.sample");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("sample.DummyServiceErrorPage3"));

        // 一致しない
        url = this.handler
                .getServiceErrorPagePath("org.intra_mart.test.sample2");
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyServiceErrorPageCommon"));
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testServiceErrorPagePath() throws Exception {

        String url = null;

        // 一致しない
        url = this.handler.getServiceErrorPagePath();
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyServiceErrorPageCommon"));
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testServiceErrorPagePathException() throws Exception {

        String url = null;

        // パッケージなし
        try {
            url = this.handler.getServiceErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey");
            fail("url = " + url);
        } catch (ServicePropertyException e) {
        }

        // パッケージあり
        try {
            url = this.handler
                    .getServiceErrorPagePath("org.intra_mart.test.sample3",
                            "dummyService", "dummyKey");
            fail("url = " + url);
        } catch (ServicePropertyException e) {
        }
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testNextPagePathWithoutKey() throws Exception {

        String path = null;

        // パッケージなし

        path = this.handler.getNextPagePath("dummyApp", "dummyService");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyNextPageWithoutKey", path);

        // パッケージあり

        path = this.handler.getNextPagePath("org.intra_mart.test.sample",
                "dummyService");
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.DummyNextPageWithoutKey", path);
    }

    public void testServiceServletPath() throws Exception {

        String url = null;
        url = this.handler.getServiceServletPath();
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyServletURL"));
    }

    /**
     * 
     * @throws Exception
     * @deprecated
     */
    public void testContextPath() throws Exception {

        String url = null;

        url = this.handler.getContextPath();
        assertTrue("url is null", url != null);
        assertTrue(url, url.equals("DummyContext"));
    }

    public void testEncodeAttributeName() throws Exception {
        String encode = this.handler.getEncodingAttributeName();
        assertEquals("encode attribute name = " + encode,
                "DummyEncodeAttribute1", encode);
    }

    public void testLocaleAttributeName() throws Exception {
        String locale = this.handler.getLocaleAttributeName();
        assertEquals("locale attribute name = " + locale,
                "DummyLocaleAttribute1", locale);
    }

    public void testApplicationParamName() throws Exception {
        String applicationParam = this.handler.getApplicationParamName();
        assertEquals("application param name = " + applicationParam,
                "DummyApplicationParam1", applicationParam);
    }

    public void testServiceParamName() throws Exception {
        String serviceParam = this.handler.getServiceParamName();
        assertEquals("service param name = " + serviceParam,
                "DummyServiceParam1", serviceParam);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppForLocale() throws Exception {

        String path = null;

        // パッケージなし

        // 一致しない
        path = this.handler.getInputErrorPagePath("i18n", new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "input.error.page.path(--)", path);

        // パッケージあり

        // 一致しない
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.input.error.page.path(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppForLocaleLanguage() throws Exception {

        String path = null;

        // パッケージなし

        // 言語が一致
        path = this.handler.getInputErrorPagePath("i18n", new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "input.error.page.path(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("i18n", new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "input.error.page.path(--)", path);

        // パッケージあり

        // 言語が一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.input.error.page.path(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.input.error.page.path(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国が一致
        path = this.handler.getInputErrorPagePath("i18n",
                new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "input.error.page.path(aa_AA)", path);

        // 言語が一致
        path = this.handler.getInputErrorPagePath("i18n",
                new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "input.error.page.path(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("i18n",
                new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "input.error.page.path(--)", path);

        // パッケージあり

        // 言語・国が一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.input.error.page.path(aa_AA)",
                path);

        // 言語が一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.input.error.page.path(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.input.error.page.path(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        path = this.handler.getInputErrorPagePath("i18n", new Locale("aa",
                "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "input.error.page.path(aa_AA_AAA)", path);

        // 言語・国が一致
        path = this.handler.getInputErrorPagePath("i18n", new Locale("aa",
                "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "input.error.page.path(aa_AA)", path);

        // 言語が一致
        path = this.handler.getInputErrorPagePath("i18n", new Locale("aa",
                "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "input.error.page.path(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("i18n", new Locale("bb",
                "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "input.error.page.path(--)", path);

        // パッケージあり

        // 言語・国・バリアントが一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path(aa_AA_AAA)", path);

        // 言語・国が一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.input.error.page.path(aa_AA)",
                path);

        // 言語が一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.input.error.page.path(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.input.error.page.path(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceForLocale() throws Exception {

        String path = null;

        // パッケージなし

        // 一致しない
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService(--)", path);

        // パッケージあり

        // 一致しない
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceForLocaleLanguage()
            throws Exception {

        String path = null;

        // パッケージあり

        // 言語が一致
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService(--)", path);

        // パッケージなし

        // 言語が一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国が一致
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService(aa_AA)", path);

        // 言語が一致
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService(--)", path);

        // パッケージあり

        // 言語・国が一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService(aa_AA)", path);

        // 言語が一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService(aa_AA_AAA)", path);

        // 言語・国が一致
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService(aa_AA)", path);

        // 言語が一致
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService(--)", path);

        // パッケージあり

        // 言語・国・バリアントが一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService(aa_AA_AAA)", path);

        // 言語・国が一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService(aa_AA)", path);

        // 言語が一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceKeyForLocale() throws Exception {

        String path = null;

        // パッケージなし

        // 一致しない
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService.dummyKey(--)", path);

        // パッケージなし

        // 一致しない
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService.dummyKey(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceKeyForLocaleLanguage()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語が一致
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService.dummyKey(--)", path);

        // パッケージあり

        // 言語が一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService.dummyKey(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceKeyForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国が一致
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService.dummyKey(aa_AA)", path);

        // 言語が一致
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService.dummyKey(--)", path);

        // パッケージあり

        // 言語・国が一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService.dummyKey(aa_AA)",
                path);

        // 言語が一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService.dummyKey(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathAppServiceKeyForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService.dummyKey(aa_AA_AAA)", path);

        // 言語・国が一致
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService.dummyKey(aa_AA)", path);

        // 言語が一致
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path.dummyService.dummyKey(--)", path);

        // パッケージあり

        // 言語・国・バリアントが一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals(
                "path = " + path,
                "sample.input.error.page.path.dummyService.dummyKey(aa_AA_AAA)",
                path);

        // 言語・国が一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService.dummyKey(aa_AA)",
                path);

        // 言語が一致
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getInputErrorPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.input.error.page.path.dummyService.dummyKey(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathExceptionForLocale() throws Exception {

        String path = null;

        // パッケージなし

        // 一致しない
        try {
            path = this.handler.getInputErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // パッケージあり

        // 一致しない
        try {
            path = this.handler.getInputErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathExceptionForLocaleLanguage()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語が一致
        try {
            path = this.handler.getInputErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getInputErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("bb", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // パッケージあり

        // 言語が一致
        try {
            path = this.handler.getInputErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getInputErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("bb", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathExceptionForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国が一致
        try {
            path = this.handler.getInputErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", "AA"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語が一致
        try {
            path = this.handler.getInputErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", "BB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getInputErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("bb", "BB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // パッケージあり

        // 言語・国が一致
        try {
            path = this.handler.getInputErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", "AA"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語が一致
        try {
            path = this.handler.getInputErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", "BB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getInputErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("bb", "BB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathExceptionForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        try {
            path = this.handler.getInputErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", "AA", "AAA"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語・国が一致
        try {
            path = this.handler.getInputErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", "AA", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語が一致
        try {
            path = this.handler.getInputErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", "BB", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getInputErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("bb", "BB", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // パッケージあり

        // 言語・国・バリアントが一致
        try {
            path = this.handler.getInputErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", "AA", "AAA"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語・国が一致
        try {
            path = this.handler.getInputErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", "AA", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語が一致
        try {
            path = this.handler.getInputErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", "BB", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getInputErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("bb", "BB", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathForLocale() throws Exception {

        String path = null;

        // 一致しない
        path = this.handler.getInputErrorPagePath(new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathForLocaleLanguage() throws Exception {

        String path = null;

        // 言語が一致
        path = this.handler.getInputErrorPagePath(new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "input.error.page.path(aa)(common)",
                path);

        // 一致しない
        path = this.handler.getInputErrorPagePath(new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // 言語・国が一致
        path = this.handler.getInputErrorPagePath(new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "input.error.page.path(aa_AA)(common)",
                path);

        // 言語が一致
        path = this.handler.getInputErrorPagePath(new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "input.error.page.path(aa)(common)",
                path);

        // 一致しない
        path = this.handler.getInputErrorPagePath(new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testInputErrorPagePathForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // 言語・国・バリアントが一致
        path = this.handler
                .getInputErrorPagePath(new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "input.error.page.path(aa_AA_AAA)(common)", path);

        // 言語・国が一致
        path = this.handler
                .getInputErrorPagePath(new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "input.error.page.path(aa_AA)(common)",
                path);

        // 言語が一致
        path = this.handler
                .getInputErrorPagePath(new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "input.error.page.path(aa)(common)",
                path);

        // 一致しない
        path = this.handler
                .getInputErrorPagePath(new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyInputErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithKeyForLocale() throws Exception {

        String path = null;

        // パッケージなし

        // 一致しない
        path = this.handler.getNextPagePath("i18n", "dummyService", "dummyKey",
                new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "nextpage.path.dummyService.dummyKey(--)", path);

        // パッケージあり

        // 一致しない
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.nextpage.path.dummyService.dummyKey(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithKeyForLocaleLanguage() throws Exception {

        String path = null;

        // パッケージなし

        // 言語が一致
        path = this.handler.getNextPagePath("i18n", "dummyService", "dummyKey",
                new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "nextpage.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getNextPagePath("i18n", "dummyService", "dummyKey",
                new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "nextpage.path.dummyService.dummyKey(--)", path);

        // パッケージあり

        // 言語が一致
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.nextpage.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.nextpage.path.dummyService.dummyKey(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithKeyForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国が一致
        path = this.handler.getNextPagePath("i18n", "dummyService", "dummyKey",
                new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "nextpage.path.dummyService.dummyKey(aa_AA)", path);

        // 言語が一致
        path = this.handler.getNextPagePath("i18n", "dummyService", "dummyKey",
                new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "nextpage.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getNextPagePath("i18n", "dummyService", "dummyKey",
                new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "nextpage.path.dummyService.dummyKey(--)", path);

        // パッケージあり

        // 言語・国が一致
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.nextpage.path.dummyService.dummyKey(aa_AA)", path);

        // 言語が一致
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.nextpage.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.nextpage.path.dummyService.dummyKey(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithKeyForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        path = this.handler.getNextPagePath("i18n", "dummyService", "dummyKey",
                new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "nextpage.path.dummyService.dummyKey(aa_AA_AAA)", path);

        // 言語・国が一致
        path = this.handler.getNextPagePath("i18n", "dummyService", "dummyKey",
                new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "nextpage.path.dummyService.dummyKey(aa_AA)", path);

        // 言語が一致
        path = this.handler.getNextPagePath("i18n", "dummyService", "dummyKey",
                new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "nextpage.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getNextPagePath("i18n", "dummyService", "dummyKey",
                new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "nextpage.path.dummyService.dummyKey(--)", path);

        // パッケージあり

        // 言語・国・バリアントが一致
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.nextpage.path.dummyService.dummyKey(aa_AA_AAA)", path);

        // 言語・国が一致
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.nextpage.path.dummyService.dummyKey(aa_AA)", path);

        // 言語が一致
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.nextpage.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", "dummyKey", new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.nextpage.path.dummyService.dummyKey(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithoutKeyForLocale() throws Exception {

        String path = null;

        // パッケージなし

        // 一致しない
        path = this.handler.getNextPagePath("i18n", "dummyService", new Locale(
                "", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "nextpage.path.dummyService(--)", path);

        // パッケージあり

        // 一致しない
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.nextpage.path.dummyService(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithoutKeyForLocaleLanguage() throws Exception {

        String path = null;

        // パッケージなし

        // 言語が一致
        path = this.handler.getNextPagePath("i18n", "dummyService", new Locale(
                "aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "nextpage.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getNextPagePath("i18n", "dummyService", new Locale(
                "bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "nextpage.path.dummyService(--)", path);

        // パッケージあり

        // 言語が一致
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.nextpage.path.dummyService(aa)",
                path);

        // 一致しない
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.nextpage.path.dummyService(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithoutKeyForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国が一致
        path = this.handler.getNextPagePath("i18n", "dummyService", new Locale(
                "aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "nextpage.path.dummyService(aa_AA)",
                path);

        // 言語が一致
        path = this.handler.getNextPagePath("i18n", "dummyService", new Locale(
                "aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "nextpage.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getNextPagePath("i18n", "dummyService", new Locale(
                "bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "nextpage.path.dummyService(--)", path);

        // パッケージあり

        // 言語・国が一致
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.nextpage.path.dummyService(aa_AA)", path);

        // 言語が一致
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.nextpage.path.dummyService(aa)",
                path);

        // 一致しない
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.nextpage.path.dummyService(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testNextPagePathWithoutKeyForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        path = this.handler.getNextPagePath("i18n", "dummyService", new Locale(
                "aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "nextpage.path.dummyService(aa_AA_AAA)",
                path);

        // 言語・国が一致
        path = this.handler.getNextPagePath("i18n", "dummyService", new Locale(
                "aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "nextpage.path.dummyService(aa_AA)",
                path);

        // 言語が一致
        path = this.handler.getNextPagePath("i18n", "dummyService", new Locale(
                "aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "nextpage.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getNextPagePath("i18n", "dummyService", new Locale(
                "bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "nextpage.path.dummyService(--)", path);

        // パッケージあり

        // 言語・国・バリアントが一致
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.nextpage.path.dummyService(aa_AA_AAA)", path);

        // 言語・国が一致
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.nextpage.path.dummyService(aa_AA)", path);

        // 言語が一致
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.nextpage.path.dummyService(aa)",
                path);

        // 一致しない
        path = this.handler.getNextPagePath("org.intra_mart.test.i18n",
                "dummyService", new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.nextpage.path.dummyService(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testServiceControllerNameForLocale() throws Exception {

        String controller = null;

        // パッケージなし

        // 一致しない
        controller = this.handler.getServiceControllerName("i18n",
                "dummyService", new Locale("", ""));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "controller.class.dummyService(--)", controller);

        // パッケージあり

        // 一致しない
        controller = this.handler.getServiceControllerName(
                "org.intra_mart.test.i18n", "dummyService",
                new Locale("", ""));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "sample.controller.class.dummyService(--)", controller);
    }

    /**
     * @throws Exception
     */
    public void testServiceControllerNameForLocaleLanguage() throws Exception {

        String controller = null;

        // パッケージなし

        // 言語が一致
        controller = this.handler.getServiceControllerName("i18n",
                "dummyService", new Locale("aa", ""));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "controller.class.dummyService(aa)", controller);

        // 一致しない
        controller = this.handler.getServiceControllerName("i18n",
                "dummyService", new Locale("bb", ""));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "controller.class.dummyService(--)", controller);

        // パッケージあり

        // 言語が一致
        controller = this.handler.getServiceControllerName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        ""));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "sample.controller.class.dummyService(aa)", controller);

        // 一致しない
        controller = this.handler.getServiceControllerName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("bb",
                        ""));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "sample.controller.class.dummyService(--)", controller);
    }

    /**
     * @throws Exception
     */
    public void testServiceControllerNameForLocaleLanguageCountry()
            throws Exception {

        String controller = null;

        // パッケージなし

        // 言語・国が一致
        controller = this.handler.getServiceControllerName("i18n",
                "dummyService", new Locale("aa", "AA"));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "controller.class.dummyService(aa_AA)", controller);

        // 言語が一致
        controller = this.handler.getServiceControllerName("i18n",
                "dummyService", new Locale("aa", "BB"));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "controller.class.dummyService(aa)", controller);

        // 一致しない
        controller = this.handler.getServiceControllerName("i18n",
                "dummyService", new Locale("bb", "BB"));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "controller.class.dummyService(--)", controller);

        // パッケージあり

        // 言語・国が一致
        controller = this.handler.getServiceControllerName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "AA"));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "sample.controller.class.dummyService(aa_AA)", controller);

        // 言語が一致
        controller = this.handler.getServiceControllerName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "BB"));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "sample.controller.class.dummyService(aa)", controller);

        // 一致しない
        controller = this.handler.getServiceControllerName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("bb",
                        "BB"));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "sample.controller.class.dummyService(--)", controller);
    }

    /**
     * @throws Exception
     */
    public void testServiceControllerNameForLocaleLanguageCountryVariant()
            throws Exception {

        String controller = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        controller = this.handler.getServiceControllerName("i18n",
                "dummyService", new Locale("aa", "AA", "AAA"));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "controller.class.dummyService(aa_AA_AAA)", controller);

        // 言語・国が一致
        controller = this.handler.getServiceControllerName("i18n",
                "dummyService", new Locale("aa", "AA", "BBB"));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "controller.class.dummyService(aa_AA)", controller);

        // 言語が一致
        controller = this.handler.getServiceControllerName("i18n",
                "dummyService", new Locale("aa", "BB", "BBB"));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "controller.class.dummyService(aa)", controller);

        // 一致しない
        controller = this.handler.getServiceControllerName("i18n",
                "dummyService", new Locale("bb", "BB", "BBB"));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "controller.class.dummyService(--)", controller);

        // パッケージあり

        // 言語・国・バリアントが一致
        controller = this.handler.getServiceControllerName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "AA", "AAA"));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "sample.controller.class.dummyService(aa_AA_AAA)", controller);

        // 言語・国が一致
        controller = this.handler.getServiceControllerName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "AA", "BBB"));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "sample.controller.class.dummyService(aa_AA)", controller);

        // 言語が一致
        controller = this.handler.getServiceControllerName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "BB", "BBB"));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "sample.controller.class.dummyService(aa)", controller);

        // 一致しない
        controller = this.handler.getServiceControllerName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("bb",
                        "BB", "BBB"));
        assertNotNull("controller is null", controller);
        assertEquals("controller = " + controller,
                "sample.controller.class.dummyService(--)", controller);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppForLocale() throws Exception {

        String path = null;

        // パッケージなし

        // 一致しない
        path = this.handler.getServiceErrorPagePath("i18n", new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "service.error.page.path(--)", path);

        // パッケージなし

        // 一致しない
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.service.error.page.path(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppForLocaleLanguage() throws Exception {

        String path = null;

        // パッケージなし

        // 言語が一致
        path = this.handler.getServiceErrorPagePath("i18n",
                new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "service.error.page.path(aa)", path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath("i18n",
                new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "service.error.page.path(--)", path);

        // パッケージあり

        // 言語が一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.service.error.page.path(aa)",
                path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.service.error.page.path(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国が一致
        path = this.handler.getServiceErrorPagePath("i18n", new Locale("aa",
                "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "service.error.page.path(aa_AA)", path);

        // 言語が一致
        path = this.handler.getServiceErrorPagePath("i18n", new Locale("aa",
                "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "service.error.page.path(aa)", path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath("i18n", new Locale("bb",
                "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "service.error.page.path(--)", path);

        // パッケージあり

        // 言語・国が一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.service.error.page.path(aa_AA)",
                path);

        // 言語が一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.service.error.page.path(aa)",
                path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.service.error.page.path(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        path = this.handler.getServiceErrorPagePath("i18n", new Locale("aa",
                "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "service.error.page.path(aa_AA_AAA)",
                path);

        // 言語・国が一致
        path = this.handler.getServiceErrorPagePath("i18n", new Locale("aa",
                "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "service.error.page.path(aa_AA)", path);

        // 言語が一致
        path = this.handler.getServiceErrorPagePath("i18n", new Locale("aa",
                "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "service.error.page.path(aa)", path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath("i18n", new Locale("bb",
                "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "service.error.page.path(--)", path);

        // パッケージあり

        // 言語・国・バリアントが一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path(aa_AA_AAA)", path);

        // 言語・国が一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.service.error.page.path(aa_AA)",
                path);

        // 言語が一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.service.error.page.path(aa)",
                path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.service.error.page.path(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceForLocale() throws Exception {

        String path = null;

        // パッケージなし

        // 一致しない
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService(--)", path);

        // パッケージあり

        // 一致しない
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService",
                new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceForLocaleLanguage()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語が一致
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService(--)", path);

        // パッケージあり

        // 言語が一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("bb",
                        ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国が一致
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService(aa_AA)", path);

        // 言語が一致
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService(--)", path);

        // パッケージあり

        // 言語・国が一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService(aa_AA)", path);

        // 言語が一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("bb",
                        "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService(aa_AA_AAA)", path);

        // 言語・国が一致
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService(aa_AA)", path);

        // 言語が一致
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService(--)", path);

        // パッケージあり

        // 言語・国・バリアントが一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService(aa_AA_AAA)", path);

        // 言語・国が一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService(aa_AA)", path);

        // 言語が一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("bb",
                        "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceKeyForLocale()
            throws Exception {

        String path = null;

        // パッケージなし

        // 一致しない
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService.dummyKey(--)", path);

        // パッケージあり

        // 一致しない
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService.dummyKey(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceKeyForLocaleLanguage()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語が一致
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService.dummyKey(--)", path);

        // パッケージあり

        // 言語が一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService.dummyKey(aa)",
                path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService.dummyKey(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceKeyForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国が一致
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService.dummyKey(aa_AA)", path);

        // 言語が一致
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService.dummyKey(--)", path);

        // パッケージあり

        // 言語・国が一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService.dummyKey(aa_AA)",
                path);

        // 言語が一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService.dummyKey(aa)",
                path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService.dummyKey(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathAppServiceKeyForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService.dummyKey(aa_AA_AAA)",
                path);

        // 言語・国が一致
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService.dummyKey(aa_AA)", path);

        // 言語が一致
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path.dummyService.dummyKey(--)", path);

        // パッケージあり

        // 言語・国・バリアントが一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals(
                "path = " + path,
                "sample.service.error.page.path.dummyService.dummyKey(aa_AA_AAA)",
                path);

        // 言語・国が一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService.dummyKey(aa_AA)",
                path);

        // 言語が一致
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService.dummyKey(aa)",
                path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.service.error.page.path.dummyService.dummyKey(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathExceptionForLocale() throws Exception {

        String path = null;

        // パッケージなし

        // 一致しない
        try {
            path = this.handler.getServiceErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // パッケージあり

        // 一致しない
        try {
            path = this.handler.getServiceErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathExceptionForLocaleLanguage()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語が一致
        try {
            path = this.handler.getServiceErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getServiceErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("bb", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // パッケージあり

        // 言語が一致
        try {
            path = this.handler.getServiceErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getServiceErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("bb", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathExceptionForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国が一致
        try {
            path = this.handler.getServiceErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", "AA"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語が一致
        try {
            path = this.handler.getServiceErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", "BB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getServiceErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("bb", "BB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // パッケージあり

        // 言語・国が一致
        try {
            path = this.handler.getServiceErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", "AA"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語が一致
        try {
            path = this.handler.getServiceErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", "BB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getServiceErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("bb", "BB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathExceptionForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        try {
            path = this.handler.getServiceErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", "AA", "AAA"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語・国が一致
        try {
            path = this.handler.getServiceErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", "AA", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語が一致
        try {
            path = this.handler.getServiceErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", "BB", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getServiceErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("bb", "BB", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // パッケージあり

        // 言語・国・バリアントが一致
        try {
            path = this.handler.getServiceErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", "AA", "AAA"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語・国が一致
        try {
            path = this.handler.getServiceErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", "AA", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語が一致
        try {
            path = this.handler.getServiceErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", "BB", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getServiceErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("bb", "BB", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathForLocale() throws Exception {

        String path = null;

        // 一致しない
        path = this.handler.getServiceErrorPagePath(new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathForLocaleLanguage() throws Exception {

        String path = null;

        // 言語が一致
        path = this.handler.getServiceErrorPagePath(new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "service.error.page.path(aa)(common)",
                path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath(new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // 言語・国が一致
        path = this.handler.getServiceErrorPagePath(new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path(aa_AA)(common)", path);

        // 言語が一致
        path = this.handler.getServiceErrorPagePath(new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "service.error.page.path(aa)(common)",
                path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath(new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testServiceErrorPagePathForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // 言語・国・バリアントが一致
        path = this.handler.getServiceErrorPagePath(new Locale("aa", "AA",
                "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path(aa_AA_AAA)(common)", path);

        // 言語・国が一致
        path = this.handler.getServiceErrorPagePath(new Locale("aa", "AA",
                "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "service.error.page.path(aa_AA)(common)", path);

        // 言語が一致
        path = this.handler.getServiceErrorPagePath(new Locale("aa", "BB",
                "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "service.error.page.path(aa)(common)",
                path);

        // 一致しない
        path = this.handler.getServiceErrorPagePath(new Locale("bb", "BB",
                "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummyServiceErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppForLocale() throws Exception {

        String path = null;

        // パッケージなし

        // 一致しない
        path = this.handler.getSystemErrorPagePath("i18n", new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "system.error.page.path(--)", path);

        // パッケージあり

        // 一致しない
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.system.error.page.path(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppForLocaleLanguage() throws Exception {

        String path = null;

        // パッケージなし

        // 言語が一致
        path = this.handler
                .getSystemErrorPagePath("i18n", new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "system.error.page.path(aa)", path);

        // 一致しない
        path = this.handler
                .getSystemErrorPagePath("i18n", new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "system.error.page.path(--)", path);

        // パッケージあり

        // 言語が一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.system.error.page.path(aa)",
                path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.system.error.page.path(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国が一致
        path = this.handler.getSystemErrorPagePath("i18n", new Locale("aa",
                "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "system.error.page.path(aa_AA)", path);

        // 言語が一致
        path = this.handler.getSystemErrorPagePath("i18n", new Locale("aa",
                "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "system.error.page.path(aa)", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath("i18n", new Locale("bb",
                "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "system.error.page.path(--)", path);

        // パッケージあり

        // 言語・国が一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.system.error.page.path(aa_AA)",
                path);

        // 言語が一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.system.error.page.path(aa)",
                path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.system.error.page.path(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        path = this.handler.getSystemErrorPagePath("i18n", new Locale("aa",
                "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "system.error.page.path(aa_AA_AAA)",
                path);

        // 言語・国が一致
        path = this.handler.getSystemErrorPagePath("i18n", new Locale("aa",
                "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "system.error.page.path(aa_AA)", path);

        // 言語が一致
        path = this.handler.getSystemErrorPagePath("i18n", new Locale("aa",
                "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "system.error.page.path(aa)", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath("i18n", new Locale("bb",
                "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "system.error.page.path(--)", path);

        // パッケージあり

        // 言語・国・バリアントが一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path(aa_AA_AAA)", path);

        // 言語・国が一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.system.error.page.path(aa_AA)",
                path);

        // 言語が一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.system.error.page.path(aa)",
                path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "sample.system.error.page.path(--)",
                path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceForLocale() throws Exception {

        String path = null;

        // パッケージなし

        // 一致しない
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService(--)", path);

        // パッケージあり

        // 一致しない
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService",
                new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceForLocaleLanguage()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語が一致
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService(--)", path);

        // パッケージあり

        // 言語が一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("bb",
                        ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国が一致
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService(aa_AA)", path);

        // 言語が一致
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService(--)", path);

        // パッケージあり

        // 言語・国が一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService(aa_AA)", path);

        // 言語が一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("bb",
                        "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService(aa_AA_AAA)", path);

        // 言語・国が一致
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService(aa_AA)", path);

        // 言語が一致
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService(--)", path);

        // パッケージあり

        // 言語・国・バリアントが一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService(aa_AA_AAA)", path);

        // 言語・国が一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService(aa_AA)", path);

        // 言語が一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService(aa)", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", new Locale("bb",
                        "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceKeyForLocale()
            throws Exception {

        String path = null;

        // パッケージなし

        // 一致しない
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService.dummyKey(--)", path);

        // パッケージあり

        // 一致しない
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService.dummyKey(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceKeyForLocaleLanguage()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語が一致
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService.dummyKey(--)", path);

        // パッケージあり

        // 言語が一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService.dummyKey(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceKeyForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国が一致
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService.dummyKey(aa_AA)", path);

        // 言語が一致
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService.dummyKey(--)", path);

        // パッケージあり

        // 言語・国が一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService.dummyKey(aa_AA)",
                path);

        // 言語が一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService.dummyKey(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathAppServiceKeyForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService.dummyKey(aa_AA_AAA)", path);

        // 言語・国が一致
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService.dummyKey(aa_AA)", path);

        // 言語が一致
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath("i18n", "dummyService",
                "dummyKey", new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path.dummyService.dummyKey(--)", path);

        // パッケージあり

        // 言語・国・バリアントが一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals(
                "path = " + path,
                "sample.system.error.page.path.dummyService.dummyKey(aa_AA_AAA)",
                path);

        // 言語・国が一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService.dummyKey(aa_AA)",
                path);

        // 言語が一致
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService.dummyKey(aa)", path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath(
                "org.intra_mart.test.i18n", "dummyService", "dummyKey",
                new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "sample.system.error.page.path.dummyService.dummyKey(--)", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathExceptionForLocale() throws Exception {

        String path = null;

        // パッケージなし

        // 一致しない
        try {
            path = this.handler.getSystemErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // パッケージあり

        // 一致しない
        try {
            path = this.handler.getSystemErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathExceptionForLocaleLanguage()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語が一致
        try {
            path = this.handler.getSystemErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getSystemErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("bb", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // パッケージあり

        // 言語が一致
        try {
            path = this.handler.getSystemErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getSystemErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("bb", ""));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathExceptionForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国が一致
        try {
            path = this.handler.getSystemErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", "AA"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語が一致
        try {
            path = this.handler.getSystemErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", "BB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getSystemErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("bb", "BB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // パッケージあり

        // 言語・国が一致
        try {
            path = this.handler.getSystemErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", "AA"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語が一致
        try {
            path = this.handler.getSystemErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", "BB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getSystemErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("bb", "BB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathExceptionForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        try {
            path = this.handler.getSystemErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", "AA", "AAA"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語・国が一致
        try {
            path = this.handler.getSystemErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", "AA", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語が一致
        try {
            path = this.handler.getSystemErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("aa", "BB", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getSystemErrorPagePath("dummyApp3",
                    "dummyService", "dummyKey", new Locale("bb", "BB", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // パッケージあり

        // 言語・国・バリアントが一致
        try {
            path = this.handler.getSystemErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", "AA", "AAA"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語・国が一致
        try {
            path = this.handler.getSystemErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", "AA", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 言語が一致
        try {
            path = this.handler.getSystemErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("aa", "BB", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }

        // 一致しない
        try {
            path = this.handler.getSystemErrorPagePath(
                    "org.intra_mart.test.dummyApp3", "dummyService",
                    "dummyKey", new Locale("bb", "BB", "BBB"));
            fail("path = " + path);
        } catch (ServicePropertyException e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathForLocale() throws Exception {

        String path = null;

        // 一致しない
        path = this.handler.getSystemErrorPagePath(new Locale("", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummySystemErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathForLocaleLanguage() throws Exception {

        String path = null;

        // 言語が一致
        path = this.handler.getSystemErrorPagePath(new Locale("aa", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "system.error.page.path(aa)(common)",
                path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath(new Locale("bb", ""));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummySystemErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathForLocaleLanguageCountry()
            throws Exception {

        String path = null;

        // 言語・国が一致
        path = this.handler.getSystemErrorPagePath(new Locale("aa", "AA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "system.error.page.path(aa_AA)(common)",
                path);

        // 言語が一致
        path = this.handler.getSystemErrorPagePath(new Locale("aa", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "system.error.page.path(aa)(common)",
                path);

        // 一致しない
        path = this.handler.getSystemErrorPagePath(new Locale("bb", "BB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummySystemErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testSystemErrorPagePathForLocaleLanguageCountryVariant()
            throws Exception {

        String path = null;

        // 言語・国・バリアントが一致
        path = this.handler
                .getSystemErrorPagePath(new Locale("aa", "AA", "AAA"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path,
                "system.error.page.path(aa_AA_AAA)(common)", path);

        // 言語・国が一致
        path = this.handler
                .getSystemErrorPagePath(new Locale("aa", "AA", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "system.error.page.path(aa_AA)(common)",
                path);

        // 言語が一致
        path = this.handler
                .getSystemErrorPagePath(new Locale("aa", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "system.error.page.path(aa)(common)",
                path);

        // 一致しない
        path = this.handler
                .getSystemErrorPagePath(new Locale("bb", "BB", "BBB"));
        assertNotNull("path is null", path);
        assertEquals("path = " + path, "DummySystemErrorPageCommon", path);
    }

    /**
     * @throws Exception
     */
    public void testTransitionNameForLocale() throws Exception {

        String transition = null;

        // パッケージなし

        // 一致しない
        transition = this.handler.getTransitionName("i18n", "dummyService",
                new Locale("", ""));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "transition.class.dummyService(--)", transition);

        // パッケージあり

        // 一致しない
        transition = this.handler.getTransitionName(
                "org.intra_mart.test.i18n", "dummyService",
                new Locale("", ""));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "sample.transition.class.dummyService(--)", transition);
    }

    /**
     * @throws Exception
     */
    public void testTransitionNameForLocaleLanguage() throws Exception {

        String transition = null;

        // パッケージなし

        // 言語が一致
        transition = this.handler.getTransitionName("i18n", "dummyService",
                new Locale("aa", ""));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "transition.class.dummyService(aa)", transition);

        // 一致しない
        transition = this.handler.getTransitionName("i18n", "dummyService",
                new Locale("bb", ""));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "transition.class.dummyService(--)", transition);

        // パッケージあり

        // 言語が一致
        transition = this.handler.getTransitionName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        ""));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "sample.transition.class.dummyService(aa)", transition);

        // 一致しない
        transition = this.handler.getTransitionName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("bb",
                        ""));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "sample.transition.class.dummyService(--)", transition);
    }

    /**
     * @throws Exception
     */
    public void testTransitionNameForLocaleLanguageCountry() throws Exception {

        String transition = null;

        // パッケージなし

        // 言語・国が一致
        transition = this.handler.getTransitionName("i18n", "dummyService",
                new Locale("aa", "AA"));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "transition.class.dummyService(aa_AA)", transition);

        // 言語が一致
        transition = this.handler.getTransitionName("i18n", "dummyService",
                new Locale("aa", "BB"));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "transition.class.dummyService(aa)", transition);

        // 一致しない
        transition = this.handler.getTransitionName("i18n", "dummyService",
                new Locale("bb", "BB"));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "transition.class.dummyService(--)", transition);

        // パッケージあり

        // 言語・国が一致
        transition = this.handler.getTransitionName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "AA"));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "sample.transition.class.dummyService(aa_AA)", transition);

        // 言語が一致
        transition = this.handler.getTransitionName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "BB"));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "sample.transition.class.dummyService(aa)", transition);

        // 一致しない
        transition = this.handler.getTransitionName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("bb",
                        "BB"));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "sample.transition.class.dummyService(--)", transition);
    }

    /**
     * @throws Exception
     */
    public void testTransitionNameForLocaleLanguageCountryVariant()
            throws Exception {

        String transition = null;

        // パッケージなし

        // 言語・国・バリアントが一致
        transition = this.handler.getTransitionName("i18n", "dummyService",
                new Locale("aa", "AA", "AAA"));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "transition.class.dummyService(aa_AA_AAA)", transition);

        // 言語・国が一致
        transition = this.handler.getTransitionName("i18n", "dummyService",
                new Locale("aa", "AA", "BBB"));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "transition.class.dummyService(aa_AA)", transition);

        // 言語が一致
        transition = this.handler.getTransitionName("i18n", "dummyService",
                new Locale("aa", "BB", "BBB"));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "transition.class.dummyService(aa)", transition);

        // 一致しない
        transition = this.handler.getTransitionName("i18n", "dummyService",
                new Locale("bb", "BB", "BBB"));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "transition.class.dummyService(--)", transition);

        // パッケージあり

        // 言語・国・バリアントが一致
        transition = this.handler.getTransitionName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "AA", "AAA"));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "sample.transition.class.dummyService(aa_AA_AAA)", transition);

        // 言語・国が一致
        transition = this.handler.getTransitionName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "AA", "BBB"));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "sample.transition.class.dummyService(aa_AA)", transition);

        // 言語が一致
        transition = this.handler.getTransitionName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("aa",
                        "BB", "BBB"));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "sample.transition.class.dummyService(aa)", transition);

        // 一致しない
        transition = this.handler.getTransitionName(
                "org.intra_mart.test.i18n", "dummyService", new Locale("bb",
                        "BB", "BBB"));
        assertNotNull("transition is null", transition);
        assertEquals("transition = " + transition,
                "sample.transition.class.dummyService(--)", transition);
    }
}