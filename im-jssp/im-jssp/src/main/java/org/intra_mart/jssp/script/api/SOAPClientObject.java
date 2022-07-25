package org.intra_mart.jssp.script.api;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.axis2.AxisFault;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.util.CommandLineOptionConstants;
import org.apache.axis2.util.CommandLineOptionParser;
import org.apache.axis2.util.JavaUtils;
import org.apache.axis2.util.URLProcessor;
import org.apache.axis2.wsdl.codegen.CodeGenConfiguration;
import org.apache.axis2.wsdl.codegen.CodeGenerationEngine;
import org.apache.axis2.wsdl.codegen.CodeGenerationException;
import org.intra_mart.common.aid.jdk.java.lang.ExtendedClassLoader;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContextManager;
import org.intra_mart.common.platform.log.Logger;
import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilder;
import org.intra_mart.jssp.script.provider.ScriptScopeBuilderManager;
import org.intra_mart.jssp.util.JavaScriptUtility;
import org.intra_mart.jssp.util.config.HomeDirectory;
import org.intra_mart.jssp.util.config.JSSPConfigHandler;
import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// PLAN [ns] エラーが発生しても再起動せずに反映できるようにしたい。
/**
 * SOAPClient オブジェクト。<br/>
 * 
 * <h2><a name='summary'>概要</a></h2>
 * このオブジェクトは、SOAP/WSDLベースのWebサービスを呼び出すためのオブジェクトです。<br/>
 * このオブジェクトを利用することにより、XMLやJavaを意識することなく、Webサービスを呼び出すことが可能です。<br/>
 * <br/>
 * SOAPClientオブジェクトを利用したWebサービスの呼び出しは、以下の３ステップで実現できます。<br/>
 * <br/>
 * 　　●ステップ１ ： WSDLを指定して SOAPClientオブジェクトのインスタンスを生成 ・・・ <a href="#APIListStart">「コンストラクタ」</a>参照<br/>
 * 　　●ステップ２ ： Webサービスを呼び出すソースコードのサンプルを表示 ・・・ <a href="#getSampleCodeString">「getSampleCode() 関数」</a>参照<br/>
 * 　　●ステップ３ ： Webサービスの呼び出し （ステップ２で出力されたコードをカスタマイズ）<br/>
 * <br/>
 * 
 * 具体的なWebサービス実行方法は、<a href="#sampleCode">サンプルコード</a> 参照してください。<br/>
 * <br/>
 * なお、Webサービスの結果がSOAPフォルトとして返却された場合、{@link SOAPFault} オブジェクトが例外としてスローされます。<br/>
 * {@link SOAPFault} オブジェクトを捕捉する（=Webサービス・オペレーションの実行ロジック部分をtry/catchで囲む)ことで、
 * {@link SOAPFault} オブジェクトを利用したエラー処理を行う事が可能です。<br/>
 * 
 * <br/>
 * 
 * <h2>注意事項</h2>
 * <ul>
 * 	<li>SOAPClientオブジェクトは、データバインディング方式に「ADB」を利用したAxis2のスタブを利用しています。
 * 		そのため、Axis2が対応していないWebサービスを呼び出すことはできません。</li>
 * 	<li>SOAPClientオブジェクトは、document-literal スタイルの Web サービスに対応しています。</li>
 * 	<li>SOAPClientは、非同期型コールバック形式のWebサービスを実行することはできません。</li>
 * </ul>
 * 
 * <br/>
 * 
 * <h2><a name='setting'>設定</a></h2>
 * Application Runtime上の conf/imart.xml にて、SOAPClientオブジェクトの各種設定が可能です。<br/>
 * imart.xml の intra-mart/platform/service/application/jssp タグに以下の設定が可能です。
 * 
 * <table border="1">
 * 	<tr>
 * 		<th>設定項目</th>
 * 		<th>概要</th>
 * 		<th>初期値</th>
 * 	</tr>
 * 	<tr>
 * 		<td>soap-client/mode</td>
 * 		<td>
 * 			SOAPClientのインスタンス生成時に行われるWebサービスのスタブ生成
 * 			（＝WSDLの解析、Javaスタブ・ソースの生成＆コンパイル、および、JavaScriptソースの生成）に関する設定です。
 * 			以下の3つの設定が可能です。
 * 			<ul>
 * 				<li>
 * 					Everytime：
 * 					Webサービスのスタブを毎回作成します。開発時に利用する設定です。
 * 				</li>
 * 				<br/>
 * 				<li>
 * 					Once：
 * 					Webサービスのスタブが存在しない場合のみスタブを作成します。
 * 				</li>
 * 				<br/>
 * 				<li>
 * 					Never：
 * 					Webサービスのスタブを自動生成しません。このモードの場合、別途、Webサービスのスタブを配備する必要があります。
 * 					具体的には、Axis2が生成するJavaのスタブ・クラスをクラスパスに追加し、
 * 					SOAPClientオブジェクトで生成されたJavaScriptのスタブ・ソースを
 * 					ソースディレクトリ（通常は%IM_HOME%/pages/src/）に追加する必要があります。
 * 				</li>
 * 			</ul>
 * 		</td>
 * 		<td>Once</td>
 * 	</tr>
 * 	<tr>
 * 		<td>soap-client/work-dir</td>
 * 		<td>
 * 			Webサービスのスタブ、および、WSDLファイルを展開するディレクトリです。
 * 			Application Runtimeがインストールされているディレクトリからの相対パスで指定します。
 * 		</td>
 * 		<td>ファンクションコンテナの自動コンパイル時のクラスファイル出力先ディレクトリ</td>
 * 	</tr>
 * 	<tr>
 * 		<td>soap-client/javac-encoding</td>
 * 		<td>
 * 			Webサービスのスタブをコンパイルする際のJavaソースの文字コード
 * 		</td>
 * 		<td>UTF-8</td>
 * 	</tr>
 * 	<tr>
 * 		<td>soap-client/javac-verbose</td>
 * 		<td>
 * 			Webサービスのスタブをコンパイルする際の詳細情報出力可否設定です。<br/>
 * 			trueの場合、詳細情報が出力され、falseの場合詳細情報は出力されません。
 * 		</td>
 * 		<td>false</td>
 * 	</tr>
 * 	<tr>
 * 		<td>soap-client/wsdl/storage/import-location/suffixes/suffix</td>
 * 		<td>
 * 			スタブ生成に必要なファイルの拡張子を設定します。
 * 			Storage Service上に保存されているWSDLファイルを利用する際に必要な設定です。
 * 			例えば、あるWSDLファイルで参照している要素が、別のファイルで定義されている場合、その定義ファイルの拡張子をここに設定します。
 * 		</td>
 * 		<td>「.xsd」と「.wsdl」</td>
 * 	</tr>
 * 	<tr>
 * 		<td>soap-client/wsdl/storage/import-location/sub-dirs/sub-dir</td>
 * 		<td>
 * 			スタブ生成に必要なファイルが格納されているディレクトリ名の設定です。
 * 			Storage Service上に保存されているWSDLファイルを利用する際に必要な設定です。
 * 			例えば、あるWSDLファイルで参照している要素が、別のファイルで定義されている場合、
 * 			そのファイルが保存されているディレクトリ名をここに設定します。
 * 			WSDLファイルが保存されているディレクトリのサブディレクトリ名として利用されます。
 * 		</td>
 * 		<td>「xsd」</td>
 * 	</tr>
 * </table>
 * 
 * <br/>
 *
 * <h2><a name='sampleCode'>サンプルコード</a></h2>
 * 	<table border="1">
 * 		<tr>
 * 			<th>
 * 				サンプル
 * 			</th>
 * 		</tr>
 * 		<tr>
 * 			<td>
 * 				<font size="-1.5">
<pre>
  1:  var wsdlFileURL = "http://localhost:8080/imart/services/SampleMemberInfoOperatorService?wsdl";
  2:  
  3:  var wsUserID       = "ueda";
  4:  var wsPassword     = "ueda";
  5:  var wsLoginGroupID = "default";
  6:  
  7:  /&#42;&#42;
  8:   &#42; SOAPClientオブジェクトを利用してWebサービスを呼び出すサンプルです。
  9:   &#42;/
 10:  function init(args){
 11:      add();
 12:      findAll();
 13:  }
 14:  
 15:  /&#42;&#42;
 16:   &#42; メンバー情報を追加します。
 17:   &#42;/
 18:  function add(){
 19:      
 20:      //&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;
 21:      // ステップ１：WSDLを指定して SOAPClientオブジェクト のインスタンスを生成
 22:      //&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;
 23:      try {
 24:          var soapClient = new SOAPClient(wsdlFileURL);
 25:          Debug.print("ステップ１ 完了");
 26:      } 
 27:      catch(ex) {
 28:          Debug.browse("エラーが発生しました。", ex);
 29:      }
 30:  
 31:  
 32:      //&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;
 33:      // ステップ２：Webサービスを呼び出すソースコードのサンプルを表示
 34:      //&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;
 35:      var sampleCode = soapClient.getSampleCode("add");
 36:      var msg  = "ステップ２ 完了。" + "\n";
 37:          msg += "Webサービスを呼び出すソースコードのサンプルが表示されました。"                    + "\n";
 38:          msg += "pages/src/sample/web_service/client/member_info_operator_client.jsの35, 40行目を" + "\n";
 39:          msg += "コメントアウトしてステップ３を実行してください";
 40:      Debug.browse(msg, sampleCode);
 41:      
 42:      
 43:      //&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;
 44:      // ステップ３： Webサービスの呼び出し （ステップ２で出力された内容をカスタマイズ）
 45:      //&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;
 46:      // ↓↓↓↓　コピー＆ペースト (ここから)　↓↓↓↓
 47:      //-------------------------------
 48:      // Sample Data : 'wsUserInfo'
 49:      //-------------------------------
 50:      var wsUserInfo =
 51:      /&#42; Object &lt;WSUserInfo&gt; &#42;/
 52:      {
 53:          /&#42; String &#42;/
 54:          "password" : WSAuthDigestGenerator4WSSE.getDigest(wsUserID, wsPassword) ,
 55:  
 56:          /&#42; String &#42;/
 57:          "authType" : WSAuthDigestGenerator4WSSE.getAuthType(),
 58:  
 59:          /&#42; String &#42;/
 60:          "userID" : wsUserID,
 61:  
 62:          /&#42; String &#42;/
 63:          "loginGroupID" : wsLoginGroupID
 64:      };
 65:      
 66:      //-------------------------------
 67:      // Sample Data : 'member'
 68:      //-------------------------------
 69:      var member =
 70:      /&#42; Object &lt;Member&gt; &#42;/
 71:      {
 72:          /&#42; Boolean &#42;/
 73:          "married" : true,
 74:  
 75:          /&#42; Number &#42;/
 76:          "age" : 123,
 77:  
 78:          /&#42; String &#42;/
 79:          "name" : "prop_name",
 80:  
 81:          /&#42; String &#42;/
 82:          "id" : "prop_id",
 83:  
 84:          /&#42; Array &lt;Member[]&gt; &#42;/
 85:          "children" : [
 86:  
 87:          ],
 88:  
 89:          /&#42; Date (Thu Jun 19 2008 12:34:56 GMT+0900 (JST)) &#42;/
 90:          "birthDate" : new Date(1213846496000)
 91:      };    
 92:      // ↑↑↑↑　コピー＆ペースト (ここまで)　↑↑↑↑
 93:  
 94:  
 95:      //-------------------------------
 96:      // Webサービスの呼び出し
 97:      //-------------------------------
 98:      try{
 99:          var result = soapClient.add(wsUserInfo, member);
100:      }
101:      catch(soapFault){
102:          Debug.browse("エラーが発生しました。", soapFault);
103:      }
104:  
105:      Debug.browse("ステップ３ 完了",
106:                   "追加しました。",
107:                   "結果：" + result);
108:  }
</pre>
 * 				</font>
 * 			</td>
 * 		</tr>
 * 	</table>
 * 
 * <br/>
 * 
 * <a name="APIListStart"/>
 * 
 * @scope public
 * @name SOAPClient
 * @since 7.0 
 */
