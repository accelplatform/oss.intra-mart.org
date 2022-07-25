package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import javax.servlet.ServletContext;

/**
 * レスポンスイベントに関わる各種コンポーネント（リスナやバリデータ）をを構築するビルダです。
 * 
 */
public interface HttpServletResponseEventComponentBuilder{
	/**
	 * ビルダを初期化します。
	 * @param initParameterValue パラメータ
	 * @param servletContext サーブレットコンテキスト
	 * @throws HttpServletResponseEventException エラーが発生した場合
	 */
	public void init(String initParameterValue, ServletContext servletContext) throws HttpServletResponseEventException;

	/**
	 * リスナの配列を返します。
	 * @return リスナの配列
	 * @throws HttpServletResponseEventListenerException エラーが発生した場合
	 */
	public HttpServletResponseEventListener[] getListeners() throws HttpServletResponseEventListenerException;

	/**
	 * バリデータの配列を返します。
	 * @return バリデータの配列
	 * @throws HttpServletResponseEventValidatorException エラーが発生した場合
	 */
	public HttpServletResponseEventValidator[] getValidators() throws HttpServletResponseEventValidatorException;
}

