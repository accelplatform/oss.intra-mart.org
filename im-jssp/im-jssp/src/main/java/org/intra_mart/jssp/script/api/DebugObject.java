package org.intra_mart.jssp.script.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;

import org.intra_mart.common.aid.jdk.util.charset.AdvancedOutputStreamWriter;
import org.intra_mart.jssp.exception.JavaScriptDebugBrowseException;
import org.intra_mart.jssp.script.FoundationScriptScope;
import org.intra_mart.jssp.util.JsUtil;
import org.intra_mart.jssp.util.SimpleLog;
import org.intra_mart.jssp.util.config.HomeDirectory;
import org.intra_mart.jssp.util.config.JSSPConfigHandler;
import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.xml.XMLObject;


/**
 * <!-- JavaScriptオブジェクト -->
 * デバッグ情報を表示するオブジェクト。 <BR>
 * <BR>
 * 開発中のデバッグ時に利用するＡＰＩが含まれるオブジェクトです。 <BR>
 * 
 * @scope public
 * @name Debug
 */
public class DebugObject extends ScriptableObject implements Cloneable, java.io.Serializable {

    private static final String DATA_COLOR  = "CornSilk";
    private static final String INDEX_COLOR = "DarkSeaGreen";
    private static final String TITLE_COLOR = "SkyBlue";
    private static final String TYPE_COLOR  = "Thistle";

    private static PrintWriter fileWriter;

    /**
     * 日付を表形式に表示するソースを作成するメソッド。<br>
     * 日付型変数を受け取って、ブラウザ上で表示するための ＨＴＭＬソースを作成
     * 
     * @param target 解析対象 JavaScript DATE 型変数 
     * @return ＨＴＭＬソース
     */
    private static String dateToHTML(Date target) {
        return toBrowse(Context.toString(target));
    }


    /**
     * JavaScript オブジェクトの JavaScript 上の変数型を取得するメソッド。
     * 
     * @param target チェック対象 JavaScript オブジェクト 
     * @return 変数型を示すキーワード（文字列）
     */
    private static String getVariableType(Object target) {
        if (target == null) {
            return "null";
        }
        if (target instanceof Undefined) {
            return "Undefined";
        }

        if (target instanceof String) {
            return "String";
        }
        if (target instanceof Number) {
            return "Number";
        }
        if (target instanceof Boolean) {
            return "Boolean";
        }

        if (JsUtil.isDate(target)) {
            return "Date";
        }
        if (target instanceof NativeArray) {
            return "Array";
        }
        if (target instanceof XMLObject) {
            return "XML";
        }

        if (target instanceof Function) {
            return "Function";
        }
        if (target instanceof ScriptableObject) {
            return "Object";
        }

        // その他
        return "JAVA";
    }


