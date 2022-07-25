package org.intra_mart.common.aid.jsdk.javax.servlet.exception;

/**
 * Content-Length 指定がないためにリクエストを正常に処理できないことを
 * 通知するための例外です。
 * 
 */
public class LengthRequiredException extends BadRequestException{
	/**
	 * 指定のメッセージを持つ例外を作成します。
	 * @param message メッセージ
	 */
	public LengthRequiredException(String message){
		super(message);
	}

	/**
	 * 指定のメッセージと原因となった例外を持つ例外を作成します。
	 * @param message メッセージ
	 * @param rootCause 原因となった例外
	 */
	public LengthRequiredException(String message, Throwable rootCause){
		super(message, rootCause);
	}

	/**
	 * 例外を作成します。
	 */
	public LengthRequiredException(){
		super();
	}
}
