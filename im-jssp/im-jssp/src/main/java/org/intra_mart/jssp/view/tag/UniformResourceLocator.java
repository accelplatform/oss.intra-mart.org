package org.intra_mart.jssp.view.tag;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.intra_mart.common.aid.jdk.util.charset.CharacterSetManager;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContextManager;
import org.intra_mart.jssp.page.JSSPQuery;
import org.intra_mart.jssp.page.JSSPQueryManager;
import org.intra_mart.jssp.util.charset.CharsetHandler;
import org.intra_mart.jssp.util.charset.CharsetHandlerManager;

/**
 * JSSPページ遷移情報を含んだURLを生成します。<br/>
 * 以下のタグで利用されます。<br/>
 * <ul>
 * 	<li>&lt;IMART type="form"&gt;</li>
 * 	<li>&lt;IMART type="frame"&gt;</li>
 * 	<li>&lt;IMART type="link"&gt;</li>
 * </ul>
 */
public class UniformResourceLocator {

	private JSSPQuery jsspQuery;
	private String labelName;
	private Map<String, String> args = new HashMap<String, String>();
	
	
	/**
	 * UniformResourceLocator を生成します。(画面遷移用)<br/>
	 * <br/>
	 * 遷移先ページには、引数 pagePath が設定され、<br/>
	 * 遷移元ページには、現在処理中のページが設定されます。<br/>
	 * 
	 * @param pagePath 遷移先ページパス（拡張子なし）
	 */
	public UniformResourceLocator(String pagePath){
		super();
		
		// ページ遷移情報を含まない状態のJSSPQueryを生成
		this.jsspQuery = JSSPQueryManager.createJSSPQuery();

		// 遷移先ページを設定
		this.jsspQuery.setNextEventPagePath(pagePath);

		JSSPQuery current = JSSPQueryManager.currentJSSPQuery();
		if(current != null){
			// 現在処理中のページパスの追加(Main content)
			String currentPath = current.getNextEventPagePath();
			if(currentPath != null){
				this.jsspQuery.setFromPagePath(currentPath);
			}
		}
	}

	
	/**
	 * UniformResourceLocator を生成します。(リロード用)<br/>
	 * <br/>
	 * 遷移先ページには、および、
	 * 遷移元ページには、現在処理中のページが設定されます。<br/>
	 */
	public UniformResourceLocator(){
		super();

		// ページ遷移情報を含まない状態のJSSPQueryを生成
		this.jsspQuery = JSSPQueryManager.createJSSPQuery();

		JSSPQuery current = JSSPQueryManager.currentJSSPQuery();
		if(current != null){
			// 現在処理中のページパスの追加(Main content)
			String currentPath = current.getNextEventPagePath();
			if(currentPath != null){
				this.jsspQuery.setNextEventPagePath(currentPath);
				this.jsspQuery.setFromPagePath(currentPath);
			}
			else{
				throw new IllegalStateException("Current NextEventPagePath is undefined.");
			}
		}
		else{
			throw new IllegalStateException("Current JSSPQuery is undefined.");
		}
	}


	/**
	 * JSSPページ遷移情報、および、セッション情報を伴ったＵＲＬを返却します。<br/>
	 * （具体的には、<pre>サーブレットパス[? クエリ文字列 [# ラベル(フラグメント)]]</pre>が返却されます）<br/>
	 * なお、このメソッドの返却値の「クエリ文字列」部分には、{@link #setArgument()}で設定したＵＲＬ引数は<b>含まれません</b>。<br/>
	 * 
	 * @return URL文字列
	 */
	public String scriptNameWithSession(){
		HttpServletResponse response = this.currentHttpServletResponse();

		if(this.labelName == null){
			return response.encodeURL(this.jsspQuery.createJSSPQueryString());
		}
		else{
			return response.encodeURL(this.jsspQuery.createJSSPQueryString() + "#" + labelName);
		}
	}

	
	/**
	 * JSSPページ遷移情報、および、セッション情報を伴ったＵＲＬを返却します。<br/>
	 * （具体的には、<pre>サーブレットパス[? クエリ文字列 [# ラベル(フラグメント)]]</pre>が返却されます）<br/>
	 * なお、このメソッドの返却値の「クエリ文字列」部分には、{@link #setArgument()}で設定したＵＲＬ引数が含まれます。<br/>
	 * 
	 * @return URL文字列
	 */
	public String locationWithSession() throws UnsupportedEncodingException{
		if(this.args.isEmpty()){
			return this.scriptNameWithSession();
		}
		else{
			HttpServletResponse response = this.currentHttpServletResponse();
			CharsetHandler handler = CharsetHandlerManager.getCharsetHandler();
			String encodingName = CharacterSetManager.toJDKName(handler.getCharacterEncoding());

			StringBuffer buf = new StringBuffer();
			Iterator view = this.args.entrySet().iterator();
			while(view.hasNext()){
				Map.Entry me = (Map.Entry) view.next();
				// ＵＲＬエンコード後設定
				buf.append("&" + URLEncoder.encode((String) me.getKey(), encodingName) +
						   "=" + URLEncoder.encode((String) me.getValue(), encodingName));
			}

			String scriptName = this.jsspQuery.createJSSPQueryString();

			// クエリ文字列が生成されていない場合(連結のための「&」をつけない)
			if(scriptName.indexOf("?") == -1){
				if(this.labelName == null){
					return response.encodeURL(scriptName + "?" + buf.substring(1));
				}
				else{
					return response.encodeURL(scriptName + "?" + buf.substring(1) + "#" + labelName);
				}
			}
			// クエリ文字列が生成されている場合(連結のために「&」をつける)
			else {
				if(this.labelName == null){
					return response.encodeURL(scriptName + buf.substring(0));
				}
				else{
					return response.encodeURL(scriptName + buf.substring(0) + "#" + labelName);
				}

			}
		}
	}

	
	/**
	 * JSSPページ遷移情報用INPUTタグを生成します。<br/>
	 * このINPUTタグは、GETメソッドでフォームがサブミットされた場合に必要となります。<br/>
	 * 
	 * @return JSSPページ遷移情報用INPUTタグ
	 */
	public String createInputTag4JSSPQuery(){		
		return this.jsspQuery.createJSSPQueryInputTagString();
	}

	
	/**
	 * ＵＲＬ引数を設定します。
	 * 
	 * @param key キー
	 * @param value 値
	 */
	public void setArgument(String key, String value){
		args.put(key, value);
	}

	
	/**
	 * ラベル（フラグメント）名を設定します。
	 *   
	 * @param name ラベル（フラグメント）名
	 */
	public void setLabel(String name){
		labelName = name;
	}

	
	/**
	 * リクエスト時起動関数引数の設定メソッド for JavaScript
	 * @param funcName JavaScript 関数名
	 */
	public void setAction(String funcName){
		this.jsspQuery.setActionEventName(funcName);
	}

	
	/**
	 * 現在のスレッドに関連付けられたHttpServletResponseを取得します。
	 * @return
	 */
	private HttpServletResponse currentHttpServletResponse(){
		HTTPContext httpContext = HTTPContextManager.getInstance().getCurrentContext();
		if(httpContext != null){
			return httpContext.getResponse();
		}
		else{
			throw new IllegalStateException("HttpServletResponse not found.");
		}
	}
}
