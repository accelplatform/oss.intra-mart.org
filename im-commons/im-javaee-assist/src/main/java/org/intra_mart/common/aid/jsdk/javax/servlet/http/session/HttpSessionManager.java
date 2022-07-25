package org.intra_mart.common.aid.jsdk.javax.servlet.http.session;

/**
 * HttpSession に関する情報を提供するクラスの共通インタフェース定義です。
 *
 */
public interface HttpSessionManager{
	/**
	 * 現在有効な HttpSession の数を返します。
	 * @return 有効な HttpSession の個数
	 */
	public int getCount();

	/**
	 * 現在有効なすべての HttpSession のセッションＩＤを返します。
	 * セッションＩＤは、{@link javax.servlet.http.HttpSession#getId()} が
	 * 返す値です。
	 * @return セッションＩＤの配列
	 */
	public String[] getAllIds();

	/**
	 * 指定のＩＤに関連付けられているＨＴＴＰセッションが
	 * 作られた時刻を返します。（任意のオペレーション）
	 * このメソッドが返す値は、
	 * {@link javax.servlet.http.HttpSession#getCreationTime()}
	 * が返す値です。<p>
	 * 指定のＩＤに関連付けられている有効なＨＴＴＰセッションがない場合、
	 * {@link NoSuchSessionException} をスローします。<p>
	 * このインタフェースの実装が、このメソッドをサポートしない場合、
	 * {@link java.lang.UnsupportedOperationException} をスローします。
	 * @param id セッションＩＤ
	 * @return セッションが作成された時刻
	 * @throws NoSuchSessionException 指定のＩＤに関連付けられた有効なセッションを見つけられなかった場合
	 * @throws UnsupportedOperationException このメソッドがサポートされない場合
	 */
	public long getCreationTime(String id) throws NoSuchSessionException, UnsupportedOperationException;

	/**
	 * 指定のＩＤに関連付けられてるセッションに関連したリクエストをクライアントが送った最後の時刻を返します。（任意のオペレーション）
	 * このメソッドが返す値は、
	 * {@link javax.servlet.http.HttpSession#getLastAccessedTime()}
	 * が返す値です。<p>
	 * 指定のＩＤに関連付けられている有効なＨＴＴＰセッションがない場合、
	 * {@link NoSuchSessionException} をスローします。<p>
	 * このインタフェースの実装が、このメソッドをサポートしない場合、
	 * {@link java.lang.UnsupportedOperationException} をスローします。
	 *
	 * @param id セッションＩＤ
	 * @return セッションに対して最後にアクセスした時刻
	 * @throws NoSuchSessionException 指定のＩＤに関連付けられた有効なセッションを見つけられなかった場合
	 * @throws UnsupportedOperationException このメソッドがサポートされない場合
	 */
	public long getLastAccessedTime(String id) throws NoSuchSessionException, UnsupportedOperationException;

	/**
	 * 指定のＩＤに関連付けられてるセッションを保ち続ける最大の秒数を返します。（任意のオペレーション）
	 * このメソッドが返す値は、
	 * {@link javax.servlet.http.HttpSession#getMaxInactiveInterval()}
	 * が返す値です。<p>
	 * 指定のＩＤに関連付けられている有効なＨＴＴＰセッションがない場合、
	 * {@link NoSuchSessionException} をスローします。<p>
	 * このインタフェースの実装が、このメソッドをサポートしない場合、
	 * {@link java.lang.UnsupportedOperationException} をスローします。
	 *
	 * @param id セッションＩＤ
	 * @return クライアントからのリクエストの間に何秒間セッションを持続するかを表す数値
	 * @throws NoSuchSessionException 指定のＩＤに関連付けられた有効なセッションを見つけられなかった場合
	 * @throws UnsupportedOperationException このメソッドがサポートされない場合
	 */
	public int getMaxInactiveInterval(String id) throws NoSuchSessionException, UnsupportedOperationException;
}
