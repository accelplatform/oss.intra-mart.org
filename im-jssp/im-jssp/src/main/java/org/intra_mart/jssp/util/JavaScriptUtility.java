package org.intra_mart.jssp.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import org.intra_mart.common.platform.log.Logger;
import org.intra_mart.jssp.exception.IllegalConversionException;
import org.intra_mart.jssp.page.JSSPQuery;
import org.intra_mart.jssp.page.JSSPQueryManager;
import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilder;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilderManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.WrappedException;
import org.mozilla.javascript.Wrapper;

/**
 * JavaScriptとJava間のオブジェクト変換や関数実行などを行なうユーティリティクラスです。
 */
public class JavaScriptUtility {

	/** 文字コード変換を行わずに、バイト配列を扱う為の文字コード */
	public static final String NON_CONVERT_CHARSET = "ISO-8859-1";
	
	private static Logger _logger = Logger.getLogger();
	
	/** ユーティリティクラスにつき、コンストラクタなし。 */
	private JavaScriptUtility(){}
	
	/**
	 * JavaScript関数を実行します。<br/>
	 * 指定されたページのファンクションコンテナ内に定義されている関数を実行します。<br/>
	 * このメソッドは、返却値の無い関数を実行する場合に利用します。<br/>
	 * <br/>
	 * このメソッドを呼び出すことは、
	 * {@link #executeFunction(String, String, Class, Object...)
	 * <tt>executeFunction(pagePath, functionName, void.class, args)</tt>}
	 * を呼び出すことに相当します。
	 * 
	 * @param pagePath　ページパス（拡張子なし）
	 * @param functionName　実行関数名
	 * @param args　関数の引数
	 * @throws IllegalConversionException　Java形式からJavaScript形式への変換に失敗した場合。
	 * @throws Exception JavaScript関数の実行時にエラーが発生した場合。
	 * 
	 * @see #executeFunction(String, String, Class, Object...)
	 */
	public static void executeVoidFunction(final String pagePath, 
										   final String functionName, 
										   final Object... args)
									throws IllegalConversionException, Exception {
		executeFunction(pagePath, functionName, void.class, args);
	}
	
	/**
	 * JavaScript関数を実行します。<br/>
	 * 指定されたページのファンクションコンテナ内に定義されている関数を実行します。<br/>
	 * <br/>
	 * このメソッドを利用することで、関数のパラメータがJava形式からJavaScript形式へ自動的に変換されます。<br/>
	 * 同様に、関数の実行結果も、JavaScript形式からJava形式へ自動的に変換されます。<br/>
	 * <br/>
	 * Java形式 から JavaScript形式への変換は、{@link #javaBeanToJS(Object)}を利用しています。<br/>
	 * JavaScript形式 から Java形式への変換は、{@link #jsToJavaBean(Object, Class)}を利用しています。<br/>
	 * <br/>
	 * 型変換の対応表は、{@link #javaBeanToJS(Object)}、および、
	 * {@link #jsToJavaBean(Object, Class)}の説明を参照してください。
	 * 
	 * 変換後のJavaクラスを配列で指定する場合、
	 * 例えば、Memberクラスの配列を変換後のクラスに指定する場合は以下のようになります。
	 * <pre>
	 * (Member[]) JavaScriptUtility.executeFunction(pagePath, functionName, <b><u>Member[].class</u></b>, args);
	 * </pre>
	 * 
	 * @param pagePath　ページパス（拡張子なし）
	 * @param functionName　実行関数名
	 * @param returnType 関数返却値の変換後のJavaクラス
	 * @param args　関数の引数
	 * @return 関数の実行結果（引数 returnType で指定されたクラスに変換されています）
	 * @throws IllegalConversionException　Java形式 から JavaScript形式への変換、または、
	 * 									  JavaScript形式 から Java形式への変換に失敗した場合。
	 * @throws Exception JavaScript関数の実行時にエラーが発生した場合。
	 */
	public static Object executeFunction(final String pagePath, 
										 final String functionName, 
										 final Class<?> returnType,
										 final Object... args)
									throws IllegalConversionException, Exception {

		// =====================
		// JavaからJSへ変換
		// =====================
		Object[] args4JS = null;
		try {
			args4JS = new Object[args.length];
			for(int idx = 0; idx < args.length; idx++){
				args4JS[idx] = javaBeanToJS(args[idx]);
			}
		}
		catch (Exception e) {
			throw new IllegalConversionException(e);
		}
		
		// JSSPQueryの設定
		JSSPQuery currentJsspQuery = JSSPQueryManager.currentJSSPQuery();
		JSSPQuery newJsspQuery = (currentJsspQuery != null) ? 
									JSSPQueryManager.createJSSPQuery(pagePath, currentJsspQuery.getFromPagePath()) : 
									JSSPQueryManager.createJSSPQuery(pagePath);
		JSSPQueryManager.entry(newJsspQuery);

		// =====================
		// JS実行
		// =====================
		Object jsResult = null;
		try{
			Context cx = Context.enter();	// JS実行環境コンテキストの作成
			
			ScriptableObject parentScope = 
				new ScriptScope(Thread.currentThread().getName().concat(String.valueOf(System.currentTimeMillis())));		
			
			// スコープ生成
			ScriptScopeBuilder builder = ScriptScopeBuilderManager.getBuilder();
			ScriptScope scriptScope = builder.getScriptScope(pagePath);

			// 実行
			jsResult = scriptScope.call(cx, functionName, args4JS, parentScope);
		}
		catch (WrappedException we) {
			Throwable t = we.getWrappedException();
			if(t instanceof Error){
				throw (Error) t;
			}
			else{
				throw (Exception) t;
			}
		}
		catch(Exception e){
			throw e;
		}
		finally{
			JSSPQueryManager.entry(currentJsspQuery);
			Context.exit();
		}

		// =====================
		// JSからJavaへ変換
		// =====================
		try {
			if(returnType == null || returnType.equals(void.class)){
				return null;
			}
			else{
				Object ret = jsToJavaBean(jsResult, returnType);
				return ret;
			}
		}
		catch (IllegalConversionException e) {
			throw e;
		}
		catch (Exception e) {
			throw new IllegalConversionException(e);
		}
	}

	
	// PLAN　Map対応（ValueObjectにて対応＝１階層か？）
	// PLAN　List対応
	private static Logger _logger4javaBeanToJS = Logger.getLogger(JavaScriptUtility.class.getName() + ".javaBeanToJS");
	
