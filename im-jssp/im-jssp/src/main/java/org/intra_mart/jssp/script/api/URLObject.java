package org.intra_mart.jssp.script.api;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.intra_mart.common.aid.jdk.util.charset.CharacterSetManager;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContextManager;
import org.intra_mart.jssp.util.charset.CharsetHandler;
import org.intra_mart.jssp.util.charset.CharsetHandlerManager;
import org.intra_mart.jssp.view.tag.UniformResourceLocator;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

/**
 * URL操作クラス。
 * 
 * @scope public
 * @name URL
 */
public class URLObject extends ScriptableObject implements Cloneable, java.io.Serializable{

	private UniformResourceLocator uniformResourceLocator;
	
	/**
	 * コンストラクタ。
	 * 
	 * @scope public
	 * @param pagePath String プログラムのページパス
	 * @return URLオブジェクト
	 */
	public static Object jsConstructor(Context cx, Object[] args, Function ctorObj, boolean inNewExpr){
		if(inNewExpr){
			// 引数の数の確認
			if(args.length > 0){
				// 引数の型の確認
				if(args[0] instanceof String){
					// 文字列の両端の空白を除去しつつインスタンス生成
					return new URLObject(((String) args[0]).trim());
				}
			}

			// 引数不適切につき
			return new URLObject(null);
		}
		else{
			return null;
		}
	}

	/**
     * JSオブジェクト名を取得します。<BR>
     * <BR>
     * @return JSオブジェクト名
     * @see jp.co.intra_mart.system.javascript.Scriptable#getClassName()
     */
	public String getClassName(){
		return "URL";
	}

	
	/**
	* コンストラクタ。
	*/
	public URLObject(){
	}
	
	
	/**
	 * コンストラクタ。
	 * 
	 * @param nextPagePath 遷移先ページパス
	 */
	public URLObject(String nextPagePath){
		if(nextPagePath == null){
			// URLObjectのゼロパラメータコンストラクタ内では、UniformResourceLocatorをインスタンス化できない
			// （∵URLObjectのゼロパラメータコンストラクタはJSSP実行環境起動時に呼び出されるが、
			//   　起動時点では、現在のスレッドに紐づいたJSSPQueryを取得できないため）
			this.uniformResourceLocator = new UniformResourceLocator();			
		}
		else {
			this.uniformResourceLocator = new UniformResourceLocator(nextPagePath);			
		}
		
	}
	
	
	/**
	 * ＵＲＬエンコードを行います。<br/>
	 * <br/>
	 * 引数 enc には、ＵＲＬエンコード後の文字エンコーディング名を指定します。<br/>
	 * 引数 enc には、Java-VM が解釈できる文字エンコーディング名を指定します。<br/>
	 * 引数 enc を省略した場合は、
	 * org.intra_mart.jssp.util.charset.CharsetHandler が返却する
	 * 文字エンコーディング名を利用して、ＵＲＬエンコードを行います。<br/>
	 * <br/>
	 * org.intra_mart.jssp.util.charset.CharsetHandler は、<br/>
	 * 「<i>/org/intra_mart/jssp/util/jssp-runtime-classes.xml</i>」の
	 * 「<i>/jssp-runtime-classes/charset-handler</i>」タグで設定します。<br/>
	 * 上記 CharsetHandlerの設定を省略した場合、
	 * 常に「UTF-8」を返却するCharsetHandlerが設定されます。
	 * 
	 * @scope public
	 * @param target String エンコード対象文字列
	 * @param enc String ＵＲＬエンコード後の文字エンコーディング名（任意）
	 * @return String ＵＲＬエンコード結果文字列
	 */
	public static String jsStaticFunction_encode(Object target, Object enc) throws UnsupportedEncodingException{

		if(enc == null || enc instanceof Undefined){
			CharsetHandler handler = CharsetHandlerManager.getCharsetHandler();
			enc = CharacterSetManager.toJDKName(handler.getCharacterEncoding());
		}
		else{
			enc = ScriptRuntime.toString(enc);
		}

		return URLEncoder.encode(ScriptRuntime.toString(target), (String) enc);
	}

	
	
	/**
	 * ＵＲＬデコードを行います。<br/>
	 * <br/>
	 * 引数 enc には、ＵＲＬエンコード時に利用した文字エンコーディング名を指定します。<br/>
	 * 引数 enc には、Java-VM が解釈できる文字エンコーディング名を指定します。<br/>
	 * 引数 enc を省略した場合は、
	 * org.intra_mart.jssp.util.charset.CharsetHandler が返却する
	 * 文字エンコーディング名を利用して、ＵＲＬデコードを行います。<br/>
	 * <br/>
	 * org.intra_mart.jssp.util.charset.CharsetHandler は、<br/>
	 * 「<i>/org/intra_mart/jssp/util/jssp-runtime-classes.xml</i>」の
	 * 「<i>/jssp-runtime-classes/charset-handler</i>」タグで設定します。<br/>
	 * 上記 CharsetHandlerの設定を省略した場合、
	 * 常に「UTF-8」を返却するCharsetHandlerが設定されます。
	 * 
	 * @scope public
	 * @param target String デコード対象文字列
	 * @param enc String 文字エンコーディング名（任意）
	 * @return String ＵＲＬデコード結果文字列
	 */
	public static String jsStaticFunction_decode(Object target, Object enc) throws UnsupportedEncodingException{

		if(enc == null || enc instanceof Undefined){
			CharsetHandler handler = CharsetHandlerManager.getCharsetHandler();
			enc = CharacterSetManager.toJDKName(handler.getCharacterEncoding());
		}
		else{
			enc = ScriptRuntime.toString(enc);
		}

		return URLDecoder.decode(ScriptRuntime.toString(target), (String) enc);
	}
	
	
	/**
	 * ＵＲＬ（絶対パス）を取得します。
	 * 
	 * @scope public
	 * @return String ＵＲＬ文字列（完全体）
	 */
	public String jsFunction_absoluteLocation() throws MalformedURLException, UnsupportedEncodingException {

		HTTPContext httpContext = HTTPContextManager.getInstance().getCurrentContext();
		HttpServletRequest request = httpContext.getRequest();
		
		URL baseURL = new URL(new String(request.getRequestURL()));
		URL url = new URL(baseURL, this.jsFunction_location());
		
		return url.toExternalForm();
	}

	
	
	/**
	 * ＵＲＬを取得します。
	 * 
	 * @scope public
	 * @return String ＵＲＬ文字列
	 */
	public String jsFunction_location() throws UnsupportedEncodingException{
		return this.uniformResourceLocator.locationWithSession();
	}
	
		
	/**
	 * ＵＲＬ引数を設定します。<br>
	 * 
	 * @scope public
	 * @param name String キー
	 * @param value String 値
	 */
	public void jsFunction_setArgument(String name, String value){
		this.uniformResourceLocator.setArgument(name, value);
	}

	
	
	/**
	 * ラベルを設定します。<br>
	 * 
	 * @scope public
	 * @param name String ラベル名
	 */
	public void jsFunction_setLabel(String name){
		this.uniformResourceLocator.setLabel(name);
	}

	
	
	/**
	 * リクエスト時起動関数引数を設定します。<br>
	 * 
	 * @scope public
	 * @param funcName String JavaScript関数名
	 */
	public void jsFunction_setAction(String funcName){
		this.uniformResourceLocator.setAction(funcName);
	}
	
}

