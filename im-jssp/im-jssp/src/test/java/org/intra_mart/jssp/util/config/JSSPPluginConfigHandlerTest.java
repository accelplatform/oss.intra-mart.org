package org.intra_mart.jssp.util.config;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@SuppressWarnings("deprecation")
public class JSSPPluginConfigHandlerTest extends TestCase {

	private static final String EMPTY_CONFIG_FILE_LOCATION = "/org/intra_mart/jssp/util/config/jssp-plugin-test-empty-config.xml";
	private static final String TEMPLATE_CONFIG_FILE_LOCATION = "/org/intra_mart/jssp/util/config/jssp-plugin-test-template-config.xml";

	private static final URL TEST1 = ClassLoader.getSystemResource("org/intra_mart/jssp/util/config/plugin-test1.jar");
	private static final URL TEST2 = ClassLoader.getSystemResource("org/intra_mart/jssp/util/config/plugin-test2.jar");
	private static final URL TEST3 = ClassLoader.getSystemResource("org/intra_mart/jssp/util/config/plugin-test3.jar");
	private static final URL TEST4 = ClassLoader.getSystemResource("org/intra_mart/jssp/util/config/plugin-test4.jar");
	private static final URL TEST5 = ClassLoader.getSystemResource("org/intra_mart/jssp/util/config/plugin-test5.jar");

