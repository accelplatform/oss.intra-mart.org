package org.intra_mart.jssp.page;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.util.JSSPQueryUtil;
import org.intra_mart.jssp.util.config.JSSPConfigHandler;
import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;
import org.mozilla.javascript.Function;

public class JSSPQueryImpl implements JSSPQuery {

	private JSSPConfigHandler config = JSSPConfigHandlerManager.getConfigHandler();

	private String actionEventPagePath; // ←actionEventPagePathを設定できるのはコンストラクタのみ
	private String actionEventName;
	private String fromPagePath;
	private String nextEventPagePath;
	private String nextEventName;

	private String uriPrefix;
	private String uriSuffix;
	
	private static final int SLASH_LEN = "/".length();
	
	/**
	 * @deprecated
	 * @param request
	 * @param response
	 */
	public JSSPQueryImpl(HttpServletRequest request, HttpServletResponse response){
		this();
		
		String queryString;		
		if(request.getQueryString() != null){
			queryString = request.getQueryString();
		}
		else {
			queryString = this.createJSSPQueryStringFromPostData(request);
		}
		
		// 「RequestURI」の「コンテキストパス + /」の後ろ から 終わりまでを取得
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		int subStringIdx4Start = uri.indexOf(contextPath) + contextPath.length() + SLASH_LEN;
		
		this.nextEventPagePath = 
				getNextEventPagePathFromURI(uri.substring(subStringIdx4Start));

		this.nextEventName = 
				JSSPQueryUtil.getValueFromQueryString(queryString, config.getJsspKey4NextEventName());
	
		this.fromPagePath = 
				JSSPQueryUtil.getValueFromQueryString(queryString, config.getJsspKey4FromPagePath());

		this.actionEventPagePath = 
				JSSPQueryUtil.getValueFromQueryString(queryString, config.getJsspKey4ActionEventPagePath());

		this.actionEventName = 
				JSSPQueryUtil.getValueFromQueryString(queryString, config.getJsspKey4ActionEventName());		
	}

	
	/**
	 * @deprecated
	 * @param queryString
	 */
	public JSSPQueryImpl(String jsspQueryString){
		this();
		
		StringTokenizer tokenizerPerQuestion = new StringTokenizer(jsspQueryString, "?");
		
		if(tokenizerPerQuestion.countTokens() != 1 && tokenizerPerQuestion.countTokens() != 2){
			throw new IllegalArgumentException("Query format error [illegal postion -> '?']: " + jsspQueryString);
		}

		String uri = tokenizerPerQuestion.nextToken();
		this.nextEventPagePath = getNextEventPagePathFromURI(uri);
		
		if(tokenizerPerQuestion.hasMoreTokens()){
			String queryString = tokenizerPerQuestion.nextToken();
			this.nextEventName = JSSPQueryUtil.getValueFromQueryString(queryString, config.getJsspKey4NextEventName());
			this.fromPagePath = JSSPQueryUtil.getValueFromQueryString(queryString, config.getJsspKey4FromPagePath());
			this.actionEventPagePath = JSSPQueryUtil.getValueFromQueryString(queryString, config.getJsspKey4ActionEventPagePath());
			this.actionEventName = JSSPQueryUtil.getValueFromQueryString(queryString, config.getJsspKey4ActionEventName());
		}
	}

	/**
	 * @deprecated
	 * @param nextPagePath
	 * @param fromPagePath
	 */
	public JSSPQueryImpl(String nextPagePath, String fromPagePath){
		this();		
		this.nextEventPagePath = nextPagePath;
		this.fromPagePath = fromPagePath;		
	}
	
	/**
	 * @deprecated
	 */
	public JSSPQueryImpl(){
		this.actionEventPagePath = null;
		this.actionEventName = null;
		this.fromPagePath = null;
		this.nextEventPagePath = null;
		this.nextEventName = null;
		
		this.uriPrefix = config.getURIPrefix();
		this.uriSuffix = config.getURISuffix();	
	}

	
	