	/**
	 * JavaオブジェクトをJavaScriptオブジェクトに変換します。<br/>
	 * <br/>
	 * Javaのインスタンスを<a href="#MappingTable4JavaBeanToJS">対応表</a>に基づいて
	 * JavaScriptオブジェクトに変換します。<br/>
	 * <br/>
	 * 引数に<a href="http://java.sun.com/j2se/1.5.0/ja/docs/ja/guide/beans/index.html">JavaBean</a>
	 * 形式のインスタンスが渡された場合、そのプロパティを<a href="#MappingTable4JavaBeanToJS">対応表</a>に基づいて変換します。<br/>
	 * （Publicフィールドも変換されます。その際のJavaScriptプロパティ名は、Publicフィールド名です。）<br/>
	 * プロパティの値がJavaBean形式のインスタンスである場合、さらにそのプロパティの変換を試みます。<br/>
	 * これを繰りかえし、最終的に、<b>null</b>, <b>String</b>, <b>Number</b>,
	 * <b>Boolean</b>, <b>Array</b> で構成されるJavaScriptオブジェクトに変換します。<br/>
	 * <br/>
	 * <a name="MappingTable4JavaBeanToJS">
	 * <h3>型変換対応表（Java→JavaScript）</h3>
	 * <table border="1">
	 *  <tr>
	 *  	<th>Java</th>
	 *  	<th>&nbsp;</th>
	 *  	<th>JavaScript</th>
	 *  </tr>
	 *  <tr>
	 *  	<td>null</td>
	 *  	<td>→</td>
	 *  	<td>null</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>java.lang.String</td>
	 *  	<td>→</td>
	 *  	<td>String</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>java.lang.Number</td>
	 *  	<td>→</td>
	 *  	<td>Number</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>java.lang.Boolean</td>
	 *  	<td>→</td>
	 *  	<td>Boolean</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>java.util.Date</td>
	 *  	<td rowspan="2">→</td>
	 *  	<td rowspan="2">Date</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>java.util.Calendar</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>byte[]</td>
	 *  	<td rowspan="2">→</td>
	 *  	<td rowspan="2">String （バイナリ）</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>javax.activation.DataHandler</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>配列</td>
	 *  	<td>→</td>
	 *  	<td>Array</td>
	 *  </tr>
	 * </table>
	 * <br/>
	 * 
	 * 上記以外のクラスは変換対象外です。（例えば、List, Map, Setを変換することはできません。）<br/>
	 * 変換対象外のインスタンスが指定された場合は、プロパティを一つも持たないJavaScriptオブジェクトが返却されます。<br/>
	 * <br/>
	 * 引数にJavaBean形式のインスタンスが渡された場合、GetterとSetterが両方存在するプロパティを変換します。<br/>
	 * GetterとSetterのどちらか一方しかないプロパティは変換されません。<br/>
	 * また、プロパティの存在しないJavaBeanが渡された場合は、プロパティを一つも持たないJavaScriptオブジェクトを返却します。<br/>
	 * 同様に、GetterとSetterのどちらか一方しかないプロパティだけで構成されるJavaBeanの場合も、
	 * プロパティを一つも持たないJavaScriptオブジェクトを返却します。<br/>
	 * <br/>
	 * 
	 * @param bean 変換対象のJavaオブジェクト
	 * @return　変換後のJavaScriptオブジェクト
	 * 
	 * @throws IntrospectionException　イントロスペクション中に例外が発生した場合
	 * @throws IllegalAccessException　引数に渡されたJavaBeanのGetterにアクセスできない場合
	 * @throws InvocationTargetException　引数に渡されたJavaBeanのGetterが例外をスローする場合
	 * @throws IOException バイナリデータの変換に失敗した場合
	 */
	public static Object javaBeanToJS(final Object bean)
							throws IntrospectionException, IOException,
								   IllegalAccessException, InvocationTargetException {
	
		_logger4javaBeanToJS.trace("bean: {}", bean);
		
		// NOTE プリミティブ型は受け取りません。
		// ∵本メソッドの引数はObject型で定義されているので、
		// 　プリミティブ型の値を渡してもAutoBoxingによりラッパークラスに変換されます。

		// null
		if(bean == null){
			_logger4javaBeanToJS.trace("========== null ==========");
			return bean;
		}

		Class<?> targetType = bean.getClass();
		_logger4javaBeanToJS.trace("targetType: {}", targetType);

		// 文字列
		if (bean instanceof String) {
			_logger4javaBeanToJS.trace("========== String ==========");
			return bean;
		}
		else if (bean instanceof Character) {
			_logger4javaBeanToJS.trace("========== Character ==========");
			return ((Character)bean).toString();
		}
	
		// 数値
		else if (bean instanceof Number) {
			_logger4javaBeanToJS.trace("========== Number ==========");
			double doubleValue = ((Number)bean).doubleValue();
			return new Double(doubleValue);
		}

		// 真偽値
		else if (bean instanceof Boolean) {
			_logger4javaBeanToJS.trace("========== Boolean ==========");
			return bean;
		}

		// 日付
		else if (bean instanceof Date) {
			_logger4javaBeanToJS.trace("========== Date ==========");
			Date date = (Date) bean;
			
			Scriptable nativeDate = RuntimeObject.newDate(date.getTime());
			return nativeDate;
		}
		else if (bean instanceof Calendar) {
			_logger4javaBeanToJS.trace("========== Calendar ==========");
			Calendar cal = (Calendar) bean;
			Date date = cal.getTime();
			_logger4javaBeanToJS.trace("date: {}", date);
			
			Scriptable nativeDate = RuntimeObject.newDate(date.getTime());
			return nativeDate;
		}

		// バイナリ （バイト配列）
		else if(targetType.equals(byte[].class)){
			_logger4javaBeanToJS.trace("========== byte[] ==========");
			return new String((byte[])bean, NON_CONVERT_CHARSET);
		}
		
		// バイナリ (DataHandler)
		else if(bean instanceof javax.activation.DataHandler){
			_logger4javaBeanToJS.trace("========== javax.activation.DataHandler ==========");
			
			javax.activation.DataHandler dataHandler = (javax.activation.DataHandler) bean;

			InputStream in = dataHandler.getInputStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			try{
				byte[] buf = new byte[1024];
				
				while(true){
					int len = in.read(buf);
					
					if(len != -1) {
						out.write(buf, 0, len);
					}
					else {
						break;
					}
				}

				return out.toString(NON_CONVERT_CHARSET);
			}
			finally {
				if(in != null)  in.close();
				if(out != null) out.close();
			}
		}
		
		// 配列
		else if(targetType.isArray()){
			Class<?> componentType = targetType.getComponentType();
			_logger4javaBeanToJS.trace("========== Array ==========");
			_logger4javaBeanToJS.trace("componentType: {}", componentType);

			int length = Array.getLength(bean);
			Object[] list = new Object[length];
			
			_logger4javaBeanToJS.trace("---- Array (Start) ----");
			for(int idx = 0; idx < length; idx++){
				Object value4Java = Array.get(bean, idx);
				_logger4javaBeanToJS.trace("value4Java[{}]: {}", idx, value4Java);

				// ********* 再帰 *********
				list[idx] = javaBeanToJS(value4Java);
			}
			_logger4javaBeanToJS.trace("---- Array (Start) ----");
			
			NativeArray nativeArray = RuntimeObject.newArrayInstance(list);

			// Javaのクラス名を格納（隠しプロパティ）
			setProperty4imJavaClassName(nativeArray, "", componentType);
			return nativeArray;
		}
		
		// java.lang.Object
		else if(targetType.equals(java.lang.Object.class)){
			_logger4javaBeanToJS.trace("========== java.lang.Object ==========");
			return bean;
		}

		// 上記以外は、JavaBeanとして扱う
		else{
			_logger4javaBeanToJS.trace("========== JavaBean ==========");
			ScriptableObject jsObject = new ValueObject();

			// Publicフィールド
			Map<String, Field> pubFieldMap = getPublicFieldMap(targetType);
			if(pubFieldMap != null){
				Iterator<Map.Entry<String, Field>> it = pubFieldMap.entrySet().iterator();

				_logger4javaBeanToJS.trace("---- PublicField (Start) ----");
				while(it.hasNext()){
					Map.Entry<String, Field> entry = it.next();
					
					String propName = entry.getKey();
					Field field     = entry.getValue();
					
					_logger4javaBeanToJS.trace("--------");
					_logger4javaBeanToJS.trace("propName: {}", propName);
					_logger4javaBeanToJS.trace("field: {}", field);
					_logger4javaBeanToJS.trace("----");
					
					// ********* 再帰 *********
					Object value4Java = field.get(bean);
					_logger4javaBeanToJS.trace("value4Java: {}", value4Java);
					_logger4javaBeanToJS.trace("--------");
					
					Object obj = javaBeanToJS(value4Java);
					
					// JSオブジェクト に プロパティ を 設定
					ScriptableObject.putProperty(jsObject, propName, obj);
					
					// Javaのクラス名を格納（隠しプロパティ）
					setProperty4imJavaClassName(jsObject, propName, field.getType());
				}
				_logger4javaBeanToJS.trace("---- PublicField (End) ----");
				
			}
			
			// JavaBeans
			Map<String, PropertyDescriptor> beanPropMap = getBeanPropertyMap(targetType);
			if(beanPropMap != null){
				Iterator<Map.Entry<String, PropertyDescriptor>> it = beanPropMap.entrySet().iterator();
				
				_logger4javaBeanToJS.trace("---- JavaBeans (Start) ----");
				while(it.hasNext()){
					Map.Entry<String, PropertyDescriptor> entry = it.next();
					PropertyDescriptor propDescriptor = entry.getValue();
					
					String propName = propDescriptor.getName();
					Method readMethod = propDescriptor.getReadMethod();

					_logger4javaBeanToJS.trace("--------");
					_logger4javaBeanToJS.trace("propName: {}", propName);
					_logger4javaBeanToJS.trace("readMethod", readMethod);
					_logger4javaBeanToJS.trace("----");
					
					// ********* 再帰 *********
					Object value4Java = readMethod.invoke(bean, new Object[0]); 
					_logger4javaBeanToJS.trace("value4Java: {}", value4Java);
					_logger4javaBeanToJS.trace("--------");

					Object obj = javaBeanToJS(value4Java);
					
					// JSオブジェクト に プロパティ を 設定
					ScriptableObject.putProperty(jsObject, propName, obj);
					
					// Javaのクラス名を格納（隠しプロパティ）
					setProperty4imJavaClassName(jsObject, propName, readMethod.getReturnType());
				}
				_logger4javaBeanToJS.trace("---- JavaBeans (End) ----");
			}
			
			// Javaのクラス名を格納（隠しプロパティ）
			setProperty4imJavaClassName(jsObject, "", targetType);
			return jsObject;
		}

	}

	/** ImJson.toJSONString()で表示されるJavaクラス名の形式 */
	private static String _imJavaClassNameStyle 
		= System.getProperty(JavaScriptUtility.class.getName() + ".MethodName4imJavaClassNameProperty",
							 "getSimpleName");

	/**
	 * @param jsObject
	 * @param propName
	 * @param targetType
	 */
	private static void setProperty4imJavaClassName(final ScriptableObject jsObject, 
													final String propName, 
													final Class<?> targetType) {
		Object typeName = Undefined.instance;
		
		if("getSimpleName".equals(_imJavaClassNameStyle)){
			typeName = targetType.getSimpleName();
		}
		else if("getName".equals(_imJavaClassNameStyle)){
			typeName = targetType.getName();
		}
		else if("getCanonicalName".equals(_imJavaClassNameStyle)){
			typeName = targetType.getCanonicalName();
		}
		
		jsObject.defineProperty("__javaClassName_" + propName + "__", typeName, ScriptableObject.DONTENUM);
	}
	