	public void testGetJavaScriptAPI4Class1() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] javascriptAPI4Class = handler.getJavaScriptAPI4Class();

			assertEquals(1, javascriptAPI4Class.length);
			assertEquals("test1.Test1API", javascriptAPI4Class[0]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4Class2() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST2 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] javascriptAPI4Class = handler.getJavaScriptAPI4Class();

			assertEquals(1, javascriptAPI4Class.length);
			assertEquals("test2.Test2API", javascriptAPI4Class[0]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4Class3() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST3 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] javascriptAPI4Class = handler.getJavaScriptAPI4Class();

			assertEquals(1, javascriptAPI4Class.length);
			assertEquals("test3.Test3API", javascriptAPI4Class[0]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4Class4() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST4 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] javascriptAPI4Class = handler.getJavaScriptAPI4Class();

			assertEquals(2, javascriptAPI4Class.length);
			assertEquals("test4.Test4_0API", javascriptAPI4Class[0]);
			assertEquals("test4.Test4_1API", javascriptAPI4Class[1]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4Class5() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] javascriptAPI4Class = handler.getJavaScriptAPI4Class();

			assertEquals(0, javascriptAPI4Class.length);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4ClassAll() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1, TEST2, TEST3, TEST4, TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] javascriptAPI4Class = handler.getJavaScriptAPI4Class();

			assertEquals(5, javascriptAPI4Class.length);
			assertEquals("test1.Test1API", javascriptAPI4Class[0]);
			assertEquals("test2.Test2API", javascriptAPI4Class[1]);
			assertEquals("test3.Test3API", javascriptAPI4Class[2]);
			assertEquals("test4.Test4_0API", javascriptAPI4Class[3]);
			assertEquals("test4.Test4_1API", javascriptAPI4Class[4]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4ClassConfig1() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJavaScriptAPI4ClassConfig("test1.Test1API");

			assertEquals(1, nodeList.getLength());
			assertEquals("TEST1_API", nodeList.item(0).getTextContent());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4ClassConfig2() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST2 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJavaScriptAPI4ClassConfig("test2.Test2API");

			assertEquals(0, nodeList.getLength());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4ClassConfig3() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST3 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJavaScriptAPI4ClassConfig("test3.Test3API");

			assertEquals(3, nodeList.getLength());
			assertTrue(nodeList.item(0) instanceof Element);
			assertEquals("TEST3_1API", nodeList.item(0).getTextContent());
			assertEquals("test3_1api", ((Element) nodeList.item(0)).getTagName());
			assertTrue(nodeList.item(1) instanceof Element);
			assertEquals("TEST3_2API", nodeList.item(1).getTextContent());
			assertEquals("test3_2api", ((Element) nodeList.item(1)).getTagName());
			assertTrue(nodeList.item(2) instanceof Element);
			assertEquals("TEST3_3API", nodeList.item(2).getTextContent());
			assertEquals("test3_3api", ((Element) nodeList.item(2)).getTagName());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4ClassConfig4() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST4 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJavaScriptAPI4ClassConfig("test4.Test4_0API");

			assertEquals(3, nodeList.getLength());
			assertTrue(nodeList.item(0) instanceof Element);
			assertEquals("TEST4_1API", nodeList.item(0).getTextContent());
			assertEquals("test4_1api", ((Element) nodeList.item(0)).getTagName());
			assertTrue(nodeList.item(1) instanceof Element);
			assertEquals("TEST4_2API", nodeList.item(1).getTextContent());
			assertEquals("test4_2api", ((Element) nodeList.item(1)).getTagName());
			assertTrue(nodeList.item(2) instanceof Element);
			assertEquals("TEST4_3API", nodeList.item(2).getTextContent());
			assertEquals("test4_3api", ((Element) nodeList.item(2)).getTagName());

			nodeList = handler.getJavaScriptAPI4ClassConfig("test4.Test4_1API");

			assertEquals(0, nodeList.getLength());

		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4ClassConfig5() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJavaScriptAPI4ClassConfig("dummy");

			assertEquals(0, nodeList.getLength());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4ClassConfigAll() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1, TEST2, TEST3, TEST4, TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJavaScriptAPI4ClassConfig("test4.Test4_0API");

			assertEquals(3, nodeList.getLength());
			assertTrue(nodeList.item(0) instanceof Element);
			assertEquals("TEST4_1API", nodeList.item(0).getTextContent());
			assertEquals("test4_1api", ((Element) nodeList.item(0)).getTagName());
			assertTrue(nodeList.item(1) instanceof Element);
			assertEquals("TEST4_2API", nodeList.item(1).getTextContent());
			assertEquals("test4_2api", ((Element) nodeList.item(1)).getTagName());
			assertTrue(nodeList.item(2) instanceof Element);
			assertEquals("TEST4_3API", nodeList.item(2).getTextContent());
			assertEquals("test4_3api", ((Element) nodeList.item(2)).getTagName());

			nodeList = handler.getJavaScriptAPI4ClassConfig("test4.Test4_1API");

			assertEquals(0, nodeList.getLength());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4Class1() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] jsspTags4Class = handler.getJSSPTags4Class();

			assertEquals(1, jsspTags4Class.length);
			assertEquals("test1.Test1Tag", jsspTags4Class[0]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4Class2() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST2 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] jsspTags4Class = handler.getJSSPTags4Class();

			assertEquals(1, jsspTags4Class.length);
			assertEquals("test2.Test2Tag", jsspTags4Class[0]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4Class3() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST3 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] jsspTags4Class = handler.getJSSPTags4Class();

			assertEquals(1, jsspTags4Class.length);
			assertEquals("test3.Test3Tag", jsspTags4Class[0]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4Class4() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST4 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] jsspTags4Class = handler.getJSSPTags4Class();

			assertEquals(2, jsspTags4Class.length);
			assertEquals("test4.Test4_0Tag", jsspTags4Class[0]);
			assertEquals("test4.Test4_1Tag", jsspTags4Class[1]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4Class5() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] jsspTags4Class = handler.getJSSPTags4Class();

			assertEquals(0, jsspTags4Class.length);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4ClassAll() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1, TEST2, TEST3, TEST4, TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] jsspTags4Class = handler.getJSSPTags4Class();

			assertEquals(5, jsspTags4Class.length);
			assertEquals("test1.Test1Tag", jsspTags4Class[0]);
			assertEquals("test2.Test2Tag", jsspTags4Class[1]);
			assertEquals("test3.Test3Tag", jsspTags4Class[2]);
			assertEquals("test4.Test4_0Tag", jsspTags4Class[3]);
			assertEquals("test4.Test4_1Tag", jsspTags4Class[4]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4ClassConfig1() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJSSPTags4ClassConfig("test1.Test1Tag");

			assertEquals(1, nodeList.getLength());
			assertEquals("TEST1_TAG", nodeList.item(0).getTextContent());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4ClassConfig2() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST2 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJSSPTags4ClassConfig("test2.Test2Tag");

			assertEquals(0, nodeList.getLength());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4ClassConfig3() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST3 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJSSPTags4ClassConfig("test3.Test3Tag");

			assertEquals(3, nodeList.getLength());
			assertTrue(nodeList.item(0) instanceof Element);
			assertEquals("TEST3_1TAG", nodeList.item(0).getTextContent());
			assertEquals("test3_1tag", ((Element) nodeList.item(0)).getTagName());
			assertTrue(nodeList.item(1) instanceof Element);
			assertEquals("TEST3_2TAG", nodeList.item(1).getTextContent());
			assertEquals("test3_2tag", ((Element) nodeList.item(1)).getTagName());
			assertTrue(nodeList.item(2) instanceof Element);
			assertEquals("TEST3_3TAG", nodeList.item(2).getTextContent());
			assertEquals("test3_3tag", ((Element) nodeList.item(2)).getTagName());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4ClassConfig4() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST4 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJSSPTags4ClassConfig("test4.Test4_0Tag");

			assertEquals(3, nodeList.getLength());
			assertTrue(nodeList.item(0) instanceof Element);
			assertEquals("TEST4_1TAG", nodeList.item(0).getTextContent());
			assertEquals("test4_1tag", ((Element) nodeList.item(0)).getTagName());
			assertTrue(nodeList.item(1) instanceof Element);
			assertEquals("TEST4_2TAG", nodeList.item(1).getTextContent());
			assertEquals("test4_2tag", ((Element) nodeList.item(1)).getTagName());
			assertTrue(nodeList.item(2) instanceof Element);
			assertEquals("TEST4_3TAG", nodeList.item(2).getTextContent());
			assertEquals("test4_3tag", ((Element) nodeList.item(2)).getTagName());

			nodeList = handler.getJSSPTags4ClassConfig("test4.Test4_1Tag");

			assertEquals(0, nodeList.getLength());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4ClassConfig5() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJSSPTags4ClassConfig("dummy");

			assertEquals(0, nodeList.getLength());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4ClassConfigAll() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1, TEST2, TEST3, TEST4, TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJSSPTags4ClassConfig("test4.Test4_0Tag");

			assertEquals(3, nodeList.getLength());
			assertTrue(nodeList.item(0) instanceof Element);
			assertEquals("TEST4_1TAG", nodeList.item(0).getTextContent());
			assertEquals("test4_1tag", ((Element) nodeList.item(0)).getTagName());
			assertTrue(nodeList.item(1) instanceof Element);
			assertEquals("TEST4_2TAG", nodeList.item(1).getTextContent());
			assertEquals("test4_2tag", ((Element) nodeList.item(1)).getTagName());
			assertTrue(nodeList.item(2) instanceof Element);
			assertEquals("TEST4_3TAG", nodeList.item(2).getTextContent());
			assertEquals("test4_3tag", ((Element) nodeList.item(2)).getTagName());

			nodeList = handler.getJSSPTags4ClassConfig("test4.Test4_1Tag");

			assertEquals(0, nodeList.getLength());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetInitializer4Class1() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] initializer4Class = handler.getInitializer4Class();

			assertEquals(1, initializer4Class.length);
			assertEquals("test1.Test1Initializer", initializer4Class[0]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetInitializer4Class2() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST2 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] initializer4Class = handler.getInitializer4Class();

			assertEquals(1, initializer4Class.length);
			assertEquals("test2.Test2Initializer", initializer4Class[0]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetInitializer4Class3() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST3 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] initializer4Class = handler.getInitializer4Class();

			assertEquals(2, initializer4Class.length);
			assertEquals("test3.Test3Initializer1", initializer4Class[0]);
			assertEquals("test3.Test3Initializer2", initializer4Class[1]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetInitializer4Class4() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST4 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] initializer4Class = handler.getInitializer4Class();

			assertEquals(0, initializer4Class.length);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetInitializer4Class5() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] initializer4Class = handler.getInitializer4Class();

			assertEquals(0, initializer4Class.length);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetInitializer4ClassAll() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1, TEST2, TEST3, TEST4, TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] initializer4Class = handler.getInitializer4Class();

			assertEquals(4, initializer4Class.length);
			assertEquals("test1.Test1Initializer", initializer4Class[0]);
			assertEquals("test2.Test2Initializer", initializer4Class[1]);
			assertEquals("test3.Test3Initializer1", initializer4Class[2]);
			assertEquals("test3.Test3Initializer2", initializer4Class[3]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetContextFactoryListeners1() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] contextFactoryListeners = handler.getContextFactoryListeners();

			assertEquals(1, contextFactoryListeners.length);
			assertEquals("test1.Test1ContextFactoryListener", contextFactoryListeners[0]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetContextFactoryListeners2() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST2 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] contextFactoryListeners = handler.getContextFactoryListeners();

			assertEquals(1, contextFactoryListeners.length);
			assertEquals("test2.Test2ContextFactoryListener", contextFactoryListeners[0]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetContextFactoryListeners3() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST3 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] contextFactoryListeners = handler.getContextFactoryListeners();

			assertEquals(2, contextFactoryListeners.length);
			assertEquals("test3.Test3ContextFactoryListener1", contextFactoryListeners[0]);
			assertEquals("test3.Test3ContextFactoryListener2", contextFactoryListeners[1]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetContextFactoryListeners4() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST4 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] contextFactoryListeners = handler.getContextFactoryListeners();

			assertEquals(0, contextFactoryListeners.length);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetContextFactoryListeners5() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] contextFactoryListeners = handler.getContextFactoryListeners();

			assertEquals(0, contextFactoryListeners.length);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetContextFactoryListenersAll() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1, TEST2, TEST3, TEST4, TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(EMPTY_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] contextFactoryListeners = handler.getContextFactoryListeners();

			assertEquals(4, contextFactoryListeners.length);
			assertEquals("test1.Test1ContextFactoryListener", contextFactoryListeners[0]);
			assertEquals("test2.Test2ContextFactoryListener", contextFactoryListeners[1]);
			assertEquals("test3.Test3ContextFactoryListener1", contextFactoryListeners[2]);
			assertEquals("test3.Test3ContextFactoryListener2", contextFactoryListeners[3]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4Class1WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] javascriptAPI4Class = handler.getJavaScriptAPI4Class();

			assertEquals(7, javascriptAPI4Class.length);
			assertEquals("org.intra_mart.jssp.script.api.test.Class1", javascriptAPI4Class[0]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class2", javascriptAPI4Class[1]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class3", javascriptAPI4Class[2]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class4", javascriptAPI4Class[3]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class5", javascriptAPI4Class[4]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class6", javascriptAPI4Class[5]);
			assertEquals("test1.Test1API", javascriptAPI4Class[6]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4Class2WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST2 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] javascriptAPI4Class = handler.getJavaScriptAPI4Class();

			assertEquals(7, javascriptAPI4Class.length);
			assertEquals("org.intra_mart.jssp.script.api.test.Class1", javascriptAPI4Class[0]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class2", javascriptAPI4Class[1]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class3", javascriptAPI4Class[2]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class4", javascriptAPI4Class[3]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class5", javascriptAPI4Class[4]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class6", javascriptAPI4Class[5]);
			assertEquals("test2.Test2API", javascriptAPI4Class[6]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4Class3WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST3 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] javascriptAPI4Class = handler.getJavaScriptAPI4Class();

			assertEquals(7, javascriptAPI4Class.length);
			assertEquals("org.intra_mart.jssp.script.api.test.Class1", javascriptAPI4Class[0]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class2", javascriptAPI4Class[1]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class3", javascriptAPI4Class[2]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class4", javascriptAPI4Class[3]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class5", javascriptAPI4Class[4]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class6", javascriptAPI4Class[5]);
			assertEquals("test3.Test3API", javascriptAPI4Class[6]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4Class4WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST4 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] javascriptAPI4Class = handler.getJavaScriptAPI4Class();

			assertEquals(8, javascriptAPI4Class.length);
			assertEquals("org.intra_mart.jssp.script.api.test.Class1", javascriptAPI4Class[0]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class2", javascriptAPI4Class[1]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class3", javascriptAPI4Class[2]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class4", javascriptAPI4Class[3]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class5", javascriptAPI4Class[4]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class6", javascriptAPI4Class[5]);
			assertEquals("test4.Test4_0API", javascriptAPI4Class[6]);
			assertEquals("test4.Test4_1API", javascriptAPI4Class[7]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4Class5WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] javascriptAPI4Class = handler.getJavaScriptAPI4Class();

			assertEquals(6, javascriptAPI4Class.length);
			assertEquals("org.intra_mart.jssp.script.api.test.Class1", javascriptAPI4Class[0]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class2", javascriptAPI4Class[1]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class3", javascriptAPI4Class[2]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class4", javascriptAPI4Class[3]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class5", javascriptAPI4Class[4]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class6", javascriptAPI4Class[5]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4ClassAllWithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1, TEST2, TEST3, TEST4, TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] javascriptAPI4Class = handler.getJavaScriptAPI4Class();

			assertEquals(11, javascriptAPI4Class.length);
			assertEquals("org.intra_mart.jssp.script.api.test.Class1", javascriptAPI4Class[0]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class2", javascriptAPI4Class[1]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class3", javascriptAPI4Class[2]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class4", javascriptAPI4Class[3]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class5", javascriptAPI4Class[4]);
			assertEquals("org.intra_mart.jssp.script.api.test.Class6", javascriptAPI4Class[5]);
			assertEquals("test1.Test1API", javascriptAPI4Class[6]);
			assertEquals("test2.Test2API", javascriptAPI4Class[7]);
			assertEquals("test3.Test3API", javascriptAPI4Class[8]);
			assertEquals("test4.Test4_0API", javascriptAPI4Class[9]);
			assertEquals("test4.Test4_1API", javascriptAPI4Class[10]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4ClassConfig1WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJavaScriptAPI4ClassConfig("test1.Test1API");

			assertEquals(1, nodeList.getLength());
			assertEquals("TEST1_API", nodeList.item(0).getTextContent());

			NodeList templateNodeList1 = handler.getJavaScriptAPI4ClassConfig("org.intra_mart.jssp.script.api.test.Class1");
			assertEquals(0, templateNodeList1.getLength());

			NodeList templateNodeList2 = handler.getJavaScriptAPI4ClassConfig("org.intra_mart.jssp.script.api.test.Class6");
			assertEquals(2, templateNodeList2.getLength());
			assertTrue(templateNodeList2.item(0) instanceof Element);
			assertEquals("foo0", ((Element) templateNodeList2.item(0)).getTagName());
			assertEquals("foo_api_0", templateNodeList2.item(0).getTextContent());
			assertTrue(templateNodeList2.item(1) instanceof Element);
			assertEquals("foo1", ((Element) templateNodeList2.item(1)).getTagName());
			assertEquals("foo_api_1", templateNodeList2.item(1).getTextContent());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4ClassConfig2WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST2 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJavaScriptAPI4ClassConfig("test2.Test2API");

			assertEquals(0, nodeList.getLength());

			NodeList templateNodeList1 = handler.getJavaScriptAPI4ClassConfig("org.intra_mart.jssp.script.api.test.Class1");
			assertEquals(0, templateNodeList1.getLength());

			NodeList templateNodeList2 = handler.getJavaScriptAPI4ClassConfig("org.intra_mart.jssp.script.api.test.Class6");
			assertEquals(2, templateNodeList2.getLength());
			assertTrue(templateNodeList2.item(0) instanceof Element);
			assertEquals("foo0", ((Element) templateNodeList2.item(0)).getTagName());
			assertEquals("foo_api_0", templateNodeList2.item(0).getTextContent());
			assertTrue(templateNodeList2.item(1) instanceof Element);
			assertEquals("foo1", ((Element) templateNodeList2.item(1)).getTagName());
			assertEquals("foo_api_1", templateNodeList2.item(1).getTextContent());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4ClassConfig3WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST3 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJavaScriptAPI4ClassConfig("test3.Test3API");

			assertEquals(3, nodeList.getLength());
			assertTrue(nodeList.item(0) instanceof Element);
			assertEquals("TEST3_1API", nodeList.item(0).getTextContent());
			assertEquals("test3_1api", ((Element) nodeList.item(0)).getTagName());
			assertTrue(nodeList.item(1) instanceof Element);
			assertEquals("TEST3_2API", nodeList.item(1).getTextContent());
			assertEquals("test3_2api", ((Element) nodeList.item(1)).getTagName());
			assertTrue(nodeList.item(2) instanceof Element);
			assertEquals("TEST3_3API", nodeList.item(2).getTextContent());
			assertEquals("test3_3api", ((Element) nodeList.item(2)).getTagName());

			NodeList templateNodeList1 = handler.getJavaScriptAPI4ClassConfig("org.intra_mart.jssp.script.api.test.Class1");
			assertEquals(0, templateNodeList1.getLength());

			NodeList templateNodeList2 = handler.getJavaScriptAPI4ClassConfig("org.intra_mart.jssp.script.api.test.Class6");
			assertEquals(2, templateNodeList2.getLength());
			assertTrue(templateNodeList2.item(0) instanceof Element);
			assertEquals("foo0", ((Element) templateNodeList2.item(0)).getTagName());
			assertEquals("foo_api_0", templateNodeList2.item(0).getTextContent());
			assertTrue(templateNodeList2.item(1) instanceof Element);
			assertEquals("foo1", ((Element) templateNodeList2.item(1)).getTagName());
			assertEquals("foo_api_1", templateNodeList2.item(1).getTextContent());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4ClassConfig4WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST4 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJavaScriptAPI4ClassConfig("test4.Test4_0API");

			assertEquals(3, nodeList.getLength());
			assertTrue(nodeList.item(0) instanceof Element);
			assertEquals("TEST4_1API", nodeList.item(0).getTextContent());
			assertEquals("test4_1api", ((Element) nodeList.item(0)).getTagName());
			assertTrue(nodeList.item(1) instanceof Element);
			assertEquals("TEST4_2API", nodeList.item(1).getTextContent());
			assertEquals("test4_2api", ((Element) nodeList.item(1)).getTagName());
			assertTrue(nodeList.item(2) instanceof Element);
			assertEquals("TEST4_3API", nodeList.item(2).getTextContent());
			assertEquals("test4_3api", ((Element) nodeList.item(2)).getTagName());

			nodeList = handler.getJavaScriptAPI4ClassConfig("test4.Test4_1API");

			assertEquals(0, nodeList.getLength());

			NodeList templateNodeList1 = handler.getJavaScriptAPI4ClassConfig("org.intra_mart.jssp.script.api.test.Class1");
			assertEquals(0, templateNodeList1.getLength());

			NodeList templateNodeList2 = handler.getJavaScriptAPI4ClassConfig("org.intra_mart.jssp.script.api.test.Class6");
			assertEquals(2, templateNodeList2.getLength());
			assertTrue(templateNodeList2.item(0) instanceof Element);
			assertEquals("foo0", ((Element) templateNodeList2.item(0)).getTagName());
			assertEquals("foo_api_0", templateNodeList2.item(0).getTextContent());
			assertTrue(templateNodeList2.item(1) instanceof Element);
			assertEquals("foo1", ((Element) templateNodeList2.item(1)).getTagName());
			assertEquals("foo_api_1", templateNodeList2.item(1).getTextContent());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4ClassConfig5WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJavaScriptAPI4ClassConfig("dummy");

			assertEquals(0, nodeList.getLength());

			NodeList templateNodeList1 = handler.getJavaScriptAPI4ClassConfig("org.intra_mart.jssp.script.api.test.Class1");
			assertEquals(0, templateNodeList1.getLength());

			NodeList templateNodeList2 = handler.getJavaScriptAPI4ClassConfig("org.intra_mart.jssp.script.api.test.Class6");
			assertEquals(2, templateNodeList2.getLength());
			assertTrue(templateNodeList2.item(0) instanceof Element);
			assertEquals("foo0", ((Element) templateNodeList2.item(0)).getTagName());
			assertEquals("foo_api_0", templateNodeList2.item(0).getTextContent());
			assertTrue(templateNodeList2.item(1) instanceof Element);
			assertEquals("foo1", ((Element) templateNodeList2.item(1)).getTagName());
			assertEquals("foo_api_1", templateNodeList2.item(1).getTextContent());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJavaScriptAPI4ClassConfigAllWithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1, TEST2, TEST3, TEST4, TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJavaScriptAPI4ClassConfig("test4.Test4_0API");

			assertEquals(3, nodeList.getLength());
			assertTrue(nodeList.item(0) instanceof Element);
			assertEquals("TEST4_1API", nodeList.item(0).getTextContent());
			assertEquals("test4_1api", ((Element) nodeList.item(0)).getTagName());
			assertTrue(nodeList.item(1) instanceof Element);
			assertEquals("TEST4_2API", nodeList.item(1).getTextContent());
			assertEquals("test4_2api", ((Element) nodeList.item(1)).getTagName());
			assertTrue(nodeList.item(2) instanceof Element);
			assertEquals("TEST4_3API", nodeList.item(2).getTextContent());
			assertEquals("test4_3api", ((Element) nodeList.item(2)).getTagName());

			nodeList = handler.getJavaScriptAPI4ClassConfig("test4.Test4_1API");

			assertEquals(0, nodeList.getLength());

			NodeList templateNodeList1 = handler.getJavaScriptAPI4ClassConfig("org.intra_mart.jssp.script.api.test.Class1");
			assertEquals(0, templateNodeList1.getLength());

			NodeList templateNodeList2 = handler.getJavaScriptAPI4ClassConfig("org.intra_mart.jssp.script.api.test.Class6");
			assertEquals(2, templateNodeList2.getLength());
			assertTrue(templateNodeList2.item(0) instanceof Element);
			assertEquals("foo0", ((Element) templateNodeList2.item(0)).getTagName());
			assertEquals("foo_api_0", templateNodeList2.item(0).getTextContent());
			assertTrue(templateNodeList2.item(1) instanceof Element);
			assertEquals("foo1", ((Element) templateNodeList2.item(1)).getTagName());
			assertEquals("foo_api_1", templateNodeList2.item(1).getTextContent());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4Class1WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] jsspTags4Class = handler.getJSSPTags4Class();

			assertEquals(7, jsspTags4Class.length);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class1", jsspTags4Class[0]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class2", jsspTags4Class[1]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class3", jsspTags4Class[2]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class4", jsspTags4Class[3]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class5", jsspTags4Class[4]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class6", jsspTags4Class[5]);
			assertEquals("test1.Test1Tag", jsspTags4Class[6]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4Class2WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST2 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] jsspTags4Class = handler.getJSSPTags4Class();

			assertEquals(7, jsspTags4Class.length);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class1", jsspTags4Class[0]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class2", jsspTags4Class[1]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class3", jsspTags4Class[2]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class4", jsspTags4Class[3]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class5", jsspTags4Class[4]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class6", jsspTags4Class[5]);
			assertEquals("test2.Test2Tag", jsspTags4Class[6]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4Class3WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST3 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] jsspTags4Class = handler.getJSSPTags4Class();

			assertEquals(7, jsspTags4Class.length);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class1", jsspTags4Class[0]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class2", jsspTags4Class[1]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class3", jsspTags4Class[2]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class4", jsspTags4Class[3]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class5", jsspTags4Class[4]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class6", jsspTags4Class[5]);
			assertEquals("test3.Test3Tag", jsspTags4Class[6]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4Class4WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST4 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] jsspTags4Class = handler.getJSSPTags4Class();

			assertEquals(8, jsspTags4Class.length);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class1", jsspTags4Class[0]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class2", jsspTags4Class[1]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class3", jsspTags4Class[2]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class4", jsspTags4Class[3]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class5", jsspTags4Class[4]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class6", jsspTags4Class[5]);
			assertEquals("test4.Test4_0Tag", jsspTags4Class[6]);
			assertEquals("test4.Test4_1Tag", jsspTags4Class[7]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4Class5WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] jsspTags4Class = handler.getJSSPTags4Class();

			assertEquals(6, jsspTags4Class.length);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class1", jsspTags4Class[0]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class2", jsspTags4Class[1]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class3", jsspTags4Class[2]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class4", jsspTags4Class[3]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class5", jsspTags4Class[4]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class6", jsspTags4Class[5]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4ClassAllWithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1, TEST2, TEST3, TEST4, TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] jsspTags4Class = handler.getJSSPTags4Class();

			assertEquals(11, jsspTags4Class.length);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class1", jsspTags4Class[0]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class2", jsspTags4Class[1]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class3", jsspTags4Class[2]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class4", jsspTags4Class[3]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class5", jsspTags4Class[4]);
			assertEquals("org.intra_mart.jssp.view.tag.test.Class6", jsspTags4Class[5]);
			assertEquals("test1.Test1Tag", jsspTags4Class[6]);
			assertEquals("test2.Test2Tag", jsspTags4Class[7]);
			assertEquals("test3.Test3Tag", jsspTags4Class[8]);
			assertEquals("test4.Test4_0Tag", jsspTags4Class[9]);
			assertEquals("test4.Test4_1Tag", jsspTags4Class[10]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4ClassConfig1WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJSSPTags4ClassConfig("test1.Test1Tag");

			assertEquals(1, nodeList.getLength());
			assertEquals("TEST1_TAG", nodeList.item(0).getTextContent());

			NodeList templateNodeList1 = handler.getJSSPTags4ClassConfig("org.intra_mart.jssp.view.tag.test.Class1");
			assertEquals(0, templateNodeList1.getLength());

			NodeList templateNodeList2 = handler.getJSSPTags4ClassConfig("org.intra_mart.jssp.view.tag.test.Class6");
			assertEquals(2, templateNodeList2.getLength());
			assertTrue(templateNodeList2.item(0) instanceof Element);
			assertEquals("foo0", ((Element) templateNodeList2.item(0)).getTagName());
			assertEquals("foo_tag_0", templateNodeList2.item(0).getTextContent());
			assertTrue(templateNodeList2.item(1) instanceof Element);
			assertEquals("foo1", ((Element) templateNodeList2.item(1)).getTagName());
			assertEquals("foo_tag_1", templateNodeList2.item(1).getTextContent());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4ClassConfig2WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST2 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJSSPTags4ClassConfig("test2.Test2Tag");

			assertEquals(0, nodeList.getLength());

			NodeList templateNodeList1 = handler.getJSSPTags4ClassConfig("org.intra_mart.jssp.view.tag.test.Class1");
			assertEquals(0, templateNodeList1.getLength());

			NodeList templateNodeList2 = handler.getJSSPTags4ClassConfig("org.intra_mart.jssp.view.tag.test.Class6");
			assertEquals(2, templateNodeList2.getLength());
			assertTrue(templateNodeList2.item(0) instanceof Element);
			assertEquals("foo0", ((Element) templateNodeList2.item(0)).getTagName());
			assertEquals("foo_tag_0", templateNodeList2.item(0).getTextContent());
			assertTrue(templateNodeList2.item(1) instanceof Element);
			assertEquals("foo1", ((Element) templateNodeList2.item(1)).getTagName());
			assertEquals("foo_tag_1", templateNodeList2.item(1).getTextContent());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4ClassConfig3WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST3 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJSSPTags4ClassConfig("test3.Test3Tag");

			assertEquals(3, nodeList.getLength());
			assertTrue(nodeList.item(0) instanceof Element);
			assertEquals("TEST3_1TAG", nodeList.item(0).getTextContent());
			assertEquals("test3_1tag", ((Element) nodeList.item(0)).getTagName());
			assertTrue(nodeList.item(1) instanceof Element);
			assertEquals("TEST3_2TAG", nodeList.item(1).getTextContent());
			assertEquals("test3_2tag", ((Element) nodeList.item(1)).getTagName());
			assertTrue(nodeList.item(2) instanceof Element);
			assertEquals("TEST3_3TAG", nodeList.item(2).getTextContent());
			assertEquals("test3_3tag", ((Element) nodeList.item(2)).getTagName());

			NodeList templateNodeList1 = handler.getJSSPTags4ClassConfig("org.intra_mart.jssp.view.tag.test.Class1");
			assertEquals(0, templateNodeList1.getLength());

			NodeList templateNodeList2 = handler.getJSSPTags4ClassConfig("org.intra_mart.jssp.view.tag.test.Class6");
			assertEquals(2, templateNodeList2.getLength());
			assertTrue(templateNodeList2.item(0) instanceof Element);
			assertEquals("foo0", ((Element) templateNodeList2.item(0)).getTagName());
			assertEquals("foo_tag_0", templateNodeList2.item(0).getTextContent());
			assertTrue(templateNodeList2.item(1) instanceof Element);
			assertEquals("foo1", ((Element) templateNodeList2.item(1)).getTagName());
			assertEquals("foo_tag_1", templateNodeList2.item(1).getTextContent());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4ClassConfig4WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST4 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJSSPTags4ClassConfig("test4.Test4_0Tag");

			assertEquals(3, nodeList.getLength());
			assertTrue(nodeList.item(0) instanceof Element);
			assertEquals("TEST4_1TAG", nodeList.item(0).getTextContent());
			assertEquals("test4_1tag", ((Element) nodeList.item(0)).getTagName());
			assertTrue(nodeList.item(1) instanceof Element);
			assertEquals("TEST4_2TAG", nodeList.item(1).getTextContent());
			assertEquals("test4_2tag", ((Element) nodeList.item(1)).getTagName());
			assertTrue(nodeList.item(2) instanceof Element);
			assertEquals("TEST4_3TAG", nodeList.item(2).getTextContent());
			assertEquals("test4_3tag", ((Element) nodeList.item(2)).getTagName());

			nodeList = handler.getJSSPTags4ClassConfig("test4.Test4_1Tag");

			assertEquals(0, nodeList.getLength());

			NodeList templateNodeList1 = handler.getJSSPTags4ClassConfig("org.intra_mart.jssp.view.tag.test.Class1");
			assertEquals(0, templateNodeList1.getLength());

			NodeList templateNodeList2 = handler.getJSSPTags4ClassConfig("org.intra_mart.jssp.view.tag.test.Class6");
			assertEquals(2, templateNodeList2.getLength());
			assertTrue(templateNodeList2.item(0) instanceof Element);
			assertEquals("foo0", ((Element) templateNodeList2.item(0)).getTagName());
			assertEquals("foo_tag_0", templateNodeList2.item(0).getTextContent());
			assertTrue(templateNodeList2.item(1) instanceof Element);
			assertEquals("foo1", ((Element) templateNodeList2.item(1)).getTagName());
			assertEquals("foo_tag_1", templateNodeList2.item(1).getTextContent());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4ClassConfig5WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJSSPTags4ClassConfig("dummy");

			assertEquals(0, nodeList.getLength());

			NodeList templateNodeList1 = handler.getJSSPTags4ClassConfig("org.intra_mart.jssp.view.tag.test.Class1");
			assertEquals(0, templateNodeList1.getLength());

			NodeList templateNodeList2 = handler.getJSSPTags4ClassConfig("org.intra_mart.jssp.view.tag.test.Class6");
			assertEquals(2, templateNodeList2.getLength());
			assertTrue(templateNodeList2.item(0) instanceof Element);
			assertEquals("foo0", ((Element) templateNodeList2.item(0)).getTagName());
			assertEquals("foo_tag_0", templateNodeList2.item(0).getTextContent());
			assertTrue(templateNodeList2.item(1) instanceof Element);
			assertEquals("foo1", ((Element) templateNodeList2.item(1)).getTagName());
			assertEquals("foo_tag_1", templateNodeList2.item(1).getTextContent());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetJSSPTags4ClassConfigAllWithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1, TEST2, TEST3, TEST4, TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			NodeList nodeList = handler.getJSSPTags4ClassConfig("test4.Test4_0Tag");

			assertEquals(3, nodeList.getLength());
			assertTrue(nodeList.item(0) instanceof Element);
			assertEquals("TEST4_1TAG", nodeList.item(0).getTextContent());
			assertEquals("test4_1tag", ((Element) nodeList.item(0)).getTagName());
			assertTrue(nodeList.item(1) instanceof Element);
			assertEquals("TEST4_2TAG", nodeList.item(1).getTextContent());
			assertEquals("test4_2tag", ((Element) nodeList.item(1)).getTagName());
			assertTrue(nodeList.item(2) instanceof Element);
			assertEquals("TEST4_3TAG", nodeList.item(2).getTextContent());
			assertEquals("test4_3tag", ((Element) nodeList.item(2)).getTagName());

			nodeList = handler.getJSSPTags4ClassConfig("test4.Test4_1Tag");

			assertEquals(0, nodeList.getLength());

			NodeList templateNodeList1 = handler.getJSSPTags4ClassConfig("org.intra_mart.jssp.view.tag.test.Class1");
			assertEquals(0, templateNodeList1.getLength());

			NodeList templateNodeList2 = handler.getJSSPTags4ClassConfig("org.intra_mart.jssp.view.tag.test.Class6");
			assertEquals(2, templateNodeList2.getLength());
			assertTrue(templateNodeList2.item(0) instanceof Element);
			assertEquals("foo0", ((Element) templateNodeList2.item(0)).getTagName());
			assertEquals("foo_tag_0", templateNodeList2.item(0).getTextContent());
			assertTrue(templateNodeList2.item(1) instanceof Element);
			assertEquals("foo1", ((Element) templateNodeList2.item(1)).getTagName());
			assertEquals("foo_tag_1", templateNodeList2.item(1).getTextContent());
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetInitializer4Class1WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] initializer4Class = handler.getInitializer4Class();

			assertEquals(4, initializer4Class.length);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class1", initializer4Class[0]);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class2", initializer4Class[1]);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class3", initializer4Class[2]);
			assertEquals("test1.Test1Initializer", initializer4Class[3]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetInitializer4Class2WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST2 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] initializer4Class = handler.getInitializer4Class();

			assertEquals(4, initializer4Class.length);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class1", initializer4Class[0]);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class2", initializer4Class[1]);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class3", initializer4Class[2]);
			assertEquals("test2.Test2Initializer", initializer4Class[3]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetInitializer4Class3WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST3 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] initializer4Class = handler.getInitializer4Class();

			assertEquals(5, initializer4Class.length);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class1", initializer4Class[0]);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class2", initializer4Class[1]);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class3", initializer4Class[2]);
			assertEquals("test3.Test3Initializer1", initializer4Class[3]);
			assertEquals("test3.Test3Initializer2", initializer4Class[4]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetInitializer4Class4WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST4 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] initializer4Class = handler.getInitializer4Class();

			assertEquals(3, initializer4Class.length);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class1", initializer4Class[0]);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class2", initializer4Class[1]);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class3", initializer4Class[2]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetInitializer4Class5WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] initializer4Class = handler.getInitializer4Class();

			assertEquals(3, initializer4Class.length);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class1", initializer4Class[0]);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class2", initializer4Class[1]);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class3", initializer4Class[2]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetInitializer4ClassAllWithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1, TEST2, TEST3, TEST4, TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] initializer4Class = handler.getInitializer4Class();

			assertEquals(7, initializer4Class.length);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class1", initializer4Class[0]);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class2", initializer4Class[1]);
			assertEquals("org.intra_mart.jssp.script.initialize.test.Class3", initializer4Class[2]);
			assertEquals("test1.Test1Initializer", initializer4Class[3]);
			assertEquals("test2.Test2Initializer", initializer4Class[4]);
			assertEquals("test3.Test3Initializer1", initializer4Class[5]);
			assertEquals("test3.Test3Initializer2", initializer4Class[6]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetContextFactoryListeners1WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] contextFactoryListeners = handler.getContextFactoryListeners();

			assertEquals(4, contextFactoryListeners.length);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class1", contextFactoryListeners[0]);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class2", contextFactoryListeners[1]);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class3", contextFactoryListeners[2]);
			assertEquals("test1.Test1ContextFactoryListener", contextFactoryListeners[3]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetContextFactoryListeners2WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST2 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] contextFactoryListeners = handler.getContextFactoryListeners();

			assertEquals(4, contextFactoryListeners.length);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class1", contextFactoryListeners[0]);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class2", contextFactoryListeners[1]);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class3", contextFactoryListeners[2]);
			assertEquals("test2.Test2ContextFactoryListener", contextFactoryListeners[3]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetContextFactoryListeners3WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST3 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] contextFactoryListeners = handler.getContextFactoryListeners();

			assertEquals(5, contextFactoryListeners.length);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class1", contextFactoryListeners[0]);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class2", contextFactoryListeners[1]);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class3", contextFactoryListeners[2]);
			assertEquals("test3.Test3ContextFactoryListener1", contextFactoryListeners[3]);
			assertEquals("test3.Test3ContextFactoryListener2", contextFactoryListeners[4]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetContextFactoryListeners4WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST4 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] contextFactoryListeners = handler.getContextFactoryListeners();

			assertEquals(3, contextFactoryListeners.length);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class1", contextFactoryListeners[0]);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class2", contextFactoryListeners[1]);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class3", contextFactoryListeners[2]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetContextFactoryListeners5WithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] contextFactoryListeners = handler.getContextFactoryListeners();

			assertEquals(3, contextFactoryListeners.length);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class1", contextFactoryListeners[0]);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class2", contextFactoryListeners[1]);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class3", contextFactoryListeners[2]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void testGetContextFactoryListenersAllWithTemplate() {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[] { TEST1, TEST2, TEST3, TEST4, TEST5 }, oldClassLoader));
			JSSPPluginConfigHandler handler = null;
			try {
				handler = new JSSPPluginConfigHandler(TEMPLATE_CONFIG_FILE_LOCATION);
			} catch (ParserConfigurationException e) {
				fail("インスタンス生成に失敗.");
			} catch (SAXException e) {
				fail("インスタンス生成に失敗.");
			} catch (IOException e) {
				fail("インスタンス生成に失敗.");
			}

			String[] contextFactoryListeners = handler.getContextFactoryListeners();

			assertEquals(7, contextFactoryListeners.length);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class1", contextFactoryListeners[0]);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class2", contextFactoryListeners[1]);
			assertEquals("org.intra_mart.jssp.script.listener.test.Class3", contextFactoryListeners[2]);
			assertEquals("test1.Test1ContextFactoryListener", contextFactoryListeners[3]);
			assertEquals("test2.Test2ContextFactoryListener", contextFactoryListeners[4]);
			assertEquals("test3.Test3ContextFactoryListener1", contextFactoryListeners[5]);
			assertEquals("test3.Test3ContextFactoryListener2", contextFactoryListeners[6]);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

}