	/* (non-Javadoc)
	 * @see org.intra_mart.jssp.page.JSSPQuery#replaceJSSPQuery(org.intra_mart.jssp.page.JSSPQuery)
	 */
	public void replaceJSSPQuery(JSSPQuery jsspQuery) {

		if(jsspQuery.getActionEventPagePath() != null
		   &&
		   !"".equals(jsspQuery.getActionEventPagePath())){
			this.actionEventPagePath = jsspQuery.getActionEventPagePath();
		}
		
		if(jsspQuery.getActionEventName() != null
		   &&
		   !"".equals(jsspQuery.getActionEventName())){
			this.actionEventName = jsspQuery.getActionEventName();
		}
		
		if(jsspQuery.getFromPagePath() != null
		   && 
		   !"".equals(jsspQuery.getFromPagePath())){
			this.fromPagePath = jsspQuery.getFromPagePath();
		}
		
		if(jsspQuery.getNextEventPagePath() != null
		   &&
		   !"".equals(jsspQuery.getNextEventPagePath())){
			this.nextEventPagePath = jsspQuery.getNextEventPagePath();
		}
		
		if(jsspQuery.getNextEventName() != null
		   &&
		   !"".equals(jsspQuery.getNextEventName())){
			this.nextEventName = jsspQuery.getNextEventName();
		}
		
		if(jsspQuery.getUriPrefix() != null
		   &&
		   !"".equals(jsspQuery.getUriPrefix())){
			this.uriPrefix = jsspQuery.getUriPrefix();
		}
		
		if(jsspQuery.getUriSuffix() != null
		   &&
		   !"".equals(jsspQuery.getUriSuffix())){
			this.uriSuffix = jsspQuery.getUriSuffix();
		}
	}


	public boolean verify() {
		return true;
	}

	public String createJSSPQueryString() {

		// ---- 遷移先ページパス -------------------------------------------------
		// nextPagePathが設定されていない場合は、遷移先情報部分を空文字とする。
		// （<IMART type="submit">のpages属性が省略された場合が該当します）
		String nextPagePath = null;
		if(this.nextEventPagePath == null){
			nextPagePath = "";
		}
		else {
			nextPagePath = JSSPQueryUtil.encode(this.nextEventPagePath);			
		}

		StringBuffer buf4QueryString = new StringBuffer();

		// ---- 遷移先イベント名(＝関数名) -------------------------------------------------
		if(this.nextEventName != null){
			String key = config.getJsspKey4NextEventName();
			String value = JSSPQueryUtil.encode(this.nextEventName);
			buf4QueryString.append("&").append(key).append("=").append(value);
		}

		// ---- 遷移元ページパス -------------------------------------------------
		if(this.fromPagePath != null){
			String key = config.getJsspKey4FromPagePath();
			String value = JSSPQueryUtil.encode(this.fromPagePath);
			buf4QueryString.append("&").append(key).append("=").append(value);			
		}
		
		// ---- 遷移前イベントページパス(＝実行する関数が記述されているJSのパス) ---
		if(this.actionEventPagePath != null){
			String key = config.getJsspKey4ActionEventPagePath();
			String value = JSSPQueryUtil.encode(this.actionEventPagePath);
			buf4QueryString.append("&").append(key).append("=").append(value);						
		}

		// ---- 遷移前イベント名(＝関数名) -----------------------------------
		if(this.actionEventName != null){
			String key = config.getJsspKey4ActionEventName();
			String value = JSSPQueryUtil.encode(this.actionEventName);
			buf4QueryString.append("&").append(key).append("=").append(value);						
			
		}
		
		if(buf4QueryString.length() > 0){			
			return this.uriPrefix + nextPagePath + this.uriSuffix + "?" + buf4QueryString.substring(1); // ←先頭の「&」除去
		}
		else{
			return this.uriPrefix + nextPagePath + this.uriSuffix;			
		}
	}
	
	
	
