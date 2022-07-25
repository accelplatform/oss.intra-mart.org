package org.intra_mart.common.aid.jsdk.javax.servlet.exception;

/**
 * 指定されたコンテンツが見つからないことを通知するための例外です。
 * 
 */
public class NotFoundException extends ExtendedServletException{
	/**
	 * 指定のメッセージを持つ例外を作成します。
	 * @param message メッセージ
	 */
	public NotFoundException(String message){
		super(message);
	}

	/**
	 * 指定のメッセージと原因となった例外を持つ例外を作成します。
	 * @param message メッセージ
	 * @param rootCause 原因となった例外
	 */
	public NotFoundException(String message, Throwable rootCause){
		super(message, rootCause);
	}

	/**
	 * 例外を作成します。
	 */
	public NotFoundException(){
		super();
	}
}

