package org.intra_mart.common.aid.jsdk.javax.servlet.http.session.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.http.HttpSession;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.session.AbstractHttpSessionManager;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.session.NoSuchSessionException;


/**
 * HttpSession に関する情報を提供するクラスの共通インタフェースの実装です。
 *
 */
public class HttpSessionManagerImpl extends AbstractHttpSessionManager{
	/**
	 * このクラスから構築される唯一のインスタンスです。
	 */
	private static HttpSessionManagerImpl httpSessionManager = null;

	/**
	 * このクラスのインスタンスを返します。
	 * シングルトンパターンによって作成されているため、
	 * 常に同じインスタンスを返します。
	 */
	public static synchronized HttpSessionManagerImpl getInstance(){
		if(httpSessionManager == null){
			httpSessionManager = new HttpSessionManagerImpl();
		}
		return httpSessionManager;
	}

	/**
	 * セッションＩＤの一覧
	 */
	private Map idMap = new HashMap();
	/**
	 * セッションが作られた時刻。
	 * キーはセッションＩＤ。
	 */
	private Map creationTimeMap = new WeakHashMap();
	/**
	 * セッションに関連づいたリクエストを最後に受けた時刻。
	 * キーはセッションＩＤ。
	 */
	private Map lastAccessedTimeMap = new WeakHashMap();
	/**
	 * セッションを保持する時間
	 * キーはセッションＩＤ。
	 */
	private Map maxInactiveIntervalMap = new WeakHashMap();

	/**
	 * 新しいマネージャを構築します。
	 */
	private HttpSessionManagerImpl(){
		super();
	}

	/**
	 * 現在有効な HttpSession の数を返します。
	 * @return 有効な HttpSession の個数
	 */
	public synchronized int getCount(){
		return this.idMap.size();
	}

	/**
	 * 現在有効なすべての HttpSession のセッションＩＤを返します。
	 * セッションＩＤは、{@link javax.servlet.http.HttpSession#getId()} が
	 * 返す値です。
	 * @return セッションＩＤの配列
	 */
	public synchronized String[] getAllIds(){
		return (String[]) this.idMap.keySet().toArray(new String[this.idMap.size()]);
	}

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
	public synchronized long getCreationTime(String id){
		Object key = this.getKey(id);
		Number time = (Number) this.creationTimeMap.get(key);
		return time.longValue();
	}

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
	public synchronized long getLastAccessedTime(String id){
		Object key = this.getKey(id);
		Number time = (Number) this.lastAccessedTimeMap.get(key);
		return time.longValue();
	}

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
	public synchronized int getMaxInactiveInterval(String id){
		Object key = this.getKey(id);
		Number time = (Number) this.maxInactiveIntervalMap.get(key);
		return time.intValue();
	}

	/**
	 * セッションＩＤに関連づいた各情報へアクセスするためのハッシュキー。
	 * @param id セッションＩＤ
	 * @return 情報マップにアクセスするためのキー
	 */
	private Object getKey(String id){
		Object key = this.idMap.get(id);
		if(key != null){
			return key;
		}
		else{
			throw new IllegalStateException("HttpSession is invalid: " + id);
		}
	}

	/**
	 * セッションが作られたときに実行されるイベントリスナ。
	 * @param session セッション
	 */
	public synchronized void handleSessionCreated(HttpSession session){
		String id = session.getId();
		Object key = new Object();
		this.idMap.put(id, key);
		this.creationTimeMap.put(key, new Long(session.getCreationTime()));
		this.updateStatus(session, key);
	}

	/**
	 * セッションが破棄されたときに実行されるイベントリスナ。
	 * @param session セッション
	 */
	public synchronized void handleSessionDestroyed(HttpSession session){
		Object key = this.idMap.remove(session.getId());
		this.creationTimeMap.remove(key);
		this.lastAccessedTimeMap.remove(key);
		this.maxInactiveIntervalMap.remove(key);
	}

	/**
	 * セッションに関連づいたリクエストを受け付けたときに実行される
	 * イベントリスナ。
	 * @param session セッション
	 */
	public synchronized void handleRequestAccepted(HttpSession session){
		try{
			String id = session.getId();
			Object key = this.getKey(id);
			this.updateStatus(session, key);
		}
		catch(IllegalStateException ise){
			this.handleSessionCreated(session);
		}
	}

	/**
	 * 指定の情報で内部ステータスを更新します。
	 * @param session セッション
	 * @param key 各情報を管理する情報マップのキー
	 */
	private void updateStatus(HttpSession session, Object key){
		this.lastAccessedTimeMap.put(key, new Long(session.getLastAccessedTime()));
		this.maxInactiveIntervalMap.put(key, new Integer(session.getMaxInactiveInterval()));
	}
}