	/**
	 * JSSPページ遷移情報用INPUTタグを生成します。<br/>
	 * このINPUTタグは、GETメソッドでフォームがサブミットされた場合に必要となります。<br/>
	 * <br/>
	 * <HR>
	 * 以下、メモ。<br/><br/>
	 * GETメソッドでフォームがサブミットされた場合、<br/>
	 * ＜FORM＞タグのaction属性で指定された値に、＜INPUT＞タグの内容がクエリー文字列として付加されるが、<br/>
	 * その際、action属性で指定された値はURLエンコード<B>されず</B>、一方、付加された＜INPUT＞タグの内容はURLエンコード<B>される</B>。<br/>
	 * <br/>
	 * <I>【注意点】<br/>
	 * GETメソッドでフォームがサブミットされた場合、<br/>
	 * ＜FORM＞タグのaction属性で指定された値のクエリー文字列部分に、＜INPUT＞タグの内容が追加されるのではなく<B>置き換えられます。</B><br/>
	 * (IE6, NN7.1, FireFox1.5 にて確認。)<br/>
	 * ちなみに、POSTメソッドの場合は、INPUTタグの内容がクエリー文字列として置き換えられません（POSTのボディとして送られるので当たり前。）<br/>
	 * </I>
	 * <br/>
	 * <br/>
	 * 本メソッド内で生成される＜INPUT＞タグのvalueは、<br/>
	 * {@link JSSPQueryUtil#encode()}を利用した後の値、つまり、JSパスの区切り文字を「(2f)」となっているが、<br/>
	 * クエリーの解析時、{@link JSSPQueryUtil#getValueFromQueryString(String, String)}にて<br/>
	 * <br/>
	 * <table border="1">
	 * 	<tr align="center">
	 * 		<td>URLデコード</td>
	 * 	</tr>
	 * 	<tr align="center">
	 * 		<td>↓</td>
	 * 	</tr>
	 * 	<tr align="center">
	 * 		<td>JSSPQueryデコード</td>
	 * 	</tr>
	 * </table>
	 * <br/>
	 * されているため、<br/>
	 * JSSPQuery内では、正しいパス情報を取得することができる。<br/>
	 * <br/>
	 * <HR>
	 * 例：<br/>
	 * <pre>
	 * ＜FORM method="GET" action="sample(2f)test(2f)next_page.jssp?im_from=foo(2f)bar(2f)caller_page"＞
	 * 　　＜INPUT type="hidden" name="im_from" value="sample(2f)test(2f)caller_page"＞
	 * 　　＜INPUT type="submit" value="送信"＞
	 * ＜/FORM＞
	 * </pre>
	 *
	 *
	 *
	 * 以下のリクエストが送信されます。
	 * <pre>
	 * GET http://localhost/sample(2f)test(2f)next_page.jssp?im_from=sample%282f%29test%282f%29caller_page HTTP/1.0
	 * </pre>
	 *
	 * <table border="1">
	 * 	<tr align="center">
	 * 		<th>＜FORM＞タグのaction属性内の「(2f)」</th><th>＜INPUT＞タグ内の「(2f)」</th>
	 * 	</tr>
	 * 	<tr align="center">
	 * 		<td>(2f)</td><td>(2f)</td>
	 * 	</tr>
	 * 	<tr align="center">
	 * 		<td colspan="2">↓(ブラウザからリクエスト送信)</td>
	 * 	</tr>
	 * 	<tr align="center">
	 * 		<td>(2f)</td><td>%282f%29</td>
	 * 	</tr>
	 * 	<tr align="center">
	 * 		<td colspan="2">↓(URLデコード)</td>
	 * 	</tr>
	 * 	<tr align="center">
	 * 		<td>(2f)</td><td>(2f)</td>
	 * 	</tr>
	 * 	<tr align="center">
	 * 		<td colspan="2">↓(JSSPQueryデコード)</td>
	 * 	</tr>
	 * 	<tr align="center">
	 * 		<td>/</td><td>/</td>
	 * 	</tr>
	 * </table>
	 *
	 * @return
	 */
	public String createJSSPQueryInputTagString() {

		StringBuffer buf4InputTag = new StringBuffer();

		// ---- 遷移先ページパス -------------------------------------------------
		//		// nextPagePathはクエリー文字列に含めない
		//		//（∵本クラスでは、遷移先ページ情報を必ずサーブレットパス部分に格納するため）
		//		if(this.nextEventPagePath != null){
		//			String key = config.getJsspKey4NextEventName();
		//			String value = JSSPQueryUtil.encode(this.nextEventName);
		//			buf4InputTag.append("<INPUT type=\"hidden\" name=\"" + key + "\" value=\"" + value + "\" />");
		//		}

		// ---- 遷移先イベント名(＝関数名) -------------------------------------------------
		if(this.nextEventName != null){
			String key = config.getJsspKey4NextEventName();
			String value = JSSPQueryUtil.encode(this.nextEventName);
			buf4InputTag.append("<INPUT type=\"hidden\" name=\"" + key + "\" value=\"" + value + "\" />");
		}

		// ---- 遷移元ページパス -------------------------------------------------
		if(this.fromPagePath != null){
			String key = config.getJsspKey4FromPagePath();
			String value = JSSPQueryUtil.encode(this.fromPagePath);
			buf4InputTag.append("<INPUT type=\"hidden\" name=\"" + key + "\" value=\"" + value + "\" />");
		}

		// ---- 遷移前イベントページパス(＝実行する関数が記述されているJSのパス) ---
		if(this.actionEventPagePath != null){
			String key = config.getJsspKey4ActionEventPagePath();
			String value = JSSPQueryUtil.encode(this.actionEventPagePath);
			buf4InputTag.append("<INPUT type=\"hidden\" name=\"" + key + "\" value=\"" + value + "\" />");
		}

		// ---- 遷移前イベント名(＝関数名) -----------------------------------
		if(this.actionEventName != null){
			String key = config.getJsspKey4ActionEventName();
			String value = JSSPQueryUtil.encode(this.actionEventName);
			buf4InputTag.append("<INPUT type=\"hidden\" name=\"" + key + "\" value=\"" + value + "\" />");			
		}
		
		return buf4InputTag.toString();
		
	}


