package org.intra_mart.common.aid.jsdk.javax.servlet.exception;

/**
 * クエリが Content-Length ヘッダで指定された量よりも過剰な場合に
 * スローされる例外です。
 */
public class ExcessiveQueryException extends BadRequestException{
	/**
	 * 指定のメッセージを持つ例外を作成します。
	 * @param message メッセージ
	 */
	public ExcessiveQueryException(String message){
		super(message);
	}

	/**
	 * 指定のメッセージと原因となった例外を持つ例外を作成します。
	 * @param message メッセージ
	 * @param rootCause 原因となった例外
	 */
	public ExcessiveQueryException(String message, Throwable rootCause){
		super(message, rootCause);
	}

	/**
	 * 例外を作成します。
	 */
	public ExcessiveQueryException(){
		super();
	}
}