	/**
	 * JavaScriptオブジェクトをJavaオブジェクトに変換します。<br/>
	 * <br/>
	 * このメソッドを呼び出すことは、
	 * {@link #jsToJavaBean(Object, String, ClassLoader)
	 * <tt>jsToJavaBean(jsObject, beanClassName, java.lang.Thread.currentThread().getContextClassLoader())</tt>}
	 * を呼び出すことに相当します。
	 * 
	 * @param jsObject　変換対象のJavaScriptオブジェクト
	 * @param beanClassName 変換後のJavaクラス名
	 * @return 変換後のJavaオブジェクト
	 * 
	 * @throws IllegalConversionException 変換処理に失敗した場合
	 * @throws InstantiationException　指定された変換後のJavaクラスが、抽象クラス、インタフェース、配列クラス、プリミティブ型、または void を表す場合。または、指定された変換後のJavaクラスが無引数コンストラクタを保持しない場合、あるいはインスタンスの生成がほかの理由で失敗した場合
	 * @throws IllegalAccessException　指定された変換後のJavaクラス、またはその無引数コンストラクタにアクセスできない場合。または、JavaBeanプロパティのSetterメソッドにアクセスできない場合。
	 * @throws IntrospectionException　イントロスペクション中に例外が発生した場合
	 * @throws ClassNotFoundException 指定された変換後のJavaクラスが見つからなかった場合
	 * @throws IOException バイナリデータの変換に失敗した場合
	 * 
	 * @see #jsToJavaBean(Object, String, ClassLoader)
	 * @see #jsToJavaBean(Object, Class)
	 */
	public static Object jsToJavaBean(final Object jsObject, final String beanClassName) 
							throws IllegalConversionException, IOException,
								   InstantiationException, IllegalAccessException, 
								   IntrospectionException, ClassNotFoundException {
		return jsToJavaBean(jsObject, beanClassName, Thread.currentThread().getContextClassLoader());
	}

	/**
	 * JavaScriptオブジェクトをJavaオブジェクトに変換します。<br/>
	 * <br/>
	 * このメソッドを呼び出すことは、
	 * {@link #jsToJavaBean(Object, Class)
	 * <tt>jsToJavaBean(jsObject, classLoader.loadClass(beanClassName))</tt>}
	 * を呼び出すことに相当します。
	 * 
	 * @param jsObject 　変換対象のJavaScriptオブジェクト
	 * @param beanClassName 変換後のJavaクラス名
	 * @param classLoader 変換後のJavaクラスをロードする為のクラスローダ
	 * @return 変換後のJavaオブジェクト
	 * 
	 * @throws IllegalConversionException 変換処理に失敗した場合
	 * @throws InstantiationException　指定された変換後のJavaクラスが、抽象クラス、インタフェース、配列クラス、プリミティブ型、または void を表す場合。または、指定された変換後のJavaクラスが無引数コンストラクタを保持しない場合、あるいはインスタンスの生成がほかの理由で失敗した場合
	 * @throws IllegalAccessException　指定された変換後のJavaクラス、またはその無引数コンストラクタにアクセスできない場合。または、JavaBeanプロパティのSetterメソッドにアクセスできない場合。
	 * @throws IntrospectionException　イントロスペクション中に例外が発生した場合
	 * @throws ClassNotFoundException 指定された変換後のJavaクラスが見つからなかった場合
	 * @throws IOException バイナリデータの変換に失敗した場合
	 * 
	 * @see #jsToJavaBean(Object, Class)
	 */
	public static Object jsToJavaBean(final Object jsObject, 
									  final String beanClassName, 
									  final ClassLoader classLoader) 
							throws IllegalConversionException, IOException,
								   InstantiationException, IllegalAccessException, 
								   IntrospectionException, ClassNotFoundException {
		if(jsObject == null){
			return null;
		}
		
		Object wapper = checkPrimitiveAndWrapper(jsObject, beanClassName);
		if(wapper != null){
			return wapper;
		}
		else{
			Class<?> clazz = classLoader.loadClass(beanClassName);
			return jsToJavaBean(jsObject, clazz);
		}
	}

	private static Logger _logger4jsToJavaBean = Logger.getLogger(JavaScriptUtility.class.getName() + ".jsToJavaBean");
	