    /**
     * オブジェクトを表形式に表示するソースを作成するメソッド。<br>
     * オブジェクトを受け取って、その構造と値を表形式に ブラウザに表示するためのＨＴＭＬソースを作成
     * 
     * @param target 解析対象 JavaScript オブジェクト
     * @return ＨＴＭＬソース
     */
    private static String objectToHTML(Scriptable target) {
        StringBuffer buf = new StringBuffer();
        Object[] params = target.getIds(); // プロパティの抽出
        Scriptable proto = target.getPrototype();
        if (proto != null) {
            // 解析対象プロパティの追加(１階層下のprototypeまでサポート)
            Vector<Object> members = new Vector<Object>();
            for (int idx = 0; idx < params.length; idx++) {
                members.add(params[idx]); // ローカルプロパティの設定
            }
            params = proto.getIds();
            for (int idx = 0; idx < params.length; idx++) {
                if (!members.contains(params[idx])) {
                    members.add(params[idx]); // 基本プロパティの設定
                }
            }
            params = members.toArray();
        }

        // 表定義
        buf.append("<TABLE border width=\"100%\">");

        // パラメーターの解析
        for (int idx = 0; idx < params.length; idx++) {
            Object item;
            if (params[idx] instanceof String) {
                item = target.get((String) params[idx], null);
                if (item == Scriptable.NOT_FOUND) {
                    item = proto.get((String) params[idx], null);
                }
            } else {
                item = target.get(((Number) params[idx]).intValue(), null);
                if (item == Scriptable.NOT_FOUND) {
                    item = proto.get(((Number) params[idx]).intValue(), null);
                }
            }
            String type_name = getVariableType(item);

            buf.append("<TR valign=\"top\">"); // 行定義
            buf.append("<TH align=\"right\" bgcolor=\"" + INDEX_COLOR + "\" nowrap>");
            buf.append(params[idx]); // プロパティ名称
            buf.append("</TH>");
            buf.append("<TD bgcolor=\"" + DATA_COLOR + "\" nowrap>");
            
            // String
            if (item instanceof String) {
                buf.append(toBrowse(ScriptRuntime.toString(item)));
            }
            // Array
            else if (item instanceof NativeArray) {
                buf.append(objectToHTML((ScriptableObject) item));
            }
            // Object
            else if (type_name.equals("Object")) {
                buf.append(objectToHTML((ScriptableObject) item));
            }
            // Date
            else if (JsUtil.toDate(item) != null) {
                buf.append(dateToHTML(JsUtil.toDate(item)));
            }
            // XML
            else if (type_name.equals("XML")) {
                buf.append(toBrowse(ScriptRuntime.toString(item)));
            }
            // Function
            else if (item instanceof Function) {
                buf.append("");
            }
            // その他
            else {
                buf.append(toBrowse(ScriptRuntime.toString(item)));
            }
            
            buf.append("</TD>");
            buf.append("<TD align=\"center\" bgcolor=\"" + TYPE_COLOR + "\" nowrap><I>");
            buf.append(type_name); // 変数型
            buf.append("</I></TD>");
            buf.append("</TR>"); // 行終了定義
        }

        // 表終了定義
        buf.append("</TABLE>");

        // ソースの返却
        return buf.toString();
    }


    /**
     * ブラウザに表示するための特殊文字の変換メソッド<br>
     * デフォルトで表示不可能な文字の特殊コードへの変換
     * 
     * @param target 表示対象ソース
     * @return ブラウザへの送信ソース（文字列）
     */
    private static String toBrowse(String target) {
        Reader in = new StringReader(target);
        StringBuffer buf = new StringBuffer();
        boolean cr = false;
        char chr;

        // 解析＆置換
        try {
            while ((chr = (char) in.read()) != (char) -1) {
                if (chr == '\n') {
                    buf.append("<BR>");
                    cr = false;
                    continue;
                }
                if (cr) {
                    buf.append("<BR>");
                    cr = false;
                }
                if (chr == '\r') {
                    cr = true;
                } else if (chr == '<') {
                    buf.append("&lt;");
                } else if (chr == '>') {
                    buf.append("&gt;");
                } else if (chr == '&') {
                    buf.append("&amp;");
                } else if (chr == '\"') {
                    buf.append("&quot;");
                } else if (chr == 0x20) {
                    buf.append("&nbsp;");
                } else if (chr == '\t') {
                    buf.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                } else {
                    buf.append(chr);
                }
            }
        } catch (IOException ioe) {
            // nothing
        }

        // 変換結果の返却
        return buf.toString();
    }

