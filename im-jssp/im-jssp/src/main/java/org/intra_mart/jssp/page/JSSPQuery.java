package org.intra_mart.jssp.page;

/**
 * JSSPページ遷移情報を操作するインターフェースです。<br/>
 * <br/>
 * JSSPページ遷移情報とは以下を指します。
 * <ul>
 * 	<li>遷移先ページパス</li>
 * 	<li>遷移先イベント名(＝JavaScript関数名)</li>
 * 	<li>遷移元ページパス</li>
 * 	<li>遷移前イベントページパス(＝遷移前イベントが記述されているJavaScriptソースのパス)</li>
 * 	<li>遷移前イベント名(＝JavaScript関数名)</li>
 * </ul>
 */
public interface JSSPQuery {

	/**
	 * JSSPページ遷移情報（＝JSSPクエリ）の整合性をチェックします。
	 * @return JSSPページ遷移情報が<b>正しい</b>場合は true を返却し、<br/>
	 * 			JSSPページ遷移情報が<b>正しくない</b>場合は falseを返却します。
	 */
	public boolean verify();
	
	/**
	 * JSSPページ遷移情報を含んだ文字列を作成します。<br/>
	 * 具体的には、以下の文字列を作成します。<br/>
	 * <pre>サーブレットパス[?クエリ文字列]</pre>
	 * 
	 * @return JSSPページ遷移情報を含んだ文字列
	 */
	public String createJSSPQueryString();

	/**
	 * JSSPページ遷移情報を含んだ&lt;input type="hidden"&gt;タグを作成します。<br/>
	 * （このINPUTタグは、GETメソッドでフォームがサブミットされた場合に必要となります）
	 * 
	 * @return JSSPページ遷移情報を含んだ&lt;input type="hidden"&gt;タグ
	 */
	public String createJSSPQueryInputTagString();

	
	// ================ 遷移前イベントページパス ================ 
	/**
	 * 遷移前イベントページパスを取得します。
	 * @return 遷移前イベントページパス
	 */
	public String getActionEventPagePath();
	
	//  ↓actionEventPagePathを設定できるのはコンストラクタのみ
	//	/**
	//	 * @param pagePath
	//	 */
	//	public void setActionEventPagePath(String pagePath);
	
	/**
	 * 遷移前イベントページパスを削除します。
	 */
	public void removeActionEventPagePath();

	
	// ================ 遷移前イベント名 ================ 
	/**
	 * 遷移前イベント名を取得します。
	 * @return 遷移前イベント名
	 */
	public String getActionEventName();
	
	/**
	 * 遷移前イベント名を設定します。
	 * @param functionName 遷移前イベント名
	 */
	public void setActionEventName(String functionName);
	
	/**
	 * 遷移前イベント名を削除します。
	 */
	public void removeActionEventName();

	
	// ================ 遷移元ページパス ================ 
	/**
	 * 遷移元ページパスを取得します。
	 * @return 遷移元ページパス
	 */
	public String getFromPagePath();
	
	/**
	 * 遷移元ページパスを設定します。
	 * @param pagePath 遷移元ページパス
	 */
	public void setFromPagePath(String pagePath);
	
	/**
	 * 遷移元ページパスを削除します。
	 */
	public void removeFromPagePath();

	
	// ================ 遷移先ページパス ================ 
	/**
	 * 遷移先ページパスを取得します。
	 * @return 遷移先ページパス
	 */
	public String getNextEventPagePath();
	
	/**
	 * 遷移先ページパスを設定します。
	 * @param pagePath 遷移先ページパス
	 */
	public void setNextEventPagePath(String pagePath);
	
	/**
	 * 遷移先ページパスを削除します。
	 */
	public void removeNextEventPagePath();
	
	
	// ================ 遷移先イベント名 ================ 
	/**
	 * 遷移先イベント名を取得します。
	 * @return 遷移先イベント名
	 */
	public String getNextEventName();
	
	/**
	 * 遷移先イベント名を設定します。
	 * @param functionName 遷移先イベント名
	 */
	public void setNextEventName(String functionName);
	
	/**
	 * 遷移先イベント名を削除します。
	 */
	public void removeNextEventName();
		
	
	// ================ プレフィックス ================ 
	/**
	 * JSSPページ遷移時のプレフィックスを取得します。
	 * @return JSSPページ遷移時のプレフィックス
	 */
	public String getUriPrefix();
	
	/**
	 * JSSPページ遷移時のプレフィックスを設定します。
	 * @param uriPrefix JSSPページ遷移時のプレフィックス
	 */
	public void setUriPrefix(String uriPrefix);

	
	// ================ 拡張子 ================ 
	/**
	 * JSSPページ遷移時のサフィックス（拡張子）を取得します。
	 * 返却される値は「.」を含みます。
	 * @return JSSPページ遷移時のサフィックス（拡張子）
	 */
	public String getUriSuffix();
	
	/**
	 * JSSPページ遷移時のサフィックス（拡張子）を設定します。
	 * 設定する値は「.」を含めます。
	 * @param uriSuffix JSSPページ遷移時のサフィックス（拡張子）
	 */
	public void setUriSuffix(String uriSuffix);

		
	/**
	 * 本インスタンスのページ遷移情報を
	 * 引数 jsspQuery のページ遷移情報に置き換えます。<br/>
	 * 
	 * ただし、引数 jsspQuery が保持するページ遷移情報の一部項目が null、または、空文字の場合、
	 * その一部項目に該当するページ遷移情報は置き換えられません。<br/>
	 * （本インスタンスの上記の一部項目に該当するページ遷移情報値がそのまま有効となります）
	 * 
	 * @param jsspQuery
	 */
	public void replaceJSSPQuery(JSSPQuery jsspQuery);
}