	/**
	 * JavaScriptオブジェクトをJavaオブジェクトに変換します。<br/>
	 * <br/>
	 * JavaScriptオブジェクトを、<a href="#MappingTable4JSToJavaBean">対応表</a>に基づいて
	 * 指定された変換後のJavaクラス(=引数「beanType」)のインスタンスに変換します。<br/>
	 * <br/>
	 * 変換後のJavaクラスに<a href="http://java.sun.com/j2se/1.5.0/ja/docs/ja/guide/beans/index.html">JavaBean</a>
	 * 形式のクラスを指定した場合、<br/>
	 * JavaBeanのインスタンスを新たに生成後、<a href="#MappingTable4JSToJavaBean">対応表</a>に基づいて
	 * JavaScriptオブジェクトのプロパティを、JavaBeanの同名プロパティに設定します。<br/>
	 * （JavaBeanの同名プロパティが無い場合、JavaScriptオブジェクトのプロパティは変換されません）<br/>
	 * <br/>
	 * 同様に、変換後のJavaクラス内にPublicフィールドが定義されている場合、
	 * JavaScriptオブジェクトのプロパティを、同名のPublicフィールドに設定します。<br/>
	 * （同名のPublicフィールドがない場合、JavaScriptオブジェクトのプロパティは変換されません）<br/>
	 * <br/>
	 * JavaBeanの同名プロパティの型がJavaBean形式クラスの場合、さらにそのプロパティの変換を試みます。<br/>
	 * これを繰り返し、最終的にラッパークラス、または、JavaBean形式のインスタンスに変換します。<br/>
	 * <br/>
	 * <a name="MappingTable4JSToJavaBean">
	 * <h3>型変換対応表（JavaScript→Java）</h3>
	 * <table border="1">
	 *  <tr>
	 *  	<th>JavaScript</th>
	 *  	<th>&nbsp;</th>
	 *  	<th>Java</th>
	 *  	<th>備考</th>
	 *  </tr>
	 *  <tr>
	 *  	<td>null</td>
	 *  	<td rowspan="2">→</td>
	 *  	<td rowspan="2">null</td>
	 *  	<td rowspan="2" align="center"> - </td>
	 *  </tr>
	 *  <tr>
	 *  	<td>undefined</td>
	 *  </tr>
	 *  <tr>
	 *  	<td rowspan="2">String</td>
	 *  	<td rowspan="2">→</td>
	 *  	<td>java.lang.String</td>
	 *  	<td align="center"> - </td>
	 *  </tr>
	 *  <tr>
	 *  	<td>java.lang.Character</td>
	 *  	<td>先頭１文字をCharacterに変換します。</td>
	 *  </tr>
	 *  <tr>
	 *  	<td rowspan="2">String （バイナリ）</td>
	 *  	<td rowspan="2">→</td>
	 *  	<td>byte[]</td>
	 *  	<td rowspan="2">
	 *  		「String （バイナリ）」とは、<br/>
	 *  		JavaScriptAPIの「Fileオブジェクト」や「VirtualFileオブジェクト」の「load()関数」などで<br/>
	 *  		取得できるファイルデータ（バイナリ）の事を意味します。<br/>
	 *  	</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>javax.activation.DataHandler</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>Number</td>
	 *  	<td>→</td>
	 *  	<td>java.lang.Number</td>
	 *  	<td>
	 *  		java.lang.Numberのサブクラスが指定された場合は、<br/>その型に変換されます。<br/>
	 *  		変換可能なjava.lang.Numberのサブクラスは以下の6クラスです。
	 *  		<ul>
	 *  			<li>Double</li>
	 *  			<li>Float</li>
	 *  			<li>Long</li>
	 *  			<li>Integer</li>
	 *  			<li>Short</li>
	 *  			<li>Byte</li>
	 *  		</ul>
	 *  	</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>Boolean</td>
	 *  	<td>→</td>
	 *  	<td>java.lang.Boolean</td>
	 *  	<td align="center"> - </td>
	 *  </tr>
	 *  <tr>
	 *  	<td rowspan="2">Date</td>
	 *  	<td rowspan="2">→</td>
	 *  	<td>java.util.Date</td>
	 *  	<td rowspan="2" align="center"> - </td>
	 *  </tr>
	 *  <tr>
	 *  	<td>java.util.Calendar</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>Array</td>
	 *  	<td>→</td>
	 *  	<td>配列</td>
	 *  	<td>
	 *  		JavaScriptのArray要素の型がすべて同じであり、<br/>
	 *  		かつ、その型がJavaの配列要素の型に変換可能でなければなりません。
	 *  	</td>
	 *  </tr>
	 *  <tr>
	 *  	<td rowspan="3">任意のJavaScriptオブジェクト</td>
	 *  	<td rowspan="3">→</td>
	 *  	<td>java.lang.String</td>
	 *  	<td rowspan="3">
	 *  		JavaScriptオブジェクトの文字列表現が変換可能な場合に限る。
	 *  	</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>java.lang.Number</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>java.lang.Boolean</td>
	 *  </tr>
	 * </table>
	 * <br/>
	 * 上記以外は変換対象外です。（List, Map, Setには変換できません）<br/>
	 * 変換対象外のクラスが指定された場合は、 JavaScriptオブジェクトのプロパティは変換されません。<br/>
	 * <br/>
	 * なお、このメソッドは、GetterとSetterが両方存在するJavaBeanのプロパティに対して、
	 * JavaScriptオブジェクトのプロパティをマッピングします。<br/>
	 * GetterとSetterのどちらか一方しかないJavaBeanのプロパティへのマッピングはおこないません。<br/>
	 * また、プロパティの存在しないJavaBean形式のクラスを指定した場合は、JavaScriptオブジェクトのプロパティは変換されません。<br/>
	 * 同様に、GetterとSetterのどちらか一方しかないプロパティだけで構成されるJavaBeanの場合も、
	 * JavaScriptオブジェクトのプロパティは変換されません。<br/>
	 *  
	 * @param jsObject 変換対象のJavaScriptオブジェクト
	 * @param beanType 変換後のJavaクラス
	 * @return 変換後のJavaオブジェクト
	 * 
	 * @throws IllegalConversionException 変換処理に失敗した場合
	 * @throws InstantiationException　指定された変換後のJavaクラスが、抽象クラス、インタフェース、配列クラス、プリミティブ型、または void を表す場合。または、指定された変換後のJavaクラスが無引数コンストラクタを保持しない場合、あるいはインスタンスの生成がほかの理由で失敗した場合
	 * @throws IllegalAccessException　指定された変換後のJavaクラス、またはその無引数コンストラクタにアクセスできない場合。または、JavaBeanプロパティのSetterメソッドにアクセスできない場合。
	 * @throws IntrospectionException　イントロスペクション中に例外が発生した場合
	 * @throws IOException バイナリデータの変換に失敗した場合
	 */
	public static Object jsToJavaBean(final Object jsObject, final Class<?> beanType)
							throws IllegalConversionException, IOException,
								   InstantiationException, IllegalAccessException, 
								   IntrospectionException {
		
		_logger4jsToJavaBean.trace("*************** jsToJavaBean() ******************");
		_logger4jsToJavaBean.trace("jsObject: {}", jsObject);
		_logger4jsToJavaBean.trace("beanType: {}", beanType);

		if(jsObject == null){
			return null;
		}
		
		Object wapper = checkPrimitiveAndWrapper(jsObject, beanType.getName());
		if(wapper != null){
			_logger4jsToJavaBean.trace("========== null ==========");
			return wapper;
		}

		// バイナリ （バイト配列）
		if(jsObject instanceof String && beanType.equals(byte[].class)){
			_logger4jsToJavaBean.trace("========== byte[] ==========");

			try {
				return jsObject.toString().getBytes(NON_CONVERT_CHARSET);
			}
			catch (UnsupportedEncodingException e) {
				throw new IllegalStateException(e);
			}
		}

		// バイナリ (DataHandler)
		else if(jsObject instanceof String && beanType.equals(javax.activation.DataHandler.class)){
			_logger4jsToJavaBean.trace("========== javax.activation.DataHandler ==========");

			byte[] bytes = jsObject.toString().getBytes(NON_CONVERT_CHARSET);
			DataSource ds = new ByteArrayDataSource(bytes, "application/octet-stream");
			DataHandler dh = new DataHandler(ds);
			_logger4jsToJavaBean.debug("Content-Type: {}", dh.getContentType());
			
			return dh;
		}
				
		// そのまま返却する場合
		else if (jsObject instanceof String
			||
			// NaN含む  ScriptRuntime → public static final Double NaNobj = new Double(NaN);
			jsObject instanceof Number 
			||
			jsObject instanceof Boolean
			||
			beanType.equals(jsObject.getClass())) {

			_logger4jsToJavaBean.trace("========== String || Number || Boolean || beanType ==========");
			return jsObject;
		}
				
		// Date型
		else if (JsUtil.isDate(jsObject)) {
			_logger4jsToJavaBean.trace("========== NativeDate ==========");

			Date date = (Date) Context.jsToJava(jsObject, Date.class);
			_logger4jsToJavaBean.trace("jsObject[Date]: {}", date);
			
			if(beanType.equals(Calendar.class)){
				_logger4jsToJavaBean.trace("----- Calendar -----");
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(date.getTime());
				return cal;
			}
			else{
				_logger4jsToJavaBean.trace("----- Date -----");
				return date;
			}
		}

		// Array型
		else if(jsObject instanceof NativeArray){
			_logger4jsToJavaBean.trace("========== NativeArray ==========");
			
			// 配列のコンポーネント型を表す Class
			Class<?> componentType = beanType.getComponentType();
			_logger4jsToJavaBean.trace("componentType: {}", componentType);

			NativeArray jsArray = (NativeArray) jsObject;
			int maxLength = (int)jsArray.getLength();
			
			Object returnObj;
			try {
				returnObj = Array.newInstance(componentType, maxLength);
			}
			catch (Exception e) {
				String beanTypeName = beanType.getName();
				String msg = "Cannot convert 'JavaScript NativeArray' into 'Java class <" + beanTypeName + ">'. ";
				       msg += "(<" + beanTypeName + "> may not be declared as an array)";
				
				throw new IllegalConversionException(msg, e);
			}

			_logger4jsToJavaBean.trace("---- NativeArray (Start) ----");
			for(int idx = 0; idx < maxLength; idx++){
				Object propValue = jsArray.get(idx, jsArray);

				// ********* 再帰 *********
				Object javaValue = jsToJavaBean(propValue, componentType);
				_logger4jsToJavaBean.trace("javaValue[{}]: {}", idx, javaValue);
				
				// オブジェクト配列に値を設定
				try{
					Array.set(returnObj, idx, javaValue);
				}
				catch(Exception e){
					String msg = "Cannot convert '" + propValue + "' into <" + componentType.getName() + ">." + 
								 " (beanType: " + beanType.getName() + ", index: "+ idx +")"; 
					throw new IllegalConversionException(msg, e);
				}
					
			}
			_logger4jsToJavaBean.trace("---- NativeArray (End) ----");
			
			return returnObj;
		}

		// Wrapper型
		else if(jsObject instanceof Wrapper){
			_logger4jsToJavaBean.trace("========== Wrapper ==========");

			Wrapper wrapper = (Wrapper)jsObject;
			Object wrappedValue = wrapper.unwrap();
			
			if(beanType.isInstance(wrappedValue)){
				_logger4jsToJavaBean.trace("jsObject[Wrapper]: {}", wrappedValue);
				return wrappedValue;
			}
			else{
				_logger4jsToJavaBean.trace("jsObject[Wrapper]: null");
				return null;
			}
		}
		
		// Object型
		else if(jsObject instanceof Scriptable){
			_logger4jsToJavaBean.trace("========== Scriptable ==========");

			Object returnObj = beanType.newInstance();

			Scriptable scriptable = (Scriptable) jsObject;
			Object[] ids = scriptable.getIds();
			
			_logger4jsToJavaBean.trace("---- Scriptable (Start) ----");
			for(Object id : ids){
				if(id instanceof String){
					String propName = (String)id;
					_logger4jsToJavaBean.trace("propName: {}", propName);

					// JSオブジェクトよりプロパティ値を取得
					Object propValue = scriptable.get(propName, scriptable);
					
					// Publicフィールド
					Map<String, Field> pubFieldMap = getPublicFieldMap(beanType);
					if(pubFieldMap != null){
						_logger4jsToJavaBean.trace("---- PublicField ----");
						Field publicField = pubFieldMap.get(propName);
						
						if(publicField == null){
							// プロパティ名がPascal形式の場合(.Net対応)
							String propName4LowerCamel = conv2FirstCharLowerCase(propName);
							publicField = pubFieldMap.get(propName4LowerCamel);

							_logger4jsToJavaBean.trace("propName4LowerCamel: {}", propName4LowerCamel);
						}
						
						if(publicField != null){
							Class<?> propType = publicField.getType();
	
							// ********* 再帰 *********
							Object javaValue = jsToJavaBean(propValue, propType);
							_logger4jsToJavaBean.trace("javaValue: {}", javaValue);
		
							try{
								// JavaBeanに値を設定(javaValueが)
								publicField.set(returnObj, javaValue);
								continue;
							}
							catch(Exception e){
								String msg = "Cannot convert '" + propValue + "' into <" + propType.getName() + ">." + 
											 " (beanType: " + beanType.getName() + ", property: "+ propName +")"; 
								throw new IllegalConversionException(msg, e);
							}
						}
					}
					
					// JavaBeanのプロパティ
					Map<String, PropertyDescriptor> beanPropMap = getBeanPropertyMap(beanType);
					if(beanPropMap != null){
						_logger4jsToJavaBean.trace("---- JavaBean ----");
						PropertyDescriptor propDescriptor = beanPropMap.get(propName);
						
						if(propDescriptor == null){
							// プロパティ名がPascal形式の場合(.Net対応)
							String propName4LowerCamel = conv2FirstCharLowerCase(propName);
							_logger4jsToJavaBean.trace("propName4LowerCamel: {}", propName4LowerCamel);

							propDescriptor = beanPropMap.get(propName4LowerCamel);
							if(propDescriptor == null){
								// JSには存在するが、Beanには存在しないプロパティの場合
								_logger4jsToJavaBean.trace("Property '{}': Exist on JS. But, Not exist on JavaBean.", propName4LowerCamel);
								continue;
							}
						}
						
						Method writeMethod = propDescriptor.getWriteMethod();
	
						// WriteMethodのパラメータが複数あった場合は考慮しない。
						// ちなみに、パラメータが0個ということはありえない（∵Setterだから）
						Class<?>[] parameterTypes = writeMethod.getParameterTypes();
						_logger4jsToJavaBean.trace("parameterTypes: {}", (Object[])parameterTypes);
						
						Class<?> propType = parameterTypes[0];
						_logger4jsToJavaBean.trace("propType: {}", propType);
						
						// ********* 再帰 *********
						Object javaValue = jsToJavaBean(propValue, propType);
						_logger4jsToJavaBean.trace("javaValue: {}", javaValue);

						try{
							// JavaBeanに値を設定(javaValueが)
							Object[] args = { javaValue };
							writeMethod.invoke(returnObj, args);
						}
						catch(Exception e){
							String msg = "Cannot convert '" + propValue + "' into <" + propType.getName() + ">." + 
										 " (beanType: " + beanType.getName() + ", property: "+ propName +")"; 
							throw new IllegalConversionException(msg, e);
						}
					}
				}
				else if(id instanceof Number){
					// 【対象外】JSオブジェクトのプロパティ名が数値の場合
					
					if(_logger4jsToJavaBean.isTraceEnabled()){
						_logger4jsToJavaBean.trace("==== jsToJavaBean(Object, Class<?>) : js property id is Number. ====");
						_logger4jsToJavaBean.trace("scriptable[{}] => {}", id, scriptable.get(((Number)id).intValue(), scriptable));
					}
				}
			}
			_logger4jsToJavaBean.trace("---- Scriptable (End) ----");
			
			_logger4jsToJavaBean.trace("returnObj: {}", returnObj);
			return returnObj;
		}
		
		// Undefined は nullに変換
		else if(jsObject instanceof Undefined){
			_logger4jsToJavaBean.trace("========== Undefined ==========");
			return null;
		}

		// 上記以外は変換対象外 → nullを返却
		else{
			_logger4jsToJavaBean.trace("========== NOT TARGET ==========");
			return null;
		}
	}
	