    /**
     * ブラウザへの変数情報を表示します。 <BR>
     * <BR>
     * 指定の変数の情報をブラウザ画面上に表示します。<br>
     * いかなる JavaScript 変数型の変数も表示対象として指定可能です。<br>
     * 表示対象とする変数の引数への指定個数は任意個（無限）指定可能です。<br>
     * このＡＰＩが実行されると、このＡＰＩ以降のプログラムは実行されません。<br>
     * 本メソッドは、try...catch 文では使用できません。
     * 
     * @scope public
     * @param arg Object 表示対象となる変数
     * @param ... ... 複数指定する場合は、カンマの後に続けて変数を指定します。
     * @return void
     */
    public static void jsStaticFunction_browse(Context cx, Scriptable thisObj, Object[] args, Function funObj) {

    	if(! JSSPConfigHandlerManager.getConfigHandler().isDebugBrowseEnable()){
    		return;
    	}    		
    		
    	StringBuffer buf = new StringBuffer();

        // ＨＴＭＬソースの作成
        buf
                .append("<HTML>\n<HEAD><META HTTP-EQUIV=\"Content-Type\" content=\"text/html; charset=UTF-8\"><META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\"><TITLE>intra-mart[Debug.browse()]</TITLE></HEAD>\n<BODY bgcolor=\"WhiteSmoke\" text=\"Black\">\n");

        // 操作パネル
        buf.append("<TABLE width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><TR>");
        // 戻るリンク
        buf.append("<TD align=\"left\" valign=\"top\"><A href=\"JavaScript:history.back();\"><FONT size=\"1\">&lt;- Back</FONT></A></TD>");
        // タイトル
        buf
                .append("<TD align=\"center\"><FONT size=\"7\"><B>D</B>ebug </FONT><FONT size=\"3\"><I>for</I></FONT><FONT size=\"6\"> <I><B>S</B>cript variables.</I></FONT></TD>\n");
        // リロードボタン
        buf
                .append("<TD align=\"right\" valign=\"top\"><A href=\"JavaScript:location.reload(true);\"><FONT size=\"1\">Reload -&gt;</FONT></A></TD>");
        buf.append("</TR></TABLE>\n");

        // ページタイトル
        buf.append("<CENTER><FONT size=\"1\">");
        buf.append((new java.util.Date()).toString());
        buf.append("</FONT></CENTER>\n");
        buf.append("<HR>\n");

        // 表
        for (int idx = 0; idx < args.length; idx++) {
            String type_name = getVariableType(args[idx]);
            buf.append("<TABLE width=\"100%\" border=\"8\" cellpadding=\"2\"><TR align=\"center\"><TH bgcolor=\"" + TITLE_COLOR
                    + "\" nowrap>");
            buf.append(String.valueOf(idx + 1));
            buf.append("</TH></TR></TABLE>");

            // String
            if (args[idx] instanceof String) {
                buf.append("<TABLE border align=\"center\"><TR><TD bgcolor=\"" + DATA_COLOR + "\" nowrap>");
                buf.append(toBrowse(ScriptRuntime.toString(args[idx])));
                buf.append("</TD></TR></TABLE>");
            }
            // Array
            else if (args[idx] instanceof NativeArray) {
                buf.append(objectToHTML((ScriptableObject) args[idx]));
            }
            // Object
            else if (type_name.equals("Object")) {
                buf.append(objectToHTML((ScriptableObject) args[idx]));
            }
            // Date
            else if (JsUtil.isDate(args[idx])) {
                buf.append("<TABLE border align=\"center\"><TR><TD bgcolor=\"" + DATA_COLOR + "\" nowrap>");
                buf.append(dateToHTML(JsUtil.toDate(args[idx])));
                buf.append("</TD></TR></TABLE>");
            }
            // XML、または、Functionではない
            else if (type_name.equals("XML") || !(args[idx] instanceof Function)) {
                buf.append("<TABLE border align=\"center\"><TR><TD bgcolor=\"" + DATA_COLOR + "\" nowrap>");
                buf.append(toBrowse(ScriptRuntime.toString(args[idx])));
                buf.append("</TD></TR></TABLE>");
            }

            buf.append("<TABLE width=\"100%\" border=\"0\"><TR align=\"center\"><TD bgcolor=\"" + TYPE_COLOR + "\" nowrap><I>");
            buf.append(type_name);
            buf.append("</I></TD></TR></TABLE>");
            buf.append("<HR>\n");
        }

        // 説明
        buf.append("<TABLE border=\"0\" align=\"center\">");
        buf.append("<TR><TH align=\"right\">Bold:</TH><TD align=\"left\">Object property name or Array index</TD></TR>");
        buf.append("<TR><TH align=\"right\">Italic:</TH><TD align=\"left\">Variable type</TD></TR>");
        buf.append("<TR><TH align=\"right\">Normal:</TH><TD align=\"left\">Value</TD></TR>");
        buf.append("</TABLE>\n");
        buf.append("</BODY>\n</HTML>");

        // 例外のスロー（ＪＳ実行の強制中断）
        throw new JavaScriptDebugBrowseException(buf.toString());
    }

