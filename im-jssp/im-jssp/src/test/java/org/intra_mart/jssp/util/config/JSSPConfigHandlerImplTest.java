package org.intra_mart.jssp.util.config;

import java.util.Locale;

import junit.framework.TestCase;

import org.mozilla.javascript.Context;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * JSSPConfigHandlerImplのノンパラメータコンストラクタが参照するコンフィグファイルは、
 * 「/org/intra_mart/jssp/util/config/jssp-config.xml」です。
 * 上記ファイルには、 必要最低限の設定がされているコンフィグファイルです。
 * 本テストケースでは、上記ファイルの設定値を利用してテストを行います。
 * <br/>
 * <br/>
 * 「/org/intra_mart/jssp/util/config/jssp-config-template.xml」は、
 * 設定可能な項目がすべて設定されています。
 * 本テストケースでは、上記ファイルの設定値を利用してテストを行います。
 */
public class JSSPConfigHandlerImplTest extends TestCase {

	private String allConfigFileName = "/org/intra_mart/jssp/util/config/jssp-config-template.xml";

		
	public void testGetServerCharacterEncoding_デフォルト値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();
		assertEquals(System.getProperty("file.encoding"), config.getServerCharacterEncoding());
	}

	public void testGetServerCharacterEncoding_設定値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);
		assertEquals("TestEncodingName", config.getServerCharacterEncoding());
	}

	
	public void testGetOutputDirectory4ComiledScript_デフォルト値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();
		assertEquals("WEB-INF/work/jssp/_functioncontainer", config.getOutputDirectory4ComiledScript());
	}

	public void testGetOutputDirectory4ComiledScript_設定値() throws Exception {	
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);
		assertEquals("test/work/jssp/testGetOutputDirectory4ComiledScript", config.getOutputDirectory4ComiledScript());
	}

	
	public void testGetOutputDirectory4ComiledView_デフォルト値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();
		assertEquals("WEB-INF/work/jssp/_presentationpage", config.getOutputDirectory4ComiledView());
	}

	public void testGetOutputDirectory4ComiledView_設定値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);
		assertEquals("test/work/jssp/testGetOutputDirectory4ComiledView", config.getOutputDirectory4ComiledView());
	}
	
	
	public void testGetGeneralSourceDirectories_デフォルト値() throws Exception {
		// ユーザのホームディレクトリを仮のホームディレクトリとする。
		String homePath = System.getProperty("user.home");
		HomeDirectory.definePath(homePath);

		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();
		
		String[] dirs = config.getGeneralSourceDirectories();
		
		assertEquals(1, dirs.length);
		assertEquals(homePath, dirs[0]);
	}

	public void testGetGeneralSourceDirectories_設定値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);		
		String[] dirs = config.getGeneralSourceDirectories();
		
		assertEquals(3, dirs.length);
		assertEquals("pages/src", dirs[0]);
		assertEquals("pages/product/src", dirs[1]);
		assertEquals("pages/platform/src", dirs[2]);
	}

	public void testGetSourceDirectories_デフォルト値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();		
		String[] dirs = config.getSourceDirectories(new Locale("ja_JP"));
		
		assertEquals(0, dirs.length);

	}

	public void testGetSourceDirectories_設定値() throws Exception {
		String[] dirs;
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);
		
		dirs = config.getSourceDirectories(new Locale("ja","JP"));
		assertEquals(6, dirs.length);
		assertEquals("pages/i18n/ja_JP", dirs[0]);
		assertEquals("pages/product/i18n/ja_JP", dirs[1]);
		assertEquals("pages/platform/i18n/ja_JP", dirs[2]);
		assertEquals("pages/i18n/ja", dirs[3]);
		assertEquals("pages/product/i18n/ja", dirs[4]);
		assertEquals("pages/platform/i18n/ja", dirs[5]);

		dirs = config.getSourceDirectories(new Locale("ja"));
		assertEquals(3, dirs.length);
		assertEquals("pages/i18n/ja", dirs[0]);
		assertEquals("pages/product/i18n/ja", dirs[1]);
		assertEquals("pages/platform/i18n/ja", dirs[2]);

		dirs = config.getSourceDirectories(new Locale("en", "US"));
		assertEquals(4, dirs.length);
		assertEquals("pages/product/i18n/en_US", dirs[0]);
		assertEquals("pages/platform/i18n/en_US", dirs[1]);
		assertEquals("pages/product/i18n/en", dirs[2]);
		assertEquals("pages/platform/i18n/en", dirs[3]);

		dirs = config.getSourceDirectories(new Locale("en"));
		assertEquals(2, dirs.length);
		assertEquals("pages/product/i18n/en", dirs[0]);
		assertEquals("pages/platform/i18n/en", dirs[1]);

		dirs = config.getSourceDirectories(new Locale("es", "ES", "Traditional_WIN"));
		assertEquals(6, dirs.length);
		assertEquals("pages/product/i18n/es_ES_Traditional_WIN", dirs[0]);
		assertEquals("pages/platform/i18n/es_ES_Traditional_WIN", dirs[1]);
		assertEquals("pages/product/i18n/es_ES", dirs[2]);
		assertEquals("pages/platform/i18n/es_ES", dirs[3]);
		assertEquals("pages/product/i18n/es", dirs[4]);
		assertEquals("pages/platform/i18n/es", dirs[5]);
	}

	public void testGetGeneralClassPathDirectories_デフォルト値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String[] dirs = config.getGeneralClassPathDirectories();
		assertEquals(0, dirs.length);
	}

	public void testGetGeneralClassPathDirectories_設定値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String[] dirs = config.getGeneralClassPathDirectories();
		assertEquals(3, dirs.length);
		assertEquals("pages/bin1", dirs[0]);
		assertEquals("pages/bin2", dirs[1]);
		assertEquals("pages/bin3", dirs[2]);
	}

	
	public void testGetClassPathDirectories_デフォルト値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String[] dirs = config.getClassPathDirectories(new Locale("ja", "JP"));
		assertEquals(0, dirs.length);
	}

	public void testGetClassPathDirectories_設定値() throws Exception {
		String[] dirs;
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	

		dirs = config.getClassPathDirectories(new Locale("ja","JP"));
		assertEquals(3, dirs.length);
		assertEquals("pages/bin/i18n/ja_JP/dir1", dirs[0]);
		assertEquals("pages/bin/i18n/ja_JP/dir2", dirs[1]);
		assertEquals("pages/bin/i18n/ja_JP/dir3", dirs[2]);

		dirs = config.getClassPathDirectories(new Locale("ja"));
		assertEquals(3, dirs.length);
		assertEquals("pages/bin/i18n/ja/dir1", dirs[0]);
		assertEquals("pages/bin/i18n/ja/dir2", dirs[1]);
		assertEquals("pages/bin/i18n/ja/dir3", dirs[2]);

		dirs = config.getClassPathDirectories(new Locale("en", "US"));
		assertEquals(0, dirs.length);

		dirs = config.getClassPathDirectories(new Locale("en"));
		assertEquals(0, dirs.length);

		dirs = config.getClassPathDirectories(new Locale("es", "ES", "Traditional_WIN"));
		assertEquals(0, dirs.length);
		
	}

	
	public void testGetGeneralClassArchives_デフォルト値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String[] dirs = config.getGeneralClassArchives();
		assertEquals(0, dirs.length);
	}

	public void testGetGeneralClassArchives_設定値() throws Exception {
		String[] archives;
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	

		archives = config.getGeneralClassArchives();
		assertEquals(3, archives.length);
		assertEquals("pages/bin/test1.jar", archives[0]);
		assertEquals("pages/bin/test2.jar", archives[1]);
		assertEquals("pages/bin/test3.jar", archives[2]);
	}

	
	public void testGetClassArchives_デフォルト値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String[] archives = config.getClassArchives(new Locale("ja", "JP"));
		assertEquals(0, archives.length);
	}

	public void testGetClassArchives_設定値() throws Exception {
		String[] archives;
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	

		archives = config.getClassArchives(new Locale("ja","JP"));
		assertEquals(3, archives.length);
		assertEquals("pages/bin/i18n/ja_JP/test1.jar", archives[0]);
		assertEquals("pages/bin/i18n/ja_JP/test2.jar", archives[1]);
		assertEquals("pages/bin/i18n/ja_JP/test3.jar", archives[2]);

		archives = config.getClassArchives(new Locale("ja"));
		assertEquals(3, archives.length);
		assertEquals("pages/bin/i18n/ja/test1.jar", archives[0]);
		assertEquals("pages/bin/i18n/ja/test2.jar", archives[1]);
		assertEquals("pages/bin/i18n/ja/test3.jar", archives[2]);

		archives = config.getClassArchives(new Locale("en", "US"));
		assertEquals(0, archives.length);

		archives = config.getClassArchives(new Locale("en"));
		assertEquals(0, archives.length);

		archives = config.getClassArchives(new Locale("es", "ES", "Traditional_WIN"));
		assertEquals(0, archives.length);
		
	}
	
	

	public void testGetGeneralClassArchiveDirectories_デフォルト値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String[] dirs = config.getGeneralClassArchiveDirectories();
		assertEquals(0, dirs.length);
	}

	public void testGetGeneralClassArchiveDirectories_設定値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String[] dirs = config.getGeneralClassArchiveDirectories();
		assertEquals(3, dirs.length);
		assertEquals("pages/bin/lib1", dirs[0]);
		assertEquals("pages/bin/lib2", dirs[1]);
		assertEquals("pages/bin/lib3", dirs[2]);
	}

	public void testGetClassArchiveDirectories_デフォルト値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String[] dirs = config.getClassArchiveDirectories(new Locale("ja", "JP"));
		assertEquals(0, dirs.length);
	}

	public void testGetClassArchiveDirectories_設定値() throws Exception {
		String[] dirs;
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	

		dirs = config.getClassArchiveDirectories(new Locale("ja","JP"));
		assertEquals(3, dirs.length);
		assertEquals("pages/bin/i18n/lib/ja_JP/dir1", dirs[0]);
		assertEquals("pages/bin/i18n/lib/ja_JP/dir2", dirs[1]);
		assertEquals("pages/bin/i18n/lib/ja_JP/dir3", dirs[2]);

		dirs = config.getClassArchiveDirectories(new Locale("ja"));
		assertEquals(3, dirs.length);
		assertEquals("pages/bin/i18n/lib/ja/dir1", dirs[0]);
		assertEquals("pages/bin/i18n/lib/ja/dir2", dirs[1]);
		assertEquals("pages/bin/i18n/lib/ja/dir3", dirs[2]);

		dirs = config.getClassArchiveDirectories(new Locale("en", "US"));
		assertEquals(0, dirs.length);

		dirs = config.getClassArchiveDirectories(new Locale("en"));
		assertEquals(0, dirs.length);

		dirs = config.getClassArchiveDirectories(new Locale("es", "ES", "Traditional_WIN"));
		assertEquals(0, dirs.length);

	}

	public void testGetJavaScriptAPI4Class_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String[] classNames = config.getJavaScriptAPI4Class();
		assertEquals(0, classNames.length);
	}

	public void testGetJavaScriptAPI4Class_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String[] classNames = config.getJavaScriptAPI4Class();
		assertEquals(6, classNames.length);
		assertEquals("org.intra_mart.jssp.script.api.test.Class1", classNames[0]);
		assertEquals("org.intra_mart.jssp.script.api.test.Class2", classNames[1]);
		assertEquals("org.intra_mart.jssp.script.api.test.Class3", classNames[2]);
		assertEquals("org.intra_mart.jssp.script.api.test.Class4", classNames[3]);
		assertEquals("org.intra_mart.jssp.script.api.test.Class5", classNames[4]);
		assertEquals("org.intra_mart.jssp.script.api.test.Class6", classNames[5]);
	}

	public void testGetJavaScriptAPI4ClassConfig_デフォルト値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();
		String name="sample";
		NodeList nodeList = config.getJavaScriptAPI4ClassConfig(name);
		assertEquals(0, nodeList.getLength());
	}

	public void testGetJavaScriptAPI4ClassConfig_設定済み() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);
		String name="org.intra_mart.jssp.script.api.test.Class6";
		NodeList nodeList = config.getJavaScriptAPI4ClassConfig(name);
		assertEquals(2, nodeList.getLength());
		
		Node node0 = nodeList.item(0);
		assertEquals(Node.ELEMENT_NODE, node0.getNodeType());		
		Element elem0 = (Element) node0;
		assertEquals("foo0", elem0.getTagName());
		assertEquals("foo_api_0", elem0.getTextContent());
		
		Node node1 = nodeList.item(1);
		assertEquals(Node.ELEMENT_NODE, node1.getNodeType());		
		Element elem1 = (Element) node1;
		assertEquals("foo1", elem1.getTagName());
		assertEquals("foo_api_1", elem1.getTextContent());
	}

	public void testGetJavaScriptAPI4ClassConfig_未設定() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);
		String name="hoge";
		NodeList nodeList = config.getJavaScriptAPI4ClassConfig(name);
		assertEquals(0, nodeList.getLength());
	}

	public void testGetJavaScriptAPI4Script_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String[] scripts = config.getJavaScriptAPI4Script();
		assertEquals(0, scripts.length);
	}

	public void testGetJavaScriptAPI4Script_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String[] scripts = config.getJavaScriptAPI4Script();
		assertEquals(5, scripts.length);
		assertEquals("script/api/test/Page1#func1", scripts[0]);
		assertEquals("script/api/test/Page1#func2", scripts[1]);
		assertEquals("script/api/test/Page2#func1", scripts[2]);
		assertEquals("script/api/test/Page2#func2", scripts[3]);
		assertEquals("script/api/test/Page3#func1", scripts[4]);
	}

	public void testGetJavaScriptAPI4ScriptConfig_デフォルト値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();
		String name="sample";
		NodeList nodeList = config.getJavaScriptAPI4ScriptConfig(name);
		assertEquals(0, nodeList.getLength());
	}

	public void testGetJavaScriptAPI4ScriptConfig_設定済み() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);
		String name="script/api/test/Page3#func1";
		NodeList nodeList = config.getJavaScriptAPI4ScriptConfig(name);
		assertEquals(2, nodeList.getLength());
		
		Node node0 = nodeList.item(0);
		assertEquals(Node.ELEMENT_NODE, node0.getNodeType());		
		Element elem0 = (Element) node0;
		assertEquals("bar0", elem0.getTagName());
		assertEquals("bar_api_0", elem0.getTextContent());
		
		Node node1 = nodeList.item(1);
		assertEquals(Node.ELEMENT_NODE, node1.getNodeType());		
		Element elem1 = (Element) node1;
		assertEquals("bar1", elem1.getTagName());
		assertEquals("bar_api_1", elem1.getTextContent());
	}

	public void testGetJavaScriptAPI4ScriptConfig_未設定() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);
		String name="hoge";
		NodeList nodeList = config.getJavaScriptAPI4ScriptConfig(name);
		assertEquals(0, nodeList.getLength());
	}
	
	public void testGetJSSPTags4Class_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String[] classNames = config.getJSSPTags4Class();
		assertEquals(0, classNames.length);
	}

	public void testGetJSSPTags4Class_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String[] classNames = config.getJSSPTags4Class();
		assertEquals(6, classNames.length);
		assertEquals("org.intra_mart.jssp.view.tag.test.Class1", classNames[0]);
		assertEquals("org.intra_mart.jssp.view.tag.test.Class2", classNames[1]);
		assertEquals("org.intra_mart.jssp.view.tag.test.Class3", classNames[2]);
		assertEquals("org.intra_mart.jssp.view.tag.test.Class4", classNames[3]);
		assertEquals("org.intra_mart.jssp.view.tag.test.Class5", classNames[4]);
		assertEquals("org.intra_mart.jssp.view.tag.test.Class6", classNames[5]);
	}

	public void testGetJSSPTag4ClassConfig_デフォルト値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();
		String name="sample";
		NodeList nodeList = config.getJSSPTags4ClassConfig(name);
		assertEquals(0, nodeList.getLength());
	}

	public void testGetJSSPTags4ClassConfig_設定済み() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);
		String name="org.intra_mart.jssp.view.tag.test.Class6";
		NodeList nodeList = config.getJSSPTags4ClassConfig(name);
		assertEquals(2, nodeList.getLength());
		
		Node node0 = nodeList.item(0);
		assertEquals(Node.ELEMENT_NODE, node0.getNodeType());		
		Element elem0 = (Element) node0;
		assertEquals("foo0", elem0.getTagName());
		assertEquals("foo_tag_0", elem0.getTextContent());
		
		Node node1 = nodeList.item(1);
		assertEquals(Node.ELEMENT_NODE, node1.getNodeType());		
		Element elem1 = (Element) node1;
		assertEquals("foo1", elem1.getTagName());
		assertEquals("foo_tag_1", elem1.getTextContent());
	}

	public void testGetJSSPTags4ClassConfig_未設定() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);
		String name="hoge";
		NodeList nodeList = config.getJSSPTags4ClassConfig(name);
		assertEquals(0, nodeList.getLength());
	}
	
	public void testGetJSSPTags4Script_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String[] scripts = config.getJSSPTags4Script();
		assertEquals(0, scripts.length);
	}

	public void testGetJSSPTags4Script_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String[] scripts = config.getJSSPTags4Script();
		assertEquals(5, scripts.length);
		assertEquals("view/tag/test/Tag1#tagName1", scripts[0]);
		assertEquals("view/tag/test/Tag1#tagName2", scripts[1]);
		assertEquals("view/tag/test/Tag2#tagName1", scripts[2]);
		assertEquals("view/tag/test/Tag2#tagName2", scripts[3]);		
		assertEquals("view/tag/test/Tag3#tagName1", scripts[4]);		
	}

	public void testGetJSSPTags4ScriptConfig_デフォルト値() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();
		String name="sample";
		NodeList nodeList = config.getJSSPTags4ScriptConfig(name);
		assertEquals(0, nodeList.getLength());
	}

	public void testGetJSSPTags4ScriptConfig_設定済み() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);
		String name="view/tag/test/Tag3#tagName1";
		NodeList nodeList = config.getJSSPTags4ScriptConfig(name);
		assertEquals(2, nodeList.getLength());
		
		Node node0 = nodeList.item(0);
		assertEquals(Node.ELEMENT_NODE, node0.getNodeType());		
		Element elem0 = (Element) node0;
		assertEquals("bar0", elem0.getTagName());
		assertEquals("bar_tag_0", elem0.getTextContent());
		
		Node node1 = nodeList.item(1);
		assertEquals(Node.ELEMENT_NODE, node1.getNodeType());		
		Element elem1 = (Element) node1;
		assertEquals("bar1", elem1.getTagName());
		assertEquals("bar_tag_1", elem1.getTextContent());
	}

	public void testGetJSSPTags4ScriptConfig_未設定() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);
		String name="hoge";
		NodeList nodeList = config.getJSSPTags4ScriptConfig(name);
		assertEquals(0, nodeList.getLength());
	}
	
	public void testGetInitializer4Class_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String[] classNames = config.getInitializer4Class();
		assertEquals(0, classNames.length);
	}

	public void testGetInitializer4Class_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String[] classNames = config.getInitializer4Class();
		assertEquals(3, classNames.length);
		assertEquals("org.intra_mart.jssp.script.initialize.test.Class1", classNames[0]);
		assertEquals("org.intra_mart.jssp.script.initialize.test.Class2", classNames[1]);
		assertEquals("org.intra_mart.jssp.script.initialize.test.Class3", classNames[2]);
	}

	public void testGetInitializer4Script_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String[] scripts = config.getInitializer4Script();
		assertEquals(0, scripts.length);
	}

	public void testGetInitializer4Script_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String[] scripts = config.getInitializer4Script();
		assertEquals(3, scripts.length);
		assertEquals("initialize/app/test/Page1", scripts[0]);
		assertEquals("initialize/app/test/Page2", scripts[1]);
		assertEquals("initialize/app/test/Page3", scripts[2]);
	}

	public void testGetContextFactoryListeners_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String[] classNames = config.getContextFactoryListeners();
		assertEquals(0, classNames.length);
	}

	public void testGetContextFactoryListeners_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String[] classNames = config.getContextFactoryListeners();
		assertEquals(3, classNames.length);
		assertEquals("org.intra_mart.jssp.script.listener.test.Class1", classNames[0]);
		assertEquals("org.intra_mart.jssp.script.listener.test.Class2", classNames[1]);
		assertEquals("org.intra_mart.jssp.script.listener.test.Class3", classNames[2]);
	}

	public void testGetRequestProcessScript_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String script = config.getRequestProcessScript();
		assertEquals(null, script);
	}

	public void testGetRequestProcessScript_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String script = config.getRequestProcessScript();
		assertEquals("test/request_process", script);
	}

	public void testGetInitialFunctionName_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String funcName = config.getInitialFunctionName();
		assertEquals("init", funcName);
	}

	public void testGetInitialFunctionName_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String funcName = config.getInitialFunctionName();
		assertEquals("init_test", funcName);
	}

	public void testGetFinallyFunctionName_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String funcName = config.getFinallyFunctionName();
		assertEquals("close", funcName);
	}

	public void testGetFinallyFunctionName_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String funcName = config.getFinallyFunctionName();
		assertEquals("close_test", funcName);
	}

	public void testIsDebugBrowseEnable_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		boolean bool = config.isDebugBrowseEnable();
		assertEquals(true, bool);
	}

	public void testIsDebugBrowseEnable_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		boolean bool = config.isDebugBrowseEnable();
		assertEquals(false, bool);
	}

	public void testIsDebugPrintEnable_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		boolean bool = config.isDebugPrintEnable();
		assertEquals(true, bool);
	}

	public void testIsDebugPrintEnable_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		boolean bool = config.isDebugPrintEnable();
		assertEquals(false, bool);
	}

	public void testIsDebugWriteEnable_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		boolean bool = config.isDebugWriteEnable();
		assertEquals(true, bool);
	}

	public void testIsDebugWriteEnable_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		boolean bool = config.isDebugWriteEnable();
		assertEquals(false, bool);
	}

	public void testGetDebugWriteFilePath_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String filePath = config.getDebugWriteFilePath();
		assertEquals("debug.log", filePath);
	}

	public void testGetDebugWriteFilePath_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String filePath = config.getDebugWriteFilePath();
		assertEquals("test/debug_test.log", filePath);
	}

	public void testIsDebugConsoleEnable_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		boolean bool = config.isDebugConsoleEnable();
		assertEquals(true, bool);
	}

	public void testIsDebugConsoleEnable_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		boolean bool = config.isDebugConsoleEnable();
		assertEquals(false, bool);
	}
	
	public void testGetJsspKey4ActionEventName_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String name = config.getJsspKey4ActionEventName();
		assertEquals("im_action", name);
	}

	public void testGetJsspKey4ActionEventName_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String name = config.getJsspKey4ActionEventName();
		assertEquals("im_action_test", name);
	}

	public void testGetJsspKey4ActionEventPagePath_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String name = config.getJsspKey4ActionEventPagePath();
		assertEquals("im_event", name);
	}

	public void testGetJsspKey4ActionEventPagePath_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String name = config.getJsspKey4ActionEventPagePath();
		assertEquals("im_event_test", name);
	}

	public void testGetJsspKey4FromPagePath_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String name = config.getJsspKey4FromPagePath();
		assertEquals("im_from", name);
	}

	public void testGetJsspKey4FromPagePath_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String name = config.getJsspKey4FromPagePath();
		assertEquals("im_from_test", name);
	}

	public void testGetJsspKey4Mark_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String name = config.getJsspKey4Mark();
		assertEquals("im_mark", name);
	}

	public void testGetJsspKey4Mark_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String name = config.getJsspKey4Mark();
		assertEquals("im_mark_test", name);
	}

	public void testGetJsspKey4NextEventName_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String name = config.getJsspKey4NextEventName();
		assertEquals("im_func", name);
	}

	public void testGetJsspKey4NextEventName_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String name = config.getJsspKey4NextEventName();
		assertEquals("im_func_test", name);
	}

	public void testGetJsspKey4NextEventPagePath_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String name = config.getJsspKey4NextEventPagePath();
		assertEquals("", name);
	}

	public void testGetJsspKey4NextEventPagePath_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String name = config.getJsspKey4NextEventPagePath();
		assertEquals("im_next_test", name);
	}

	public void testGetSignatureKey_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String name = config.getSignatureKey();
		assertEquals("org.intra_mart.jssp.signature.id", name);
	}

	public void testGetSignatureKey_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String name = config.getSignatureKey();
		assertEquals("org.intra_mart.jssp.signature.id_test", name);
	}

	public void testGetURIPrefix_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String name = config.getURIPrefix();
		assertEquals("", name);
	}

	public void testGetURIPrefix_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String name = config.getURIPrefix();
		assertEquals("prefix_test", name);	
	}

	public void testGetURISuffix_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		String name = config.getURISuffix();
		assertEquals(".jssps", name);
	}

	public void testGetURISuffix_設定済() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		String name = config.getURISuffix();
		assertEquals(".jssp_test", name);
	}

	public void testGetLanguageVersion_デフォルト値() throws Exception { 
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl();	
		int version = config.getLanguageVersion();
		assertEquals(Context.VERSION_DEFAULT, version);
	}

	public void testGetLanguageVersion_設定済_小数点指定() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl(allConfigFileName);	
		int version = config.getLanguageVersion();
		assertEquals(Context.VERSION_1_6, version);
	}

	public void testGetLanguageVersion_設定済_整数指定() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl("/org/intra_mart/jssp/util/config/jssp-config_ValidVersionInteger.xml");	
		int version = config.getLanguageVersion();
		assertEquals(Context.VERSION_1_5, version);
	}

	public void testGetLanguageVersion_設定済_不正バージョン_桁多い() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl("/org/intra_mart/jssp/util/config/jssp-config_IllegalVersionOverflow.xml");	
		try{
			int version = config.getLanguageVersion();
		}
		catch(IllegalArgumentException e){
			assertTrue(e.getMessage().indexOf("Bad language version") != -1);
		}
	}
	
	public void testGetLanguageVersion_設定済_不正バージョン_文字列指定() throws Exception {
		JSSPConfigHandlerImpl config = new JSSPConfigHandlerImpl("/org/intra_mart/jssp/util/config/jssp-config_IllegalVersionNumberFormatException.xml");	
		try{
			int version = config.getLanguageVersion();
		}
		catch(IllegalArgumentException e){
			assertTrue(e.getMessage().indexOf("Bad language version") != -1);
		}
	}
}
