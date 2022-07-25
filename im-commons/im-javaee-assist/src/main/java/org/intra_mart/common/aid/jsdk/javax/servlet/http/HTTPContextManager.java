package org.intra_mart.common.aid.jsdk.javax.servlet.http;



/**
 * HTTP コンテキスト・マネージャ
 * 
 */
public abstract class HTTPContextManager {

	/**
	 * デフォルトの実装クラス名
	 */
	private static final String defaultImplClassName = "org.intra_mart.common.aid.jsdk.javax.servlet.http.impl.HTTPContextManagerImpl";

	/**
	 * 唯一のインスタンス
	 */
	private static HTTPContextManager _instance = null;
	
	/**
	 * HTTP コンテキスト・マネージャのインスタンスを取得します。
	 * @return HTTP コンテキスト・マネージャ
	 */
	public static synchronized HTTPContextManager getInstance() {

		if (_instance == null) {
			
			try {
				Thread t = Thread.currentThread();
				Class clazz = t.getContextClassLoader().loadClass(defaultImplClassName);
				_instance = (HTTPContextManager) clazz.newInstance();
			}
			catch(ClassNotFoundException ex) {
				IllegalStateException ise = new IllegalStateException(ex.getMessage());
				ise.initCause(ex);
				throw ise;
			}
			catch(InstantiationException e) {
				IllegalStateException ise = new IllegalStateException(e.getMessage());
				ise.initCause(e);
				throw ise;
			}
			catch(IllegalAccessException e) {
				IllegalStateException ise = new IllegalStateException(e.getMessage());
				ise.initCause(e);
				throw ise;
			}
		}
		
		return _instance;
	}

	/**
	 * デフォルトコンストラクタ
	 * {@link HTTPContextManager#getInstance()}を利用してください。
	 */
	protected HTTPContextManager() {
		super();
	}

	/**
	 * 現在のスレッドに関連付けられた HTTP コンテキストを返します。
	 * @return 現在のスレッドに関連付けられた HTTP コンテキスト
	 */
	public abstract HTTPContext getCurrentContext();
	
	/**
	 * マネージャーが保持するすべての HTTP コンテキストを返します。
	 * @return マネージャーが保持するすべての HTTP コンテキストの配列
	 * @throws UnsupportedOperationException サブクラスが本メソッドをサポートしていない場合
	 */
	public abstract HTTPContext[] getAllContext() throws UnsupportedOperationException;
	
	/**
	 * マネージャーが保持する HTTP コンテキストの数を返します。
	 * @return マネージャーが保持する HTTP コンテキストの数
	 * @throws UnsupportedOperationException サブクラスが本メソッドをサポートしていない場合
	 */
	public abstract int getLength() throws UnsupportedOperationException;

}

