package org.intra_mart.common.aid.jsdk.javax.servlet.exception;

/**
 * アクセス権限がないことを通知する例外です。
 * 
 */
public class ForbiddenException extends ExtendedServletException{
	/**
	 * 指定のメッセージを持つ例外を作成します。
	 * @param message メッセージ
	 */
	public ForbiddenException(String message){
		super(message);
	}

	/**
	 * 指定のメッセージと原因となった例外を持つ例外を作成します。
	 * @param message メッセージ
	 * @param rootCause 原因となった例外
	 */
	public ForbiddenException(String message, Throwable rootCause){
		super(message, rootCause);
	}

	/**
	 * 例外を作成します。
	 */
	public ForbiddenException(){
		super();
	}
}

