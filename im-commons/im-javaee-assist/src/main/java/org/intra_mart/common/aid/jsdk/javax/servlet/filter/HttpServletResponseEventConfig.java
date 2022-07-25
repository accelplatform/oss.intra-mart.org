package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import java.util.Enumeration;

import javax.servlet.ServletContext;


/**
 * レスポンスの各メソッド呼び出しに対するイベントを
 * 設定するためのオブジェクトです。
 * リスナやコントローラの初期化の際に情報を渡すために使われます。
 * 
 */
public interface HttpServletResponseEventConfig{
	/**
	 * 指定した名前の初期化パラメータの値である String、
	 * あるいはパラメータが存在しなければ null を返します。
	 * @param name 初期化パラメータの名前を指定する String
	 * @return 初期化パラメータの値である Strin
	 */
	public String getInitParameter(String name);

	/**
	 * 初期化パラメータの名前である String オブジェクトの Enumeration を返します。
	 * または、初期化パラメータが無ければ空の Enumeration  を返します。
	 * @return 初期化パラメータの名前である String オブジェクトの Enumeration
	 */
	public Enumeration getInitParameterNames();

	/**
	 * 呼び出したコントローラを実行している ServletContext への参照を返します。
	 * @return 呼び出したコントローラが Servlet コンテナとの対話に使っている {@link javax.servlet.ServletContext} オブジェクト
	 */
	public ServletContext getServletContext();

}
