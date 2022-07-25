package org.intra_mart.jssp.exception;

/**
 * JSSP実行時に発生した例外のスーパークラスです。
 */
public class JSSPException extends RuntimeException{
	/**
	 * 新しい例外を作成します。
	 */
	protected JSSPException(){
		super();
	}

	/**
	 * 新しい例外を作成します。
	 * @param message 例外のメッセージ
	 */
	protected JSSPException(String message){
		super(message);
	}

	/**
	 * 新しい例外を作成します。
	 * @param message 例外のメッセージ
	 * @param cause 原因
	 */
	protected JSSPException(String message, Throwable cause){
		super(message, cause);
	}

	/**
	 * 新しい例外を作成します。
	 * @param cause 原因
	 */
	protected JSSPException(Throwable cause){
		super(cause);
	}
}
