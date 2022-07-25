package org.intra_mart.common.aid.jsdk.javax.servlet.exception;

/**
 * サーバの都合により一時的にリクエストを処理できないことを
 * 通知するための例外です。
 * 
 */
public class ServiceUnavailableException extends ExtendedServletException{
	/**
	 * 指定のメッセージを持つ例外を作成します。
	 * @param message メッセージ
	 */
	public ServiceUnavailableException(String message){
		super(message);
	}

	/**
	 * 指定のメッセージと原因となった例外を持つ例外を作成します。
	 * @param message メッセージ
	 * @param rootCause 原因となった例外
	 */
	public ServiceUnavailableException(String message, Throwable rootCause){
		super(message, rootCause);
	}

	/**
	 * 例外を作成します。
	 */
	public ServiceUnavailableException(){
		super();
	}
}