	/**
	 * 引数で与えられた文字列の先頭一文字を小文字に変換して返却します。
	 * @param value 変換対象文字列
	 * @return 先頭の文字を小文字に変換した文字列。
	 */
	private static String conv2FirstCharLowerCase(final String value) {
		String lowered = value.substring(0, 1).toLowerCase();
		if(value.length() > 1){
			lowered = lowered.concat(value.substring(1));
		}
		return lowered;
	}
	
	/**
	 * @param jsObject
	 * @param className
	 * @return プリミティブ、および、ラッパークラスで無い場合はnullを返却。
	 */
	private static Object checkPrimitiveAndWrapper(final Object jsObject, final String className){

		if(jsObject == null || className == null){
			throw new IllegalArgumentException("Do not pass the arguments 'null'.");
		}
		
		else if(className.equals(String.class.getName()) ){

			if(jsObject instanceof String){
				return jsObject;
			}
			else {
				// プリミティブ、および、ラッパークラスではないと判定。
				return null;
			}
		}
		
		else if(className.equals(char.class.getName())
				||
				className.equals(Character.class.getName())) {

			return jsObject.toString().charAt(0);
		}
		
		else if(className.equals(double.class.getName())
			   ||
			   className.equals(Double.class.getName())) {
					
			if(jsObject instanceof Number){
				Number number = (Number)jsObject;
				return new Double(number.doubleValue());
			}
			else {
				return new Double(jsObject.toString());
			}
		}
		else if(className.equals(float.class.getName())
				||
				className.equals(Float.class.getName()) ){
			
			if(jsObject instanceof Number){
				Number number = (Number)jsObject;
				return new Float(number.floatValue());
			}
			else{
				return new Float(jsObject.toString());
			}
		}
		else if(className.equals(long.class.getName())
				||
				className.equals(Long.class.getName()) ){
			
			if(jsObject instanceof Number){
				Number number = (Number)jsObject;
				return new Long(number.longValue());
			}
			else{
				return new Long(jsObject.toString());
			}
		}
		else if(className.equals(int.class.getName())
				||
				className.equals(Integer.class.getName()) ){
			
			if(jsObject instanceof Number){
				Number number = (Number)jsObject;
				return new Integer(number.intValue());
			}
			else{
				return new Integer(jsObject.toString());
			}
		}
		else if(className.equals(short.class.getName())
				||
				className.equals(Short.class.getName()) ){
			
			if(jsObject instanceof Number){
				Number number = (Number)jsObject;
				return new Short(number.shortValue());
			}
			else{
				return new Short(jsObject.toString());
			}
		}
		else if(className.equals(byte.class.getName())
				||
				className.equals(Byte.class.getName()) ){
			
			if(jsObject instanceof Number){
				Number number = (Number)jsObject;
				return new Byte(number.byteValue());
			}
			else{
				return new Byte(jsObject.toString());
			}
		}		
		
		else if(className.equals(boolean.class.getName())
			   ||
			   className.equals(Boolean.class.getName())) {
	
				return Boolean.valueOf(jsObject.toString());
		}
		
		// ここまで来たらWrapperやプリミティブではない
		return null;
	}

	/**
	 * <a href="http://java.sun.com/j2se/1.5.0/ja/docs/ja/guide/beans/index.html">JavaBean</a>クラスのプロパティ情報を取得します。<br/>
	 * 引数で指定されたクラスのプロパティに関する情報を格納したMapを返却します。
	 * 返却されるMapの形式は、キーが「プロパティ名」、値が「{@link PropertyDescriptor}」 の形式です。<br/>
	 * 
	 * 指定されたクラスに、GetterとSetterが両方あるプロパティが一つも無かった場合、nullを返却します。
	 * 
	 * @param beanClass　JavaBeanクラス
	 * @return プロパティ情報を格納したMap。（Mapの形式は、キーが「プロパティ名」、値が「{@link PropertyDescriptor}」の形式）
	 * @throws IntrospectionException　イントロスペクション中に例外が発生した場合
	 */
	public static Map<String, PropertyDescriptor> getBeanPropertyMap(final Class<?> beanClass) 
														throws IntrospectionException {
		
		if(BEAN_PROP_INFO_MAP.get(beanClass) == null){
			synchronized(BEAN_PROP_INFO_MONITOR){
				if(BEAN_PROP_INFO_MAP.get(beanClass) == null){
					
					// Beanのプロパティに関するMapを作成（key = propName, value = PropertyDescriptor）
					Map<String, PropertyDescriptor> map = new WeakHashMap<String, PropertyDescriptor>();
					
					BeanInfo info = Introspector.getBeanInfo(beanClass);
					PropertyDescriptor[] propDiscriptorList = info.getPropertyDescriptors();
					
					_logger.trace("---- propDiscriptorList (Start) ----");
					for(PropertyDescriptor propDiscriptor : propDiscriptorList){
						_logger.trace("propDiscriptor: {}", propDiscriptor);
						
						// Getter/Setterが両方あること。（それ以外はこのメソッドではプロパティとみなさない）
						if(propDiscriptor.getReadMethod() != null 
						   &&
						   propDiscriptor.getWriteMethod() != null
						){
							String propName = propDiscriptor.getName();
							map.put(propName, propDiscriptor);
							_logger.trace("propName: {}", propName);
						}
					}
					_logger.trace("---- propDiscriptorList (End) ----");
					
					if(!map.isEmpty()){
						BEAN_PROP_INFO_MAP.put(beanClass, map);
					}
				}
			}
		}
		
		return BEAN_PROP_INFO_MAP.get(beanClass);
	}

	// Beanのプロパティに関するMapを格納するMap(上記の#getBeanPropertyMap()経由で取得すること)
	private static Map<Class<?>, Map<String, PropertyDescriptor>> BEAN_PROP_INFO_MAP = new WeakHashMap<Class<?>, Map<String, PropertyDescriptor>>();
	private static Object BEAN_PROP_INFO_MONITOR = new Object();

	
	
	/**
	 * 引数で指定されたクラスのPublicフィールドに関する情報を格納したMapを返却します。
	 * 返却されるMapの形式は、キーにPublicフィールド名、値に{@link Field} です。<br/>
	 * 
	 * Publicフィールドが存在しないクラスの場合、nullを返却します。
	 * 
	 * @param clazz　対象クラス
	 * @return　Publicフィールドに関する情報を格納したMap。（Mapの形式は、キーが「Publicフィールド名」、値が「{@link Field}」の形式）<br/>
	 * 			Publicフィールドが存在しないクラスの場合、nullを返却します。
	 */
	public static Map<String, Field> getPublicFieldMap(final Class<?> clazz){
		
		if(PUBLIC_FIELD_INFO_MAP.get(clazz) == null){
			synchronized(PUBLIC_FIELD_INFO_MONITOR){
				if(PUBLIC_FIELD_INFO_MAP.get(clazz) == null){
					
					// クラスのPublicフィールドに関するMapを作成（key = propName, value = Field）
					Map<String, Field> map = new WeakHashMap<String, Field>();
					
					Field[] fields = clazz.getFields();
					
					_logger.trace("---- fields (Start) ----");
					for(Field f : fields){
						_logger.trace("f: {}", f);
						
						// finalではないフィールドを追加
						if(Modifier.isFinal(f.getModifiers()) == false){
							String fieldName = f.getName();
							map.put(fieldName, f);
							_logger.trace("fieldName: {}", fieldName);
						}
					}
					_logger.trace("---- fields (End) ----");

					if(!map.isEmpty()){
						PUBLIC_FIELD_INFO_MAP.put(clazz, map);
					}
				}
			}
		}
		return PUBLIC_FIELD_INFO_MAP.get(clazz);
	}

	// クラスのPublicフィールドに関するMapを格納するMap(上記の#getPublicFieldMap()経由で取得すること)
	private static Map<Class<?>, Map<String, Field>> PUBLIC_FIELD_INFO_MAP = new WeakHashMap<Class<?>, Map<String, Field>>();
	private static Object PUBLIC_FIELD_INFO_MONITOR = new Object();
	
	
	/**
	 * 指定されたクラスの サンプルデータ設定済みインスタンスを生成します。<br/>
	 * <br/>
	 * このメソッドを呼び出すことは、
	 * {@link #newInstanceFilledSampleData(String, ClassLoader, String)
	 * <tt>newInstanceFilledSampleData(beanClassName, Thread.currentThread().getContextClassLoader(), sampleValue)</tt>}
	 * を呼び出すことに相当します。
	 * 
	 * @param beanClassName 生成するインスタンスのクラス名
	 * @param sampleValue 指定されたクラスがjava.lang.String、または、java.lang.Characterだった場合のサンプルデータの値。
	 * @return 指定されたクラスのサンプルデータ設定済みインスタンス
	 * 
	 * @throws IntrospectionException イントロスペクション中に例外が発生した場合
	 * @throws IllegalAccessException 指定されたクラス、またはその無引数コンストラクタにアクセスできない場合。または、JavaBeanプロパティのSetterメソッドにアクセスできない場合
	 * @throws InvocationTargetException　JavaBeanのSetterが例外をスローする場合
	 * @throws ClassNotFoundException 指定されたクラスが見つからなかった場合 
	 * 
	 * @see #newInstanceFilledSampleData(String, ClassLoader, String)
	 */
	public static Object newInstanceFilledSampleData(final String beanClassName, 
												   final String sampleValue) 
							throws IntrospectionException,
								   IllegalAccessException, InvocationTargetException, 
								   ClassNotFoundException{
		return newInstanceFilledSampleData(beanClassName, Thread.currentThread().getContextClassLoader(), sampleValue);
	}

