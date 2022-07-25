package org.intra_mart.common.aid.jsdk.javax.servlet.exception;


/**
 * 不正なリクエストを受け付けたことを通知するための例外です。
 */
public class BadRequestException extends ExtendedServletException{
	/**
	 * 指定のメッセージを持つ例外を作成します。
	 * @param message メッセージ
	 */
	public BadRequestException(String message){
		super(message);
	}

	/**
	 * 指定のメッセージと原因となった例外を持つ例外を作成します。
	 * @param message メッセージ
	 * @param rootCause 原因となった例外
	 */
	public BadRequestException(String message, Throwable rootCause){
		super(message, rootCause);
	}

	/**
	 * 例外を作成します。
	 */
	public BadRequestException(){
		super();
	}
}

