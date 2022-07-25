package org.intra_mart.jssp.exception;

/**
 * 機能を実現するために必須の例外のスーパークラスです。
 * この例外は、エラーを通知するものではありません。
 * スクリプトの実行を強制的に中断して、他の機能を実行するために用いられる
 * 処理分起通知用例外です。
 */
public class JSSPTransitionalException extends JSSPException{
	/**
	 * 新しい例外を作成します。
	 */
	protected JSSPTransitionalException(){
		super();
	}

	/**
	 * 新しい例外を作成します。
	 * @param message 例外のメッセージ
	 */
	protected JSSPTransitionalException(String message){
		super(message);
	}

	/**
	 * 新しい例外を作成します。
	 * @param message 例外のメッセージ
	 * @param cause 原因
	 */
	protected JSSPTransitionalException(String message, Throwable cause){
		super(message, cause);
	}

	/**
	 * 新しい例外を作成します。
	 * @param cause 原因
	 */
	protected JSSPTransitionalException(Throwable cause){
		super(cause);
	}
}
