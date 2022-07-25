package org.intra_mart.common.aid.jsdk.javax.servlet.exception;

/**
 * リクエストを受け付けるためには認証が必要であることを通知するための例外です。
 * 
 */
public class UnauthorizedException extends ExtendedServletException{
	/**
	 * 指定のメッセージを持つ例外を作成します。
	 * @param message メッセージ
	 */
	public UnauthorizedException(String message){
		super(message);
	}

	/**
	 * 指定のメッセージと原因となった例外を持つ例外を作成します。
	 * @param message メッセージ
	 * @param rootCause 原因となった例外
	 */
	public UnauthorizedException(String message, Throwable rootCause){
		super(message, rootCause);
	}

	/**
	 * 例外を作成します。
	 */
	public UnauthorizedException(){
		super();
	}
}

