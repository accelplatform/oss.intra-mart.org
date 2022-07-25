package org.intra_mart.common.aid.jsdk.javax.servlet.exception;

/**
 * クエリがない場合にスローされる例外です。
 */
public class EmptyQueryException extends UnripeQueryException{
	/**
	 * 指定のメッセージを持つ例外を作成します。
	 * @param message メッセージ
	 */
	public EmptyQueryException(String message){
		super(message);
	}

	/**
	 * 指定のメッセージと原因となった例外を持つ例外を作成します。
	 * @param message メッセージ
	 * @param rootCause 原因となった例外
	 */
	public EmptyQueryException(String message, Throwable rootCause){
		super(message, rootCause);
	}

	/**
	 * 例外を作成します。
	 */
	public EmptyQueryException(){
		super();
	}
}