public class SOAPClientObject extends ScriptableObject implements Cloneable, java.io.Serializable {

	private static final String TARGET_AXIS2_VERSION = "1.4";
	
	private static final Logger _logger = Logger.getLogger();
	private static final String STUB_SUFFIX = "Stub";

	private static StubGenerationMode _stubGenMode = StubGenerationMode.Once;
	private static File _soapClientWorkDir = ScriptScopeBuilderManager.getBuilder().getOutputDirectory();
	private static String _jsPath4SOAPClientHelper = "jssp/script/api/soap_client_helper";
	private static boolean _javacVerbose = false;
	private static String _javacEncoding = "UTF-8";
	
	private static ThreadLocal<ClassLoader> _soapClientClassLoaderThreadLocal = new ThreadLocal<ClassLoader>();
	
	static {
		initializeSOAPClientWorkDir(null);
		initializeStubGenerationMode(null);
		initializeJsPath4SOAPClientHelper(null);
		initializeJavacEncoding(null);
		initializeJavacVerbose(null);
		
		try {
			initializeExcludeClass4getSampleCode(null);
		}
		catch (ClassNotFoundException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	@Override
	public String getClassName() {
		return "SOAPClient";
	}
	
	/**
	 * WSDLを URL文字列 で指定するコンストラクタ。<br/>
	 * <br/>
	 * SOAPClientは、インスタンス生成時に以下の処理を行います。
	 * <ol>
	 * 	<li>WSDLの解析</li>
	 * 	<li>Webサービス・クライアントとなるJavaスタブ・クラスのソース生成</li>
	 * 	<li>Javaスタブ・クラスのコンパイル</li>
	 * 	<li>Javaスタブ・クラスを呼び出すJavaScriptソースの生成</li>
	 * </ol>
	 * 
	 * 上記で示した一連の処理は、インスタンス生成時に一度だけ行われます。<br/>
	 * 2回目以降のインスタンス生成では上記処理が省略され、1回目に作成されたスタブが利用されます。<br/>
	 * なお、この動作は、初期設定時の動作です。設定を変更する方法は <a href="#setting">こちら</a> を参照してください。<br/>
	 * <br/>
	 * WSDL内に複数のWebサービスが定義されている場合は、第2引数「serviceName」に実行したいWebサービス名を指定します。<br/>
	 * <br/>
	 * WSDLのポート要素を指定したい場合は、第3引数「portName」に利用したいポート名を指定します。<br/>
	 * <br/>
	 * WSDL内に定義されているエンドポイントとは異なるエンドポイントを利用する場合は、第4引数「endpoint」に指定してください。<br/>
	 * serviceNameやportNameを指定しない場合は、以下のようにエンドポイントを設定してください。
	 * 
	 * <table border="1">
	 * <tr><td>
	 * <font size="-1.5"><pre>
	 * var <font color="blue">wsdlUrl</font>  = "http://localhost:8080/imart/services/SampleMemberInfoOperatorService?wsdl";
	 * var <font color="red">endpoint</font> = "http://localhost:9999/imart/services/SampleMemberInfoOperatorService";
	 * var soapClient = new SOAPClient(<font color="blue">wsdlUrl</font>, null, null, <font color="red">endpoint</font>);</pre></font>
	 * </td></tr>
	 * </table>
	 * 
	 * <br/>
	 * なお、WSDLファイルは、<b>Storage Service</b>上に存在するWSDLファイルを利用することも可能です。
	 * 詳しくは、<a href="#SOAPClientVirtualFileStringStringString">WSDLを VirtualFile オブジェクトで指定するコンストラクタ</a>
	 * を参照してください。
	 * <br/>
	 * 
	 * @scope public
	 * 
	 * @param wsdlUrl String WSDLを示すURL
	 * @param serviceName ?String Webサービス名 (WSDL内に複数のWebサービスが定義されている場合は必須)
	 * @param portName ?String Webサービスのポート名
	 * @param endpoint ?String エンドポイントのURL
	 */
	public static Object jsConst_SOAPClient(String dummy1){
	    // 複数コンストラクタ用ダミー
	    return null;
	}

	/**
	 * WSDLを {@link File} オブジェクトで指定するコンストラクタ。<br/>
	 * <br/>
	 * このコンストラクタを利用すると、Application Runtime上に保存されているWSDLファイルを利用することが可能です。<br/>
	 * 第1引数に、WSDLファイルを指し示している File オブジェクトを指定してください。
	 * 
	 * そのほかの動作は、<a href="#SOAPClientStringStringStringString">「WSDLをURLで指定するコンストラクタ」</a>と同等です。
	 * 
	 * @scope public
	 * 
	 * @param wsdlOnAppRuntime File WSDLを示すFileオブジェクト
	 * @param serviceName ?String Webサービス名 (WSDL内に複数のWebサービスが定義されている場合は必須)
	 * @param portName ?String Webサービスのポート名
	 * @param endpoint ?String エンドポイントのURL
	 */
	public static Object jsConst_SOAPClient(String dummy1, String dummy2){
	    // 複数コンストラクタ用ダミー
	    return null;
	}

	/**
	 * WSDLを {@link VirtualFile} オブジェクトで指定するコンストラクタ。<br/>
	 * <br/>
	 * このコンストラクタを利用すると、Storage Service 上に保存されているWSDLファイルを利用することが可能です。<br/>
	 * 第1引数に、WSDLファイルを指し示している VirtualFile オブジェクトを指定してください。<br/>
	 * <br/>
	 * なお、WSDLファイルの解析時に、別のWSDLファイル や 別のXMLスキーマファイル
	 * （以降、「XSDファイル」と呼ぶ）が必要な場合は、
	 * コンストラクタに指定したWSDLファイルと同じディレクトリ（または、設定可能なサブディレクトリ）にそれらのファイルを保存してください。<br/>
	 * <br/>
	 * 上記は、SOAPメッセージの送受信時に使われる要素が、指定したWSDLファイル内ではなく、
	 * 別のXSDファイルで定義されている場合が当てはまります。<br/>
	 * <br/>
	 * WSDLファイルの解析時に必要なファイルの拡張子や、必要なファイルが格納されているディレクトリ名の設定方法は、
	 * <a href="#setting">こちら</a> を参照してください。<br/>
	 * <br/>
	 * そのほかの動作は、<a href="#SOAPClientStringStringStringString">「WSDLをURLで指定するコンストラクタ」</a>と同等です。
	 * 
	 * @scope public
	 * 
	 * @param wsdlOnStorage VirtualFile WSDLを示す VirtualFile オブジェクト
	 * @param serviceName ?String Webサービス名 (WSDL内に複数のWebサービスが定義されている場合は必須)
	 * @param portName ?String Webサービスのポート名
	 * @param endpoint ?String エンドポイントのURL
	 */
	public static Object jsConst_SOAPClient(String dummy1, String dummy2, String dummy3){
	    // 複数コンストラクタ用ダミー
	    return null;
	}
	
	
	public static Object jsConstructor(Context cx, Object[] args, Function ctorObj, boolean inNewExpr) {		
		if (inNewExpr == false) {
			throw new IllegalStateException("This constructor must be invoked via \"new\" operator.");
		}

		String wsdlUri = null;
		String portName = null;
		String serviceName = null;
		String targetEndpoint = null;

		int idx = 0;
		if(args.length > idx && args[idx] != null){
			wsdlUri = getURI4WSDL(args[idx]);
		}
		else{
			throw new IllegalArgumentException("Please set one or more arguments.");
		}

		idx = 1;
		if(args.length > idx && args[idx] != null){
			if(!(args[idx] instanceof Undefined) && Context.toString(args[idx]).length() != 0){
				serviceName = Context.toString(args[idx]);
			}
		}

		idx = 2;
		if(args.length > idx && args[idx] != null){
			if(!(args[idx] instanceof Undefined) && Context.toString(args[idx]).length() != 0){
				portName = Context.toString(args[idx]);
			}
		}
		
		idx = 3;
		if(args.length > idx && args[idx] != null){
			if(!(args[idx] instanceof Undefined) && Context.toString(args[idx]).length() != 0){
				targetEndpoint = Context.toString(args[idx]);
			}
		}
		
		SOAPClientObject instance = new SOAPClientObject(wsdlUri, serviceName, portName, targetEndpoint); 
		return instance;
	}

	/**
	 * @param object
	 * @return
	 */
	private static String getURI4WSDL(Object object) {
		String wsdlUri = null;
		
		if(object instanceof String){
			wsdlUri = Context.toString(object);
		}
		else if(object instanceof FileAccessObject){
			FileAccessObject wsdlFileAccessObject = (FileAccessObject) object;
			File wsdlFile= new File(wsdlFileAccessObject.jsFunction_path());
			wsdlUri = wsdlFile.toURI().toString();
		}
		else {
			throw new IllegalStateException("WSDL must be specified.: " + object);
		}

		return wsdlUri;
	}


	public SOAPClientObject(){
	}

	/**
	 * @param wsdlUri
	 * @param serviceName
	 * @param portName
	 * @param targetEndpoint
	 */
	public SOAPClientObject(String wsdlUri,
							String serviceName,
							String portName,
							String targetEndpoint) {

		ClassLoader origin = Thread.currentThread().getContextClassLoader();

		if(StubGenerationMode.Never.equals(_stubGenMode) == false){
			
			// 「JSの実行スレッドに紐づくクラスローダ」と「jsToJavaBean()実行時のクラスローダ」を同じにする
			ClassLoader oldExtendedClassLoader = _soapClientClassLoaderThreadLocal.get();
			
			if(oldExtendedClassLoader == null || StubGenerationMode.Everytime.equals(_stubGenMode)){
				ExtendedClassLoader classLoader4WsStub = new ExtendedClassLoader(origin);
				classLoader4WsStub.setLogLevel(Logger.Level.DEBUG);
				classLoader4WsStub.addClassPath(_soapClientWorkDir);
				
				Thread.currentThread().setContextClassLoader(classLoader4WsStub);
				_soapClientClassLoaderThreadLocal.set(classLoader4WsStub);
			}
			else{
				Thread.currentThread().setContextClassLoader(oldExtendedClassLoader);
			}
		}

		try {
			// コンフィグ生成
			CodeGenConfiguration config = getCodeGenConfiguration(wsdlUri, _soapClientWorkDir, serviceName, portName);
			
			// Stubクラス名取得
			String stubClassName = getStubClassName(config);
			
			if(isEnableStubGenAndCompile(stubClassName)){
				synchronized(MONITOR_4_JavaStub){
					if(isEnableStubGenAndCompile(stubClassName)){ // ←もう一度チェック

						// Stubソース生成
						generateStub(wsdlUri, _soapClientWorkDir, serviceName, portName, stubClassName);
		
						// Stubクラスコンパイル
						int result = compileStub(stubClassName, _soapClientWorkDir, _soapClientWorkDir);
						if (result != 0) {
							throw new IllegalStateException("Compile Failed: " + stubClassName + " (result->" + result + ")");
						}
						
					}
				}
			}

			if(isEnableJsStubGen(stubClassName)){
				synchronized (MONITOR_4_JsStub){
					if(isEnableJsStubGen(stubClassName)){ // ←もう一度チェック
						
						// Stubクラスを呼び出すJSソース生成
						generateStubJS(wsdlUri, stubClassName, _soapClientWorkDir);
						
					}
				}
			}

			// JS Stubの実行
			executeStubJS(stubClassName, _soapClientWorkDir, targetEndpoint);
		}
		catch (Exception e) {
			_logger.error(e.getMessage(), e);
			throw Context.throwAsScriptRuntimeEx(e);
		}
		finally{
			if(StubGenerationMode.Never.equals(_stubGenMode) == false){
				Thread.currentThread().setContextClassLoader(origin);
			}
		}
	}

	/**
	 * @param stubClassName
	 * @return
	 */
	private boolean isEnableStubGenAndCompile(String stubClassName){
		if( StubGenerationMode.Everytime.equals(_stubGenMode)
			||
		    (
		    	StubGenerationMode.Once.equals(_stubGenMode) 
		    	&&
		    	isExistStubClass(stubClassName) == false
		    )
		){
			return true;
		}
		else{
			return false;
		}
		
	}
	private static Object MONITOR_4_JavaStub = new Object();
	
	/**
	 * @param stubClassName
	 * @return
	 */
	private boolean isEnableJsStubGen(String stubClassName){
		if( StubGenerationMode.Everytime.equals(_stubGenMode)
			||
			(
				StubGenerationMode.Once.equals(_stubGenMode)
				&&
				isExistStubJS(stubClassName, _soapClientWorkDir) == false
			) 
		){
			return true;
		}
		else{
			return false;
		}
	}
	private static Object MONITOR_4_JsStub = new Object();

	
	/**
	 * @return
	 * 
	 */
	public static ClassLoader getCurrentThreadClassLoader4SOAPClient() {
		ClassLoader cl = _soapClientClassLoaderThreadLocal.get();
		if(cl != null){
			return cl;
		}
		else{
			return Thread.currentThread().getContextClassLoader();
		}
	}
		
	/**
	 * @param classLoader
	 * @param stubClassName
	 * @param endpoint
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object newStubInstance(final ClassLoader classLoader, 
										 final String stubClassName, 
										 final String endpoint) 
							throws ClassNotFoundException, SecurityException, 
								   NoSuchMethodException, IllegalArgumentException, 
								   InstantiationException, IllegalAccessException, 
								   InvocationTargetException{
		
		ClassLoader cl = (classLoader != null) ?  classLoader : getCurrentThreadClassLoader4SOAPClient();
		
		Class<?> stubClass = cl.loadClass(stubClassName);
		if(endpoint == null){
			return stubClass.newInstance();
		}
		else{
			Constructor<?> stubConstructor = stubClass.getConstructor(String.class);
			return stubConstructor.newInstance(endpoint);
		}
	}
	

	/**
	 * @param arg
	 */
	protected static void initializeSOAPClientWorkDir(File arg) {
		String tagName = "work-dir";
		
		if(arg != null){
			_soapClientWorkDir = arg;
		}
		else {
			String workDir = getConfigValue(tagName);
			if(workDir != null){
				File dir = new File(workDir);
				
				if(dir.isAbsolute() == true){
					_soapClientWorkDir = dir;
				}
				else{
					_soapClientWorkDir = new File(HomeDirectory.instance(), dir.getPath());
				}
			}
		}
		
		if(_soapClientWorkDir == null){
			_soapClientWorkDir = ScriptScopeBuilderManager.getBuilder().getOutputDirectory();
		}
		
		_logger.debug("{} -> {}", tagName, _soapClientWorkDir);
	}

	
	/**
	 * @param arg
	 */
	protected static void initializeStubGenerationMode(StubGenerationMode arg) {
		String tagName = "mode";
		
		if(arg != null){
			_stubGenMode = arg;
		}
		else{
			String configValue = getConfigValue(tagName);
			
			if(configValue != null){
				configValue = configValue.toUpperCase(); // 大文字・小文字を区別しない
	
				StubGenerationMode[] modes = StubGenerationMode.values();
				for(StubGenerationMode mode : modes){
					String modeString = mode.toString().toUpperCase(); // 大文字・小文字を区別しない
					if(configValue.equals(modeString)){
						_stubGenMode = mode;
						break;
					}
				}
			}
		}

		if(_logger.isDebugEnabled()){
			_logger.debug("{} -> {}", tagName, StubGenerationMode.class.getSimpleName() + "." + _stubGenMode);
		}
	}

	/**
	 * @param arg
	 */
	protected static void initializeJsPath4SOAPClientHelper(String arg) {
		String tagName = "helper";
		
		if(arg != null){
			_jsPath4SOAPClientHelper = arg;
		}
		else{
			String configValue = getConfigValue(tagName);
			
			if(configValue != null){
				_jsPath4SOAPClientHelper = configValue;
			}
		}
		
		_logger.debug("{} -> {}", tagName, _jsPath4SOAPClientHelper);
	}

	/**
	 * @param arg
	 */
	protected static void initializeJavacEncoding(String arg) {
		String tagName = "javac-encoding";
		
		if(arg != null){
			_javacEncoding = arg;
		}
		else{
			String configValue = getConfigValue(tagName);
			
			if(configValue != null){
				_javacEncoding = configValue;
			}
		}
		
		_logger.debug("{} -> {}", tagName, _javacEncoding);
		
	}
	
	/**
	 * @param arg
	 */
	protected static void initializeJavacVerbose(Boolean arg) {
		String tagName = "javac-verbose";
		
		if(arg != null){
			_javacVerbose = arg;
		}
		else{
			String configValue = getConfigValue(tagName);
			
			if(configValue != null){
				_javacVerbose = Boolean.valueOf(configValue);
			}
		}
		
		_logger.debug("{} -> {}", tagName, _javacVerbose);
		
	}

	/**
	 * @param list
	 * @throws ClassNotFoundException 
	 */
	protected static void initializeExcludeClass4getSampleCode(Collection<String> list) throws ClassNotFoundException {
		
		Collection<String> configValues = list;

		if(configValues == null){
			configValues = new ArrayList<String>();
		}
		
		// PLAN コンフィグファイルにて、インスタンス生成の対象外とするクラスを設定可能にする
		// デフォルト値
		configValues.add("org.apache.axis2.databinding.types.URI");
		
		for(String className : configValues){
			Class<?> clazz = Class.forName(className);
			
			// サンプルデータ設定済みインスタンスの生成の対象外とする
			JavaScriptUtility.addExcludeClass4newInstanceFilledSampleData(clazz);

			_logger.debug("initializeExcludeClass4getSampleCode -> {}", clazz);
		}
	}

	/**
	 * @param wsdlLocationURI
	 * @param outputLocation
	 * @param serviceName
	 * @param portName
	 * @return
	 */
	private CodeGenConfiguration getCodeGenConfiguration(String wsdlLocationURI, 
														  File outputLocation,
														  String serviceName,
														  String portName) {
		try{
			CodeGenerationEngine engine = getCachedCodeGenEngine(wsdlLocationURI, outputLocation, serviceName, portName);
			CodeGenConfiguration codeGenConfiguration = engine.getConfiguration();
			return codeGenConfiguration;
		}
		catch (CodeGenerationException cge) {
			throw Context.throwAsScriptRuntimeEx(cge);
		}
	}

	/**
	 * CodeGenerationEngineの取得。（キャッシュあり）
	 * 
	 * @param wsdlLocationURI
	 * @param outputLocation
	 * @param serviceName
	 * @param portName
	 * @return
	 * @throws CodeGenerationException
	 */
	private CodeGenerationEngine getCachedCodeGenEngine(String wsdlLocationURI, 
													  	File outputLocation,
													  	String serviceName,
													  	String portName ) throws CodeGenerationException {
		
		String enginesKey = wsdlLocationURI + "|" + serviceName + "|" + portName;
		
		CodeGenerationEngine engine = cache4codeGenEngine.get(enginesKey);
		
		if(StubGenerationMode.Everytime.equals(_stubGenMode) || engine == null){
			synchronized(cache4codeGenEngine){
				// 引数を作成				
				String[] args = generateArgs(wsdlLocationURI, outputLocation, serviceName, portName);
				
				// CodeGenerationEngineを生成 （）
				CommandLineOptionParser commandLineOptionParser = new CommandLineOptionParser(args);
				engine = new CodeGenerationEngine(commandLineOptionParser);
				
				// キャッシュ
				cache4codeGenEngine.put(enginesKey, engine);
			}
		}

		return engine;
	}
	private static Map<String, CodeGenerationEngine> cache4codeGenEngine = new HashMap<String, CodeGenerationEngine>();

	/**
	 * @param wsdlLocationURI
	 * @param outputLocation
	 * @param portName
	 * @param serviceName
	 * @return
	 */
	private String[] generateArgs(String wsdlLocationURI, File outputLocation,
			  					  String serviceName, String portName) {
		
		List<String> argsList = new ArrayList<String>();
		
		argsList.add("-" + CommandLineOptionConstants.WSDL2JavaConstants.OUTPUT_LOCATION_OPTION_LONG);
		argsList.add(outputLocation.getAbsolutePath());
		argsList.add("-" + CommandLineOptionConstants.WSDL2JavaConstants.WSDL_LOCATION_URI_OPTION);
		argsList.add(wsdlLocationURI);
		argsList.add("-" + CommandLineOptionConstants.WSDL2JavaConstants.SOURCE_FOLDER_NAME_OPTION_LONG);
		argsList.add(""); // ←「src」ディレクトリを作らない
		argsList.add("-" + CommandLineOptionConstants.WSDL2JavaConstants.NO_BUILD_XML_OPTION_LONG);
		argsList.add("-" + CommandLineOptionConstants.WSDL2JavaConstants.OVERRIDE_OPTION_LONG);
		argsList.add("-" + CommandLineOptionConstants.WSDL2JavaConstants.CODEGEN_SYNC_ONLY_OPTION_LONG);
		
		if(serviceName != null){
			argsList.add("-" + CommandLineOptionConstants.WSDL2JavaConstants.SERVICE_NAME_OPTION_LONG);
			argsList.add(serviceName);
		}

		if(portName != null){
			argsList.add("-" + CommandLineOptionConstants.WSDL2JavaConstants.PORT_NAME_OPTION_LONG);
			argsList.add(portName);
		}
		
		_logger.trace("argsList: {}", argsList);
		
		String[] args = argsList.toArray(new String[argsList.size()]);
		return args;
	}
	
	private String getStubClassName(CodeGenConfiguration codeGenConfiguration) {
		// 【注意】  Webサービスを指定して呼び出す場合は、SOAPClientのコンストラクタ引数にてそのサービス名を指定してください。
		
		// スタブのクラス名を取得
		// AxisServiceが複数存在しても、特定の一つのWebサービスに対するスタブを対象とする。
		// (∵スタブソース生成、および、コンパイルの同期化を行うために、
		//　　　　　JSソースで「new SOAPClient()」とした際に呼び出せるWebサービスは一つとしている)
		// なお、特定の一つのWebサービスとは、codeGenConfiguration.getAxisService()で取得可能なWebサービスとします。
		AxisService axisService = codeGenConfiguration.getAxisService();
		String packageName = URLProcessor.makePackageName(codeGenConfiguration.getTargetNamespace());
		String stubName = JavaUtils.isJavaKeyword(axisService.getName()) ?
							JavaUtils.makeNonJavaKeyword(axisService.getName()) + STUB_SUFFIX :
							JavaUtils.capitalizeFirstChar(JavaUtils.xmlNameToJava(axisService.getName())) + STUB_SUFFIX;

		// スタブのクラス名をFQCNで生成
		String stubFQCN = packageName + "." + stubName;
		return stubFQCN;
	}

	
	private static String getConfigValue(String tagName) {
		JSSPConfigHandler config = JSSPConfigHandlerManager.getConfigHandler();
		NodeList list = config.getJavaScriptAPI4ClassConfig(SOAPClientObject.class.getName());
		
		for(int idx = 0; idx < list.getLength(); idx++){
			Element elem = (Element) list.item(idx);
			if(tagName.equals(elem.getTagName())){
				return elem.getTextContent();
			}
		}
		
		return null;
	}

	private boolean isExistStubClass(String stubFQCN) {
		// スタブクラスの存在チェック
		boolean isExistStubClass = false;
		try {
			Class<?> clazz = getCurrentThreadClassLoader4SOAPClient().loadClass(stubFQCN);
			if(clazz != null){
				isExistStubClass = true;
			}
		}
		catch (ClassNotFoundException e) { /* Do Nothing */ }
		return isExistStubClass;
	}

	private boolean isExistStubJS(String stubClassName, File srcDir) {

		ScriptScope scope;
		try {
			scope = getScriptScope(stubClassName, srcDir);
			if(scope != null){
				return true;
			}
			else{
				return false;
			}
		}
		catch (Exception e) {
			_logger.trace(e.getMessage(), e);
			return false;
		}
	}

	private ScriptScope getScriptScope(String stubClassName, File srcDir) 
								throws FileNotFoundException,
										InstantiationException, 
										IllegalAccessException, 
										IOException,
										ClassNotFoundException, 
										JavaScriptException {
		
		ScriptScopeBuilder builder = ScriptScopeBuilderManager.getBuilder();
		String source = stubClassName.replace(".", File.separator);
		ScriptScope scope = builder.getScriptScope(source, srcDir);
		return scope;
	}

	/**
	 * Javaスタブのソースコード生成
	 * @param wsdlLocationURI
	 * @param outputLocation
	 * @param serviceName
	 * @param portName
	 * @param stubClassName
	 * @throws CodeGenerationException
	 */
	protected void generateStub(String wsdlLocationURI, 
											  File outputLocation,
											  String serviceName,	
											  String portName,
											  String stubClassName) throws CodeGenerationException  {
		_logger.info("Generate Stub(Java): {}", stubClassName);
		
		// エンジン取得
		CodeGenerationEngine engine = getCachedCodeGenEngine(wsdlLocationURI, outputLocation, serviceName, portName);
		
		// Javaスタブのソースコード生成
		engine.generate();
		
	}

	/**
	 * Javaスタブをコンパイル
	 * 
	 * @param className
	 * @param srcDir
	 * @param destDir
	 * @return
	 * @throws IOException
	 */
	protected int compileStub(String className, File srcDir, File destDir) throws IOException{
	    
		if(destDir.exists() == false){
			destDir.mkdirs();
		}
		
		ArrayList<String> argsList = new ArrayList<String>();		
		argsList.add("javac"); 
	    
		// すべてのデバッグ情報を生成する
		argsList.add("-g"); 
				
		// 警告を発生させない
		argsList.add("-nowarn");
		
		// 生成されたクラスファイルを格納する位置を指定する
		argsList.add("-d");
		argsList.add("\"" + destDir.getAbsolutePath() + "\"");

		// 入力ソースファイルを検索する位置を指定する
		argsList.add("-sourcepath");
		argsList.add("\"" + srcDir.getAbsolutePath() + "\"");

		// ソースファイルが使用する文字エンコーディングを指定する
		argsList.add("-encoding");
		argsList.add(_javacEncoding);

		// ユーザクラスファイルを検索する位置を指定する
		String classpath = getAxis2ClassPath();
		argsList.add("-cp");
		argsList.add(classpath);
			    
	    // コンパイル対象ソースファイル名に変換
		String source = className.replace(".", File.separator) + ".java";
		argsList.add(source);

		// コンパイラの動作についてメッセージを出力する
		if(_javacVerbose){
			argsList.add("-verbose");
		}
		
		String[] args;
		
		if(!isWindows()){
			StringBuilder unix = new StringBuilder();
			unix.append("cd ");
			unix.append(srcDir.getAbsolutePath());
			unix.append(";");
			
			for (int i = 0; i < argsList.size(); i++) {
				unix.append(" ");
				unix.append(argsList.get(i));
			}
			args = new String[3];
			args[0] = "/bin/sh";
			args[1] = "-c";
			args[2] = unix.toString();
	    	_logger.debug("Compile Command(UNIX) -> {} {} {}", (Object[]) args);
		}
		else{
			args = argsList.toArray(new String[argsList.size()]);
	    	_logger.debug("Compile Command(Windows) -> {}", argsList);
		}


		// コンパイル実行
    	_logger.info("Compile Stub(Java): {}{}{}", new Object[]{srcDir, File.separator, source});
    	_logger.debug("Compile classpath -> '{}'", classpath);
    	Runtime runtime = Runtime.getRuntime();
	    
	    // エラー内容出力
	    Process process = runtime.exec(args, null, srcDir);
	    
	    int status = -1;
	    try {
		    if (process != null) {
		    	InputStream inputStream = process.getInputStream();
		    	InputStream errorStream = process.getErrorStream();

		    	waitForErrors(System.err, inputStream, errorStream);
		    }

		    status = process.waitFor();
		}
		catch (Exception e) {
			_isDead4CompileProcess = true;
			_logger.trace(e.getMessage(), e);
		}
		
		return status;
	}

	private static final String LINE_SEP = System.getProperty("line.separator");

	private static boolean debugFlg = true;
	private static final String JS_LF = "\\n";
	private static final String outerSeparator = "        str += \"************************************************************\" + \"" + JS_LF + "\"";
	private static final String innerSeparator = "        str += \"//-------------------------------\" + \"" + JS_LF + "\"";
	private static final String name4JavaScriptUtility = "Packages." + JavaScriptUtility.class.getName();
	private static final String name4SOAPClientObject = "Packages." + SOAPClientObject.class.getName();
	private static final String code4ClassLoader = name4SOAPClientObject + ".getCurrentThreadClassLoader4SOAPClient();";

	/**
	 * JSスタブのソースコード生成
	 * 
	 * @param wsdlUri
	 * @param stubClassName
	 * @param destDir
	 * @throws IOException
	 * @throws IntrospectionException
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	protected void generateStubJS(String wsdlUri, String stubClassName, File destDir) 
									throws IOException, IntrospectionException, ClassNotFoundException, 
										   NoSuchFieldException, IllegalArgumentException, 
										   IllegalAccessException, XPathExpressionException, 
										   ParserConfigurationException, SAXException {

		Class<?> stubClass = getCurrentThreadClassLoader4SOAPClient().loadClass(stubClassName);
		
		// PLAN CallBackHandlerの扱い
		// publicなメソッドを呼び出し対象のオペレーションとする。(先頭が「start」のメソッドは対象外)
		List<Method> methods4operation = new ArrayList<Method>();
		for(Method m : stubClass.getDeclaredMethods()){
			if(Modifier.isPublic(m.getModifiers()) && m.getName().indexOf("start") != 0){
				methods4operation.add(m);
			}
		}
		
		// PLAN JSのLoggerを利用したトレース文
		StringBuilder strBuilder4source = new StringBuilder();
		StringBuilder strBuilder4usage = new StringBuilder();
		
		// ヘッダ
		generateFunc4Header(wsdlUri, strBuilder4source, stubClass);
		 
		// init() 関数
		generateFunc4init(stubClassName, strBuilder4source);

		// Webサービス・オペレーション名取得関数
		generateFunc4getOperationNames(strBuilder4source, methods4operation);
		
		// サンプルコード表示関数（全オペレーション）
		generateFunc4getSampleCodeAll(strBuilder4source);

		// Webサービス・オペレーション実行関数
		generateFunc4executeOperationFuncs(stubClass, wsdlUri, strBuilder4source, strBuilder4usage, methods4operation);
		
		// サンプルコードの表示 （各オペレーション）
		generateFunc4getSampleCode(strBuilder4source, strBuilder4usage);
		
		
		File jsFile = new File(destDir, stubClassName.replace(".", File.separator) + ".js");

		// 親ディレクトリを作成
		File parent = jsFile.getParentFile();
		if(parent.exists() == false){
			parent.mkdirs();
		}

		jsFile.delete();
		jsFile.createNewFile();
		
		_logger.info("Generate Stub(JS): {}", jsFile.getAbsoluteFile());		
		Writer writer = new FileWriter(jsFile);
		writer.write(strBuilder4source.toString());
		writer.close();
	}

	/**
	 * ヘッダ
	 */
	private void generateFunc4Header(String wsdlUri, StringBuilder source, Class<?> stubClass) {
		source.append("/** ")																		.append(LINE_SEP);
		source.append(" * This is " + this.getClassName() + ": \"" + stubClass.getName() + "\"")	.append(LINE_SEP);
		source.append(" * ")																		.append(LINE_SEP);
		source.append(" * This file was generated automatically on " + new Date())					.append(LINE_SEP);
		source.append(" */")																		.append(LINE_SEP);
		source.append("var wsdlUri     = \"" + wsdlUri + "\";")										.append(LINE_SEP);
		source.append("var classLoader = " + code4ClassLoader)										.append(LINE_SEP);
		source.append(LINE_SEP);
		source.append("load('" + _jsPath4SOAPClientHelper + "');")									.append(LINE_SEP);
	}

	/**
	 * init() 関数
	 */
	private void generateFunc4init(String stubClassName, StringBuilder source) {
		source.append(LINE_SEP);
		source.append("function init( targetEndpoint ) {")											.append(LINE_SEP);
		source.append("    var endpoint = (targetEndpoint == undefined) ? null : targetEndpoint;")	.append(LINE_SEP);
		source.append("    this.stub = " + name4SOAPClientObject);
		source.append(							".newStubInstance(classLoader, ");
		source.append(											"\"" + stubClassName + "\", ");
		source.append(											"endpoint);")						.append(LINE_SEP);
		source.append("}")																			.append(LINE_SEP);
	}

	/**
	 * Webサービス・オペレーション名取得関数
	 */
	private void generateFunc4getOperationNames(StringBuilder source, List<Method> methods4operation) {
		source.append(LINE_SEP);
		source.append("function getOperationNames() {")		.append(LINE_SEP);
		source.append("    return [");
		
		boolean firstFlg = true;
		Iterator<Method> it4operation = methods4operation.iterator();
		while(it4operation.hasNext()){
			Method operation = it4operation.next();
			String operationName = "\"" + operation.getName() + "\"";
			
			if(firstFlg == true){
				firstFlg = false;
				source.append(operationName); 
			}
			else{
				source.append(", " + operationName);
			}
		}
		
		source.append("];")									.append(LINE_SEP);
		source.append("}")									.append(LINE_SEP);
	}

	/**
	 * サンプルコード表示関数（全オペレーション）
	 */
	private void generateFunc4getSampleCodeAll(StringBuilder source) {
		source.append(LINE_SEP);
		source.append("function getSampleCodeAll(){")								.append(LINE_SEP);
		source.append("    var names = getOperationNames();")						.append(LINE_SEP);
		source.append(LINE_SEP);
		source.append("    var str = '';")											.append(LINE_SEP);
		source.append("    var sourceCode = '';")									.append(LINE_SEP);
		source.append(     outerSeparator)											.append(LINE_SEP);
		source.append("        str += 'All oparation names:' + '\\n';")				.append(LINE_SEP);
		source.append(LINE_SEP);
		source.append("    var max = names.length;")								.append(LINE_SEP);
		source.append("    for(var idx = 0; idx < max; idx++){")					.append(LINE_SEP);
		source.append("        str += '  [' + idx + ']: ' + names[idx] + '()\\n';")	.append(LINE_SEP);
		source.append("        sourceCode += getSampleCode(names[idx]) + '\\n';")	.append(LINE_SEP);
		source.append("    }")														.append(LINE_SEP);
		source.append(LINE_SEP);
		source.append(     outerSeparator)											.append(LINE_SEP);
		source.append("    return sourceCode + str;")								.append(LINE_SEP);

		source.append("}")															.append(LINE_SEP);
	}

	/**
	 * Webサービス・オペレーション実行関数
	 */
	private void generateFunc4executeOperationFuncs(Class<?> stubClass, String wsdlUri, StringBuilder source, StringBuilder usage, List<Method> methods4operation) throws ParserConfigurationException,
			IOException, XPathExpressionException, SAXException, IntrospectionException {
		Iterator<Method> it4operation = methods4operation.iterator();
		while(it4operation.hasNext()){
			Method operation = it4operation.next();
			String operatinoName = operation.getName();
			Class<?>[] operationParamTypes = operation.getParameterTypes();
			Class<?> returnType = operation.getReturnType();

						source.append(LINE_SEP);
						source.append("function " + operatinoName + "() {")													.append(LINE_SEP);
						source.append("    try	{")																			.append(LINE_SEP);

			// ----------------
			// サンプルコード出力用
			// ----------------
			StringBuilder usage4Head = new StringBuilder();
			StringBuilder usage4Body = new StringBuilder();
			usage4Head.append("    if(operationName == \"" + operatinoName + "\"){")						.append(LINE_SEP);
			usage4Head.append(         outerSeparator)														.append(LINE_SEP);
			usage4Head.append("        str += \" Usage:\" + \"" + JS_LF + "\";")							.append(LINE_SEP);
			usage4Head.append( returnType.equals(void.class) ? 
							  "        str += \"    /* (void) */ soapClient." + operatinoName + "(" : 
							  "        str += \"    var result = soapClient." + operatinoName + "(" );
			
			if(operationParamTypes.length != 0){
				Class<?> operationParamType = operationParamTypes[0];
				String paramTypeName = operationParamType.getName();
				
						source.append("        var clazz          = classLoader.loadClass(\"" + paramTypeName + "\");")		.append(LINE_SEP);
						source.append("        var operationParam = clazz.newInstance();")									.append(LINE_SEP);
						
				// Webサービスのパラメータクラスのプロパティ順を取得。
				// （∵SOAPClient#オペレーション()に指定した引数の順序(=arguments オブジェクト)とあわせるため）
				List<String> propNameList = getPropertyNameList(operationParamType, wsdlUri);
				Iterator<String> propNameIt = propNameList.iterator();
				
				// Webサービスのパラメータクラスを取得
				int idx = 0;
				Map<String, PropertyDescriptor> beanPropMap = JavaScriptUtility.getBeanPropertyMap(operationParamType);

				while(propNameIt.hasNext()){
					String propName = propNameIt.next();
					PropertyDescriptor propDescriptor = beanPropMap.get(propName);
					
					if(propDescriptor == null){
						// プロパティ名がPascal形式の場合(.Net対応)
						String propName4LowerCamel = propName.substring(0, 1).toLowerCase();
						if(propName.length() > 1){
							propName4LowerCamel = propName4LowerCamel.concat(propName.substring(1));
						}
						propDescriptor = beanPropMap.get(propName4LowerCamel);
						
						if(propDescriptor == null){
							throw new IllegalStateException(
									"'" + propName + "' is not found in " + operationParamType.getName() + ". " +
									"(WSDL=" + wsdlUri + ")");
						}
						else{
							propName = propName4LowerCamel;
						}
					}
					
					Method setter = propDescriptor.getWriteMethod();
					if(setter == null){
						continue;
					}

					Class<?> setterParamType = setter.getParameterTypes()[0];
					String setterParamTypeName = setterParamType.getName();

						source.append(LINE_SEP);
						source.append("        var jsArg_" + propName + "    = ");
						source.append(				"(arguments[" + idx + "] == undefined)");
						source.append(					" ? null : arguments[" + idx + "];")								.append(LINE_SEP);
					
						source.append("        var beanType_" + propName + " = '");
						source.append(				setterParamTypeName + "';")												.append(LINE_SEP);
					
						source.append("        var javaArg_" + propName + "  = ");
						source.append(				name4JavaScriptUtility + ".jsToJavaBean");
						source.append(					"(jsArg_" + propName + ", beanType_" + propName + ", classLoader);").append(LINE_SEP);
					
						source.append("        operationParam." + setter.getName() + "(");

					// Primitive型(∵JSの変数として保持すると、RhinoがWrapperクラスに変換するため、パラメータ型の相違で該当のsetterが呼び出せない)
					if(setterParamTypeName.equals("char")) {
						source.append(			"( new Packages.java.lang.Character(javaArg_" + propName + ") ).charValue()");
					}
					else if(setterParamTypeName.equals("double")) {
						source.append(			"( new Packages.java.lang.Double(javaArg_" + propName + ") ).doubleValue()");
					}
					else if(setterParamTypeName.equals("float")) {
						source.append(			"( new Packages.java.lang.Float(javaArg_" + propName + ") ).floatValue()");
					}
					else if(setterParamTypeName.equals("long")) {
						source.append(			"( new Packages.java.lang.Long(javaArg_" + propName + ") ).longValue()");
					}
					else if(setterParamTypeName.equals("int")) {
						source.append(			"( new Packages.java.lang.Integer(javaArg_" + propName + ") ).intValue()");
					}
					else if(setterParamTypeName.equals("short")) {
						source.append(			"( new Packages.java.lang.Short(javaArg_" + propName + ") ).shortValue()");
					}
					else if(setterParamTypeName.equals("byte")) {
						source.append(			"( new Packages.java.lang.Byte(javaArg_" + propName + ") ).byteValue()");
					}
					else if(setterParamTypeName.equals("boolean")) {
						source.append(			"( new Packages.java.lang.Boolean(javaArg_" + propName + ") ).booleanValue()");
					}
					// Primitive型以外
					else{
						source.append(			"javaArg_" + propName);
					}
					source.append(	   ");")																				.append(LINE_SEP);
					
					// ----------------
					// サンプルコード出力用
					// ----------------
					usage4Head.append( (idx == 0) ? "" : ", " );
					usage4Head.append(propName);

					usage4Body.append(		   LINE_SEP);
					usage4Body.append(		   innerSeparator)												.append(LINE_SEP);
					usage4Body.append("        str += \"// Sample Data : ");
					usage4Body.append(				"'" + propName + "'\" + \"" + JS_LF + "\";")			.append(LINE_SEP);
					usage4Body.append(		   innerSeparator)												.append(LINE_SEP);
					
					usage4Body.append("        var beanType_" + propName + " = ");
					usage4Body.append(				"'" + setterParamTypeName + "';")						.append(LINE_SEP);
					
					usage4Body.append("        var bean_" + propName + "     = ");
					usage4Body.append(				name4JavaScriptUtility + ".newInstanceFilledSampleData");
					usage4Body.append(						"(beanType_" + propName + ",");
					usage4Body.append(						" classLoader,");
					usage4Body.append(						" \"" + propName + "\");")						.append(LINE_SEP);
					
					usage4Body.append("        var js_" + propName + "       = ");
					usage4Body.append(				name4JavaScriptUtility + ".javaBeanToJS");
					usage4Body.append(						"(bean_" + propName + ");")						.append(LINE_SEP);
					
					usage4Body.append("            js_" + propName + "       = ");
					usage4Body.append(				"normalize(js_" + propName + ");")						.append(LINE_SEP);
					
					usage4Body.append("        str += \"var " + propName + " = \" + \"" + JS_LF + "\";")	.append(LINE_SEP);
					usage4Body.append("        str += ImJson.toJSONString(js_" + propName + ", " + debugFlg + ") + \";\" + ");
					usage4Body.append(				"\"" + JS_LF + "\";")									.append(LINE_SEP);
					
					usage4Body.append("        str += \"" + JS_LF + "\";")									.append(LINE_SEP);

					idx++;
				}
			}
			
			usage4Head.append("); \" + \"" + JS_LF + "\";")													.append(LINE_SEP);
			usage4Head.append("        str += \"" + JS_LF + "\";")											.append(LINE_SEP);
			usage4Body.insert(0, usage4Head);

			usage4Body.append(		   outerSeparator)														.append(LINE_SEP);
			usage4Body.append(		   LINE_SEP);
			usage4Body.append("        return str;")														.append(LINE_SEP);
			usage4Body.append("    }")																		.append(LINE_SEP);
			
			usage.append(usage4Body);

					source.append(LINE_SEP);

			if(returnType.equals(void.class)){
				// 引数が存在する場合
				if(operationParamTypes.length != 0){
					source.append("        this.stub." + operatinoName + "(operationParam);")								.append(LINE_SEP);
				}
				// 引数がない場合
				else{
					source.append("        this.stub." + operatinoName + "();")												.append(LINE_SEP);
				}
					source.append("        return;")																		.append(LINE_SEP);
			}
			else{
				List<String> getterNameList = new ArrayList<String>();
				
				Map<String, PropertyDescriptor> returnTypeProp = JavaScriptUtility.getBeanPropertyMap(returnType);
				Iterator<PropertyDescriptor> propDescIt = returnTypeProp.values().iterator();
				while(propDescIt.hasNext()){
					PropertyDescriptor propDesc = propDescIt.next();
					
					// setterが無いプロパティは除外 （例：「class」プロパティ）
					if(propDesc.getReadMethod() != null && propDesc.getWriteMethod() != null){
						getterNameList.add(propDesc.getReadMethod().getName());
					}
				}
				
				// 引数が存在する場合
				if(operationParamTypes.length != 0){
					source.append("        var javaResult = this.stub." + operatinoName + "(operationParam);")				.append(LINE_SEP);
				}
				// 引数がない場合
				else{
					source.append("        var javaResult = this.stub." + operatinoName + "();")							.append(LINE_SEP);
				}
				
				// 戻り値が単一の場合 
				if(getterNameList.size() == 1){
					String getterName = getterNameList.get(0);
					source.append("        var jsResult   = " + name4JavaScriptUtility + ".javaBeanToJS");
					source.append(								"(javaResult." + getterName + "());")						.append(LINE_SEP);
				}
				// 戻り値が複数の場合
				else{
					source.append("        var jsResult   = " + name4JavaScriptUtility + ".javaBeanToJS(javaResult);")		.append(LINE_SEP);
				}

					source.append("            jsResult   = normalize(jsResult);")											.append(LINE_SEP);
					source.append("        return jsResult;")																.append(LINE_SEP);
			}
			
					source.append("    }")																					.append(LINE_SEP);
					source.append("    catch(e){")																			.append(LINE_SEP);
					source.append("        Logger.getLogger('" + stubClass.getName() + "').error(e.message, e);")			.append(LINE_SEP);
					source.append(LINE_SEP);
					source.append("        if(e.javaException == undefined ")												.append(LINE_SEP);
					source.append("           ||")																			.append(LINE_SEP);
					source.append("           e.javaException.getClass().getName() != '" + AxisFault.class.getName()+ "'){").append(LINE_SEP);
					source.append("            throw e;")																	.append(LINE_SEP);
					source.append("        }")																				.append(LINE_SEP);
					source.append("        else{")																			.append(LINE_SEP);
					source.append("            var soapFault = new SOAPFault(e.javaException);")							.append(LINE_SEP);
					source.append("            throw soapFault;")															.append(LINE_SEP);
					source.append("        }")																				.append(LINE_SEP);
					source.append("    }")																					.append(LINE_SEP);
					source.append("}")																						.append(LINE_SEP);
		}
	}

	/**
	 * サンプルコードの表示 （各オペレーション）
	 */
	private void generateFunc4getSampleCode(StringBuilder source, StringBuilder usage) {
		source.append(LINE_SEP);
		source.append("function getSampleCode(operationName){")										.append(LINE_SEP);
		source.append(LINE_SEP);
		source.append("    if( isBlank(operationName) ){")											.append(LINE_SEP);
		source.append("        return getSampleCodeAll();")											.append(LINE_SEP);
		source.append("    }")																		.append(LINE_SEP);
		source.append(LINE_SEP);
		source.append("    var str = \"\";")														.append(LINE_SEP);
		source.append(LINE_SEP);
		source.append(	   usage); // Usageを追加
		source.append(LINE_SEP);
		source.append("    str = \"Operation '\" + operationName + \"()' is not defined.\";")		.append(LINE_SEP);
		source.append("    return str;")															.append(LINE_SEP);
		source.append("}")																			.append(LINE_SEP);
	}

	/**
	 * JSスタブのinit()関数実行
	 * 
	 * @param stubClassName
	 * @param srcDir
	 * @param targetEndpoint
	 * @throws JavaScriptException
	 * @throws FileNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	protected void executeStubJS(String stubClassName, File srcDir, String targetEndpoint)
						throws JavaScriptException, FileNotFoundException, 
							   InstantiationException, IllegalAccessException, 
							   IOException, ClassNotFoundException {

		ScriptScope scope = getScriptScope(stubClassName, srcDir);
		this.setPrototype(scope);

		// init()内でJavaのStubインスタンスを生成している
		scope.call(Context.enter(), "init", new Object[]{targetEndpoint});
	}

	/**
	 * Axis2のクラスパス解決
	 * @return
	 */
	protected String getAxis2ClassPath() {		
		if(classPath4Axis2 == null){
			synchronized (MONITOR_4_classPath4Axis2) {
				
				if(classPath4Axis2 == null){ // ←もう一度チェック
					
					// 環境変数
					String axis2HomeEnv = System.getenv("AXIS2_HOME");
					if(axis2HomeEnv != null){
						String axis2libDir = axis2HomeEnv + File.separator + "lib";
						classPath4Axis2 = getAxis2ClassPath(axis2libDir);
					}
					
					// WEB-INF/lib 配下のライブラリ
					if(classPath4Axis2 == null){
						HTTPContext context = HTTPContextManager.getInstance().getCurrentContext();
						if(context != null && context.getServletContext() != null){
							String axis2libDir = context.getServletContext().getRealPath("/WEB-INF/lib");
							classPath4Axis2 = getAxis2ClassPath(axis2libDir);
						}
					}
					
					// デフォルト値 → 「./lib」
					if(classPath4Axis2 == null){
						String axis2libDir = "." + File.separator + "lib";
						classPath4Axis2 = getAxis2ClassPath(axis2libDir);
					}
					
					if(classPath4Axis2 == null){
						throw new IllegalStateException("Axis2 " + TARGET_AXIS2_VERSION + " library is not found. Please set 'AXIS2_HOME' environment variable.");
					}
				}
			}
		}
		
		return classPath4Axis2;
	}
	private static String classPath4Axis2; // ex-> "/usr/local/axis2/lib/foo.jar:/usr/local/axis2/lib/hoge.jar"
	private static final Object MONITOR_4_classPath4Axis2 = new Object();


	/**
	 * @param axis2Home
	 * @return
	 */
	private String getAxis2ClassPath(String axis2Home) {
		if(axis2Home == null){
			return null;
		}
		
		String errMessage = "";
		
		File libDir = new File(axis2Home);
		File[] libFiles = libDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar") || name.endsWith(".zip");
			}
		});
		