    /**
     * コンソールへメッセージを出力します。 <BR>
     * <BR>
     * 
     * @scope public
     * @param msg
     *            String メッセージ
     * @return void
     */
    public static String jsStaticFunction_print(String msg) {
    	
    	if(! JSSPConfigHandlerManager.getConfigHandler().isDebugPrintEnable()){
    		return msg;
    	}    		

    	System.out.println(msg);
        return msg;
    }

    /**
     * ファイルへメッセージを出力します。 <BR>
     * <BR>
     * 指定の文字列をファイル内へ出力します。 <br>
     * 出力ファイルはコンテキストパス直下の debug.log という名前のファイルです。 （設定にて変更可能）<br>
     * メソッドの呼び出しが複数回にわたる場合、ファイルへは追記モードで全てのメッセージが出力されます。 <br>
     * 
     * @scope public
     * @param msg
     *            String メッセージ
     * @return void
     */
    public static synchronized void jsStaticFunction_write(String msg) {

    	if(! JSSPConfigHandlerManager.getConfigHandler().isDebugWriteEnable()){
    		return;
    	}    		
    	
    	if (fileWriter == null) {
    		String path = JSSPConfigHandlerManager.getConfigHandler().getDebugWriteFilePath();

			File debugWriteFile = new File(path);			
			if(! debugWriteFile.isAbsolute()){
				debugWriteFile = new File(HomeDirectory.instance(), path);		
			}
    		
            try {
            	FileOutputStream fos = new FileOutputStream(debugWriteFile.getAbsolutePath(), true);

            	JSSPConfigHandler config = JSSPConfigHandlerManager.getConfigHandler();
            	String serverCharset = config.getServerCharacterEncoding();

        		try {
					Writer writer = new AdvancedOutputStreamWriter(fos, serverCharset);
					fileWriter = new PrintWriter(writer, true);
				}
        		catch (Exception e) {
	    			// TODO [OSS-JSSP] ログ機能実装
	            	SimpleLog.logp(Level.FINE, e.getMessage(), e);
	            	
	            	fileWriter = getPrintWriter(fos, serverCharset);
				}
            }
            catch (FileNotFoundException fnfe) {
    			// TODO [OSS-JSSP] ログ機能実装
            	SimpleLog.logp(Level.WARNING, "File open error: " + debugWriteFile.getAbsolutePath(), fnfe);
                return;
            }
        }

        // データ書出
        java.util.Date now = new java.util.Date();
        fileWriter.println(now.toString().concat(": ").concat(msg));
    }


	/**
	 * @param fos
	 * @param charset
	 * @return
	 */
	private static PrintWriter getPrintWriter(FileOutputStream fos, String charset) {
		try {
		    return new PrintWriter(new OutputStreamWriter(fos, charset), true);
		}
		catch (UnsupportedEncodingException uee) {
			// TODO [OSS-JSSP] ログ機能実装
        	SimpleLog.logp(Level.FINE, uee.getMessage(), uee);
        	
			return new PrintWriter(fos);
		}
	}
    
	private static Scriptable imJsonObject = null;
	private static Function toJSONStringFunc = null;
	private static Object debugConsoleMonitor = new Object();
	