	public String getActionEventName() {
		return actionEventName;
	}

	public void setActionEventName(String actionEventName) {
		ScriptScope scope = ScriptScope.current();
		Object value = scope.get(actionEventName, null);
		
		if(value != null){
			if(value instanceof Function){
				this.actionEventName = actionEventName;
				this.actionEventPagePath = scope.getScriptSourcePath();
			}
			else{
				throw new IllegalArgumentException("Function is not found: " + actionEventName + "() in " + scope.getScriptSourcePath());
			}
		}
		else{
			throw new IllegalArgumentException("Unknown Script has no Function: " + actionEventName + "()");
		}
		
		
	}

	public String getActionEventPagePath() {
		return actionEventPagePath;
	}
	// 本設定が出来るのはコンストラクタのみ
	//	public void setActionEventPagePath(String actionEventPagePath) {
	//		this.actionEventPagePath = actionEventPagePath;
	//	}

	
	public String getFromPagePath() {
		return fromPagePath;
	}
	public void setFromPagePath(String fromPagePath) {
		this.fromPagePath = fromPagePath;
	}

	
	public String getNextEventName() {
		return nextEventName;
	}
	public void setNextEventName(String nextEventName) {
		this.nextEventName = nextEventName;
	}

	
	public String getNextEventPagePath() {
		return nextEventPagePath;
	}
	public void setNextEventPagePath(String nextEventPagePath) {
		this.nextEventPagePath = nextEventPagePath;
	}

	
	public String getUriPrefix() {
		return this.uriPrefix;
	}
	public void setUriPrefix(String uriPrefix) {
		this.uriPrefix = uriPrefix;
	}