		String axisVersionRaw = null;
		StringBuilder classpath = new StringBuilder();
		
		for (File libFile : libFiles) {
			if(libFile.getName().indexOf("axis2-kernel") != -1){
				Properties msgProp;
				try {
					JarFile jarFile = new JarFile(libFile);
					msgProp = getPropertiesFromJarFile(jarFile, "org/apache/axis2/i18n/resource.properties");
					axisVersionRaw = msgProp.getProperty("axisVersionRaw");
					_logger.debug("axisVersionRaw: {}", axisVersionRaw);
					
					if(!TARGET_AXIS2_VERSION.equals(axisVersionRaw)){
						if(_logger.isDebugEnabled()){
							errMessage += "SOAPClient needs Apache Axis2 version " + TARGET_AXIS2_VERSION + ". ";
							errMessage += "But you specified Axis2 of version " + axisVersionRaw + ".";
							_logger.debug(errMessage);
						}
						classpath = null;
						break;
					}
				}
				catch (IOException e) {
					_logger.debug(e.getMessage(), e);
					
					classpath = null;
					break;
				}
			}
			
			classpath.append("\"" + libFile.getAbsolutePath() + "\"").append(File.pathSeparator);
		}

		if(axisVersionRaw == null){
			_logger.debug("You must specify Axis2 version {} library directory.", TARGET_AXIS2_VERSION);
			return null;
		}
		else if(classpath == null){
			return null;
		}
		else{
			return classpath.toString();
		}
	}
	
	/**
	 * @param jarFile
	 * @param resourceName
	 * @return
	 * @throws IOException
	 */
	private static Properties getPropertiesFromJarFile(JarFile jarFile, String resourceName) throws IOException {

		JarEntry jarEntry = jarFile.getJarEntry(resourceName);

		InputStream in = jarFile.getInputStream(jarEntry);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			byte[] buf = new byte[1024];

			while (in.available() > 0) {
				int len = in.read(buf);
				out.write(buf, 0, len);
			}
		}
		finally {
			if(out != null) out.flush();
			if(in  != null) in.close();
		}
		
		byte[] bytes = out.toByteArray();
		
		Properties properties = new Properties();
		ByteArrayInputStream bai = new ByteArrayInputStream(bytes);
		
		try{
			properties.load(bai);
		}
		finally{
			if(bai != null) bai.close();
		}
		
		return properties;
	}
	
	private boolean _isDead4CompileProcess;
	private void waitForErrors(PrintStream ps, InputStream inputStream, InputStream errorStream) throws IOException {
		byte[] buffer = new byte[256];
		int stderrLen;
		int stdoutLen;

		if (inputStream == null || errorStream == null)
			return;

		do {
			while ((stderrLen = errorStream.available()) > 0) {
				stderrLen = errorStream.read(buffer, 0, buffer.length);
				if (stderrLen <= 0)
					break;

				ps.write(buffer, 0, stderrLen);
			}

			while ((stdoutLen = inputStream.available()) > 0) {
				stdoutLen = inputStream.read(buffer, 0, buffer.length);
				if (stdoutLen <= 0) {
					break;
				}

				ps.write(buffer, 0, stdoutLen);
			}

			if (stderrLen < 0 && stdoutLen < 0)
				return;

			if (stderrLen == 0) {
				stderrLen = errorStream.read(buffer, 0, buffer.length);
				if (stderrLen > 0) {
					ps.write(buffer, 0, stderrLen);
				}
			}

			if (stderrLen < 0 && stdoutLen == 0) {
				stdoutLen = inputStream.read(buffer, 0, buffer.length);
				if (stdoutLen > 0) {
					ps.write(buffer, 0, stdoutLen);
				}
			}
		}
		while (!_isDead4CompileProcess && (stderrLen >= 0 || stdoutLen >= 0));
	}

	/**
	 * @return
	 */
	private static boolean isWindows(){
		String os = System.getProperty("os.name");

		if(os != null && os.toUpperCase().indexOf("WINDOWS") != -1){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * @param wsdlUrl
	 * @param operationParamType
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @throws SAXException
	 */
	private static List<String> getPropertyNameList(Class<?> operationParamType, String wsdlUrl) 
						throws ParserConfigurationException, IOException, XPathExpressionException, SAXException{

		List<String> propertyNamesList = new ArrayList<String>();

		QName myQName = null;
		InputStream is = null;
		try {
			Field field4MyQName = operationParamType.getField("MY_QNAME");
			myQName = (QName) field4MyQName.get(null);
			_logger.trace("operationParamType:{}, myQName:{}", operationParamType.getName(), myQName);
	
			// 単独で定義されているElement
			StringBuilder exp4standalone = new StringBuilder();
			exp4standalone.append("//definitions/types/schema[@targetNamespace=\"" + myQName.getNamespaceURI() + "\"]");
			exp4standalone.append("/element[@name=\"" + myQName.getLocalPart() + "\"]");
	
			// ComplexTypeなど、シーケンスが指定された状態で定義されているElement
			StringBuilder exp4sequence = new StringBuilder();
			exp4sequence.append(exp4standalone);
			exp4sequence.append("/*/sequence/*");
			
			URL url = new URL(wsdlUrl);
			is = url.openStream();

			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(is);
		
			// XPathで取得
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			
			XPathExpression xpathExp = xpath.compile(exp4sequence.toString());
			Object result4sequence = xpathExp.evaluate(doc, XPathConstants.NODESET);
			
			// ComplexTypeなど、シーケンスが指定された状態で定義されているElement
			if(result4sequence != null){
				NodeList nodeList4sequence = (NodeList) result4sequence;
				addPropertyNameFromNodeList(propertyNamesList, nodeList4sequence);
			}
			
			// 単独で定義されているElement
			if(propertyNamesList.size() == 0){
				xpathExp = xpath.compile(exp4standalone.toString());
				Object result4standalone = xpathExp.evaluate(doc, XPathConstants.NODESET);
			
			    if(result4standalone != null){
					NodeList nodeList4standalone = (NodeList) result4standalone;
					addPropertyNameFromNodeList(propertyNamesList, nodeList4standalone);
			    }
			}
			
			// 別ファイルでElementが定義されている場合
			if(propertyNamesList.size() == 0){
				propertyNamesList = getPropertyNameListFromBean(operationParamType);
			}
		}
		catch (Exception e){
			IllegalStateException ise = new IllegalStateException("Not found '" + myQName + "' element in WSDL(='" + wsdlUrl + "').", e);
			_logger.warn(ise.getMessage(), ise); // 警告
			
			propertyNamesList = getPropertyNameListFromBean(operationParamType);
		}
		finally{
			if(is != null){
				is.close();
			}
		}

		return propertyNamesList;
	}

	private static List<String> getPropertyNameListFromBean(Class<?> beanType) {
		_logger.trace("getPropertyNameListFromBean beanType: {}", beanType);
		
		List<String> list = new ArrayList<String>();

		try {
			Map<String, PropertyDescriptor> map = JavaScriptUtility.getBeanPropertyMap(beanType);
			
			// キーの「自然順序付け」でソート
			// （∵Beanのプロパティ名の取得順は常に決まっている。（Class#getDeclaredMethods()利用）
			//   しかし、BeanであるJavaスタブクラスのソース生成方法がプロパティの順序を考慮しているかを保障できないため）
			SortedMap<String, PropertyDescriptor> sortMap = new TreeMap<String, PropertyDescriptor>(map);
			
			Iterator<String> it = sortMap.keySet().iterator();
			while(it.hasNext()){
				String propName = it.next();
				list.add(propName);

				_logger.trace("getPropertyNameListFromBean propName: {}", propName);
			}
		}
		catch (IntrospectionException e) {
			throw new IllegalStateException(e);
		}
		
		return list;
	}

	/**
	 * @param list
	 * @param nodeList
	 */
	private static void addPropertyNameFromNodeList(List<String> list, NodeList nodeList) {

		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element)nodeList.item(i);
			NamedNodeMap namedNodeMap = element.getAttributes();
			
			_logger.trace("--------------[" + i + "]--------------");
			_logger.trace("{}", namedNodeMap.getNamedItem("name"));
			_logger.trace("{}", namedNodeMap.getNamedItem("ref"));
			_logger.trace("{}", namedNodeMap.getNamedItem("type"));
			_logger.trace("{}", namedNodeMap.getNamedItem("minOccurs"));
			_logger.trace("{}", namedNodeMap.getNamedItem("nillable"));
			_logger.trace("{}", element.getNodeName());
			
			String propertyName = null;
			
			Node propertyNameNode = namedNodeMap.getNamedItem("name");
			if(propertyNameNode != null){
				propertyName = propertyNameNode.getNodeValue(); 
			}
			else{
				propertyNameNode = namedNodeMap.getNamedItem("ref");
				if(propertyNameNode != null){
					String nodeValue = propertyNameNode.getNodeValue();
					int pos = nodeValue.indexOf(":");
					propertyName = (pos == -1) ? nodeValue : nodeValue.substring(pos + 1);
				}
				else{
					// 属性「ref」も存在しない場合はエラー
					throw new IllegalStateException("Property name is not defined: " + element);
				}
			}

			list.add(propertyName);
		}
	}

    /**
     * Webサービスを呼び出すソースコードのサンプルを返却します。<br/>
     * <br/>
     * getSampleCode()の引数にはWebサービス・オペレーション名を指定します。<br/>
     * オペレーション名は <a href="#getOperationNames">getOperationNames()</a> でも取得可能です。<br/>
     * 引数を指定せずに getSampleCode() を実行すると、Webサービス内で利用可能なすべてのオペレーションに関するソースコードが返却されます。<br/>
     * <br/>
     * getSampleCode()関数の目的はサンプルコードの表示です。<br/>
     * したがって、getSampleCode()関数の実行ロジックは、Webサービスを呼び出すコードの作成完了後に削除してください。<br/>
     * <br/>
     * 以下に、getSampleCode()関数で生成される、Webサービスを呼び出すソースコードのサンプルを示します。<br/>
     * 
 * 	<table border="1">
 * 		<tr>
 * 			<th>
 * 				Webサービスを呼び出すソースコードのサンプル
 * 			</th>
 * 		</tr>
 * 		<tr>
 * 			<td>
 * 				<font size="-1.5">
<pre>
 1: &#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;
 2:  Usage:
 3:     var result = soapClient.add(wsUserInfo, member);
 4: 
 5: //-------------------------------
 6: // Sample Data : 'wsUserInfo'
 7: //-------------------------------
 8: var wsUserInfo =
 9: /&#42; Object <b>&lt;<font color="red">WSUserInfo</font>&gt;</b> &#42;/
10: {
11:     /&#42; String &#42;/
12:     "password" : "prop_password",
13: 
14:     /&#42; String &#42;/
15:     "authType" : "prop_authType",
16: 
17:     /&#42; String &#42;/
18:     "userID" : "prop_userID",
19: 
20:     /&#42; String &#42;/
21:     "loginGroupID" : "prop_loginGroupID"
22: };
23: 
24: //-------------------------------
25: // Sample Data : 'member'
26: //-------------------------------
27: var member =
28: /&#42; Object <b>&lt;<font color="blue">Member</font>&gt;</b> &#42;/
29: {
30:     /&#42; Boolean &#42;/
31:     "married" : true,
32: 
33:     /&#42; Number &#42;/
34:     "age" : 123,
35: 
36:     /&#42; String &#42;/
37:     "name" : "prop_name",
38: 
39:     /&#42; String &#42;/
40:     "id" : "prop_id",
41: 
42:     /&#42; Array <b>&lt;<font color="blue">Member</font>[]&gt;</b> &#42;/
43:     "children" : [
44: 
45:     ],
46: 
47:     /&#42; Date (Thu Jun 19 2008 12:34:56 GMT+0900 (JST)) &#42;/
48:     "birthDate" : new Date(1213846496000)
49: };
50: 
51: &#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;
</pre>
 * 				</font>
 * 			</td>
 * 		</tr>
 * 	</table>
 * 
 	* <br/>
     * サンプルコードはJSON形式で出力されます。そのため、コピー＆ペーストで利用することが可能です。<br/>
     * <br/>
     * また、コメントとしてJavaScriptの型情報が付与されています。<br/>
     * 9行目の「<b>&lt;<font color="red">WSUserInfo</font>&gt;</b>」
     * や
     * 28行目の「<b>&lt;<font color="blue">Member</font>&gt;</b>」など、
     * JavaScriptの型情報の右側に表示されている「<b>&lt;&gt;</b>」内の文字列は、そのオブジェクト構成を表す名称です。<br/>
     * <br/>
     * 例えば、42行目の「<b>&lt;<font color="blue">Member</font>[]&gt;</b>」は、
     * 「childrenプロパティには、28行目で示されている<b>&lt;<font color="blue">Member</font>&gt;</b>形式のオブジェクトが配列で格納される」
     * ことを意味しています。<br/>
     * <br/>
     * 
     * なお、XMLスキーマの restriction で定義されている型などは、サンプルデータが生成されません。
     * サンプルデータが生成されていない型については、WSDL、および、実行するWebサービスの仕様を確認してください。
     * 
     * @scope public
     * @param operationName　?String Webサービス・オペレーション名 (省略時はWebサービス内で利用可能なすべてのオペレーションに関するソースコードが返却されます)
     * @return String Webサービスを呼び出すソースコードのサンプル
     */
    public void getSampleCode(String operationName){
        // APIリスト用ダミー関数
    	// この関数は、自動生成されるJSファイル内で定義されています。
    }

    /**
     * このWebサービスで利用可能なオペレーション名を返却します。
     * 
     * @scope public
     * @return Array Webサービス・オペレーション名の配列
     */
    public Object getOperationNames() {
        // APIリスト用ダミー関数
    	// この関数は、自動生成されるJSファイル内で定義されています。
    	return null;
    }



    /**
     * タイムアウト値を設定します。
     * 
     * @scope public
     * @param timeOutInMilliSeconds Number タイムアウト値（ミリ秒））
     */
    public void setTimeOutInMilliSeconds(Number timeOutInMilliSeconds) {
        // APIリスト用ダミー関数
    	// 実態は、以下に定義されています。
    	// im_root/services/resource/pages/platform/src/system/common/parts/soap_client/soap_client_helper.js
    }
	
    /**
     * SOAPClientの関連リソースをクリーンアップします。<br/>
	 * このメソッドを呼び出すことは、Axis2のorg.apache.axis2.client.Stub#cleanup()メソッドを呼び出すことに相当します。
	 * 
     * @scope public
     */
    public void cleanup() {
        // APIリスト用ダミー関数
    	// 実態は、以下に定義されています。
    	// im_root/services/resource/pages/platform/src/system/common/parts/soap_client/soap_client_helper.js
    }
	
    /**
     * MTOM (Message Transmission Optimization Mechanism)の有効・無効を設定します。
	 * 
     * @scope public
     * @param bool Boolean MTOMを有効にする場合は trueを、無効にする場合は false を設定します。
     */
    public void setEnableMTOM(boolean bool) {
        // APIリスト用ダミー関数
    	// 実態は、以下に定義されています。
    	// im_root/services/resource/pages/platform/src/system/common/parts/soap_client/soap_client_helper.js
    }

    /**
	 * 送信するSOAPメッセージの文字セットを設定します。<br/>
	 * 設定しない場合の初期値は「UTF-8」です。
	 * 
     * @scope public
     * @param charset String 文字セット名
     */
    public void setCharacterSetEncoding(String charset) {
        // APIリスト用ダミー関数
    	// 実態は、以下に定義されています。
    	// im_root/services/resource/pages/platform/src/system/common/parts/soap_client/soap_client_helper.js
    }

    /**
	 *　Webサービスのスタブ・ソース作成モード。
	 */
	public enum StubGenerationMode{
		
		/**	
		 * Webサービスのスタブ・ソースを毎回作成します。
		 */
		Everytime,
		
		/**
		 * Webサービスのスタブが存在しない場合のみ作成します。<br/>
		 * なお、スタブ・ソース作成モードが未設定の場合、この「<b>Once</b>」モードが適用されます。 
		 */
		Once,
		
		/**	
		 * Webサービスのスタブ・ソースを作成しません。<br/>
		 * 
		 * このモードの場合、別途、Webサービスのスタブを配備する必要があります。<br/>
		 * 具体的には、Axis2が生成するJavaのスタブ・クラスをクラスパスに追加し、<br/>
		 * SOAPClientオブジェクトで生成されたJavaScriptのスタブ・ソースをJSのソース検索ディレクトリに追加する必要があります。<br/>
		 * <br/>
		 * Axis2によるのWebサービスのスタブ・ソース生成に関しては、以下を参照してください。<br/>
		 * <ul>
		 *   <li><a href="http://ws.apache.org/axis2/tools/1_4/CodegenToolReference.html">Code Generator Tool Guide for Command Line and Ant Task</a></li>
		 * </ul>
		 */
		Never
 	}
	
}