    /**
     * オブジェクトの内容をコンソール上に表示します。<br/>
     * <br/>
     * 表示される内容は JSON 形式の文字列です。<br/>
     * そのため、本メソッドの出力内容を JavaScriptソースにコピーすることで、<br/>
     * JavaScriptオブジェクトに戻すことが出来ます。<br/>
     * <br/>
	 * <strong>サンプルコード</strong>
	 * 	<table border="1">
	 * 		<tr>
	 * 			<th>
	 * 				ファンクションコンテナ（.js）
	 * 			</th>
	 * 		</tr>
	 * 		<tr>
	 * 			<td>
	 * 				<font size="-1">
	 * <pre>
	 * var obj = 
	 * // Debug.console()の結果 ここから<b>
	 * /&#42; Object &#42;/
	 * {
	 *     /&#42; String &#42;/
	 *     "prop1" : "文字列1",
	 * 
	 *     /&#42; Date &#42;/  // Fri May 25 2007 16:50:16 GMT+0900 (JST)
	 *     "prop2" : new Date(1180079416500),
	 * 
	 *     /&#42; Number &#42;/
	 *     "prop3" : 256
	 * }</b>
	 * // Debug.console()の結果 ここまで
	 * </pre>
	 * 				</font>
	 * 			</td>
	 * 		</tr>
	 * 	</table>
	 * <br/>
	 *
     * このメソッドは、<pre><a href="im_json.html#toJSONStringObjectBoolean">ImJson#toJSONString()</a></pre>を利用しています。<br/>
     * そのため、JSON文字列として正しくないプロパティは表示されません。<br/>
     * (例えば、Array型のオブジェクトに設定されたプロパティは、表示されません)<br/>
     * <br/>
	 * JSON (JavaScript Object Notation)は、軽量のデータ交換フォーマットです。<br/>
	 * JSON については、<a href="http://www.json.org/json-ja.html">JSON の紹介</a> を参照してください。
     * 
     * @scope public
     * 
     * @param arg Object 表示対象となる変数
     * @param ... ... 複数指定する場合は、カンマの後に続けて変数を指定します。
     * @return void
     */
    public static void jsStaticFunction_console(Context cx, Scriptable thisObj, Object[] args, Function funObj) {

    	if(! JSSPConfigHandlerManager.getConfigHandler().isDebugConsoleEnable()){
    		return;
    	}    		

    	if(toJSONStringFunc == null){
    		synchronized(debugConsoleMonitor){
    	    	if(toJSONStringFunc == null){
    	        	FoundationScriptScope foundationScriptScope = FoundationScriptScope.instance();
    	        	
    	    		Object obj = foundationScriptScope.get("ImJson", foundationScriptScope);
    	        	if(obj instanceof Scriptable){
    	        		imJsonObject = (Scriptable) obj;
    	        	}
    	        	else{
    	        		throw new IllegalStateException("\"ImJson\" is not defined.");
    	        	}
    	        	
    	    		Object func = imJsonObject.get("toJSONString", imJsonObject);
    	    		if(func instanceof Function){
    	    			toJSONStringFunc = (Function)func;
    	    		}
    	    		else{
    	        		throw new IllegalStateException("\"ImJson#toJSONString()\" is not defined.");
    	    		}
    	    	}
    		}
    	}
		
    	for (int idx = 0; idx < args.length; idx++) {
    		
			Object[] toJSONStringArgs = { args[idx], new Boolean(true) };
			String jsonString = (String) toJSONStringFunc.call(cx, imJsonObject, imJsonObject, toJSONStringArgs);
			
    		System.out.println("========== " + (idx + 1) + " ==========");
    		System.out.println(jsonString);
    		System.out.println();
    	}
		
    }

	/**
	 * JavaScript 実行環境下での名称取得メソッド
	 * @return JavaScript 実行環境上での名称
	 */
    public String getClassName() {
        return "Debug";
    }
}

/* End of File */