package org.intra_mart.jssp.exception;

/**
 * 実行エラーを通知する例外のスーパークラスです。
 */
public class JSSPSystemException extends JSSPException{
	/**
	 * 新しい例外を作成します。
	 */
	protected JSSPSystemException(){
		super();
	}

	/**
	 * 新しい例外を作成します。
	 * @param message 例外のメッセージ
	 */
	protected JSSPSystemException(String message){
		super(message);
	}

	/**
	 * 新しい例外を作成します。
	 * @param message 例外のメッセージ
	 * @param cause 原因
	 */
	protected JSSPSystemException(String message, Throwable cause){
		super(message, cause);
	}

	/**
	 * 新しい例外を作成します。
	 * @param cause 原因
	 */
	protected JSSPSystemException(Throwable cause){
		super(cause);
	}
}
