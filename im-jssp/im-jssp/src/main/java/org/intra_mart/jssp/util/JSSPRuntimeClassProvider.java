package org.intra_mart.jssp.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


import org.intra_mart.common.aid.jdk.java.lang.StringUtil;
import org.intra_mart.common.aid.jdk.javax.xml.DOMBuilder;
import org.intra_mart.common.aid.jdk.javax.xml.DOMInfo;
import org.intra_mart.common.aid.jdk.javax.xml.XmlNode;
import org.xml.sax.SAXException;

/**
 * JSSPの実行環境で利用されるクラスを供給します
 */
public class JSSPRuntimeClassProvider {

	private static final String DEFAULT_SOURCE_PROVIDER_FILE = "/org/intra_mart/jssp/util/default-jssp-runtime-classes.xml";
	private static final String SOURCE_PROVIDER_FILE         = "/org/intra_mart/jssp/util/jssp-runtime-classes.xml";

	private static JSSPRuntimeClassProvider _instance = null;

	private Map<String, String> providers = new HashMap<String, String>();

	/**
	 * インスタンスを取得します。<BR>
	 * <BR>
	 * 
	 * @return インスタンス
	 */
	public static synchronized JSSPRuntimeClassProvider getInstance() {
		try {
			if (_instance == null) {
				_instance = new JSSPRuntimeClassProvider();
			}
		}
		catch (Exception e) {
			throw new IllegalStateException("JSSP Configuration Error", e);

		}

		return _instance;
	}

	/**
	 * コンストラクタ。<BR>
	 * <BR>
	 * 
	 * @throws SAXException
	 * @throws IOException
	 * 
	 */
	private JSSPRuntimeClassProvider() throws IOException, SAXException {
		super();

		// デフォルトの設定ファイル
		InputStream defaultIn = JSSPRuntimeClassProvider.class.getResourceAsStream(DEFAULT_SOURCE_PROVIDER_FILE);
		if (defaultIn != null) {
			DOMInfo domInfo = DOMBuilder.newInstance(defaultIn);
			XmlNode root = new XmlNode(domInfo.getRootNode());
			defaultIn.close();
			XmlNode[] nodes = root.getChildNode();
			for (int i = 0; i < nodes.length; i++) {
				providers.put(nodes[i].getNodeName(), nodes[i].getValue());
			}
		}

		// 通常の設定ファイル
		InputStream in = JSSPRuntimeClassProvider.class.getResourceAsStream(SOURCE_PROVIDER_FILE);
		if (in != null) {
			DOMInfo domInfo = DOMBuilder.newInstance(in);
			XmlNode root = new XmlNode(domInfo.getRootNode());
			in.close();
			XmlNode[] nodes = root.getChildNode();
			for (int i = 0; i < nodes.length; i++) {
				providers.put(nodes[i].getNodeName(), nodes[i].getValue());
			}
		}
	}

	/**
	 * JSSPの実行環境で利用されるクラスのインスタンスを取得します。 <br/>
	 * 
	 * @param key
	 *            識別キー
	 * @param ctorTypes
	 *            コンストラクタのパラメータタイプ
	 * @param ctorParams
	 *            コンストラクタのパラメータ
	 * @return 識別キーで指定されるクラスのインスタンス
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public Object getClassInstance(String key, Class[] ctorTypes, Object[] ctorParams) 
					throws InstantiationException,
							IllegalAccessException, 
							ClassNotFoundException, 
							SecurityException, 
							NoSuchMethodException,
							IllegalArgumentException, 
							InvocationTargetException {

		String className = getClassName(key);

		try {
			
			if (ctorTypes == null || ctorTypes.length == 0) {
				return Class.forName(className).newInstance();
			}
			else {
				Constructor ctor = Class.forName(className).getConstructor(ctorTypes);
				return ctor.newInstance(ctorParams);
			}
		}
		catch (ClassNotFoundException e) {
			throw new ClassNotFoundException("Class is not found: " + className);
		}

	}

	/**
	 * クラス名を取得します。<BR>
	 * <BR>
	 * 識別キーの該当するクラス名が存在しなかった場合は、空文字列を返却します。<BR>
	 * <br>
	 * 
	 * @param key
	 *            識別キー
	 * @return クラス名
	 */
	public String getClassName(String key) {
		String result = this.providers.get(key);
		if (result != null) {
			return result;
		}
		else {
			return StringUtil.EMPTY_STRING;
		}
	}

}