	/**
	 * 指定されたクラスの サンプルデータ設定済みインスタンスを生成します。<br/>
	 * <br/>
	 * このメソッドを呼び出すことは、
	 * {@link #newInstanceFilledSampleData(Class, String)
	 * <tt>newInstanceFilledSampleData(classLoader.loadClass(beanClassName), sampleValue)</tt>}
	 * を呼び出すことに相当します。
	 * 
	 * @param beanClassName 生成するインスタンスのクラス名
	 * @param classLoader　beanClassNameで指定されるクラスをロードする為のクラスローダ
	 * @param sampleValue 指定されたクラスがjava.lang.String、または、java.lang.Characterだった場合のサンプルデータの値。
	 * @return 指定されたクラスのサンプルデータ設定済みインスタンス
	 * 
	 * @throws IntrospectionException イントロスペクション中に例外が発生した場合
	 * @throws IllegalAccessException 指定されたクラス、またはその無引数コンストラクタにアクセスできない場合。または、JavaBeanプロパティのSetterメソッドにアクセスできない場合
	 * @throws InvocationTargetException　JavaBeanのSetterが例外をスローする場合
	 * @throws ClassNotFoundException 指定されたクラスが見つからなかった場合 
	 * 
	 * @see #newInstanceFilledSampleData(Class, String)
	 */
	public static Object newInstanceFilledSampleData(final String beanClassName, 
												   final ClassLoader classLoader, 
												   final String sampleValue) 
							throws IntrospectionException,
								   IllegalAccessException, InvocationTargetException, 
								   ClassNotFoundException{
		
		if(beanClassName.equals("char")) {
			return generatePrimitiveOrWrapperInstance(char.class, sampleValue);
		}
		else if(beanClassName.equals("double")) {
			return generatePrimitiveOrWrapperInstance(double.class, sampleValue);
		}
		else if(beanClassName.equals("float")) {
			return generatePrimitiveOrWrapperInstance(float.class, sampleValue);
		}
		else if(beanClassName.equals("long")) {
			return generatePrimitiveOrWrapperInstance(long.class, sampleValue);
		}
		else if(beanClassName.equals("int")) {
			return generatePrimitiveOrWrapperInstance(int.class, sampleValue);
		}
		else if(beanClassName.equals("short")) {
			return generatePrimitiveOrWrapperInstance(short.class, sampleValue);
		}
		else if(beanClassName.equals("byte")) {
			return generatePrimitiveOrWrapperInstance(byte.class, sampleValue);
		}
		else if(beanClassName.equals("boolean")) {
			return generatePrimitiveOrWrapperInstance(boolean.class, sampleValue);
		}
		else{
			Class<?> clazz = classLoader.loadClass(beanClassName);
			return newInstanceFilledSampleData(clazz, sampleValue);
		}
	}
	
	/**
	 * {@link #newInstanceFilledSampleData(Class, String)}メソッドで利用される、
	 * java.lang.String用のサンプルデータ初期値。<br/>
	 */
	public static final String SAMPLE_DATA_FOR_STRING_DEFAULT = "__default__";
	
	/**
	 * {@link #newInstanceFilledSampleData(Class, String)}メソッドで利用される、
	 * java.lang.Number用のサンプルデータ初期値。
	 */
	public static final double SAMPLE_DATA_FOR_NUMBER_DEFAULT = 123;
	
	/**
	 * {@link #newInstanceFilledSampleData(Class, String)}メソッドで利用される、
	 * java.lang.Boolean用のサンプルデータ初期値。
	 */
	public static final boolean SAMPLE_DATA_FOR_BOOLEAN_DEFAULT = true;
	
	/**
	 * {@link #newInstanceFilledSampleData(Class, String)}メソッドで利用される、
	 * java.util.Date用のサンプルデータ初期値（ミリ秒）。<br/>
	 * "Mon June 19 2008 12:34:56 GMT+0900 (JST)"をあらわします。
	 */
	public static final long SAMPLE_DATA_FOR_DATE_MILLIS_DEFAULT = 1213846496000L;
	
	/**
	 * {@link #newInstanceFilledSampleData(Class, String)}メソッドで利用される、
	 * サンプルデータが配列の場合の要素数。
	 */
	public static final int SAMPLE_DATA_ARRAY_LENGTH_DEFAULT = 1;
	
	/**
	 * 指定されたクラスの サンプルデータ設定済みインスタンスを生成します。<br/>
	 * <br/>
	 * <a href="#RelationSampleDataAndClass">表：サンプルデータとクラスの関係</a>に基づいて
	 * 引数「beanType」で指定されたクラスのサンプルデータ設定済みインスタンスを生成します。<br/>
	 * <br/>
	 * 生成するインスタンスのクラス(=引数「beanType」)に、<a href="http://java.sun.com/j2se/1.5.0/ja/docs/ja/guide/beans/index.html">JavaBean</a>
	 * 形式のクラスが指定された場合、<br/>
	 * JavaBeanのインスタンスを新たに生成後、
	 * <a href="#RelationSampleDataAndClass">表：サンプルデータとクラスの関係</a>に基づいて各プロパティの値を設定します。<br/>
	 * <br/>
	 * プロパティの型がJavaBean形式のクラスである場合、さらにサンプルデータの設定を行ないます。<br/>
	 * なお、プロパティの型が既に走査されたクラス、または、自身と同じクラスの場合、そのプロパティにはnullが設定されます。
	 * <br/>
	 * これを繰り返し、サンプルデータ設定済みインスタンスを生成します。<br/>
	 * 
	 * <a name="RelationSampleDataAndClass">
	 * <h3>表：サンプルデータとクラスの関係</h3>
	 * 
	 * 指定されたクラスが配列の場合の要素数は「1」です。
	 * なお、以下のサンプルデータの値、および、配列要素数は、
	 * {@link #initializeSampleData(String, Number, Boolean, Date, int)}で設定可能です。<br/>
	 * 
	 * <table border="1">
	 *  <tr>
	 *  	<th>Javaクラス</th>
	 *  	<th>サンプルデータ</th>
	 *  	<th>備考</th>
	 *  </tr>
	 *  <tr>
	 *  	<td>java.lang.String</td>
	 *  	<td>{@link JavaScriptUtility#SAMPLE_DATA_FOR_STRING_DEFAULT}</td>
	 *  	<td>
	 *  		引数「sampleValue」が指定されている場合は、その値が設定されます。<br/>
	 *  		JavaBeanプロパティの場合は「prop_プロパティ名」形式で設定されます。
	 *  	</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>java.lang.Character</td>
	 *  	<td>java.lang.Stringのサンプルデータ１文字目を元に生成</td>
	 *  	<td align="center"> - </td>
	 *  </tr>
	 *  <tr>
	 *  	<td>java.lang.Number</td>
	 *  	<td>new Double({@link JavaScriptUtility#SAMPLE_DATA_FOR_NUMBER_DEFAULT})</td>
	 *  	<td align="center"> - </td>
	 *  </tr>
	 *  <tr>
	 *  	<td>java.lang.Boolean</td>
	 *  	<td>Boolean.valueOf({@link JavaScriptUtility#SAMPLE_DATA_FOR_BOOLEAN_DEFAULT})</td>
	 *  	<td align="center"> - </td>
	 *  </tr>
	 *  <tr>
	 *  	<td>java.util.Date</td>
	 *  	<td>new Date({@link JavaScriptUtility#SAMPLE_DATA_FOR_DATE_MILLIS_DEFAULT})</td>
	 *  	<td>
	 *  		「Mon June 19 2008 12:34:56 GMT+0900 (JST)」をあらわします。
	 *  	</td>
	 *  </tr>
	 *  <tr>
	 *  	<td>java.util.Calendar</td>
	 *  	<td>java.util.Dateのサンプルデータを元に生成</td>
	 *  	<td align="center"> - </td>
	 *  </tr>
	 * </table>
	 * <br/>
	 * <br/>
	 * 上記以外のクラスはサンプルデータ生成対象外です。（例えば、List, Map, Setは対象外です。）<br/>
	 * サンプルデータ生成対象外のクラスが指定された場合は、サンプルデータの設定を行いません。<br/>
	 * 
	 * <h3>例</h3>
	 * 以下の3つのクラスが存在するとします。（&lt;&gt;の中はクラス名をあらわしています）
	 * <pre><hr align="left" width="30%"/>
	 * クラスA
	 *   ├─プロパティ a    : &lt;A&gt;
	 *   ├─プロパティ b    : &lt;B&gt;
	 *   ├─プロパティ wrap : &lt;String&gt;
	 *   ├─プロパティ a'   : &lt;A[]&gt;
	 *   └─プロパティ b'   : &lt;B[]&gt;
	 * 
	 * クラスB
	 *   ├─プロパティ a    : &lt;A&gt;
	 *   ├─プロパティ b    : &lt;B&gt;
	 *   ├─プロパティ c    : &lt;C&gt;
	 *   ├─プロパティ wrap : &lt;String&gt;
	 *   ├─プロパティ a'   : &lt;A[]&gt;
	 *   ├─プロパティ b'   : &lt;B[]&gt;
	 *   └─プロパティ c'   : &lt;C[]&gt;
	 * 
	 * クラスC
	 *   ├─プロパティ a    : &lt;A&gt;
	 *   ├─プロパティ b    : &lt;B&gt;
	 *   ├─プロパティ c    : &lt;C&gt;
	 *   ├─プロパティ wrap : &lt;String&gt;
	 *   ├─プロパティ a'   : &lt;A[]&gt;
	 *   ├─プロパティ b'   : &lt;B[]&gt;
	 *   └─プロパティ c'   : &lt;C[]&gt;
	 * <hr align="left" width="30%"/>
	 * </pre>
	 * 
	 * <pre><code>newInstanceFilledSampleData(クラスA, "サンプル");</code></pre>
	 * を実行した場合の返却値は以下のようになります。
	 * <pre>
	 * <hr align="left" width="30%"/>
	 * 返却値 ・・・①
	 *   ├─ a = null (∵自身と同じクラス)
	 *   ├─ b ・・・②
	 *   │  ├─ a = null (∵①で走査済み)
	 *   │  ├─ b = null (∵自身と同じクラス)
	 *   │  ├─ c ・・・③
	 *   │  │  ├─ a = null (∵①で走査済み)
	 *   │  │  ├─ b = null (∵②で走査済み)
	 *   │  │  ├─ c = null (∵自身と同じクラス)
	 *   │  │  ├─ wrap = "prop_wrap"
	 *   │  │  ├─ a' = 空の配列 (∵①で走査済み)
	 *   │  │  ├─ b' = 空の配列 (∵②で走査済み)
	 *   │  │  └─ c' = 空の配列 (∵③で走査済み)
	 *   │  ├─ wrap = "prop_wrap"
	 *   │  ├─ a' = 空の配列 (∵①で走査済み)
	 *   │  ├─ b' = 空の配列 (∵②で走査済み)
	 *   │  └─ c' = 空の配列 (∵③で走査済み)
	 *   ├─ wrap = "prop_wrap"
	 *   ├─ a' = 空の配列 (∵①で走査済み)
	 *   └─ b' = 空の配列 (∵②で走査済み)
	 * <hr align="left" width="30%"/>
	 * </pre>
	 * 
	 * 
	 * @param beanType　生成するインスタンスのクラス
	 * @param sampleValue　指定されたクラスがjava.lang.String、または、java.lang.Characterだった場合のサンプルデータの値。
	 * @return　指定されたクラスのサンプルデータ設定済みインスタンス
	 * 
	 * @throws IntrospectionException イントロスペクション中に例外が発生した場合
	 * @throws IllegalAccessException 指定されたクラス、またはその無引数コンストラクタにアクセスできない場合。または、JavaBeanプロパティのSetterメソッドにアクセスできない場合
	 * @throws InvocationTargetException　JavaBeanのSetterが例外をスローする場合
	 * 
	 * @see #initializeSampleData(String, Number, Boolean, Date, int)
	 */
	public static Object newInstanceFilledSampleData(final Class<?> beanType, 
													 final String sampleValue) 
							throws IntrospectionException, 
								   IllegalAccessException, InvocationTargetException {
		
		try{
			threadLocal4newInstanceFilledSampleData.set(new ArrayList<Class<?>>());
			
			return newInstanceFilledSampleData(beanType, sampleValue, false);
		}
		finally{
			threadLocal4newInstanceFilledSampleData.remove();
		}
	}
	
