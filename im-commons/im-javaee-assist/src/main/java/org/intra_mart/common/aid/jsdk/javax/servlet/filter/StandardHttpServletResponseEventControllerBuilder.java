package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.ExtendedHttpServletResponse;


/**
 * 標準的なコントローラを構築するビルダです。
 * 
 */
public class StandardHttpServletResponseEventControllerBuilder implements HttpServletResponseEventControllerBuilder{
	/**
	 * 新しいビルダを構築します。
	 */
	public StandardHttpServletResponseEventControllerBuilder(){
		super();
	}

	/**
	 * ビルダを初期化します。
	 * @param initParameterValue パラメータ
	 * @param servletContext サーブレットコンテキスト
	 * @throws HttpServletResponseEventControllerException エラーが発生した場合
	 */
	public void init(String initParameterValue, ServletContext servletContext) throws HttpServletResponseEventControllerException{
		return;
	}

	/**
	 * コントローラを返します。
	 * @return コントローラ
	 * @throws HttpServletResponseEventControllerException エラーが発生した場合
	 */
	public HttpServletResponseEventController createController(HttpServletRequest request, ExtendedHttpServletResponse response, HttpServletResponseEventListener listener) throws HttpServletResponseEventControllerException{
		return new SimpleHttpServletResponseEventController(request, response, listener);
	}



	/**
	 * レスポンスのラッパークラス。<p>
	 * {@link javax.servlet.http.HttpServletResponse} が持つ
	 * 各メソッド（非推奨を除く）をオーバーライドして、
	 * リスナに処理を委任するロジックを追加します。
	 */
	private static class SimpleHttpServletResponseEventController extends AbstractHttpServletResponseEventController{
		/**
		 * 各メソッド呼び出しに対するリスナ
		 */
		private HttpServletResponseEventListener eventListener = null;
		/**
		 * リクエスト
		 */
		private HttpServletRequest httpServletRequest = null;

		/**
		 * 新しいインスタンスを生成します。
		 * @param request リクエスト
		 * @param reqponse ラップするレスポンス
		 * @param listener イベントリスナ
		 */
		public SimpleHttpServletResponseEventController(HttpServletRequest request, ExtendedHttpServletResponse response, HttpServletResponseEventListener eventListener){
			super(response);
			this.httpServletRequest = request;
			this.eventListener = eventListener;
		}

		// ----- HttpServletResponseEventController interface
		/**
		 * リクエストを返します。
		 * @return 現在のリクエスト
		 */
		protected HttpServletRequest getHttpServletRequest(){
			return this.httpServletRequest;
		}

		/**
		 * リスナを返します。
		 * @return リスナ
		 */
		protected HttpServletResponseEventListener getHttpServletResponseEventListener(){
			return this.eventListener;
		}
	}
}
