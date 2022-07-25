package org.intra_mart.common.aid.jsdk.javax.servlet.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * レスポンスの Content-Type を自動解決するフィルタです。<BR>
 * このフィルタの次のチェーン以降でレスポンス(ServletResponse)に対して、
 * #setCopntentType() を呼び出さなかった場合、
 * #getWriter() または #getOutputStream() 呼び出し時に Content-Type を自動的に設定します。
 * <p>
 * 設定する Content-Type は、このフィルタの初期化パラメータ Content-Type で
 * 指定された値です。
 * 初期化パラメータ Content-Type が未定義の場合、text/html が設定されます。
 *
 */
public class ResponseContentTypeManagementFilter extends AbstractFilter {
	/**
	 * デフォルトの MIME タイプ
	 */
	private String contentType = "text/html";

	/**
	 * 新しくフィルタを作成します。
	 */
	public ResponseContentTypeManagementFilter(){
		super();
	}

	/**
	 * このフィルタを初期化します。
	 */
	public void handleInit(){
		FilterConfig config = getFilterConfig();

		// Content-Type 設定値を取得
		String type = config.getInitParameter("Content-Type");
		if(type != null){
			this.contentType = type;
		}
	}

	/**
	 * レスポンスソースの Content-Type を
	 * 自動解決する HttpServletResponse を作成して、
	 * 次のフィルタチェーンを実行します。<p>
	 * このメソッドが作る HttpServletResponse は、引数 response をラップし、
	 * #getWriter() または #getOutputStream() メソッドが実行された時に
	 * #setContentType() を一度も呼び出されていない場合、
	 * フィルタの初期化パラメータで設定されている Content-Type をセットしてから、
	 * 引数 response の #getWriter() または #getOutputStream() メソッドを
	 * 呼び出します。
	 *
	 * @param servletRequest リクエスト
	 * @param servletResponse レスポンス
	 * @param filterChain フィルタ
	 * @throws IOException フィルタ処理実行時にIOExceptionが発生
	 * @throws ServletException フィルタ処理実行時にServletExceptionが発生
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException{
		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
		HttpServletResponseWrapper responseWrapper = new ContentTypeManagementHttpServletResponse(httpServletResponse, this.contentType);

		filterChain.doFilter(servletRequest, responseWrapper);
	}
}


class ContentTypeManagementHttpServletResponse extends HttpServletResponseWrapper{
	/**
	 * デフォルトの Content-Type
	 */
	private String defaultContentType = "text/html";
	/**
	 * このオブジェクトの #setContentType() メソッドを呼び出してセットされた
	 * コンテンツタイプを表す文字列。
	 */
	private String contentType = null;

	/**
	 * 引数 response をラップした新しいレスポンスを作成します。
	 * @param response 基礎となるレスポンス
	 */
	private ContentTypeManagementHttpServletResponse(HttpServletResponse response){
		super(response);
	}

	/**
	 * 引数 response をラップした新しいレスポンスを作成します。
	 * @param response 基礎となるレスポンス
	 * @param type Content-Type 設定を未定義だった場合のデフォルト値
	 */
	public ContentTypeManagementHttpServletResponse(HttpServletResponse response, String type){
		this(response);
		this.defaultContentType = type;
	}

	/**
	 * クライアントに送り返されるレスポンスのコンテントタイプをセットします。
	 * コンテントタイプには、例えば、 text/html; charset=ISO-8859-4  のように
	 * 文字エンコーディングのタイプが含まれることがあります。
	 * <p>
	 * PrintWriter オブジェクトを取得する場合はあらかじめこのメソッドを
	 * 実行しておくべきです。
	 * @param type コンテントタイプを指定する String
	 */
	public void setContentType(String type){
		this.getResponse().setContentType(type);	// レスポンスに設定
		this.contentType = type;					// 設定内容の保存
	}

	/**
	 * 文字データをクライアントに送り返すのに使用する PrintWriterオブジェクトを
	 * 返します。<br>
	 * ここで適用される文字エンコーディングは
	 * setContentType(java.lang.String) メソッドで
	 * charset= の形式で指定したものです。
	 * 指定の文字エンコーディングに対応したPrintWriter  型のオブジェクトを
	 * 取得するにはこのメソッドよりも前に
	 * setContentType(java.lang.String) メソッドが
	 * 実行されていなければなりません。
	 * <p>
	 * 必要ならば、使用されている文字エンコーディングを反映するように
	 * レスポンスの MIME タイプが修正されます。<p>
	 * PrintWriter の flash() メソッドを呼び出すと
	 * レスポンスがコミットされます。
	 * <p>
	 * このメソッドか getOutputStream() メソッドのどちらかを
	 * ボディメッセージの出力に使用します。両方は使えません。
	 *
	 * @return クライアントに文字データを送り返すことができる PrintWriter オブジェクト
	 * @throws UnsupportedEncodingException setContentType  メソッドで指定された文字エンコーディングがサポートされていない場合
	 * @throws IllegalStateException このレスポンスオブジェクトの getOutputStream  メソッドがすでに実行されていた場合
	 * @throws UnsupportedOperationException アクセスセキュリティのセッションが解決できなかった場合
	 * @throws IOException 入出力時に例外が発生した場合
	 */
	public PrintWriter getWriter() throws IOException{
		if(this.contentType == null){
			// Content-Typeが不明→設定
			this.getResponse().setContentType(this.defaultContentType);
		}

		return this.getResponse().getWriter();
	}

	/**
	 * レスポンスにバイナリデータを出力する際に使用する
	 * ServletOutputStream型のオブジェクトを返します。
	 * Servlet コンテナがバイナリデータのエンコードをすることはありません。
	 * <p>
	 * レスポンスをコミットするには ServletOutputStream の flush() メソッドを
	 * 呼び出してください。
	 * このメソッドか getWriter() メソッドのどちらかをメッセージボディの出力に
	 * 使います。両方を使うことはできません。
	 * @retirn バイナリデータ出力に使用する ServletOutputStream
	 * @throws IllegalStateException このレスポンスですでに getWriter()  メソッドが実行されていた場合
	 * @throws IOException 入出力時に例外が発生した場合
	 */
	public ServletOutputStream getOutputStream() throws IOException{
		if(this.contentType == null){
			// Content-Typeが不明→設定
			this.getResponse().setContentType(this.defaultContentType);
		}

		return this.getResponse().getOutputStream();
	}
}


