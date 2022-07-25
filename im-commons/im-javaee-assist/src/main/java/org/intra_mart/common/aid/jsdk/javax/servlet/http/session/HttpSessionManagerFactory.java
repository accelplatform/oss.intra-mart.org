package org.intra_mart.common.aid.jsdk.javax.servlet.http.session;

/**
 * {@link HttpSessionManager} を作成するファクトリクラスの抽象実装です。
 * 
 */
public abstract class HttpSessionManagerFactory{
	/**
	 * 標準提供となるファクトリの実装クラス名
	 */
	private static final String implClassName = "org.intra_mart.common.aid.jsdk.javax.servlet.http.session.impl.HttpSessionManagerFactoryImpl";
	/**
	 * インスタンスを返します。<br>
	 * このメソッドが返すファクトリは、システムに標準実装されているものです。
	 * 独自実装を利用する場合は、このクラスを継承して新しいサブクラスを
	 * 作成してください。
	 * @return ファクトリオブジェクトを返します。
	 */
	public static HttpSessionManagerFactory getInstance(){
		try{
			try{
				ClassLoader loader = HttpSessionManagerFactory.class.getClassLoader();
				Class clazz = loader.loadClass(implClassName);
				return (HttpSessionManagerFactory) clazz.newInstance();
			}
			catch(ClassNotFoundException cnfe){
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				Class clazz = loader.loadClass(implClassName);
				return (HttpSessionManagerFactory) clazz.newInstance();
			}
		}
		catch(Throwable t){
			throw new IllegalStateException("No implementation error.");
		}
	}

	/**
	 * 新しい HttpSessionManagerFactory を構築します。
	 */
	protected HttpSessionManagerFactory(){
		super();
	}

	/**
	 * マネージャを返します。
	 * @return マネージャ
	 */
	public abstract HttpSessionManager getManager();
}
