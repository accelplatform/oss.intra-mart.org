package org.intra_mart.common.aid.jsdk.javax.servlet.exception;

/**
 * クエリが Content-Lnegth ヘッダで指定された量に満たない場合に
 * スローされる例外です。
 * 
 */
public class UnripeQueryException extends BadRequestException{
	/**
	 * 指定のメッセージを持つ例外を作成します。
	 * @param message メッセージ
	 */
	public UnripeQueryException(String message){
		super(message);
	}

	/**
	 * 指定のメッセージと原因となった例外を持つ例外を作成します。
	 * @param message メッセージ
	 * @param rootCause 原因となった例外
	 */
	public UnripeQueryException(String message, Throwable rootCause){
		super(message, rootCause);
	}

	/**
	 * 例外を作成します。
	 */
	public UnripeQueryException(){
		super();
	}
}