	public String getUriSuffix() {
		return this.uriSuffix;
	}
	public void setUriSuffix(String urlSuffix) {
		this.uriSuffix = urlSuffix;
	}

	
	public void removeActionEventName() {
		this.actionEventName = null;
	}

	public void removeActionEventPagePath() {
		this.actionEventPagePath = null;
	}

	public void removeFromPagePath() {
		this.fromPagePath = null;
	}

	public void removeNextEventName() {
		this.nextEventName = null;
		
	}

	public void removeNextEventPagePath() {
		this.nextEventPagePath = null;
		
	}	


	/**
	 * POST された形式のデータ内からIMクエリ情報のクエリー文字列部分のStringを生成します。<BR>
	 * 
	 * @param req リクエスト
	 * @return
	 */
	private String createJSSPQueryStringFromPostData(HttpServletRequest req){

		StringBuffer sb = new StringBuffer();
		
		String jsspValue4Mark = req.getParameter(config.getJsspKey4Mark());
		if(jsspValue4Mark != null){
			sb.append("&");
			sb.append(config.getJsspKey4Mark());
			sb.append("=");
			sb.append(jsspValue4Mark);			
		}
		
		String jsspKey4FromPagePath = req.getParameter(config.getJsspKey4FromPagePath());
		if(jsspKey4FromPagePath != null){
			sb.append("&");
			sb.append(config.getJsspKey4FromPagePath());
			sb.append("=");
			sb.append(jsspKey4FromPagePath);			
		}

		String jsspKey4ActionEventName = req.getParameter(config.getJsspKey4ActionEventName());
		if(jsspKey4ActionEventName != null){
			sb.append("&");
			sb.append(config.getJsspKey4ActionEventName());
			sb.append("=");
			sb.append(jsspKey4ActionEventName);			
		}

		String jsspKey4ActionEventPagePath = req.getParameter(config.getJsspKey4ActionEventPagePath());
		if(jsspKey4ActionEventPagePath != null){
			sb.append("&");
			sb.append(config.getJsspKey4ActionEventPagePath());
			sb.append("=");
			sb.append(jsspKey4ActionEventPagePath);			
		}
		
		String jsspKey4NextEventName = req.getParameter(config.getJsspKey4NextEventName());
		if(jsspKey4NextEventName != null){
			sb.append("&");
			sb.append(config.getJsspKey4NextEventName());
			sb.append("=");
			sb.append(jsspKey4NextEventName);			
		}

		if(sb.indexOf("&") == 0){
			// 先頭の「&」を削除
			return sb.substring(1);		
		}
		else {
			return sb.toString();		
		}
		
	}
	
	/**
	 * @param uri
	 * @return
	 */
	private String getNextEventPagePathFromURI(String uri){

		// 「拡張子」を除去
		int subStringIdx4End   = uri.lastIndexOf(".");
		String pagePath = uri.substring(0, subStringIdx4End);
		
		// (2f)が含まれていたら、最後の「/」以下がページパス
		if(pagePath.indexOf("(2f)") != -1){
			int subStringIdx4LastSlash = pagePath.lastIndexOf("/");
			pagePath = pagePath.substring(subStringIdx4LastSlash + SLASH_LEN);
		}
		
		// 「プレフィックス」を除去
		int subStringIdx4Prefix = pagePath.indexOf(this.uriPrefix);
		if(subStringIdx4Prefix != -1){
			pagePath = pagePath.substring(subStringIdx4Prefix + this.uriPrefix.length());
		}
		
		// デコード
		String decodedPath = JSSPQueryUtil.decode(pagePath);
		
		// 2個以上連続している「/」を削除
		return decodedPath.replaceAll("/{2,}", "");
				
	}

}
