package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

/**
 * 絶対表現のＵＲＬをレスポンスするためのフィルタ。<p>
 * このフィルタの目的は、ServletResponse#encodeURL() の実行結果として、
 * 単にセッションＩＤ情報を付加するだけではなく、必ず絶対形式のＵＲＬを
 * 作成することです。
 * したがって、このフィルタは、チェーンの次サーブレットまたはフィルタに対して、
 * ラップしたレスポンスを渡します。
 * <p>
 * このフィルタがレスポンスをラップするのは、事前に指定されたセッションに
 * 関連したリクエストのみです。
 * {@link #entry(ServletRequest)} または {@link #entry(HttpSession)} で
 * 登録していないセッションに関するリクエストについては、
 * レスポンスをラップすることなくチェーンの次のサーブレットまたはフィルタを
 * 実行します。
 *
 */
public class AbsoluteLinkFilter extends AbstractFilter {
	/**
	 * イベントで作成したＵＲＬかどうかを判定するための情報を
	 * セッションに保存しておくためのキー
	 */
	private static final String ID_EVENT = "org.intra_mart.servlet.HTTPActionEventFilter#event.session";

	/**
	 * このフィルタによってＵＲＬの制御を行うことを定義します。<p>
	 * この処理は、このリクエスト中のみでなく、
	 * 同じセッションのすべてのリクエストに対して有効となります。
	 * @param request リクエスト
	 * @throws NullPointerException 引数が null の場合
	 * @throws IllegalStateException セッションを取得できなかった場合
	 */
	public static void entry(HttpServletRequest request){
		if(request != null){
			HttpSession session = request.getSession(true);
			if(session != null){
				entry(session);
			}
			else{
				throw new IllegalStateException("Session is not found.");
			}
		}
		else{
			throw new NullPointerException("Method-parameter error at AbsoluteLinkFilter#entry(): request is null.");
		}
	}

	/**
	 * このフィルタによってＵＲＬの制御を行うことを定義します。<p>
	 * この処理は、このリクエスト中のみでなく、
	 * 同じセッションのすべてのリクエストに対して有効となります。
	 * @param session セッション
	 * @throws NullPointerException 引数が null の場合
	 */
	public static void entry(HttpSession session){
		if(session != null){
			session.setAttribute(ID_EVENT, Boolean.TRUE);
		}
		else{
			throw new NullPointerException("Method-parameter error at AbsoluteLinkFilter#entry(): session is null.");
		}
	}

	/**
	 * 指定のセッションに関する、このフィルタの処理を停止します。
	 * この処理は、このリクエスト中のみでなく、
	 * 同じセッションのすべてのリクエストに対して有効となります。<p>
	 * 既にセッションが有効でない場合、このメソッドは何も行いません。
	 * @param request リクエスト
	 * @throws NullPointerException 引数が null の場合
	 */
	public static void leave(HttpServletRequest request){
		if(request != null){
			HttpSession session = request.getSession(false);
			if(session != null){ leave(session); }
		}
		else{
			throw new NullPointerException("Method-parameter error at AbsoluteLinkFilter#leave(): request is null.");
		}
	}

	/**
	 * 指定のセッションに関する、このフィルタの処理を停止します。
	 * この処理は、このリクエスト中のみでなく、
	 * 同じセッションのすべてのリクエストに対して有効となります。<p>
	 * @param session セッション
	 * @throws NullPointerException 引数が null の場合
	 */
	public static void leave(HttpSession session){
		if(session != null){
			session.setAttribute(ID_EVENT, Boolean.FALSE);
		}
		else{
			throw new NullPointerException("Method-parameter error at AbsoluteLinkFilter#leave(): session is null.");
		}
	}

	/**
	 * フィルタを新しく作成します。
	 */
	public AbsoluteLinkFilter() {
		super();
	}

	/**
	 * フィルタ処理します。<p>
	 * 現在のリクエストに関連付けられているセッションが、
	 * このクラスの {@link #entry(HttpSession)} で定義済みのセッションの場合、
	 * レスポンスをラップします。
	 * ラップされたレスポンスは、javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String) の
	 * 返り値として、必ず絶対形式のＵＲＬを返すようになります。<p>
	 * レスポンスをラップするかどうかに関わらず、
	 * このメソッドは、フィルタチェーン内の次のチェーンを呼び出します。
	 *
	 * @param request リクエスト
	 * @param response レスポンス
	 * @param chain フィルタチェーンの次のビュー
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException{
		// セッションの取得
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpSession httpSession = httpServletRequest.getSession(false);

		if(httpSession != null){
			if(Boolean.TRUE.equals(httpSession.getAttribute(ID_EVENT))){
				// ターゲットになっている
				chain.doFilter(request, new HttpServletResponseWrapper4absuluteApplication(httpServletRequest, (HttpServletResponse) response));
			}
			else{
				// 処理は不要
				chain.doFilter(request, response);
			}
		}
		else{
			// セッション不明→スルー
			chain.doFilter(request, response);
		}
	}


	/**
	 * 他環境から自動的に絶対パスでリンクできるようにＵＲＬを自動制御するレスポンス
	 */
	private static class HttpServletResponseWrapper4absuluteApplication extends HttpServletResponseWrapper{
		private HttpServletRequest request = null;
		private URL contextURL = null;
		private Properties cache = new Properties();

		protected HttpServletResponseWrapper4absuluteApplication(HttpServletRequest request, HttpServletResponse response){
			super(response);
			this.request = request;
		}

		public String encodeURL(String url){
			// URL の絶対パスを求めてセッション情報を付加したものを返却
			String targetURL = this.cache.getProperty(url);
			if(targetURL == null){
				try {
					if(this.contextURL == null){
						this.contextURL = new URL(new String(this.request.getRequestURL()));
					}

					URL absoluteURL = new URL(this.contextURL, url);
					targetURL = absoluteURL.toExternalForm();
				}
				catch(MalformedURLException murle){
					targetURL = url;
				}

				HttpServletResponse response = (HttpServletResponse) this.getResponse();
				targetURL = response.encodeURL(targetURL);
				this.cache.setProperty(url, targetURL);		// キャッシュ
			}

			return targetURL;
		}
	}
}
