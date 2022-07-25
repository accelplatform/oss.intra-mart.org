package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse;


/**
 * コントローラを構築するビルダです。
 */
public interface HttpServletResponseEventControllerBuilder{
	/**
	 * ビルダを初期化します。
	 * @param initParameterValue パラメータ
	 * @param servletContext サーブレットコンテキスト
	 * @throws HttpServletResponseEventControllerException エラーが発生した場合
	 */
	public void init(String initParameterValue, ServletContext servletContext) throws HttpServletResponseEventControllerException;

	/**
	 * コントローラを返します。
	 * @return コントローラ
	 * @throws HttpServletResponseEventControllerException エラーが発生した場合
	 */
	public HttpServletResponseEventController createController(HttpServletRequest request, ExtendedHttpServletResponse response, HttpServletResponseEventListener listener) throws HttpServletResponseEventControllerException;
}