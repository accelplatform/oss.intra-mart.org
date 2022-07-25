package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse;

/**
 * レスポンスイベントコントローラのインタフェース。
 */
public interface HttpServletResponseEventController extends ExtendedHttpServletResponse{
	/**
	 * このビルダを初期化します。
	 * @param config 初期化パラメータ
	 *
	 * @throws HttpServletResponseEventControllerException 初期化に失敗した場合
	 */
	public void init(HttpServletResponseEventConfig config) throws HttpServletResponseEventControllerException;
}
