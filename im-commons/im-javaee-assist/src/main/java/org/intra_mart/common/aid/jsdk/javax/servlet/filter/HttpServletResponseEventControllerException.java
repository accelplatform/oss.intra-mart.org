package org.intra_mart.common.aid.jsdk.javax.servlet.filter;


/**
 * コントローラに関わる処理中にエラーが発生した事を通知する例外です。
 */
public class HttpServletResponseEventControllerException extends HttpServletResponseEventException{
	/**
	 * 詳細メッセージを null にして、新規例外を構築します。
	 * このコンストラクタでは原因を初期化しません。
	 */
	public HttpServletResponseEventControllerException(){
		super();
	}

	/**
	 * 指定された詳細メッセージおよび原因を使用して新規例外を構築します。
	 * このコンストラクタでは原因を初期化しません。
	 * @param message 詳細メッセージ。詳細メッセージは {@link java.lang.Throwable#getMessage()} メソッドによる取得用に保存される
	 */
	public HttpServletResponseEventControllerException(String message){
		super(message);
	}

	/**
	 * 指定された詳細メッセージおよび原因を使用して新規例外を構築します。
	 * <p>
	 * cause と関連付けられた詳細メッセージが、
	 * この例外の詳細メッセージに自動的に統合されることはありません。
	 * @param message 詳細メッセージ。詳細メッセージは {@link java.lang.Throwable#getMessage()} メソッドによる取得用に保存される
	 * @param cause 原因 ({@link java.lang.Throwable#getCause()} メソッドによる取得用に保存される)。null 値が許可される。null 値は原因が存在しないか未知であることを示す
	 */
	public HttpServletResponseEventControllerException(String message, Throwable cause){
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して新規例外を構築します。
	 * @param cause 原因 ({@link java.lang.Throwable#getCause()} メソッドによる取得用に保存される)。null 値が許可される。null 値は原因が存在しないか未知であることを示す
	 */
	public HttpServletResponseEventControllerException(Throwable cause){
		super(cause);
	}
}