	private static List<Class<?>> excludeClassList4newInstanceFilledSampleData = new ArrayList<Class<?>>();

	/**
	 * {@link #newInstanceFilledSampleData(Class, String)}等のメソッドで、
	 * サンプルデータ設定済みインスタンスの生成を行わないようするクラスを追加します。
	 * 
	 * @param excludeClassList4newInstanceFilledSampleData　サンプルデータ設定済みインスタンスの生成を行わないようするクラス
	 * @see {@link JavaScriptUtility#newInstanceFilledSampleData(Class, String)}
	 * @see {@link JavaScriptUtility#newInstanceFilledSampleData(String, String)}
	 * @see {@link JavaScriptUtility#newInstanceFilledSampleData(String, ClassLoader, String)}
	 */
	public static void addExcludeClass4newInstanceFilledSampleData(Class<?> excludeConvertClass) {
		JavaScriptUtility.excludeClassList4newInstanceFilledSampleData.add(excludeConvertClass);
		_logger.debug("excludeConvertClassList: {}", excludeClassList4newInstanceFilledSampleData);
	}
	

	private static ThreadLocal<List<Class<?>>> threadLocal4newInstanceFilledSampleData = new ThreadLocal<List<Class<?>>>();

	private static Logger _logger4newInstance = 
		Logger.getLogger(JavaScriptUtility.class.getName() + ".newInstanceFilledSampleData");
	
	/**
	 * 再帰用のメソッド
	 */
	private static Object newInstanceFilledSampleData(final Class<?> beanType,
													  final String sampleValue,
													  final boolean isLast) 
							throws IntrospectionException,  
								   IllegalAccessException, InvocationTargetException {
		
		_logger4newInstance.trace("------------");
		_logger4newInstance.trace("isLast: {}", isLast);
		_logger4newInstance.trace("beanType: {}", beanType);
		_logger4newInstance.trace("sampleValue: {}", sampleValue);
		_logger4newInstance.trace("------------");

		// ===================
		// 変換対象外の場合はnullを返却。（設定する値の形式が決まっているクラスは変換対象外とすることを想定）
		// 例：org.apache.axis2.databinding.types.URI#setQueryString()
		// ===================
		if(excludeClassList4newInstanceFilledSampleData.contains(beanType)){
			return null;
		}
		
		Object beanInstance = generatePrimitiveOrWrapperInstance(beanType, sampleValue);

		if(beanInstance != null){
			Object primitiveOrWrapperInstance = beanInstance;
			_logger4newInstance.trace("primitiveOrWrapperInstance: {}", primitiveOrWrapperInstance);
			
			return primitiveOrWrapperInstance;
		}

		// 走査済みクラスとして設定
		setScannedClass(beanType);
		_logger4newInstance.trace("[execute] setScannedClass(): {}", beanType);

		_logger4newInstance.trace("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");

		try {
			beanInstance = beanType.newInstance();
		}
		catch (InstantiationException e) {
			_logger4newInstance.trace("interface or Abstract class", e);
			return generatePrimitiveOrWrapperInstance(String.class, beanType.getName());
		}

		// Publicフィールド
		Map<String, Field> pubFieldMap = getPublicFieldMap(beanType);
		if(pubFieldMap != null){
			
			Iterator<Map.Entry<String, Field>> it = pubFieldMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, Field> entry = it.next();
				
				String propName = entry.getKey();
				Field field     = entry.getValue();
				Class<?> propType = field.getType();
				String newSampleValue = "prop_" + propName;
				
				_logger4newInstance.trace("=========================");
				_logger4newInstance.trace("----");
				_logger4newInstance.trace("propName: {}", propName);
				_logger4newInstance.trace("newsampleValue: {}", newSampleValue);
				_logger4newInstance.trace("----");
				_logger4newInstance.trace("isLast: {}", isLast);
				_logger4newInstance.trace("beanType: {}", beanType);
				_logger4newInstance.trace("propType: {}", propType);

				// 再帰チェック
				Object data = newInstanceFilledSampleDataRecursive(beanType, isLast, propType, newSampleValue);
				
				try{
					field.set(beanInstance, data);
				}
				catch (Exception e) {
					// プロパティ値を設定せずに、処理続行！
					// XMLスキーマの「restriction」や引数ミスマッチ発生時を想定 （∵interface or Abstractの場合のサンプルデータは、その型名(=String)であるため）
					_logger4newInstance.trace("[Case of Public Field] " + e.getMessage(), e);
				}
					
				_logger4newInstance.trace("=========================");
				
			}
		}

		// JavaBeanの場合
		Map<String, PropertyDescriptor> beanPropMap = getBeanPropertyMap(beanType);
		if(beanPropMap != null){
			Iterator<Map.Entry<String, PropertyDescriptor>> it = beanPropMap.entrySet().iterator();
			
			while(it.hasNext()){
				Map.Entry<String, PropertyDescriptor> entry = it.next();
				PropertyDescriptor propDescriptor = entry.getValue();
	
				Method setter = propDescriptor.getWriteMethod();
				if(setter == null){
					continue;
				}
	
				_logger4newInstance.trace("=========================");
				Class<?> propType = propDescriptor.getPropertyType();
				String propName = propDescriptor.getName();
				String newSampleValue = "prop_" + propName;
				
				_logger4newInstance.trace("----");
				_logger4newInstance.trace("propName: {}", propName);
				_logger4newInstance.trace("newsampleValue: {}", newSampleValue);
				_logger4newInstance.trace("----");
				_logger4newInstance.trace("isLast: {}", isLast);
				_logger4newInstance.trace("beanType: {}", beanType);
				_logger4newInstance.trace("propType: {}", propType);
				
				// 再帰チェック
				Object data = newInstanceFilledSampleDataRecursive(beanType, isLast, propType, newSampleValue);
				
				try {
					setter.invoke(beanInstance, data);
				}
				catch (Exception e) {
					// プロパティ値を設定せずに、処理続行！
					// XMLスキーマの「restriction」や引数ミスマッチ発生時を想定 （∵interface or Abstractの場合のサンプルデータは、その型名(=String)であるため）
					_logger4newInstance.trace("[Case of JavaBean] " + e.getMessage(), e);
				}
				
				_logger4newInstance.trace("=========================");
				
			}
		}
		_logger4newInstance.trace("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");

		return beanInstance;
	}

	/**
	 * @param beanType
	 * @param isLast
	 * @param propType
	 * @param newSampleValue
	 * @return
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static Object newInstanceFilledSampleDataRecursive(
												final Class<?> beanType, 
												final boolean isLast,
												final Class<?> propType,
												final String newSampleValue)
	
							throws IntrospectionException, IllegalAccessException, 
								   InvocationTargetException {

		Object data;
		
		// 通常のオブジェクトの場合
		if(!propType.isArray()){
			boolean isRecursive = isRecursive(isLast, beanType, propType);
			_logger4newInstance.trace("--OBJECT-- isRecursive: {}", isRecursive);
			
			if(isRecursive){
				// 再帰
				_logger4newInstance.trace("--OBJECT-- [RECURSIVE] newInstanceFilledSampleData()");
				data = newInstanceFilledSampleData(propType, newSampleValue, isLast);
			}
			else{
				_logger4newInstance.trace("--OBJECT-- [execute] generatePrimitiveOrWrapperInstance()");
				data = generatePrimitiveOrWrapperInstance(propType, newSampleValue);
			}
		}
		
		// 配列の場合
		else{
			Class<?> componentType = propType.getComponentType();

			boolean isRecursive = isRecursive(isLast, beanType, componentType);
			
			_logger4newInstance.trace("--ARRAY-- componentType: {}", componentType);
			_logger4newInstance.trace("--ARRAY-- isRecursive: {}", isRecursive);

			if(isRecursive){
				data = Array.newInstance(componentType, SAMPLE_DATA_ARRAY_LENGTH);
				for(int idx = 0; idx < SAMPLE_DATA_ARRAY_LENGTH; idx++){
					// 再帰
					_logger4newInstance.trace("--ARRAY-- [RECURSIVE] newInstanceFilledSampleData()");
					Object dataElem = newInstanceFilledSampleData(componentType, newSampleValue, isLast);
					Array.set(data, idx, dataElem);
				}
			}
			else {
				_logger4newInstance.trace("--ARRAY-- [execute] generateEmptyArray()");
				data = Array.newInstance(componentType, 0); // 空の配列
			}
		}
		return data;
	}

	/**
	 * @param isLast
	 * @param beanType
	 * @param propType
	 * @return
	 */
	private static boolean isRecursive(final boolean isLast,
									   final Class<?> beanType, 
									   final Class<?> propType) {
		return !isLast
			   &&
			   !beanType.getName().equals(propType.getName())
			   &&
			   !isScannedClass(propType);
		
	}

	/**
	 * @param beanType
	 */
	private static void setScannedClass(final Class<?> beanType) {
		List<Class<?>> list = threadLocal4newInstanceFilledSampleData.get();
		list.add(beanType);
		threadLocal4newInstanceFilledSampleData.set(list);
	}
		
	/**
	 * @param beanType
	 * @return
	 */
	private static boolean isScannedClass(final Class<?> beanType) {
		List<Class<?>> list = threadLocal4newInstanceFilledSampleData.get();
		return list.contains(beanType);
	}

	/**
	 * @param type
	 * @param sampleValue
	 * @return
	 */
	private static Object generatePrimitiveOrWrapperInstance(final Class<?> type,
															 final String sampleValue) {

		String def = sampleValue;
		if(def == null || def.length() == 0) {
			def = SAMPLE_DATA_FOR_STRING;
		}
		
		// ===================
		// 文字列
		// ===================
		if(type.equals(java.lang.String.class)){
			return new String(def);
		}
		
		else if(type.equals(java.lang.Character.class)
				||
				type.equals(char.class)) {
			char ch = def.charAt(0);
			return new Character(ch);
		}
		
		// ===================
		// 数値
		// ===================
		else if(type.equals(java.lang.Number.class)
				||
				type.equals(java.lang.Double.class)
				||
				type.equals(double.class)){
			return SAMPLE_DATA_FOR_NUMBER.doubleValue();
		}
		
		else if(type.equals(java.lang.Float.class)
				||
				type.equals(float.class)){
			return SAMPLE_DATA_FOR_NUMBER.floatValue();
		}
		else if(type.equals(java.lang.Long.class)
				||
				type.equals(long.class)){
			return SAMPLE_DATA_FOR_NUMBER.longValue();
		}

		else if(type.equals(java.lang.Integer.class)
				||
				type.equals(int.class)){
			return SAMPLE_DATA_FOR_NUMBER.intValue();
		}

		else if(type.equals(java.lang.Short.class)
				||
				type.equals(short.class)){
			return SAMPLE_DATA_FOR_NUMBER.shortValue();
		}
		
		else if(type.equals(java.lang.Byte.class)
				||
				type.equals(byte.class)){
			return SAMPLE_DATA_FOR_NUMBER.byteValue();
		}
		
		// ===================
		// 真偽値
		// ===================
		else if(type.equals(java.lang.Boolean.class)
				||
				type.equals(boolean.class)){
			
			return SAMPLE_DATA_FOR_BOOLEAN;
		}
		
		// ===================
		// 日付 (Date)
		// ===================
		else if(type.equals(java.util.Date.class)){
			return SAMPLE_DATA_FOR_DATE;
		}
		
		// ===================
		// 日付 (Calendar)
		// ===================
		else if(type.equals(java.util.Calendar.class)){
			Calendar cal = Calendar.getInstance();
			cal.setTime(SAMPLE_DATA_FOR_DATE);
			
			return cal;
		}
		
		// ===================
		// java.lang.Object
		// ===================
		else if(type.equals(java.lang.Object.class)){
			return def;
		}
		
		// ===================
		// 上記以外 （≒ Primitive型、Wrapperクラス、および、java.lang.Objectクラスではない場合）
		// ===================
		else{
			return null;
		}
	}

	private static String SAMPLE_DATA_FOR_STRING = SAMPLE_DATA_FOR_STRING_DEFAULT;
	private static Number SAMPLE_DATA_FOR_NUMBER = new Double(SAMPLE_DATA_FOR_NUMBER_DEFAULT);
	private static Boolean SAMPLE_DATA_FOR_BOOLEAN = Boolean.valueOf(SAMPLE_DATA_FOR_BOOLEAN_DEFAULT);
	private static Date SAMPLE_DATA_FOR_DATE = new Date(SAMPLE_DATA_FOR_DATE_MILLIS_DEFAULT);
	private static int SAMPLE_DATA_ARRAY_LENGTH = SAMPLE_DATA_ARRAY_LENGTH_DEFAULT;

	static{
		initializeSampleData(null, null, null, null, -1);
	}
	
	/**
	 * {@link #newInstanceFilledSampleData(Class, String)}メソッドで利用されるサンプルデータを初期化します。
	 * 
	 * @param str java.lang.String用のサンプルデータ。空文字設定不可。<br/>
	 * 			  nullが指定された場合は、{@link JavaScriptUtility#SAMPLE_DATA_FOR_STRING_DEFAULT}が設定されます。
	 * 
	 * @param num java.lang.Number用のサンプルデータ。<br/>
	 * 			  nullが指定された場合は、new Double({@link JavaScriptUtility#SAMPLE_DATA_FOR_NUMBER_DEFAULT})が設定されます。
	 * 
	 * @param bool java.lang.Boolean用のサンプルデータ。<br/>
	 * 			   nullが指定された場合は、Boolean.valueOf({@link JavaScriptUtility#SAMPLE_DATA_FOR_BOOLEAN_DEFAULT})が設定されます。
	 * 
	 * @param date　java.util.Dateの用のサンプルデータ。<br/>
	 * 			   nullが指定された場合は、new Date({@link JavaScriptUtility#SAMPLE_DATA_FOR_DATE_MILLIS_DEFAULT})が設定されます。
	 * 
	 * @param arrayLength サンプルデータが配列の場合の要素数。<br/>
	 * 					  負数が指定された場合は、{@link JavaScriptUtility#SAMPLE_DATA_ARRAY_LENGTH_DEFAULT}が設定されます。
	 */
	public static void initializeSampleData(final String str, 
											final Number num, 
											final Boolean bool, 
											final Date date, 
											final int arrayLength){

		// String
		if(str != null && str.length() != 0){
			SAMPLE_DATA_FOR_STRING = str;
		}
		else {
			SAMPLE_DATA_FOR_STRING = SAMPLE_DATA_FOR_STRING_DEFAULT;
		}
		
		// Number
		if(num != null){
			SAMPLE_DATA_FOR_NUMBER = num;
		}
		else{
			SAMPLE_DATA_FOR_NUMBER = new Double(SAMPLE_DATA_FOR_NUMBER_DEFAULT);
		}
		
		// Boolean
		if(bool != null){
			SAMPLE_DATA_FOR_BOOLEAN = bool;
		}
		else{
			SAMPLE_DATA_FOR_BOOLEAN = Boolean.valueOf(SAMPLE_DATA_FOR_BOOLEAN_DEFAULT);
		}
		
		// Calendar
		if(date != null){
			SAMPLE_DATA_FOR_DATE = date;
		}
		else{
			SAMPLE_DATA_FOR_DATE = new Date(SAMPLE_DATA_FOR_DATE_MILLIS_DEFAULT);
		}
		
		// Array Length
		if(!(arrayLength < 0)){
			SAMPLE_DATA_ARRAY_LENGTH = arrayLength;
		}
		else{
			SAMPLE_DATA_ARRAY_LENGTH = SAMPLE_DATA_ARRAY_LENGTH_DEFAULT;
		}
	}
	
}
